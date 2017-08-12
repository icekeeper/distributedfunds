package birthday.service

import birthday.model.UserRank
import core.model.Balance
import core.model.User


interface RankService {
    fun calculateRanks(balances: List<Balance>): Map<User, UserRank>
}