package org.elsys.ip.forms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

typealias EntityId = Long

@SpringBootApplication
class FormsApplication

fun main(args: Array<String>) {
    runApplication<FormsApplication>(*args)
}
