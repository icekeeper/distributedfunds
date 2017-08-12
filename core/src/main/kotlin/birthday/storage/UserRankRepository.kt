package birthday.storage

import birthday.model.UserRank
import core.model.Fund
import core.model.User


interface UserRankRepository {

    fun getUserRank(user: User, fund: Fund): UserRank

    fun getUsersRanks(fund: Fund): Map<User, UserRank>

    fun saveUsersRanks(fund: Fund, ranks: Map<User, UserRank>)
}