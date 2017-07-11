package core.service

import core.model.Balance
import core.model.Fund
import core.model.User


interface FundOperations {

    fun getFunds(): List<Fund>

    fun getFund(fundId: Long): Fund

    fun createFund(name: String, description: String, creatorUserId: Long): Fund

    fun renameFund(fundId: Long, name: String): Fund

    fun changeDescription(fundId: Long, description: String): Fund

    fun addUsers(fundId: Long, userIds: List<Long>)

    fun removeUser(fundId: Long, userIds: List<Long>)

    fun getFundUsers(fundId: Long): List<User>

    fun getFundUserBalance(fundId: Long, userId: Long): Balance

    fun getFundUserBalances(userId: Long): List<Balance>

}

