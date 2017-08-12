package core.storage.exposed

import core.model.Fund
import core.model.User
import core.storage.FundRepository
import core.storage.dao.*
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


class ExposedFundRepository : FundRepository {

    companion object {
        fun fundFromEntity(entity: FundEntity): Fund {
            return fundFromEntity(entity, UserEntity[entity.supervisor])
        }

        fun fundFromEntity(entity: FundEntity, supervisorEntity: UserEntity): Fund {
            val supervisor = ExposedUserRepository.userFromEntity(supervisorEntity)
            return Fund(entity.id.value, entity.name, entity.description, supervisor)
        }
    }

    override fun create(name: String, description: String, supervisor: User): Fund = transaction {
        val supervisorEntity = UserEntity[supervisor.id]
        val fundEntity = FundEntity.new {
            this.name = name
            this.description = description
            this.supervisor = supervisorEntity.id
        }
        fundFromEntity(fundEntity, supervisorEntity)
    }

    override fun getAll(): List<Fund> = transaction {
        val fundEntities = FundEntity.all()
        val supervisorEntities = UserEntity.forEntityIds(fundEntities.map { it.supervisor })
        val supervisorEntitiesById = supervisorEntities.associateBy { it.id.value }
        fundEntities.map { fundFromEntity(it, supervisorEntitiesById[it.supervisor.value]!!) }
    }

    override fun get(id: Long): Fund? = transaction {
        val fundEntity = FundEntity.findById(id)
        if (fundEntity != null) {
            val supervisorEntity = UserEntity[fundEntity.supervisor]
            fundFromEntity(fundEntity, supervisorEntity)
        } else {
            null
        }
    }

    private fun getExisting(id: Long): Fund = transaction {
        val fundEntity = FundEntity[id]
        val supervisorEntity = UserEntity[fundEntity.supervisor]
        fundFromEntity(fundEntity, supervisorEntity)
    }

    override fun updateName(fund: Fund, name: String): Fund = transaction {
        val fundEntity = FundEntity[fund.id]
        fundEntity.name = name
        getExisting(fund.id)
    }

    override fun updateDescription(fund: Fund, description: String): Fund = transaction {
        val fundEntity = FundEntity[fund.id]
        fundEntity.description = description
        getExisting(fund.id)
    }

    override fun linkUsers(fund: Fund, users: List<User>) = transaction {
        val linkedUserIds = FundsUsers
                .slice(FundsUsers.user)
                .select { FundsUsers.fund eq fund.id }
                .map { it[FundsUsers.user].value }
                .toSet()

        val notLinkedUserIds = users.filterNot { linkedUserIds.contains(it.id) }.map { it.id }

        if (!notLinkedUserIds.isEmpty()) {
            val fundEntityId = EntityID(fund.id, Funds)
            FundsUsers.batchInsert(notLinkedUserIds) {
                this[FundsUsers.fund] = fundEntityId
                this[FundsUsers.user] = EntityID(it, Users)
            }
        }
    }

    override fun unlinkUsers(fund: Fund, users: List<User>): Unit = transaction {
        FundsUsers.deleteWhere {
            (FundsUsers.fund eq fund.id) and (FundsUsers.user inList users.map { it.id })
        }
    }

    override fun getLinkedUsers(fund: Fund): List<User> = transaction {
        val linkedUserIds = FundsUsers
                .slice(FundsUsers.user)
                .select { FundsUsers.fund eq fund.id }
                .map { it[FundsUsers.user] }

        UserEntity.forEntityIds(linkedUserIds).map { ExposedUserRepository.userFromEntity(it) }
    }

    override fun getUserFunds(user: User): List<Fund> = transaction {
        val fundIds = FundsUsers
                .slice(FundsUsers.fund)
                .select { FundsUsers.user eq user.id }
                .map { it[FundsUsers.fund] }

        val supervisorIdsByFund: MutableMap<Long, EntityID<Long>> = mutableMapOf()
        Funds
                .slice(Funds.id, Funds.supervisor)
                .select { Funds.id inList fundIds }
                .forEach { supervisorIdsByFund.put(it[Funds.id].value, it[Funds.supervisor]) }

        val supervisors = UserEntity.forEntityIds(supervisorIdsByFund.values.toList()).toList()

        FundEntity.forEntityIds(fundIds).map {
            val supervisorId = supervisorIdsByFund[it.id.value]
            val supervisorEntity = supervisors.first { it.id == supervisorId }
            fundFromEntity(it, supervisorEntity)
        }
    }
}