package com.crisordonez.registro.configuration

import com.crisordonez.registro.service.CuentaUsuarioDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val cuentaUsuarioDetailService: CuentaUsuarioDetailService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests { registry ->

                // Health / root
                registry.requestMatchers("/", "/health").permitAll()

                // CORS preflight
                registry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Autenticación — app móvil y admin web
                registry.requestMatchers(
                    "/api/auth/login",
                    "/usuarios/registro",
                    "/usuarios/autenticar",
                    "/usuarios/validar",
                    "/usuarios/cambiar-contrasena",
                    "/usuarios/public-indent/*"
                ).permitAll()

                // Recursos públicos — app móvil
                registry.requestMatchers(
                    "/archivo/nombre/**",
                    "/info-socioeconomica/ficha/existe/*"
                ).permitAll()

                // Catálogos de solo lectura — consumidos por la ficha socioeconómica antes del registro
                registry.requestMatchers(HttpMethod.GET, "/ocupaciones").permitAll()
                registry.requestMatchers(HttpMethod.GET, "/rango-tiempo-examen").permitAll()

                // Recursos multimedia — lectura pública (app móvil, sitio web); escritura solo ADMIN
                registry.requestMatchers(HttpMethod.GET, "/recursos", "/recursos/**").permitAll()

                // Registro de dispositivo FCM — ocurre antes de la autenticación completa
                registry.requestMatchers(HttpMethod.POST, "/dispositivo").permitAll()
                registry.requestMatchers(HttpMethod.GET, "/dispositivo/*").permitAll()

                // Notificaciones — invocadas por el scheduler interno y clias-notificaciones
                registry.requestMatchers(HttpMethod.POST, "/notificaciones").permitAll()
                registry.requestMatchers(HttpMethod.POST, "/notificaciones/programadas").permitAll()

                // ── Admin Web (clias-admin) ───────────────────────────────────────────────────
                // /api/users/** y /api/medicos/** requieren JWT (anyRequest().authenticated() los cubre)
                // Los endpoints de solo lectura / operación del flujo médico siguen abiertos mientras
                // se migra el resto de clias-admin para enviar el token en cada petición.
                registry.requestMatchers(HttpMethod.GET,  "/api/codigosqr").permitAll()
                registry.requestMatchers(HttpMethod.GET,  "/api/codigosqr/validar/**").permitAll()
                registry.requestMatchers(HttpMethod.POST, "/api/codigosqr").permitAll()
                registry.requestMatchers("/api/dispositivos_registrados/**").permitAll()
                registry.requestMatchers(HttpMethod.GET,  "/api/estado-dispositivos/**").permitAll()
                registry.requestMatchers("/api/ubicaciones/**").permitAll()
                registry.requestMatchers(HttpMethod.GET,  "/api/pacientes/**").permitAll()
                registry.requestMatchers("/prueba/medico/**").permitAll()
                registry.requestMatchers("/prueba/admin/**").permitAll()
                // ─────────────────────────────────────────────────────────────────────────────

                // Todo lo demás requiere JWT válido;
                // la autorización por rol se aplica en cada método con @PreAuthorize
                registry.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun userDetailService(): UserDetailsService = cuentaUsuarioDetailService

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(cuentaUsuarioDetailService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun authenticationManager(): AuthenticationManager = ProviderManager(authenticationProvider())

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOriginPatterns = listOf(
                "http://localhost:*",
                "http://127.0.0.1:*",
                "https://clias.ucuenca.edu.ec"
            )
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With")
            exposedHeaders = listOf("Authorization")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().also { it.registerCorsConfiguration("/**", config) }
    }
}
