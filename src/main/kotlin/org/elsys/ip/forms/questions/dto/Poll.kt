package org.elsys.ip.forms.questions.dto

import org.elsys.ip.forms.EntityId
import org.elsys.ip.forms.questions.*

data class PollDTO(
        val title: String,
        val questions: List<QuestionDTO>,
        val id: EntityId
) {
    constructor(poll: Poll): this(
            poll.title,
            poll.questions.map { QuestionDTO(it) },
            poll.id
    )
}

data class QuestionDTO(
        val question: String,
        val answers: List<AnswerDTO>,
        val id: EntityId
) {
    constructor(question: Question): this(
            question.question,
            question.answers.map { AnswerDTO(it) },
            question.id
    )
}

data class AnswerDTO(
        val answer: String,
        val id: EntityId
) {
    constructor(answer: Answer): this(
            answer.answer,
            answer.id
    )
}

data class ResponseDTO(
        val pollId: EntityId = -1,
        val responses: MutableList<EntityId> = mutableListOf()
)