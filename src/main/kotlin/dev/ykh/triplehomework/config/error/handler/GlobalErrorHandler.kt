package dev.ykh.triplehomework.config.error.handler

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler

import org.springframework.web.server.ServerWebExchange

import com.fasterxml.jackson.databind.ObjectMapper
import dev.ykh.triplehomework.utils.loggerFor
import dev.ykh.triplehomework.web.v1.response.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

private val logger = loggerFor<GlobalErrorHandler>()

@Configuration
@Order(-2)
class GlobalErrorHandler(private val objectMapper: ObjectMapper) : ErrorWebExceptionHandler {

    override fun handle(serverWebExchange: ServerWebExchange, ex: Throwable): Mono<Void> = mono(Dispatchers.IO) {

        logger.error(ex.localizedMessage)
        logger.error(ex.stackTraceToString())

        // Http Status
        //TODO : Exception 세분화
        val statusCode = when {
            ex is Exception -> HttpStatus.INTERNAL_SERVER_ERROR
            else -> HttpStatus.OK
        }
        var errorCode = statusCode.value()

        serverWebExchange.response.statusCode = statusCode
        serverWebExchange.response.headers.contentType = MediaType.APPLICATION_JSON

        val bufferFactory = serverWebExchange.response.bufferFactory()
        val result = Result<String>(
            code = errorCode,
            description = ex.localizedMessage
        )
        val dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(result))
        serverWebExchange.response.writeWith(dataBuffer.toMono()).awaitFirstOrNull()
    }
}