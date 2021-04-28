package org.elsys.ip.forms.questions

import org.elsys.ip.forms.*
import org.elsys.ip.forms.auth.UsersService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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
        val pollDto = PollDTO(poll)
        model.addAttribute("poll", pollDto)
        model.addAttribute("pollId", id)

        return "polls/poll"
    }

    @GetMapping("{id}/start")
    fun startPoll(@PathVariable(name = "id") id: EntityId): String {
        val poll = pollsService.getPoll(id) ?: throw EntityNotFound(id, "Poll")
        if (usersService.currentUser()!!.id != poll.author.id) {
            throw UnauthorizedAccess()
        }

        pollsService.startPoll(id)

        return "redirect:/polls/${id}"
    }

    @GetMapping("{id}/add-question")
    fun addQuestion(
            @PathVariable(name = "id") id: EntityId,
            @RequestParam(name = "answersCount", defaultValue = "4") answersCount: Int,
            model: Model
    ): String {
        model.addAttribute("answersCount", answersCount)
        val questionDTO = QuestionDTO(answers = MutableList(answersCount) { "" })
        model.addAttribute("question", questionDTO)
        return "polls/editQuestion"
    }

    @PostMapping("{id}/add-question")
    fun addQuestion(
        @PathVariable(name = "id") id: EntityId,
        @ModelAttribute(name = "question") questionDTO: QuestionDTO
    ): String {
        pollsService.addQuestionToPoll(id, questionDTO.question, questionDTO.answers)
        return "redirect:/polls/${id}"
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
        val questions: List<QuestionDTO> = emptyList(),
        val open: Boolean = false
) {
    constructor(poll: Poll): this(
            poll.title,
            poll.questions.map { QuestionDTO(it) },
            poll.open
    )
}

data class QuestionDTO(
        val question: String = "",
        val answers: MutableList<String> = mutableListOf()
) {
    constructor(question: Question) : this(
            question.question,
            question.answers.map { it.answer } .toMutableList()
    )
}
