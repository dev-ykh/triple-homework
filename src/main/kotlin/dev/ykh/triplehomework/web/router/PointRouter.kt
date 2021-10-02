package dev.ykh.triplehomework.web.router

import dev.ykh.triplehomework.web.handler.PointsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class PointRouter(
    private val pointsHandler: PointsHandler
) {

    @Bean
    fun routes() = coRouter {
        "/v1".nest {
            "/points/{userId}".nest {
                listOf(
                    //특정 회원 포인트 조회
                    GET("", pointsHandler::get),

                    //특정 회원 포인트 적립 조회
                    POST("", pointsHandler::saveAndReturn)
                )
            }
        }
    }
}