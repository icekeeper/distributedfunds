package ws.dto

import java.time.LocalDate

interface Dto

data class UserDto(val id: Long,
                   val login: String,
                   val name: String) : Dto

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
                                val supervisorId: Long,
                                val userIds: List<Long>) : Dto
