package ws.dto

import org.jetbrains.ktor.http.HttpStatusCode

interface Dto

data class DtoCollection(val collection: Iterable<Dto>)

data class Page(val data: List<Dto>,
                val pageNumber: Int,
                val pageSize: Int,
                val pagesCount: Int) : Dto

data class ErrorResponse(val code: String,
                         val httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest,
                         val parameters: List<String>)

data class ErrorDto(val code: String,
                    val parameters: List<String>) : Dto