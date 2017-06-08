package ws

import com.google.gson.Gson
import model.Fund
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.receive
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.routing.*
import service.FundOperations
import ws.dto.CreateFundRequestDto
import ws.dto.FundDescriptionDto
import ws.dto.FundDto

fun Route.fund(gson: Gson, fundOperations: FundOperations) {

    route("fund") {
        post {
            val body = call.request.receive<String>()
            val createFundRequest = gson.fromJson(body, CreateFundRequestDto::class.java)

            val fund = fundOperations.createFund(createFundRequest.name, createFundRequest.description, createFundRequest.supervisorId)
            fundOperations.addUsers(fund.id, createFundRequest.userIds)

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
            call.respond(dtos)
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

            call.respond(fundUsersWithIds.map { userToDto(it) })
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


