package ws

import com.google.gson.GsonBuilder
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.content.TextContent
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.withCharset
import org.jetbrains.ktor.jetty.Jetty
import org.jetbrains.ktor.logging.CallLogging
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.routing.routing
import org.jetbrains.ktor.transform.transform
import service.StorageBackedFundOperations
import service.StorageBackedUserOperations
import storage.dao.initDao
import storage.exposed.ExposedFundRepository
import storage.exposed.ExposedUserRepository
import ws.dto.Dto


fun main(args: Array<String>) {
    initDao()

    val userStorage = ExposedUserRepository()
    val userOperations = StorageBackedUserOperations(userStorage)

    val fundStorage = ExposedFundRepository()
    val fundOperations = StorageBackedFundOperations(fundStorage, userStorage)

    val gson = GsonBuilder().setPrettyPrinting().create()

    val server = embeddedServer(Jetty, 8080) {
        routing {
            install(CallLogging)

            route("/api/front") {

                user(gson, userOperations)
                fund(gson, fundOperations)

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

