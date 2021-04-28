package org.elsys.ip.forms.questions.dto

import org.elsys.ip.forms.questions.*

data class EditPollDTO(
        val title: String = "",
        val questions: List<EditQuestionDTO> = emptyList(),
        val open: Boolean = false,
) {
    constructor(poll: Poll): this(
            poll.title,
            poll.questions.map { EditQuestionDTO(it) },
            poll.open
    )
}

data class EditQuestionDTO(
        val question: String = "",
        val answers: MutableList<String> = mutableListOf(),
        val multipleChoice: Boolean = false,
        val required: Boolean = false
) {
    constructor(question: Question): this(
            question.question,
            question.answers.map { it.answer } .toMutableList(),
            question.multipleChoice,
            question.required
    )
}
