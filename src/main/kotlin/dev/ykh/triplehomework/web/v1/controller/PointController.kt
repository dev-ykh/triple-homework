package dev.ykh.triplehomework.web.v1.controller

import dev.ykh.triplehomework.domain.common.ActionType
import dev.ykh.triplehomework.service.PointsService
import dev.ykh.triplehomework.web.v1.request.PointsRequest
import dev.ykh.triplehomework.web.v1.response.PointsView
import dev.ykh.triplehomework.web.v1.response.Result
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class PointController(
    private val pointsService: PointsService
) {

    @GetMapping("/points/{userId}")
    suspend fun getPointsByUserId(
        @PathVariable userId: String
    ): Result<PointsView> {
        return Result(pointsService.getPointsByUserId(userId))
    }

    @PostMapping("/points/{userId}")
    suspend fun saveAndRemovePoint(
        @PathVariable userId: String,
        @RequestBody pointsRequest: PointsRequest
    ): Result<PointsView> {

        //리뷰 관련 엑션 분기
        when(pointsRequest.action) {
            //리뷰 추가
            ActionType.ADD -> {
                pointsService.add(userId, pointsRequest)
            }
            //리뷰 수정
            ActionType.MOD -> {
                pointsService.mod(userId, pointsRequest)
            }
            //리뷰 삭제
            ActionType.DELETE -> {
                pointsService.delete(userId, pointsRequest)
            }
            else -> {
                throw Exception("ActionType is not set")
            }
        }

        return Result(pointsService.getPointsByUserId(userId))
    }
}