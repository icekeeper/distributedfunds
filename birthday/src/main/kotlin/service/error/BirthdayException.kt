package service.error

import org.jetbrains.ktor.http.HttpStatusCode


class BirthdayException(val errorCode: BirthdayErrorCode,
                        val httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest,
                        vararg val parameters: String) : Exception()