package org.elsys.ip.forms.auth

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtUtil(
        @Value("\${jwt.secret}")
        private val secret: String
) {
    companion object {
        const val JWT_TOKEN_VALIDITY_MILLIS: Long = 5 * 60 * 60 * 1000
    }

    fun username(token: String): String = claim(token, Claims::getSubject)

    fun expirationDate(token: String): Date = claim(token, Claims::getExpiration)

    fun <T> claim(token: String, resolver: (Claims) -> T): T = resolver(claims(token))

    private fun claims(token: String) = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body

    private fun expired(token: String) = expirationDate(token).before(now())

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        val username = userDetails.username

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now())
                .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_MILLIS))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun valid(token: String, userDetails: UserDetails): Boolean {
        val tokenUsername = username(token)
        return tokenUsername == userDetails.username && !expired(token)
    }
}

fun now() = Date()