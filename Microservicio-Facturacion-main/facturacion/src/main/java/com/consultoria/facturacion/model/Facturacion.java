package com.consultoria.facturacion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "factura")
public class Facturacion {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long clienteId;

    @NotNull
    private Long proyectoId;
    
    @NotNull
    private Double monto;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pago_id")
    private Pago pago;

}
