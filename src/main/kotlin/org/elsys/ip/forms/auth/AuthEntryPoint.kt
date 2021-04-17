package org.elsys.ip.forms.auth

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.*

@Component
class AuthEntryPoint : AuthenticationEntryPoint {
    override fun commence(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authException: AuthenticationException?
    ) {
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}