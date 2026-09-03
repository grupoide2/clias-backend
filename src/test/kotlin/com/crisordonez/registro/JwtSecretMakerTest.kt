package com.crisordonez.registro

import io.jsonwebtoken.Jwts
import jakarta.xml.bind.DatatypeConverter
import org.junit.jupiter.api.Test

class JwtSecretMakerTest {

    @Test
    fun generateSecretKey() {
        val secretKey = Jwts.SIG.HS512.key().build()
        val decoded = DatatypeConverter.printHexBinary(secretKey.encoded)
        println("Key = $decoded")
    }
}