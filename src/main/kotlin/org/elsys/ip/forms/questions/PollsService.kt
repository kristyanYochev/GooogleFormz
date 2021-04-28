package org.elsys.ip.forms.questions

import org.elsys.ip.forms.*
import org.elsys.ip.forms.auth.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PollsService(
        private val pollsRepo: PollsRepo
) {
    fun createEmptyPoll(title: String, author: User) = pollsRepo.save(Poll(title, author = author))

    fun getPoll(id: EntityId) = pollsRepo.findByIdOrNull(id)

    fun closePoll(pollId: EntityId) {
        val maybePoll = pollsRepo.findById(pollId)
        if (maybePoll.isEmpty) throw EntityNotFound(pollId, "Poll")

        maybePoll.ifPresent {
            it.open = false
            pollsRepo.save(it)
        }
    }
}