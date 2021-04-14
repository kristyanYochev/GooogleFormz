package org.elsys.ip.forms

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class EntityNotFound(id: EntityId, entityName: String) :
        ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "$entityName with id $id cannot be found"
        )