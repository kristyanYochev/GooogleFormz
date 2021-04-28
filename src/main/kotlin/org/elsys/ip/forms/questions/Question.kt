package org.elsys.ip.forms.questions

import org.elsys.ip.forms.EntityId
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
data class Question(
        val question: String,
        @OneToMany(
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        @JoinColumn(name = "questionId", referencedColumnName = "id")
        val answers: List<Answer>,
        val multipleChoice: Boolean = false,
        val required: Boolean = false,
        @Id
        @GeneratedValue
        val id: EntityId = 0
)

interface QuestionsRepo : CrudRepository<Question, EntityId>
