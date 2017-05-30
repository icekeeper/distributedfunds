package storage.exposed

import domain.Fund
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import storage.FundStorage
import storage.IdEntityHolder
import storage.dao.*
import storage.model.FundDescription


class ExposedFundStorage : FundStorage {
    override fun linkUser(fundId: Long, userId: Long): Boolean = transaction {
        val linked = FundsUsers.select { (FundsUsers.fund eq fundId) and (FundsUsers.user eq userId) }.count() == 1

        if (!linked) {
            FundsUsers.insert {
                it[fund] = EntityID(fundId, Funds)
                it[user] = EntityID(userId, Users)
            }
        }

        !linked
    }

    override fun unlinkUser(fundId: Long, userId: Long): Boolean = transaction {
        val linked = FundsUsers.select { (FundsUsers.fund eq fundId) and (FundsUsers.user eq userId) }.count() == 1

        if (linked) {
            FundsUsers.deleteWhere {
                (FundsUsers.fund eq fundId) and (FundsUsers.user eq userId)
            }
        }

        linked
    }

    override fun store(fund: Fund): Long = transaction {
        val supervisorEntity = UserEntity.find { Users.login eq fund.supervisor.login }.first()
        FundEntity.new {
            this.name = fund.name
            this.description = fund.description
            this.supervisor = supervisorEntity.id
        }.id.value
    }


    override fun updateName(id: Long, name: String): Fund = transaction {
        val fundEntity = FundEntity[id]
        fundEntity.name = name
        get(id)
    }

    override fun updateDescription(id: Long, description: String): Fund = transaction {
        val fundEntity = FundEntity[id]
        fundEntity.description = description
        get(id)
    }

    override fun get(id: Long): Fund = transaction {
        val fundEntity = FundEntity[id]
        val creator = ExposedUserStorage.userFromEntity(UserEntity[fundEntity.supervisor])

        val userIds = FundsUsers.select { FundsUsers.fund eq fundEntity.id }.map { it[FundsUsers.user] }
        val users = UserEntity.forEntityIds(userIds).map { ExposedUserStorage.userFromEntity(it) }

        Fund(fundEntity.name, fundEntity.description, creator, users)
    }

    override fun getSupervisorId(fundId: Long): Long = transaction {
        val fundEntity = FundEntity[fundId]
        fundEntity.supervisor.value
    }

    override fun getLinkedUserIds(fundId: Long): List<Long> = transaction {
        FundsUsers.select { FundsUsers.fund eq fundId }.map { it[FundsUsers.user].value }
    }

    override fun getFundDescriptions(): List<IdEntityHolder<FundDescription>> = transaction {
        FundEntity.all().map { IdEntityHolder(it.id.value, FundDescription(it.name, it.description)) }
    }
}