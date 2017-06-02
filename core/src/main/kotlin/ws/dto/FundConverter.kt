package ws.dto

import domain.Fund
import domain.User
import service.FundOperations
import storage.IdEntityHolder
import storage.model.FundDescription


class FundConverter(private val fundOperations: FundOperations,
                    private val userDtoConverter: DtoConverter<UserDto, User>) : DtoConverter<FundDto, Fund> {
    override fun toDto(entity: IdEntityHolder<Fund>): FundDto {

        val supervisor = userDtoConverter.toDto(fundOperations.getSupervisor(entity.id))
        val users = fundOperations.getFundUsersWithIds(entity.id).map { userDtoConverter.toDto(it) }

        return FundDto(entity.id,
                entity.entity.name,
                entity.entity.description,
                supervisor,
                users)
    }
}

class FundDescriptionConverter : DtoConverter<FundDescriptionDto, FundDescription> {
    override fun toDto(entity: IdEntityHolder<FundDescription>): FundDescriptionDto {

        return FundDescriptionDto(entity.id,
                entity.entity.name,
                entity.entity.description)
    }

}