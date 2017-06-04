package ws.dto

import domain.User
import storage.IdEntityHolder


class UserConverter : DtoConverter<UserDto, User> {

    override fun toDto(entity: IdEntityHolder<User>): UserDto {
        return UserDto(
                entity.id,
                entity.entity.login,
                entity.entity.name
        )
    }
}