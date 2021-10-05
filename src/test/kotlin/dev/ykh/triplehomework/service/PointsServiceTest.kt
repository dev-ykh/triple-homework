package dev.ykh.triplehomework.service

import dev.ykh.triplehomework.domain.PlacesReviewsMapping
import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.common.ActionType
import dev.ykh.triplehomework.domain.common.EventDetailType
import dev.ykh.triplehomework.domain.common.EventType
import dev.ykh.triplehomework.repository.PlacesReviewsRepository
import dev.ykh.triplehomework.repository.PointsRepository
import dev.ykh.triplehomework.repository.ReviewsPhototsRepository
import dev.ykh.triplehomework.service.v1.PointsService
import dev.ykh.triplehomework.web.v1.request.PointsRequest
import io.mockk.*
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class PointsServiceTest {

    private var pointsRepository = mockk<PointsRepository>()
    private var placesReviewsRepository = mockk<PlacesReviewsRepository>()
    private var reviewsPhototsRepository = mockk<ReviewsPhototsRepository>()

    private lateinit var pointsService: PointsService
    private lateinit var userId: String
    private lateinit var placeId: String
    private lateinit var reviewId: String
    private lateinit var points: List<Points>
    private val pointsDeleted: MutableList<Points> = mutableListOf()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        this.pointsService = PointsService(pointsRepository, placesReviewsRepository, reviewsPhototsRepository,
            1,1,1)
        userId = "6b056865-ca6c-4125-988c-f34412e4a880"
        placeId = "ceb98c86-f11e-4e08-8997-82fecccc9e23"
        reviewId = "734fedae-df7b-4968-9006-6362e80e0618"

        points = listOf(
            Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_CONTENTS, placeId, reviewId),
            Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_PHOTOS, placeId, reviewId)
        )

        points.forEach {
            pointsDeleted.add(
                Points(it.userId, -it.point, it.type, EventDetailType.REMOVE_REVIEW, placeId, reviewId)
            )
        }
    }

    @Test
    fun `개인별 포인트 이력 조회`() = runBlockingTest {
        mockingRepository(userId, "")
        val pointsList = pointsService.getPointsByUserId(userId)

        assertNotNull(pointsList)

        coVerify { pointsRepository.findAllByUserId(userId) }
        assertEquals(pointsList.pointList.size, 2)
    }

    @Test
    fun `리뷰 등록 시 포인트 적립_글자`() = runBlockingTest {
        mockingRepository(userId, "withFirst")
        val pointsRequest = PointsRequest(
            type = EventType.REVIEW,
            action = ActionType.ADD,
            placeId = placeId,
            content = "정말 멋진 곳입니다.",
            attachedPhotoIds = emptyArray(),
            reviewId = reviewId
        )
        pointsService.add(userId, pointsRequest)
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_CONTENTS) }
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_FIRST_REVIEW) }
        coVerify { placesReviewsRepository.findTopByPlaceIdAndIsDeletedOrderByCreatedAtAsc(placeId, false) }
        coVerify { pointsRepository.save(any()) }
    }

    @Test
    fun `리뷰 등록 시 포인트 적립_사진`() = runBlockingTest {
        mockingRepository(userId, "withPhotoAndFirst")
        val pointsRequest = PointsRequest(
            type = EventType.REVIEW,
            action = ActionType.ADD,
            placeId = placeId,
            content = "정말 멋진 곳입니다.",
            attachedPhotoIds = arrayOf("ceb98c86-f11e-4e08-8997-82fecccc9e11", "ceb22486-f11e-4e08-8997-82fecccc9e23"),
            reviewId = reviewId
        )
        pointsService.add(userId, pointsRequest)
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_CONTENTS) }
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_PHOTOS) }
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_FIRST_REVIEW) }
        coVerify { placesReviewsRepository.findTopByPlaceIdAndIsDeletedOrderByCreatedAtAsc(placeId, false) }
        coVerify { pointsRepository.save(any()) }
    }

    @Test
    fun `리뷰 등록 시 포인트 적립_첫리뷰`() = runBlockingTest {
        mockingRepository(userId, "withFirst")
        val pointsRequest = PointsRequest(
            type = EventType.REVIEW,
            action = ActionType.ADD,
            placeId = placeId,
            content = "정말 멋진 곳입니다.",
            attachedPhotoIds = emptyArray(),
            reviewId = reviewId
        )
        pointsService.add(userId, pointsRequest)
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_CONTENTS) }
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_FIRST_REVIEW) }
        coVerify { placesReviewsRepository.findTopByPlaceIdAndIsDeletedOrderByCreatedAtAsc(placeId, false) }
        coVerify { pointsRepository.save(any()) }
    }

    @Test
    fun `리뷰 수정 시 포인트 적립`() = runBlockingTest {
        mockingRepository(userId, "onlyPhotoAdd")
        val pointsRequest = PointsRequest(
            type = EventType.REVIEW,
            action = ActionType.MOD,
            placeId = placeId,
            content = "정말 멋진 곳입니다.",
            attachedPhotoIds = arrayOf("ceb98c86-f11e-4e08-8997-82fecccc9e11", "ceb22486-f11e-4e08-8997-82fecccc9e23"),
            reviewId = reviewId
        )
        pointsService.mod(userId, pointsRequest)
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_PHOTOS) }
        coVerify { reviewsPhototsRepository.countByReviewIdAndIsDeleted(reviewId, false) }
        coVerify { pointsRepository.save(any()) }
    }

    @Test
    fun `리뷰 수정 시 포인트 회수`() = runBlockingTest {
        mockingRepository(userId, "onlyPhotoRemove")
        val pointsRequest = PointsRequest(
            type = EventType.REVIEW,
            action = ActionType.MOD,
            placeId = placeId,
            content = "정말 멋진 곳입니다.",
            attachedPhotoIds = emptyArray(),
            reviewId = reviewId
        )
        pointsService.mod(userId, pointsRequest)
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.REMOVE_PHOTOS) }
        coVerify { reviewsPhototsRepository.countByReviewIdAndIsDeleted(reviewId, false) }
        coVerify { pointsRepository.save(any()) }
    }

    @Test
    fun `리뷰 삭제`() = runBlockingTest {
        mockingRepository(userId, "deleteReview")
        val pointsRequest = PointsRequest(
            type = EventType.REVIEW,
            action = ActionType.DELETE,
            placeId = placeId,
            content = "",
            attachedPhotoIds = emptyArray(),
            reviewId = reviewId
        )
        pointsService.delete(userId, pointsRequest)
        coVerify { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.REMOVE_REVIEW) }
        coVerify { pointsRepository.findAllByUserIdAndPlaceId(userId, placeId) }
        coVerify { pointsRepository.save(any()) }
    }

//    @Test
//    fun `UUID`() = runBlockingTest {
//        println(UUID.randomUUID())
//        println(UUID.randomUUID())
//        println(UUID.randomUUID())
//    }

    private fun mockingRepository(userId: String, type: String) {

        val pointsList = mutableListOf<Points>(
            Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_CONTENTS, placeId, reviewId)
        )
        when(type) {
            "withPhotoAndFirst" -> {
                pointsList.addAll(
                    mutableListOf(
                        Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_PHOTOS, placeId, reviewId),
                        Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_FIRST_REVIEW, placeId, reviewId)
                    )
                )
            }
            "withFirst" -> {
                pointsList.add(Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_FIRST_REVIEW, placeId, reviewId))
            }
            "onlyPhotoAdd" -> {
                val point = Points(userId, 1, EventType.REVIEW, EventDetailType.ADD_PHOTOS, placeId, reviewId)
                coEvery { reviewsPhototsRepository.countByReviewIdAndIsDeleted(reviewId, false) } returns 0L
            }
            "onlyPhotoRemove" -> {
                val point = Points(userId, -1, EventType.REVIEW, EventDetailType.REMOVE_PHOTOS, placeId, reviewId)
                coEvery { reviewsPhototsRepository.countByReviewIdAndIsDeleted(reviewId, false) } returns 0L
            }
            "deleteReview" -> {
                coEvery { pointsRepository.findAllByUserIdAndPlaceId(userId, placeId) } returns points.asFlow()
            }
        }

        val placesReviews = PlacesReviewsMapping(placeId, reviewId, false)

        coEvery { pointsRepository.save(any()) } returns Unit
        coEvery { pointsRepository.findAllByUserId(userId) } returns points.asFlow()
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_CONTENTS) } returns 0L
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_PHOTOS) } returns 0L
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_FIRST_REVIEW) } returns 0L
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.REMOVE_PHOTOS) } returns 0L
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.REMOVE_REVIEW) } returns 0L
        coEvery { placesReviewsRepository.findTopByPlaceIdAndIsDeletedOrderByCreatedAtAsc(placeId, false) } returns placesReviews
    }
}