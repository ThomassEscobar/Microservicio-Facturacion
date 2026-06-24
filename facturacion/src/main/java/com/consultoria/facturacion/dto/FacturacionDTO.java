package com.consultoria.facturacion.dto;

import com.consultoria.facturacion.model.EstadoFactura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturacionDTO {

    private Long id;

    private Long clienteId;

    private Long proyectoId;

    private Double monto;

    private EstadoFactura estado;

}


