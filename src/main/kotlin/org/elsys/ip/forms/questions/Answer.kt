package org.elsys.ip.forms.questions

import org.elsys.ip.forms.EntityId
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
data class Answer(
        val answer: String,
        @Id
        @GeneratedValue
        val id: EntityId = 0
)

interface AnswersRepo : CrudRepository<Answer, EntityId>
