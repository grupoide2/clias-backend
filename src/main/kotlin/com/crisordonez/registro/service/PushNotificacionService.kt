package com.crisordonez.registro.service

import com.crisordonez.registro.model.responses.NotificacionResponse
import com.crisordonez.registro.repository.DispositivoAppUsuarioRepository
import org.slf4j.LoggerFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID


@Service
open class PushNotificacionService(
    @Autowired private val dispositivoAppRepository: DispositivoAppUsuarioRepository,
    @Autowired private val dispositivoAppUsuarioService: DispositivoAppUsuarioService,
) : PushNotificacionServiceInterface {

    private val logger = LoggerFactory.getLogger(PushNotificacionService::class.java)

    override fun enviarPushFCM(usuarioPublicId: UUID, notificacion: NotificacionResponse) {
        val dispositivos = dispositivoAppRepository.findAllByUsuarioPublicId(usuarioPublicId)

        val tokens = dispositivos.map { it.fcmToken }
        logger.info("🔍 Tokens encontrados para $usuarioPublicId: ${tokens.joinToString()}")

        val dispositivosUnicos = dispositivos.distinctBy { it.fcmToken }
        logger.info("📦 Tokens únicos filtrados: ${dispositivosUnicos.map { it.fcmToken }}")



        if (dispositivosUnicos.isEmpty()) {
            logger.info("No hay dispositivos móviles registrados con la app para el usuario $usuarioPublicId")
            return
        }

        dispositivosUnicos.forEach { dispositivo ->
            val message = Message.builder()
                .setToken(dispositivo.fcmToken)
                .setNotification(
                    Notification.builder()
                        .setTitle(notificacion.titulo)
                        .setBody(notificacion.mensaje)
                        .build()
                )
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .putData("tipoNotificacion", notificacion.tipoNotificacion.name)
                .putData("publicId", notificacion.publicId.toString())
                .putData("titulo", notificacion.titulo)
                .putData("mensaje", notificacion.mensaje)
                .putData("tipoAccion", notificacion.tipoAccion.name)
                .putData("accion", notificacion.accion ?: "")
                .putData("fechaEnvio", notificacion.fechaCreacion.toString())
                .putData("leido", notificacion.notificacionLeida.toString())
                .build()

            try {
                val response = FirebaseMessaging.getInstance().send(message)
                logger.info("✅ Mensaje FCM enviado a ${dispositivo.fcmToken}: $response del usuario $usuarioPublicId")
            } catch (e: Exception) {
                logger.error("❌ Error al enviar notificación a ${dispositivo.fcmToken}: ${e.message}")
                if (e.message?.contains("Requested entity was not found") == true) {
                    dispositivoAppUsuarioService.eliminarTokenFCMInvalido(dispositivo.fcmToken)
                }
            }
        }
    }


}
