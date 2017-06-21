package ws.routing

import model.Fund
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.routing.*
import service.FundOperations
import ws.*
import ws.util.post

fun Route.fund(fundOperations: FundOperations) {

    route("fund") {
        post<CreateFundRequestDto> { (name, description, supervisorId, userIds) ->
            val fund = fundOperations.createFund(name, description, supervisorId)
            fundOperations.addUsers(fund.id, userIds)

            call.respond(fundToFundDto(fund, fundOperations))
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLong()
            val fund = fundOperations.getFund(id!!)
            call.respond(fundToFundDto(fund, fundOperations))
        }

        get {
            val funds = fundOperations.getFunds()
            val dtos = funds.map { fundToDescriptionDto(it) }
            call.respond(DtoCollection(dtos))
        }

        put("{fundId}/user/{userId}") {
            val fundId = call.parameters["fundId"]!!.toLong()
            val userId = call.parameters["userId"]!!.toLong()

            fundOperations.addUsers(fundId, listOf(userId))
            call.response.status(HttpStatusCode.OK)
        }

        delete("{fundId}/user/{userId}") {
            val fundId = call.parameters["fundId"]!!.toLong()
            val userId = call.parameters["userId"]!!.toLong()

            fundOperations.removeUser(fundId, listOf(userId))
            call.response.status(HttpStatusCode.OK)
        }

        get("{id}/user") {
            val id = call.parameters["id"]!!.toLong()
            val fundUsersWithIds = fundOperations.getFundUsers(id)

            call.respond(DtoCollection(fundUsersWithIds.map { userToDto(it) }))
        }

        get("{fundId}/user/{userId}/balance") {
            val fundId = call.parameters["fundId"]!!.toLong()
            val userId = call.parameters["userId"]!!.toLong()

            val balance = fundOperations.getFundUserBalance(fundId, userId)

            call.respond(FundUserBalanceDto(fundId, userId, balance))
        }
    }
}

fun fundToDescriptionDto(fund: Fund): FundDescriptionDto {
    return FundDescriptionDto(fund.id, fund.name, fund.description)
}

fun fundToFundDto(fund: Fund, fundOperations: FundOperations): FundDto {
    val users = fundOperations.getFundUsers(fund.id).map { userToDto(it) }
    return FundDto(fund.id, fund.name, fund.description, userToDto(fund.supervisor), users)
}


