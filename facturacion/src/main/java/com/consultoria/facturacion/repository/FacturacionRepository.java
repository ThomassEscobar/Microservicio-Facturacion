package com.consultoria.facturacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consultoria.facturacion.model.EstadoFactura;
import com.consultoria.facturacion.model.Facturacion;

@Repository
public interface FacturacionRepository extends JpaRepository<Facturacion, Long> {

     Integer countByEstado(EstadoFactura estado);

}
