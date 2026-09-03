package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.EncuestaSusEntity
import com.crisordonez.registro.model.requests.EncuestaSusRequest
import com.crisordonez.registro.repository.CuentaUsuarioRepository
import com.crisordonez.registro.repository.EncuestaSusRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class EncuestaSusService(
    private val encuestaRepository: EncuestaSusRepository,
    private val cuentaUsuarioRepository: CuentaUsuarioRepository
) : EncuestaSusServiceInterface {

    override fun guardarEncuesta(request: EncuestaSusRequest): Boolean {
        val cuenta = cuentaUsuarioRepository.findByPublicId(UUID.fromString(request.cuentaUsuarioId)).orElse(null)
            ?: return false

        if (request.respuestas.size != 14) return false

        val encuesta = EncuestaSusEntity(
            item1 = request.respuestas[0],
            item2 = request.respuestas[1],
            item3 = request.respuestas[2],
            item4 = request.respuestas[3],
            item5 = request.respuestas[4],
            item6 = request.respuestas[5],
            item7 = request.respuestas[6],
            item8 = request.respuestas[7],
            item9 = request.respuestas[8],
            item10 = request.respuestas[9],
            item11 = request.respuestas[10],
            item12 = request.respuestas[11],
            item13 = request.respuestas[12],
            item14 = request.respuestas[13],
            cuentaUsuario = cuenta
        )

        encuestaRepository.save(encuesta)
        return true
    }

    override fun estaEncuestaCompletada(cuentaUsuarioId: String): Boolean {
        val cuenta = cuentaUsuarioRepository.findByPublicId(UUID.fromString(cuentaUsuarioId)).orElse(null)
        if (cuenta == null) {
            return false // Si no existe la cuenta
        }

        val encuesta = encuestaRepository.findByCuentaUsuario(cuenta)
        return encuesta != null // Si existe una encuesta para esa cuenta, la considera como completada
    }
}