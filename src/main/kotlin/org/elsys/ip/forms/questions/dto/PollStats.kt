package org.elsys.ip.forms.questions.dto

import org.elsys.ip.forms.questions.*

data class PollStatsDTO(
        val title: String = "",
        val questions: List<QuestionStatsDTO> = emptyList()
) {
    constructor(poll: Poll): this(
            poll.title,
            poll.questions.map { QuestionStatsDTO(it) }
    )
}

data class QuestionStatsDTO(
        val question: String = "",
        val answers: List<AnswerStatsDTO> = emptyList()
) {
    constructor(question: Question): this(
            question.question,
            question.answers.map { AnswerStatsDTO(it) }
    )
}

data class AnswerStatsDTO(
    val answer: String = "",
    val responseCount: Long = 0
) {
    constructor(answer: Answer): this(
            answer.answer,
            answer.responseCount
    )
}