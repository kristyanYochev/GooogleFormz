package org.elsys.ip.forms.questions.dto

import org.elsys.ip.forms.questions.*
import org.springframework.web.multipart.MultipartFile

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
        val required: Boolean = false,
        val image: String = ""
) {
    constructor(question: Question): this(
            question.question,
            question.answers.map { it.answer } .toMutableList(),
            question.multipleChoice,
            question.required,
            question.image
    )
}
