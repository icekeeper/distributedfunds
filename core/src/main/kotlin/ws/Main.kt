package ws

import com.google.gson.GsonBuilder
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.application.receive
import org.jetbrains.ktor.content.TextContent
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.http.withCharset
import org.jetbrains.ktor.jetty.Jetty
import org.jetbrains.ktor.logging.CallLogging
import org.jetbrains.ktor.routing.*
import org.jetbrains.ktor.transform.transform
import service.StorageBackedFundOperations
import service.StorageBackedUserOperations
import storage.dao.initDao
import storage.exposed.ExposedFundStorage
import storage.exposed.ExposedUserStorage
import ws.dto.*


fun main(args: Array<String>) {
    initDao()

    val userStorage = ExposedUserStorage()
    val userOperations = StorageBackedUserOperations(userStorage)

    val fundStorage = ExposedFundStorage()
    val fundOperations = StorageBackedFundOperations(fundStorage, userStorage)

    val gson = GsonBuilder().setPrettyPrinting().create()

    val userConverter = UserConverter()
    val fundConverter = FundConverter(fundOperations, userConverter)
    val fundDescriptionConverter = FundDescriptionConverter()

    val server = embeddedServer(Jetty, 8080) {
        routing {
            install(CallLogging)

            route("/api/front") {
                route("user") {
                    post {
                        val body = call.request.receive<String>()
                        val createUserRequest = gson.fromJson(body, CreateUserRequestDto::class.java)

                        val userWithId = userOperations.registerUser(createUserRequest.login, createUserRequest.name)
                        call.respond(userConverter.toDto(userWithId))
                    }

                    get("{id}") {
                        val id = call.parameters["id"]?.toLong()
                        val userWithId = userOperations.getUser(id!!)
                        call.respond(userConverter.toDto(userWithId))
                    }

                }

                route("fund") {
                    post {
                        val body = call.request.receive<String>()
                        val createFundRequest = gson.fromJson(body, CreateFundRequestDto::class.java)

                        val fundWithId = fundOperations.createFund(createFundRequest.name, createFundRequest.description, createFundRequest.supervisorId)
                        call.respond(fundConverter.toDto(fundWithId))
                    }

                    get("{id}") {
                        val id = call.parameters["id"]?.toLong()
                        val fundWithId = fundOperations.getFund(id!!)
                        call.respond(fundConverter.toDto(fundWithId))
                    }

                    get {
                        val descriptions = fundOperations.getDescriptions()
                        val dtos = descriptions.map { fundDescriptionConverter.toDto(it) }
                        call.respond(dtos)
                    }

                    put("{fundId}/user/{userId}") {
                        val fundId = call.parameters["fundId"]!!.toLong()
                        val userId = call.parameters["userId"]!!.toLong()

                        val added = fundOperations.addUser(fundId, userId)

                        if (added) {
                            call.response.status(HttpStatusCode.Created)
                        } else {
                            call.response.status(HttpStatusCode.OK)
                        }
                    }

                    delete("{fundId}/user/{userId}") {
                        val fundId = call.parameters["fundId"]!!.toLong()
                        val userId = call.parameters["userId"]!!.toLong()

                        fundOperations.removeUser(fundId, userId)
                        call.response.status(HttpStatusCode.OK)
                    }

                    get("{id}/user") {
                        val id = call.parameters["id"]!!.toLong()
                        val fundUsersWithIds = fundOperations.getFundUsersWithIds(id)

                        call.respond(fundUsersWithIds.map { userConverter.toDto(it) })
                    }
                }

            }
        }

        transform.register<Dto> {
            TextContent(gson.toJson(it), ContentType.Application.Json.withCharset(Charsets.UTF_8))
        }

        transform.register<Iterable<Dto>> {
            TextContent(gson.toJson(it), ContentType.Application.Json.withCharset(Charsets.UTF_8))
        }


    }
    server.start(wait = true)
}


