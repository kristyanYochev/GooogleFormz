package org.elsys.ip.forms.auth

import org.springframework.security.authentication.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.*

@Controller
class AuthController(
        private val usersService: UsersService,
        private val authManager: AuthenticationManager,
        private val jwtUtil: JwtUtil,
        private val passwordEncoder: PasswordEncoder
) {
    @GetMapping("/login")
    fun loginPage(model: Model): String {
        model.addAttribute("user", UserDTO())
        return "login"
    }

    @PostMapping("/login")
    fun login(@ModelAttribute("user") userDTO: UserDTO, response: HttpServletResponse): String {
        val token = authenticate(userDTO.username, userDTO.password)

        val cookie = Cookie("auth-token", token)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.maxAge = 5 * 60 * 60
        response.addCookie(cookie)

        return "redirect:"
    }

    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("user", UserDTO())
        return "register"
    }

    @PostMapping("/register")
    fun register(@ModelAttribute("user") userDTO: UserDTO): String {
        usersService.registerUser(userDTO.username, passwordEncoder.encode(userDTO.password))

        @Suppress("SpringMVCViewInspection")
        return "redirect:/login"
    }

    fun authenticate(username: String, password: String): String {
        try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (userDisabled: DisabledException) {
            throw Exception("USER_DISABLED", userDisabled)
        } catch (badCredentials: BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", badCredentials)
        }

        val userDetails = usersService.loadUserByUsername(username)
        return jwtUtil.generateToken(userDetails)
    }
}

data class UserDTO(
        val username: String = "",
        val password: String = ""
)