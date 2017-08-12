package birthday.storage.exposed

import birthday.model.UserRank
import birthday.storage.UserRankRepository
import birthday.storage.dao.UsersRanks
import core.model.Fund
import core.model.User
import core.storage.dao.Funds
import core.storage.dao.FundsUsers
import core.storage.dao.UserEntity
import core.storage.dao.Users
import core.storage.exposed.ExposedUserRepository
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


class ExposedUserRankRepository : UserRankRepository {

    override fun getUserRank(user: User, fund: Fund): UserRank = transaction {
        UsersRanks.slice(UsersRanks.rank)
                .select { (UsersRanks.fund eq fund.id) and (UsersRanks.user eq user.id) }
                .map { it[UsersRanks.rank] }
                .firstOrNull() ?: UserRank.ZEN
    }

    override fun getUsersRanks(fund: Fund): Map<User, UserRank> = transaction {
        val userIdToRankMap = FundsUsers.innerJoin(UsersRanks)
                .slice(UsersRanks.rank, FundsUsers.user)
                .select { (UsersRanks.fund eq fund.id) }
                .map { Pair(it[FundsUsers.user].value, it.tryGet(UsersRanks.rank) ?: UserRank.ZEN) }
                .toMap()

        val users = UserEntity.forIds(userIdToRankMap.keys.toList()).map { ExposedUserRepository.userFromEntity(it) }

        userIdToRankMap.mapKeys { key -> users.first { it.id == key.key } }
    }

    override fun saveUsersRanks(fund: Fund, ranks: Map<User, UserRank>): Unit = transaction {
        val fundEntityId = EntityID(fund.id, Funds)
        UsersRanks.batchInsert(ranks.entries) { entry ->
            this[UsersRanks.fund] = fundEntityId
            this[UsersRanks.user] = EntityID(entry.key.id, Users)
            this[UsersRanks.rank] = entry.value
        }
    }
}