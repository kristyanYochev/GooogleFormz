package org.elsys.ip.forms.questions

import org.elsys.ip.forms.EntityId
import org.springframework.data.repository.CrudRepository
import javax.persistence.*
import javax.transaction.Transactional

@Entity
data class Answer(
        val answer: String,
        @Id
        @GeneratedValue
        val id: EntityId = 0
)

@Transactional(Transactional.TxType.MANDATORY)
interface AnswersRepo : CrudRepository<Answer, EntityId>
