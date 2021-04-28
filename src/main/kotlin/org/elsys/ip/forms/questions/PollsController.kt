package org.elsys.ip.forms.questions

import org.elsys.ip.forms.*
import org.elsys.ip.forms.auth.UsersService
import org.elsys.ip.forms.questions.dto.*
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
            @PathVariable(name = "id") id: EntityId,
            model: Model
    ): String {
        val poll = pollsService.getPoll(id) ?: throw EntityNotFound(id, "Poll")

        if (!poll.open) throw UnauthorizedAccess()

        model.addAttribute("poll", PollDTO(poll))
        model.addAttribute("response",
                ResponseDTO(
                        poll.id,
                        MutableList(poll.questions.size) {
                            MutableList(poll.questions[it].answers.size) { -1 }
                        }
                )
        )
        return "polls/respond"
    }

    @PostMapping("{id}")
    fun poll(
            @PathVariable(name = "id") pollId: EntityId,
            @ModelAttribute(name = "response") responseDTO: ResponseDTO
    ): String {
        val poll = pollsService.getPoll(pollId) ?: throw EntityNotFound(pollId, "Poll")
        if (!poll.open) throw UnauthorizedAccess()

        pollsService.respond(responseDTO.responses.flatten())

        return "redirect:/"
    }

    @GetMapping("{id}/stats")
    fun pollStats(
            @PathVariable(name = "id") id: EntityId,
            model: Model
    ): String {
        pollsService.checkAuthor(id, usersService.currentUser()!!)

        val poll = pollsService.getPoll(id) ?: throw EntityNotFound(id, "Poll")
        val pollDto = PollStatsDTO(poll)
        model.addAttribute("poll", pollDto)

        return "polls/stats"
    }

    @GetMapping("{id}/edit")
    fun editPoll(
            @PathVariable(name = "id") id: EntityId,
            model: Model
    ): String {
        pollsService.checkAuthor(id, usersService.currentUser()!!)

        val poll = pollsService.getPoll(id) ?: throw EntityNotFound(id, "Poll")
        val pollDto = EditPollDTO(poll)
        model.addAttribute("poll", pollDto)
        model.addAttribute("pollId", id)

        return "polls/edit"
    }

    @GetMapping("{id}/start")
    fun startPoll(@PathVariable(name = "id") id: EntityId): String {
        pollsService.checkAuthor(id, usersService.currentUser()!!)

        pollsService.startPoll(id)

        @Suppress("SpringMVCViewInspection")
        return "redirect:/polls/$id"
    }

    @GetMapping("{id}/add-question")
    fun addQuestion(
            @PathVariable(name = "id") id: EntityId,
            @RequestParam(name = "answersCount", defaultValue = "4") answersCount: Int,
            model: Model
    ): String {
        pollsService.checkAuthor(id, usersService.currentUser()!!)

        model.addAttribute("answersCount", answersCount)
        val questionDTO = EditQuestionDTO(answers = MutableList(answersCount) { "" })
        model.addAttribute("question", questionDTO)
        return "polls/editQuestion"
    }

    @PostMapping("{id}/add-question")
    fun addQuestion(
        @PathVariable(name = "id") id: EntityId,
        @ModelAttribute(name = "question") editQuestionDTO: EditQuestionDTO
    ): String {
        pollsService.checkAuthor(id, usersService.currentUser()!!)

        pollsService.addQuestionToPoll(
                id,
                editQuestionDTO.question,
                editQuestionDTO.answers,
                editQuestionDTO.multipleChoice
        )

        @Suppress("SpringMVCViewInspection")
        return "redirect:/polls/$id"
    }

    @GetMapping("create")
    fun createPoll(model: Model): String {
        model.addAttribute("poll", EditPollDTO())
        return "polls/create"
    }

    @PostMapping("create")
    fun createPoll(@ModelAttribute("poll") editPollDto: EditPollDTO): String {
        val poll = pollsService.createEmptyPoll(editPollDto.title, usersService.currentUser()!!)
        return "redirect:${poll.id}"
    }
}
