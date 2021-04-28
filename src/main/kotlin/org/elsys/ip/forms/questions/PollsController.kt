package org.elsys.ip.forms.questions

import org.elsys.ip.forms.*
import org.elsys.ip.forms.auth.UsersService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Controller
@RequestMapping("/polls")
class PollsController(
        val usersService: UsersService,
        val pollsService: PollsService
) {
    @GetMapping("{id}")
    fun poll(
            @PathVariable(name="id") id: EntityId,
            model: Model
    ): String {
        val poll = pollsService.getPoll(id) ?: throw EntityNotFound(id, "Poll")
        model.addAttribute("poll", poll)

        return "polls/poll"
    }

    @GetMapping("create")
    fun createPoll(model: Model): String {
        model.addAttribute("poll", PollDTO())
        return "polls/create"
    }

    @PostMapping("create")
    fun createPoll(@ModelAttribute("poll") pollDto: PollDTO): String {
        val poll = pollsService.createEmptyPoll(pollDto.title, usersService.currentUser()!!)
        return "redirect:${poll.id}"
    }
}

data class PollDTO(
        val title: String = "",
        val questions: List<QuestionDTO> = emptyList()
)

data class QuestionDTO(
        val question: String = ""
)