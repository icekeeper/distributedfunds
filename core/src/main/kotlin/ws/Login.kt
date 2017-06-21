package ws

import org.jetbrains.ktor.application.ApplicationCallPipeline
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.routing.*
import org.jetbrains.ktor.sessions.session
import org.jetbrains.ktor.sessions.sessionOrNull
import service.UserOperations

data class Session(val userId: Long)

fun Route.login(userOperations: UserOperations) {

    route("login") {
        param("id") {
            post {
                val id = call.parameters["id"]?.toLong()!!
                val user = userOperations.getUser(id)
                call.session(Session(id))
                call.respond(userToDto(user))
            }
        }

        authorized {
            get {
                val session = call.sessionOrNull<Session>()!!
                val user = userOperations.getUser(session.userId)
                call.respond(userToDto(user))
            }
        }
    }
}

fun Route.authorized(build: Route.() -> Unit) {
    intercept(ApplicationCallPipeline.Infrastructure) {
        val session = call.sessionOrNull<Session>()
        if (session != null) {
            this.proceed()
        } else {
            call.response.status(HttpStatusCode.Unauthorized)
            this.finish()
        }
    }
    invoke(build)
}



