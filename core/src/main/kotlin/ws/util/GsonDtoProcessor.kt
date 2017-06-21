package ws.util

import com.google.gson.Gson
import org.jetbrains.ktor.application.*
import org.jetbrains.ktor.content.TextContent
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpMethod
import org.jetbrains.ktor.http.withCharset
import org.jetbrains.ktor.pipeline.PipelineContext
import org.jetbrains.ktor.pipeline.PipelinePhase
import org.jetbrains.ktor.request.contentCharset
import org.jetbrains.ktor.routing.Route
import org.jetbrains.ktor.routing.application
import org.jetbrains.ktor.routing.method
import org.jetbrains.ktor.routing.route
import org.jetbrains.ktor.util.AttributeKey
import ws.Dto
import ws.DtoCollection
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


class GsonDtoProcessor(val gson: Gson) {

    companion object Feature : ApplicationFeature<Application, Gson, GsonDtoProcessor> {
        private val CallResponsePipelineInterceptPhase = PipelinePhase("CallResponseIntercept")

        override val key = AttributeKey<GsonDtoProcessor>("GsonDtoParser")
        override fun install(pipeline: Application, configure: Gson.() -> Unit): GsonDtoProcessor {
            val gson = Gson().apply(configure)

            pipeline.phases.insertBefore(ApplicationCallPipeline.Infrastructure, CallResponsePipelineInterceptPhase)
            pipeline.intercept(CallResponsePipelineInterceptPhase) { call ->
                call.response.pipeline.intercept(ApplicationResponsePipeline.Transform) {
                    if (subject is Dto) {
                        val response = TextContent(gson.toJson(subject), ContentType.Application.Json.withCharset(Charsets.UTF_8))
                        proceedWith(response)
                    } else if (subject is DtoCollection) {
                        val dtoCollection = (subject as DtoCollection).collection
                        val response = TextContent(gson.toJson(dtoCollection), ContentType.Application.Json.withCharset(Charsets.UTF_8))
                        proceedWith(response)
                    } else {
                        proceed()
                    }
                }
            }

            return GsonDtoProcessor(gson)
        }
    }

    fun <T : Dto> parse(inputStream: InputStream, clazz: Class<T>, charset: Charset?): T {
        val reader = InputStreamReader(inputStream, charset ?: Charsets.UTF_8)
        return gson.fromJson<T>(reader, clazz)
    }

}

inline fun <reified T : Dto> Route.post(noinline body: suspend PipelineContext<ApplicationCall>.(T) -> Unit): Route {
    return method(HttpMethod.Post) {
        handle(body)
    }
}

inline fun <reified T : Dto> Route.post(path: String, noinline body: suspend PipelineContext<ApplicationCall>.(T) -> Unit): Route {
    return route(HttpMethod.Post, path) {
        handle(body)
    }
}

inline fun <reified T : Dto> Route.handle(noinline body: suspend PipelineContext<ApplicationCall>.(T) -> Unit) {
    handle {
        val dtoParser = application.feature(GsonDtoProcessor)
        val inputStream = call.request.receive<InputStream>()
        val value = dtoParser.parse(inputStream, T::class.java, call.request.contentCharset())
        body(this, value)
    }
}

