package ws.util

import com.google.gson.Gson
import org.jetbrains.ktor.application.*
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.pipeline.PipelineContext
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.application
import org.jetbrains.ktor.routing.method
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.util.AttributeKey
import ws.Dto
import java.io.InputStream
import java.io.InputStreamReader


class GsonDtoParser(val gson: Gson) {

    companion object Feature : ApplicationFeature<Application, Gson, GsonDtoParser> {
        override val key = AttributeKey<GsonDtoParser>("GsonDtoParser")
        override fun install(pipeline: Application, configure: Gson.() -> Unit): GsonDtoParser {
            return GsonDtoParser(Gson().apply(configure))
        }
    }

    fun <T : Dto> parse(inputStream: InputStream, clazz: Class<T>): T {
        val reader = InputStreamReader(inputStream, Charsets.UTF_8)
        return gson.fromJson<T>(reader, clazz)
    }

}

inline fun <reified T : Dto> Route.post(noinline body: suspend PipelineContext<ApplicationCall>.(T) -> Unit): Route {
    return method(HttpMethod.Post) {
        handle {
            val dtoParser = application.feature(GsonDtoParser)
            val inputStream = call.request.receive<InputStream>()
            val value = dtoParser.parse(inputStream, T::class.java)
            body(this, value)
        }
    }
}

inline fun <reified T : Dto> Route.post(path: String, noinline body: suspend PipelineContext<ApplicationCall>.(T) -> Unit): Route {
    return route(HttpMethod.Post, path) {
        handle {
            val dtoParser = application.feature(GsonDtoParser)
            val inputStream = call.request.receive<InputStream>()
            val value = dtoParser.parse(inputStream, T::class.java)
            body(this, value)
        }
    }
}

