package org.elsys.ip.forms.auth

import org.elsys.ip.forms.EntityId
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
data class User(
        val username: String,
        val password: String,
        @Id
        @GeneratedValue
        val id: EntityId = 0
)

interface UsersRepo : CrudRepository<User, EntityId> {
        fun findByUsername(username: String): User?
}


