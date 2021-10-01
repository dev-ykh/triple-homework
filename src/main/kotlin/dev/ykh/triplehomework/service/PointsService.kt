package dev.ykh.triplehomework.service

import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.repository.PlacesReviewsRepository
import dev.ykh.triplehomework.repository.PointsRepository
import dev.ykh.triplehomework.web.request.v1.PointsRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PointsService(
    private val pointsRepository: PointsRepository,
    private val placesReviewsRepository: PlacesReviewsRepository,
    @Value("\${add.contents.points}") val addContentsPoints: Int,
    @Value("\${add.photots.points}") val addPhototsPoints: Int,
    @Value("\${add.firstreivew.points}") val addFirstReviewPoints: Int,
) {

    fun getPointsByUserId(userId: UUID): Flow<Points>? =
        pointsRepository.findAllByUserId(userId)

    //TODO : 전체 조회가 필요한가?
    //TODO : transactional 추가
    @Transactional
    fun add(userId: UUID, pointsRequest: PointsRequest): Flow<Points>? {

        return runCatching {
            val points: MutableList<Points> = mutableListOf()

            //TODO : 간략화하기
            if(pointsRequest.content.isNotEmpty()) {
                points.add(Points(userId, addContentsPoints, pointsRequest.type, false, pointsRequest.reviewId))
            }

            if(pointsRequest.attachedPhotoIds.isNotEmpty()) {
                points.add(Points(userId, addPhototsPoints, pointsRequest.type, false, pointsRequest.reviewId))
            }

            //TODO : 특정 장소의 첫 리뷰인지 확인
            if(placesReviewsRepository.countByPlaceId(pointsRequest.placeId) == 0L) {
                points.add(Points(userId, addFirstReviewPoints, pointsRequest.type, false, pointsRequest.reviewId))
            }

            pointsRepository.saveAll(points)
        }.getOrNull()
    }
}