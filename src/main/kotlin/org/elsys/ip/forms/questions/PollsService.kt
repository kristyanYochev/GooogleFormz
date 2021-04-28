package org.elsys.ip.forms.questions

import org.elsys.ip.forms.*
import org.elsys.ip.forms.auth.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PollsService(
        private val answersRepo: AnswersRepo,
        private val pollsRepo: PollsRepo
) {
    fun createEmptyPoll(title: String, author: User, public: Boolean) = pollsRepo.save(
            Poll(title, author = author, public = public)
    )

    fun getPoll(id: EntityId) = pollsRepo.findByIdOrNull(id)

    fun publicPolls() = pollsRepo.findAllPublicAndOpenPolls()

    fun checkAuthor(pollId: EntityId, user: User) {
        if (!isAuthor(pollId, user)) throw UnauthorizedAccess()
    }

    fun isAuthor(pollId: EntityId, user: User): Boolean {
        val poll = pollsRepo.findByIdOrNull(pollId) ?: throw EntityNotFound(pollId, "Poll")
        return poll.author.id == user.id
    }

    fun addQuestionToPoll(
            id: EntityId,
            question: String,
            answers: List<String>,
            multipleChoice: Boolean,
            required: Boolean,
            imageUrl: String
    ) {
        val questionEntity = Question(question, answers.map { Answer(it) }, multipleChoice, required, imageUrl)
        val poll = pollsRepo.findByIdOrNull(id) ?: throw EntityNotFound(id, "Poll")
        poll.questions.add(questionEntity)
        pollsRepo.save(poll)
    }

    fun startPoll(pollId: EntityId) {
        val maybePoll = pollsRepo.findById(pollId)
        if (maybePoll.isEmpty) throw EntityNotFound(pollId, "Poll")

        maybePoll.ifPresent {
            it.open = true
            pollsRepo.save(it)
        }
    }

    fun closePoll(pollId: EntityId) {
        val maybePoll = pollsRepo.findById(pollId)
        if (maybePoll.isEmpty) throw EntityNotFound(pollId, "Poll")

        maybePoll.ifPresent {
            it.open = false
            pollsRepo.save(it)
        }
    }

    @Transactional
    fun respond(responses: List<EntityId>) {
        for (answerId in responses) {
            val answer = answersRepo.findByIdOrNull(answerId) ?: throw EntityNotFound(answerId, "Answer")
            answer.responseCount += 1
            answersRepo.save(answer)
        }
    }
}