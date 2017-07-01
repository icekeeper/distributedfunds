package ws.routing

import model.Balance
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.routing.*
import org.jetbrains.ktor.sessions.sessionOrNull
import service.FundOperations
import ws.*
import ws.util.post


fun Route.birthdayFunds(fundOperations: FundOperations) {

    route("fund") {
        authorized {
            post<CreateFundRequestDto> { (name, description, userIds) ->
                val session = call.sessionOrNull<Session>()!!

                val fund = fundOperations.createFund(name, description, session.userId)
                fundOperations.addUsers(fund.id, userIds + session.userId)

                call.respond(fundToFundDto(fund, fundOperations))
            }

            put("{fundId}/user/{userId}") {
                val fundId = call.parameters["fundId"]!!.toLong()
                val userId = call.parameters["userId"]!!.toLong()

                val session = call.sessionOrNull<Session>()!!
                val currentUserId = session.userId

                val fund = fundOperations.getFund(fundId)

                if (fund.supervisor.id != currentUserId) {
                    call.response.status(HttpStatusCode.Forbidden)
                } else {
                    fundOperations.addUsers(fundId, listOf(userId))
                    call.response.status(HttpStatusCode.OK)
                }
            }

            delete("{fundId}/user/{userId}") {
                val fundId = call.parameters["fundId"]!!.toLong()
                val userId = call.parameters["userId"]!!.toLong()

                val session = call.sessionOrNull<Session>()!!
                val currentUserId = session.userId

                val fund = fundOperations.getFund(fundId)

                if (fund.supervisor.id != currentUserId) {
                    call.response.status(HttpStatusCode.Forbidden)
                } else {
                    fundOperations.removeUser(fundId, listOf(userId))
                    call.response.status(HttpStatusCode.OK)
                }
            }
        }

        get {
            val funds = fundOperations.getFunds()
            val dtos = funds.map { fundToDescriptionDto(it) }
            call.respond(DtoCollection(dtos))
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLong()
            val fund = fundOperations.getFund(id!!)
            call.respond(fundToFundDto(fund, fundOperations))
        }
    }

    route("user/fund") {
        authorized {
            get {
                val session = call.sessionOrNull<Session>()!!
                val balances = fundOperations.getFundUserBalances(session.userId)

                call.respond(DtoCollection(balances.map { balanceToUserFundDescriptionDto(it) }))
            }

            get("{fundId}") {
                val session = call.sessionOrNull<Session>()!!
                val fundId = call.parameters["fundId"]!!.toLong()
                val balance = fundOperations.getFundUserBalance(fundId, session.userId)

                call.respond(balanceToUserFundDto(balance, fundOperations))
            }
        }
    }
}

//TODO: use gradations
fun formatBalance(value: Long): BalanceDto {
    return BalanceDto("${value / 100}.${value % 100}", "")
}

fun balanceToUserFundDescriptionDto(balance: Balance): UserFundDescriptionDto {
    return UserFundDescriptionDto(balance.fund.id,
            balance.fund.name,
            balance.fund.description,
            userToDto(balance.fund.supervisor),
            formatBalance(balance.value))
}

fun balanceToUserFundDto(balance: Balance, fundOperations: FundOperations): UserFundDto {
    val fund = balance.fund
    val users = fundOperations.getFundUsers(fund.id).map { userToDto(it) }

    return UserFundDto(fund.id,
            fund.name,
            fund.description,
            userToDto(fund.supervisor),
            users,
            formatBalance(balance.value))
}