package com.crisordonez.registro.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

@Service
class JwtUtil {

    private final val SECRET = "8A17B9AB37D9C2D8C82288FBEF1606A246F28557340F63802EDE551630A4AB2C75112CA50A3C87DAC9A57F1828E298DB05009425D6E26D4533A054B519CBC9C1"

    private final val VALIDITY = TimeUnit.HOURS.toMillis(8) // 8 horas (jornada de trabajo)

    fun generateToken(userDetails: UserDetails): String {

        return Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
            .signWith(generateKey())
            .compact()
    }

    private fun generateKey(): SecretKey {
        val decodedKey = Base64.getDecoder().decode(SECRET)
        return Keys.hmacShaKeyFor(decodedKey)
    }

    fun extractUserName(jwt: String): String {
        return try {
            getClaims(jwt).subject
        } catch (e: ExpiredJwtException) {
            e.claims.subject
        }
    }

    private fun getClaims(jwt: String): Claims {
        return Jwts.parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(jwt)
            .payload
    }

    fun isTokenValid(jwt: String): Boolean {
        return try {
            val claims = getClaims(jwt)
            claims.expiration.after(Date.from(Instant.now()))
        } catch (e: ExpiredJwtException) {
            false
        }
    }

    fun refreshToken(jwt: String): String? {
        if (!isTokenValid(jwt)) return null

        val userName = extractUserName(jwt)

        return Jwts.builder()
            .subject(userName)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
            .signWith(generateKey())
            .compact()

    }
}