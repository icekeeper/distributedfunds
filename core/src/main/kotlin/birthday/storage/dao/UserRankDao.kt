package birthday.storage.dao

import birthday.model.UserRank
import core.storage.dao.Funds
import core.storage.dao.FundsUsers
import core.storage.dao.Users
import org.jetbrains.exposed.sql.Table


object UsersRanks : Table() {
    val user = FundsUsers.reference("user", Users)
    val fund = FundsUsers.reference("fund", Funds)
    val rank = enumeration("rank", UserRank::class.java)
}