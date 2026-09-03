package com.crisordonez.registro.service

import com.crisordonez.registro.model.requests.AppVersionRequest
import com.crisordonez.registro.model.requests.TiempoChatRequest
import com.crisordonez.registro.model.requests.TiempoAppRequest
import com.crisordonez.registro.model.requests.CuentaUsuarioRequest
import com.crisordonez.registro.model.responses.CuentaUsuarioResponse
import java.util.UUID

interface CuentaUsuarioServiceInterface {

    fun crearCuentaUsuario(cuentaUsuario: CuentaUsuarioRequest): CuentaUsuarioResponse

    fun editarCuentaUsuario(publicId: UUID, cuentaUsuario: CuentaUsuarioRequest)

    fun getAllCuentas(): List<CuentaUsuarioResponse>

    fun getCuentaUsuario(publicId: UUID): CuentaUsuarioResponse

    fun autenticar(cuentaUsuario: CuentaUsuarioRequest): CuentaUsuarioResponse

    fun eliminarCuentaUsuario(publicId: UUID)

    fun validarExpiracionToken(token: String): String?

    fun updateContrasena(cuentaUsuario: CuentaUsuarioRequest)
    fun obtenerPublicIdPorIdInterno(id: Long): UUID

    fun updateTiempoChat(publicId: UUID, tiempoChatRequest: TiempoChatRequest)

    fun getChatTimeAvergae(): Double

    fun stablishAppVersion(publicId: UUID, appVersion: AppVersionRequest)

    fun updateTiempoApp(publicId: UUID, tiempoAppRequest: TiempoAppRequest)

}