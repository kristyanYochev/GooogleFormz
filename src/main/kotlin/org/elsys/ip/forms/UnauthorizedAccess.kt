package org.elsys.ip.forms

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UnauthorizedAccess : ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized Access")