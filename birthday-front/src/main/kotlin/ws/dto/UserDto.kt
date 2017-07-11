package ws.dto


data class UserDto(val id: Long,
                   val login: String,
                   val name: String) : Dto

data class CreateUserRequestDto(val login: String,
                                val name: String) : Dto