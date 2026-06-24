package com.consultoria.facturacion.controller;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.consultoria.facturacion.dto.FacturaPDFDTO;
import com.consultoria.facturacion.dto.FacturacionDTO;
import com.consultoria.facturacion.model.EstadoFactura;
import com.consultoria.facturacion.service.FacturacionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FacturacionController.class)
class FacturacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacturacionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private FacturacionDTO factura;
    private FacturaPDFDTO facturaPDF;

    @BeforeEach
    void setUp() {
        factura = new FacturacionDTO(
                1L,
                101L,
                201L,
                500000.0,
                EstadoFactura.PAGADA
        );

        facturaPDF = new FacturaPDFDTO(
                1L,
                "FAC-1",
                "Empresa Test",
                12345678L,
                500000.0,
                500000.0,
                "TRANSFERENCIA",
                new Date(System.currentTimeMillis())
        );
    }

    @Test
    void listar_ok() throws Exception {
        when(service.findTodos()).thenReturn(List.of(factura));

        mockMvc.perform(get("/facturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(101))
                .andExpect(jsonPath("$[0].proyectoId").value(201))
                .andExpect(jsonPath("$[0].monto").value(500000.0))
                .andExpect(jsonPath("$[0].estado").value("PAGADA"));
    }

    @Test
    void guardar_ok() throws Exception {
        when(service.guardar(any(FacturacionDTO.class))).thenReturn(factura);

        mockMvc.perform(post("/facturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(factura)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(101))
                .andExpect(jsonPath("$.estado").value("PAGADA"));
    }

    @Test
    void buscarxId_ok() throws Exception {
        when(service.buscarxId(1L)).thenReturn(factura);

        mockMvc.perform(get("/facturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(101))
                .andExpect(jsonPath("$.monto").value(500000.0));
    }

    @Test
    void obtenerFacturaPDF_ok() throws Exception {
        when(service.obtenerFacturaPDF(1L)).thenReturn(facturaPDF);

        mockMvc.perform(get("/facturas/pdf/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroFactura").value("FAC-1"))
                .andExpect(jsonPath("$.nombreCliente").value("Empresa Test"))
                .andExpect(jsonPath("$.nitCliente").value(12345678L))
                .andExpect(jsonPath("$.montoTotal").value(500000.0));
    }

    @Test
    void pendientes_ok() throws Exception {
        when(service.contarPendientes()).thenReturn(5);

        mockMvc.perform(get("/facturas/pendientes"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void pagadas_ok() throws Exception {
        when(service.contarPagadas()).thenReturn(10);

        mockMvc.perform(get("/facturas/pagadas"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void ingresos_ok() throws Exception {
        when(service.totalIngresos()).thenReturn(2500000.0);

        mockMvc.perform(get("/facturas/ingresos"))
                .andExpect(status().isOk())
                .andExpect(content().string("2500000.0"));
    }
}