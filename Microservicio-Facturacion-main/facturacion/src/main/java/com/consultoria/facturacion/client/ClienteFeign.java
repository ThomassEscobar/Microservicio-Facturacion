package com.consultoria.facturacion.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.consultoria.facturacion.dto.ClienteDTO;


@FeignClient(name = "cliente-service", url = "http://localhost:8081")
public interface ClienteFeign {

    @GetMapping("/clientes/{id}")
    ClienteDTO buscarCliente(@PathVariable Long id);

}