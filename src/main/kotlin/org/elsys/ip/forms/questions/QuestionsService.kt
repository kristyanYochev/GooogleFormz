package org.elsys.ip.forms.questions

import org.elsys.ip.forms.*
import org.springframework.stereotype.Service

@Service
class QuestionsService(private val questionsRepo: QuestionsRepo) {
    fun getAll(): Iterable<Question> = questionsRepo.findAll()

    fun addNew(question: Question) {
        questionsRepo.save(question)
    }

    fun getById(id: EntityId): Question {
        val question = questionsRepo.findById(id)
        if (question.isPresent) return question.get()

        throw EntityNotFound(id, "Question")
    }
}