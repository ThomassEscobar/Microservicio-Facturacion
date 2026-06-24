package com.consultoria.facturacion.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.consultoria.facturacion.client.ClienteFeign;
import com.consultoria.facturacion.dto.ClienteDTO;
import com.consultoria.facturacion.dto.FacturaPDFDTO;
import com.consultoria.facturacion.dto.FacturacionDTO;
import com.consultoria.facturacion.model.EstadoFactura;
import com.consultoria.facturacion.model.Facturacion;
import com.consultoria.facturacion.repository.FacturacionRepository;

@ExtendWith(MockitoExtension.class)
public class FacturacionServiceTest {

    @Mock
    private FacturacionRepository repo;

    @Mock
    private ClienteFeign clienteFeign;

    @InjectMocks
    private FacturacionService service;

    private Facturacion facturaPagada;
    private Facturacion facturaPendiente;
    private ClienteDTO clienteMock;

    @BeforeEach
    public void setUp() {
        // Factura Pagada Simulada
        facturaPagada = new Facturacion();
        facturaPagada.setId(1L);
        facturaPagada.setClienteId(10L);
        facturaPagada.setProyectoId(101L);
        facturaPagada.setMonto(1500.0);
        facturaPagada.setEstado(EstadoFactura.PAGADA);

        // Factura Pendiente Simulada
        facturaPendiente = new Facturacion();
        facturaPendiente.setId(2L);
        facturaPendiente.setClienteId(10L);
        facturaPendiente.setProyectoId(102L);
        facturaPendiente.setMonto(2500.0);
        facturaPendiente.setEstado(EstadoFactura.PENDIENTE);

        // Cliente Externo Simulado (Feign)
        clienteMock = new ClienteDTO();
        clienteMock.setId(10L);
        clienteMock.setNombreEmpresa("Tech Solutions");
        clienteMock.setNit(900123456L);
    }

    // 1. TEST FIND TODOS
    @Test
    public void testFindTodos() {
        // Arrange
        when(repo.findAll()).thenReturn(List.of(facturaPagada, facturaPendiente));

        // Act
        List<FacturacionDTO> resultado = service.findTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(EstadoFactura.PAGADA, resultado.get(0).getEstado());
        assertEquals(EstadoFactura.PENDIENTE, resultado.get(1).getEstado());

        verify(repo, times(1)).findAll();
    }

    // 2. TEST GUARDAR
    @Test
    public void testGuardar() {
        // Arrange
        FacturacionDTO dtoInput = new FacturacionDTO();
        dtoInput.setId(null);
        dtoInput.setClienteId(10L);
        dtoInput.setProyectoId(101L);
        dtoInput.setMonto(1500.0);
        dtoInput.setEstado(EstadoFactura.PAGADA);

        when(repo.save(any(Facturacion.class))).thenReturn(facturaPagada);

        // Act
        FacturacionDTO resultado = service.guardar(dtoInput);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1500.0, resultado.getMonto());
        assertEquals(EstadoFactura.PAGADA, resultado.getEstado());

        verify(repo, times(1)).save(any(Facturacion.class));
    }

    // 3. TEST BUSCAR POR ID (Caso Exitoso)
    @Test
    public void testBuscarxId_Exitoso() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.of(facturaPagada));

        // Act
        FacturacionDTO resultado = service.buscarxId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(101L, resultado.getProyectoId());

        verify(repo, times(1)).findById(1L);
    }

    // 4. TEST BUSCAR POR ID (Caso Error - RuntimeException)
    @Test
    public void testBuscarxId_NoEncontrado() {
        // Arrange
        when(repo.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.buscarxId(3L);
        });

        assertEquals("Factura no encontrada", exception.getMessage());
        verify(repo, times(1)).findById(3L);
    }

    // 5. TEST OBTENER FACTURA PDF
    @Test
    public void testObtenerFacturaPDF() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.of(facturaPagada));
        when(clienteFeign.buscarCliente(10L)).thenReturn(clienteMock);

        // Act
        FacturaPDFDTO resultado = service.obtenerFacturaPDF(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("FAC-1", resultado.getNumeroFactura());
        assertEquals("Tech Solutions", resultado.getNombreCliente());
        assertEquals(900123456L, resultado.getNitCliente());
        assertEquals(1500.0, resultado.getMontoTotal());

        verify(repo, times(1)).findById(1L);
        verify(clienteFeign, times(1)).buscarCliente(10L);
    }

    // 6. TEST CONTAR PENDIENTES
    @Test
    public void testContarPendientes() {
        // Arrange
        when(repo.countByEstado(EstadoFactura.PENDIENTE)).thenReturn(1);

        // Act
        Integer resultado = service.contarPendientes();

        // Assert
        assertEquals(1, resultado);
        verify(repo, times(1)).countByEstado(EstadoFactura.PENDIENTE);
    }

    // 7. TEST CONTAR PAGADAS
    @Test
    public void testContarPagadas() {
        // Arrange
        when(repo.countByEstado(EstadoFactura.PAGADA)).thenReturn(1);

        // Act
        Integer resultado = service.contarPagadas();

        // Assert
        assertEquals(1, resultado);
        verify(repo, times(1)).countByEstado(EstadoFactura.PAGADA);
    }

    // 8. TEST TOTAL INGRESOS
    @Test
    public void testTotalIngresos() {
        // Arrange
        // Mandamos una pagada (1500) y una pendiente (2500). El filtro del servicio solo debe sumar la pagada.
        when(repo.findAll()).thenReturn(List.of(facturaPagada, facturaPendiente));

        // Act
        Double resultado = service.totalIngresos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1500.0, resultado); // Verifica que excluyó correctamente los 2500 de la factura pendiente

        verify(repo, times(1)).findAll();
    }
}