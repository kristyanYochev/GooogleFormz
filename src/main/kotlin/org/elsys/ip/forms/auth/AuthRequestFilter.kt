package org.elsys.ip.forms.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.*

@Component
class AuthRequestFilter(
        private val usersService: UsersService,
        private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {
    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val cookie = request.cookies.findByName("auth-token")

        if (cookie != null) {
            val token = cookie.value
            val username = jwtUtil.username(token)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = usersService.loadUserByUsername(username)

                if (jwtUtil.valid(token, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                    )

                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}

fun Array<Cookie>.findByName(name: String): Cookie? {
    for (cookie in this) {
        if (cookie.name == name) return cookie
    }

    return null
}