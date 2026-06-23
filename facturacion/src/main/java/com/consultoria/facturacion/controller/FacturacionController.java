package com.consultoria.facturacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consultoria.facturacion.dto.FacturaPDFDTO;
import com.consultoria.facturacion.dto.FacturacionDTO;
import com.consultoria.facturacion.service.FacturacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturación", description = "Operaciones relacionadas con la emisión, control y métricas de facturas")
public class FacturacionController {

    @Autowired
    private FacturacionService service;

    @Operation(summary = "Listar todas las facturas", description = "Retorna el histórico completo de facturas registradas en el sistema")
    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de facturas obtenido con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<FacturacionDTO>> listar() {
        return ResponseEntity.ok(service.findTodos());
    }

    @Operation(summary = "Crear una nueva factura", description = "Registra una factura en el sistema con sus datos comerciales iniciales")
    @PostMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Factura creada con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FacturacionDTO> guardar(
            @RequestBody FacturacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(dto));
    }

    @Operation(summary = "Buscar factura por ID", description = "Obtiene la información comercial detallada de una factura específica mediante su identificador único")
    @GetMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FacturacionDTO> buscarxId(
            @PathVariable 
            @Parameter(description = "ID único de la factura", required = true, examples = @ExampleObject(value = "1")) Long id) {
        return ResponseEntity.ok(service.buscarxId(id));
    }

    @Operation(summary = "Obtener datos de Factura para PDF", description = "Extrae el DTO formateado con la información necesaria para renderizar o descargar el PDF de la factura")
    @GetMapping("/pdf/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos estructurados para PDF obtenidos con éxito"),
        @ApiResponse(responseCode = "404", description = "Factura asociada no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FacturaPDFDTO> obtenerFacturaPDF(
            @PathVariable 
            @Parameter(description = "ID de la factura a exportar", required = true, examples = @ExampleObject(value = "1")) Long id) {
        return ResponseEntity.ok(service.obtenerFacturaPDF(id));
    }

    @Operation(summary = "Contar facturas pendientes", description = "Retorna la cantidad total de facturas cuyo estado se encuentra por cobrar")
    @GetMapping("/pendientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo obtenido con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Integer> pendientes() {
        return ResponseEntity.ok(service.contarPendientes());
    }

    @Operation(summary = "Contar facturas pagadas", description = "Retorna la cantidad total de facturas liquidadas y cobradas exitosamente")
    @GetMapping("/pagadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo obtenido con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Integer> pagadas() {
        return ResponseEntity.ok(service.contarPagadas());
    }

    @Operation(summary = "Calcular total de ingresos", description = "Calcula la suma monetaria total acumulada a partir de las facturas vigentes")
    @GetMapping("/ingresos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Suma de ingresos calculada con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Double> ingresos() {
        return ResponseEntity.ok(service.totalIngresos());
    }
}