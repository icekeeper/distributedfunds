package ws.dto

import storage.IdEntityHolder
import java.time.LocalDate

interface Dto

interface DtoConverter<out T : Dto, in U> {
    fun toDto(entity: IdEntityHolder<U>): T
}

data class UserDto(val id: Long,
                   val login: String,
                   val name: String,
                   val birthday: LocalDate) : Dto

data class CreateUserRequestDto(val login: String,
                                val name: String,
                                val birthday: LocalDate) : Dto

data class FundDto(val id: Long,
                   val name: String,
                   val description: String,
                   val supervisor: UserDto,
                   val users: List<UserDto>) : Dto

data class FundDescriptionDto(val id: Long,
                              val name: String,
                              val description: String) : Dto

data class CreateFundRequestDto(val name: String,
                                val description: String,
                                val supervisorId: Long) : Dto
