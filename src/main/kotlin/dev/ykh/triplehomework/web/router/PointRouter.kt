package dev.ykh.triplehomework.web.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class PointRouter() {

    @Bean
    fun routes() = coRouter {
        "/v1".nest {
            //특정 회원 포인트 조회
            "/points/{userId}".nest {
                listOf(
                    GET("", )
                )
            }
        }
    }
}