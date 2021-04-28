package org.elsys.ip.forms.questions

import org.elsys.ip.forms.EntityId
import org.elsys.ip.forms.auth.User
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
data class Poll(
        val title: String,
        @OneToMany(
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        @JoinColumn(name = "pollId", referencedColumnName = "id")
        val questions: List<Question> = emptyList(),

        @ManyToOne
        val author: User,

        var open: Boolean = true,

        @Id
        @GeneratedValue
        val id: EntityId = 0
)

interface PollsRepo : CrudRepository<Poll, EntityId>
