package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import com.crisordonez.registro.model.errors.NotFoundException
import com.crisordonez.registro.model.mapper.CuentaUsuarioMapper.toUpdateUltimaSesion
import com.crisordonez.registro.repository.AdministradorRepository
import com.crisordonez.registro.repository.CuentaUsuarioRepository
import com.crisordonez.registro.repository.MedicoRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CuentaUsuarioDetailService : UserDetailsService {

    @Autowired
    lateinit var cuentaUsuarioRepository: CuentaUsuarioRepository

    @Autowired
    lateinit var administradorRepository: AdministradorRepository

    @Autowired
    lateinit var medicoRepository: MedicoRepository

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun loadUserByUsername(username: String?): UserDetails {
        log.info("Autenticando - Nombre usuario: $username")

        // 1) Cuenta usuario (app móvil)
        val usuario = cuentaUsuarioRepository.findByNombreUsuario(username!!)
        if (usuario.isPresent) {
            cuentaUsuarioRepository.save(usuario.get().toUpdateUltimaSesion())
            return User.builder()
                .username(usuario.get().nombreUsuario)
                .password(usuario.get().contrasena)
                .roles(usuario.get().rol)
                .build()
        }

        // 2) Administrador
        administradorRepository.findByUsuario(username)?.let { adm ->
            return User.builder()
                .username(adm.usuario)
                .password(adm.contrasenaHash)
                .roles(adm.rol)
                .build()
        }

        // 3) Médico
        medicoRepository.findByUsuario(username)?.let { med ->
            return User.builder()
                .username(med.usuario)
                .password(med.contrasena)
                .roles(med.rol)
                .build()
        }

        throw NotFoundException("El nombre de usuario no existe $username")
    }

    fun getRoles(usuario: CuentaUsuarioEntity): List<String> {
        return usuario.rol.split(",")
    }
}