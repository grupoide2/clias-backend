package com.crisordonez.registro.controller

import com.crisordonez.registro.service.RecursoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recursos")
class RecursoController @Autowired constructor(
    private val recursoService: RecursoService
) {

    @PostMapping("/aumentar-vista")
    fun aumentarVista(@RequestParam("codigo") codigo: String) {
        println("Recibido código: $codigo")  // Verifica que la solicitud llegue al controlador
        recursoService.aumentarContadorVistas(codigo)
    }

}
