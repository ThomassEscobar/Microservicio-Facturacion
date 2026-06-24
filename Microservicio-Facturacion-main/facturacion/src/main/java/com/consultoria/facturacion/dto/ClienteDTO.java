package com.consultoria.facturacion.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Long id;

    private String nombreEmpresa;

    private Long nit;

    private String sector;

    private String direccion;

    private String emailEmpresa;

}
