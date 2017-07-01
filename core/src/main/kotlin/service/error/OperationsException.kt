package service.error

import org.jetbrains.ktor.http.HttpStatusCode


open class OperationsException(val errorCode: OperationsErrorCode,
                               val httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest,
                               vararg val parameters: String) : Exception()