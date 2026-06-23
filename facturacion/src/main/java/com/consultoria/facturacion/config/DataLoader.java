package com.consultoria.facturacion.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.consultoria.facturacion.model.EstadoFactura;
import com.consultoria.facturacion.model.Facturacion;
import com.consultoria.facturacion.model.Pago;
import com.consultoria.facturacion.repository.FacturacionRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

    @Autowired
    private FacturacionRepository repository;

    @PostConstruct
    public void cargarDatos() {

        if (repository.count() > 0) {
            return;
        }

        Pago pago1 = new Pago();
        pago1.setMonto(50000.0);
        pago1.setMetodoPago("TRANSFERENCIA");

        Facturacion f1 = new Facturacion();
        f1.setClienteId(1L);
        f1.setProyectoId(1L);
        f1.setEstado(EstadoFactura.PENDIENTE);
        f1.setPago(pago1);

        Pago pago2 = new Pago();
        pago2.setMonto(30000.0);
        pago2.setMetodoPago("TARJETA");

        Facturacion f2 = new Facturacion();
        f2.setClienteId(2L);
        f2.setProyectoId(2L);
        f2.setEstado(EstadoFactura.PAGADA);
        f2.setPago(pago2);

        Pago pago3 = new Pago();
        pago3.setMonto(45000.0);
        pago3.setMetodoPago("EFECTIVO");

        Facturacion f3 = new Facturacion();
        f3.setClienteId(3L);
        f3.setProyectoId(3L);
        f3.setEstado(EstadoFactura.PENDIENTE);
        f3.setPago(pago3);

        Pago pago4 = new Pago();
        pago4.setMonto(25000.0);
        pago4.setMetodoPago("TRANSFERENCIA");

        Facturacion f4 = new Facturacion();
        f4.setClienteId(4L);
        f4.setProyectoId(4L);
        f4.setEstado(EstadoFactura.PAGADA);
        f4.setPago(pago4);

        Pago pago5 = new Pago();
        pago5.setMonto(60000.0);
        pago5.setMetodoPago("TARJETA");

        Facturacion f5 = new Facturacion();
        f5.setClienteId(5L);
        f5.setProyectoId(5L);
        f5.setEstado(EstadoFactura.PENDIENTE);
        f5.setPago(pago5);

        repository.saveAll(List.of(f1, f2, f3, f4, f5));

        System.out.println("Datos de facturacion cargados correctamente");
    }
}