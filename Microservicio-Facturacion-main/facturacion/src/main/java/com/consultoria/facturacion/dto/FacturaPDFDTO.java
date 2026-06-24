package com.consultoria.facturacion.dto;



import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaPDFDTO {

    private Long id;
    
    private String numeroFactura;

    private String nombreCliente;

    private Long nitCliente;

    private Double montoTotal;

    private Double monto;

    private String metodoPago;

    private Date fechaPago;

}