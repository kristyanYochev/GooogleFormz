package org.elsys.ip.forms.questions

import org.elsys.ip.forms.EntityId
import org.springframework.data.repository.CrudRepository
import javax.persistence.*
import javax.transaction.Transactional

@Entity
data class Question(
        val question: String,
        @OneToMany(
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        @JoinColumn(name = "questionId", referencedColumnName = "id")
        val answers: List<Answer>,
        @Id
        @GeneratedValue
        val id: EntityId = 0
)

@Transactional(Transactional.TxType.MANDATORY)
interface QuestionsRepo : CrudRepository<Question, EntityId>
