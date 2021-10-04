package dev.ykh.triplehomework.service

import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.common.EventDetailType
import dev.ykh.triplehomework.repository.PlacesReviewsRepository
import dev.ykh.triplehomework.repository.PointsRepository
import dev.ykh.triplehomework.repository.ReviewsPhototsRepository
import dev.ykh.triplehomework.utils.getUUID
import dev.ykh.triplehomework.web.v1.request.PointsRequest
import dev.ykh.triplehomework.web.v1.response.PointsSubView
import dev.ykh.triplehomework.web.v1.response.PointsView
import kotlinx.coroutines.flow.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PointsService(
    private val pointsRepository: PointsRepository,
    private val placesReviewsRepository: PlacesReviewsRepository,
    private val reviewsPhototsRepository: ReviewsPhototsRepository,
    @Value("\${add.contents.points}") val addContentsPoints: Int,
    @Value("\${add.photots.points}") val addPhototsPoints: Int,
    @Value("\${add.firstreivew.points}") val addFirstReviewPoints: Int
) {

    private val zero = 0L

    /**
     * 특정 유저의 포인트 이력 조회
     */
    suspend fun getPointsByUserId(userId: String): PointsView {
        val pointsView = PointsView(userId)
        pointsRepository.findAllByUserId(userId)
            ?.collect {
                pointsView.pointList.add(PointsSubView(it.point, it.detailType, it.createdAt))
                pointsView.totalPoints += it.point
            }

        return pointsView
    }

    /**
     * 특정 유저의 리뷰 등록으로 인한 포인트 이력 추가
     */
    @Transactional
    suspend fun add(userId: String, pointsRequest: PointsRequest) {

        val points = mutableListOf<Points>()

        //글자 입력
        //더불어 이전에 해당 place에서 본인이 포인트를 받은 이력이 있는지 확인
        if(pointsRequest.content.isNotEmpty() &&
            pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, pointsRequest.placeId, EventDetailType.ADD_CONTENTS) == zero) {

            points.add(
                Points(userId, addContentsPoints, pointsRequest.type, EventDetailType.ADD_CONTENTS, pointsRequest.placeId,
                    pointsRequest.reviewId)
            )
        }

        //사진 입력
        //더불어 이전에 해당 place에서 본인이 포인트를 받은 이력이 있는지 확인
        if(pointsRequest.attachedPhotoIds.isNotEmpty() &&
            pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, pointsRequest.placeId, EventDetailType.ADD_PHOTOS) == zero) {

            points.add(
                Points(userId, addPhototsPoints, pointsRequest.type, EventDetailType.ADD_PHOTOS, pointsRequest.placeId,
                    pointsRequest.reviewId)
            )
        }

        //특정 place의 첫번째 리뷰
        //해당 place에 리뷰가 등록된 이후에 포인트 적립이 된다는 전제
        //삭제되지 않은 것중 본인의 리뷰가 가장 먼저 등록된 경우인지 파악 (포인트 적립 이력은 무시)
        //더불어 이전에 해당 이유로 본인이 포인트를 받은 이력이 있는지 확인
        if(placesReviewsRepository.findTopByPlaceIdAndIsDeletedOrderByCreatedAtAsc(pointsRequest.placeId, false)
                ?.reviewId == pointsRequest.reviewId &&
            pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, pointsRequest.placeId, EventDetailType.ADD_FIRST_REVIEW) == zero) {

            points.add(
                Points(userId, addFirstReviewPoints, pointsRequest.type, EventDetailType.ADD_FIRST_REVIEW, pointsRequest.placeId,
                    pointsRequest.reviewId)
            )
        }

        points.forEach {
            pointsRepository.save(it)
        }
    }

    /**
     * 특정 유저의 리뷰 수정으로 인한 포인트 이력 추가/회수
     * (리뷰 글(contents)의 경우 필수입력 값으로 판단하는게 합리적이므로 사진의 추가/삭제여부만 이 함수가 호출된다고 전제한다.)
     */
    suspend fun mod(userId: String, pointsRequest: PointsRequest) {

        if(pointsRequest.attachedPhotoIds.isEmpty()) {
            //매핑이 끊겼는지 다시 확인
            //더불어 과거에 해당 리뷰에서 사진 삭제로 포인트 회수를 받은 이력이 있는지 확인 없는 경우에만 insert
            if(reviewsPhototsRepository.countByReviewIdAndIsDeleted(pointsRequest.reviewId, false) == zero &&
                pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, pointsRequest.placeId, EventDetailType.REMOVE_PHOTOS) == zero) {
                Points(userId, -addPhototsPoints, pointsRequest.type, EventDetailType.REMOVE_PHOTOS, pointsRequest.placeId, pointsRequest.reviewId)
            } else { null }
        } else {
            //같은 place에서 이전에 사진 등록으로 포인트를 본인이 받았던 이력이 있는지 확인 (중복 지급 방지)
            //더불어 현재 리뷰글로 매핑된 사진이 없는 경우에만 포인트 지급을 한다.
            if(pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, pointsRequest.placeId, EventDetailType.ADD_PHOTOS) == zero &&
                reviewsPhototsRepository.countByReviewIdAndIsDeleted(pointsRequest.reviewId, false) == zero) {
                Points(userId, addPhototsPoints, pointsRequest.type, EventDetailType.ADD_PHOTOS, pointsRequest.placeId, pointsRequest.reviewId)
            } else { null }
        }?.let {
            pointsRepository.save(it)
        }
    }

    /**
     * 특정 유저의 리뷰 삭제로 인한 포인트 이력 회수
     */
    suspend fun delete(userId: String, pointsRequest: PointsRequest) {

        //리뷰 삭제를 여러번 반복했을때 포인트 회수 이력이 중복으로 들어가지 않도록 하기 위해 리뷰 삭제로 인한 회수 이력이 기 존재하는지 파악
        if(pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, pointsRequest.placeId, EventDetailType.REMOVE_REVIEW) == zero) {
            val points = pointsRepository.findAllByUserIdAndPlaceId(userId, pointsRequest.placeId)
            val pointsEntities = mutableListOf<Points>()

            //이전에 리뷰 삭제를 여러번 반복했을때 포인트 회수 이력이 중복으로 들어가지 않도록 하기 위해 회수 이력이 기 존재하는지 파악
            points?.collect {
                pointsEntities.add(
                    Points(it.userId, -it.point, it.type, EventDetailType.REMOVE_REVIEW, pointsRequest.placeId, pointsRequest.reviewId)
                )
            }
            pointsEntities.forEach {
                pointsRepository.save(it)
            }
        }
    }
}