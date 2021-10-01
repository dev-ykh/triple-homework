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

        val userId = UUID.fromString(req.pathVariable("userId"))
        val pointView = PointsView(userId)

        pointsService.getPointsByUserId(userId)
            ?.collect {
                pointView.pointList.add(PointsSubView(it.point, it.createdAt))
                pointView.totalPoints += it.point
            }

        return ok().json().bodyValueAndAwait(Result(pointView))
    }

    /**
     * 포인트 적립
     */
    suspend fun save(req: ServerRequest): ServerResponse {
        val userId = UUID.fromString(req.pathVariable("userId"))

        val pointsRequest = req.awaitBody<PointsRequest>()
        val pointsView = PointsView(userId)

        //TODO : 글자가 1자 이상이면 1점
        //TODO : 그림이 1장 이상이면 1점
        //TODO : 특정 장소에서 처음이면 1점
        //TODO : JWT 인증 넣기
        when(pointsRequest.action) {
            //추가
            ActionType.ADD -> {
                pointsService.add(userId, pointsRequest)?.collect {
                    pointsView.pointList.add(PointsSubView(it.point, it.createdAt))
                    pointsView.totalPoints += it.point
                }
            }
            //수정
            ActionType.MOD -> {
                println(1)
            }
            //삭제
            ActionType.DELETE -> {
                println(1)
            }
            else -> println(1)
        }

        return ok().json().bodyValueAndAwait(Result(pointsView))
    }
}