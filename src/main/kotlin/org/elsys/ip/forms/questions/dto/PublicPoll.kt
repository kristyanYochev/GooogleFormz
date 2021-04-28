package org.elsys.ip.forms.questions.dto

import org.elsys.ip.forms.EntityId
import org.elsys.ip.forms.questions.Poll

data class PublicPollDTO(
        val title: String,
        val authorName: String,
        val id: EntityId
) {
    constructor(poll: Poll): this(
            poll.title,
            poll.author.username,
            poll.id
    )
}