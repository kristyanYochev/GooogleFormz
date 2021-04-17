package org.elsys.ip.forms.auth

import org.springframework.security.authentication.*
import org.springframework.security.core.userdetails.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

typealias SpringUser = org.springframework.security.core.userdetails.User

@Service
class UsersService(
        private val usersRepo: UsersRepo,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        username ?: throw UsernameNotFoundException("Username cannot be null!")

        val user = usersRepo.findByUsername(username) ?:
            throw UsernameNotFoundException("User with name $username does not exist")

        return SpringUser(user.username, user.password, emptyList())
    }

    fun registerUser(username: String, password: String) {
        usersRepo.save(User(username, password))
    }
}