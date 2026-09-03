package com.crisordonez.registro.configuration

import com.crisordonez.registro.service.CuentaUsuarioDetailService
import com.crisordonez.registro.utils.JwtUtil
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var cuentaUsuarioDetailService: CuentaUsuarioDetailService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // ←––––– RUTAS APP WEB –––––→
        if (
            path == "/" ||
            path == "/health" ||
            path == "/favicon.ico" ||
            path.startsWith("/api/auth/login") ||
            (path.startsWith("/api/codigosqr") && (request.method.equals("POST", ignoreCase = true) || request.method.equals("GET", ignoreCase = true))) ||
            (path.startsWith("/prueba/admin") && request.method.equals("GET", ignoreCase = true)) ||
            (path.startsWith("/prueba/medico/nombre/") && request.method.equals("GET", ignoreCase = true)) ||
            (path.startsWith("/prueba/medico/subir") && request.method.equals("POST", ignoreCase = true)) ||
            path.startsWith("/web/") ||
            path.startsWith("/static/") ||
            path == "/usuarios/autenticar" ||
            path == "/usuarios/registro" ||
            path == "/usuarios/validar" ||
            path == "/usuarios/cambiar-contrasena"
        ) {
            filterChain.doFilter(request, response)
            return
        }


        // ←––––– PROCESAR JWT –––––→
        val authHeader = request.getHeader("Authorization")
        println("Authorization header: $authHeader")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            println("No hay header Authorization o no comienza con 'Bearer '")
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        println("JWT extraído: $jwt")

        try {
            val userName = jwtUtil.extractUserName(jwt)
            println("Usuario extraído del token JWT: $userName")

            if (SecurityContextHolder.getContext().authentication == null && jwtUtil.isTokenValid(jwt)) {
                println("Token válido, creando autenticación")
                val userDetails = cuentaUsuarioDetailService.loadUserByUsername(userName)
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }
                SecurityContextHolder.getContext().authentication = authenticationToken
            } else {
                println("Token expirado o inválido, continuando sin autenticación")
            }
        } catch (e: JwtException) {
            println("JWT inválido (${e.javaClass.simpleName}): ${e.message}")
        }

        filterChain.doFilter(request, response)
    }
}
