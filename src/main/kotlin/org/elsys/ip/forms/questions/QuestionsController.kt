package org.elsys.ip.forms.questions

import org.elsys.ip.forms.EntityId
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/questions")
class QuestionsController(private val questionsService: QuestionsService) {
    @GetMapping
    fun allQuestions(model: Model): String {
        model.addAttribute("questions", questionsService.getAll())
        return "allQuestions"
    }

    @GetMapping("{id}")
    fun showQuestion(@PathVariable(name="id") id: EntityId, model: Model): String {
        model.addAttribute("question", questionsService.getById(id))
        return "question"
    }
}