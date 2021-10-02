package dev.ykh.triplehomework.web.handler

import dev.ykh.triplehomework.domain.common.ActionType
import dev.ykh.triplehomework.service.PointsService
import dev.ykh.triplehomework.utils.loggerFor
import dev.ykh.triplehomework.web.request.v1.PointsRequest
import dev.ykh.triplehomework.web.response.v1.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.util.*

private val logger = loggerFor<PointsHandler>()

@Component
class PointsHandler(
    private val pointsService: PointsService
) {

    /**
     * 특정 유저의 포인트 이력 정보 조회
     */
    suspend fun get(req: ServerRequest): ServerResponse {

        val userId = req.pathVariable("userId")
        return ok().json().bodyValueAndAwait(
            Result(pointsService.getPointsByUserId(userId))
        )
    }

    /**
     * 포인트 적립 (회수 포함)
     */
    suspend fun saveAndReturn(req: ServerRequest): ServerResponse {
        val userId = req.pathVariable("userId")
        val pointsRequest = req.awaitBody<PointsRequest>()

        //TODO : JWT 인증 넣기
        when(pointsRequest.action) {
            //추가 엑션
            ActionType.ADD -> {
                pointsService.add(userId, pointsRequest)
            }
            //수정 엑션
            ActionType.MOD -> {
                pointsService.mod(userId, pointsRequest)
            }
            //삭제 엑션
            ActionType.DELETE -> {
                pointsService.delete(userId, pointsRequest)
            }
            else -> {
                throw Exception("ActionType is not set")
            }
        }

        return ok().json().bodyValueAndAwait(
            Result(pointsService.getPointsByUserId(userId))
        )
    }
}