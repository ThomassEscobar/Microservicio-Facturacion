package com.consultoria.facturacion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consultoria.facturacion.client.ClienteFeign;
import com.consultoria.facturacion.dto.ClienteDTO;
import com.consultoria.facturacion.dto.FacturaPDFDTO;
import com.consultoria.facturacion.dto.FacturacionDTO;
import com.consultoria.facturacion.model.EstadoFactura;
import com.consultoria.facturacion.model.Facturacion;
import com.consultoria.facturacion.repository.FacturacionRepository;



@Service
public class FacturacionService {

    @Autowired
    private FacturacionRepository repo;

    @Autowired
    private ClienteFeign clienteFeign;

    public List<FacturacionDTO> findTodos() {

        return repo.findAll()
                .stream()
                .map(fac -> new FacturacionDTO(
                        fac.getId(),
                        fac.getClienteId(),
                        fac.getProyectoId(),
                        fac.getMonto(),
                        fac.getEstado()))
                .collect(Collectors.toList());
    }

    public FacturacionDTO guardar(FacturacionDTO dto) {

        Facturacion factura = new Facturacion();

        factura.setId(dto.getId());
        factura.setClienteId(dto.getClienteId());
        factura.setProyectoId(dto.getProyectoId());
        factura.setMonto(dto.getMonto());
        factura.setEstado(dto.getEstado());

        Facturacion guardar = repo.save(factura);

        return new FacturacionDTO(
                guardar.getId(),
                guardar.getClienteId(),
                guardar.getProyectoId(),
                guardar.getMonto(),
                guardar.getEstado());
    }

    public FacturacionDTO buscarxId(Long id) {

        Facturacion factura = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        return new FacturacionDTO(
                factura.getId(),
                factura.getClienteId(),
                factura.getProyectoId(),
                factura.getMonto(),
                factura.getEstado());
    }

    public FacturaPDFDTO obtenerFacturaPDF(Long id) {

        Facturacion factura = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        ClienteDTO cliente = clienteFeign.buscarCliente(factura.getClienteId());

        FacturaPDFDTO pdf = new FacturaPDFDTO();

        pdf.setNumeroFactura("FAC-" + factura.getId());
        pdf.setNombreCliente(cliente.getNombreEmpresa());
        pdf.setNitCliente(cliente.getNit());
        pdf.setMontoTotal(factura.getMonto());

        return pdf;
    }
    public Integer contarPendientes() {

    return repo.countByEstado(EstadoFactura.PENDIENTE);

    }

    public Integer contarPagadas() {

    return repo.countByEstado(EstadoFactura.PAGADA);

    }

    public Double totalIngresos() {

    return repo.findAll()
            .stream()
            .filter(f -> f.getEstado() == EstadoFactura.PAGADA)
            .mapToDouble(Facturacion::getMonto)
            .sum();

    }

}
