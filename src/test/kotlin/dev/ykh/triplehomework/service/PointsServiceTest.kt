package dev.ykh.triplehomework.service

import dev.ykh.triplehomework.domain.PlacesReviewsMapping
import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.common.ActionType
import dev.ykh.triplehomework.domain.common.EventDetailType
import dev.ykh.triplehomework.domain.common.EventType
import dev.ykh.triplehomework.repository.PlacesReviewsRepository
import dev.ykh.triplehomework.repository.PointsRepository
import dev.ykh.triplehomework.repository.ReviewsPhototsRepository
import dev.ykh.triplehomework.web.request.v1.PointsRequest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
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

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        this.pointsService = PointsService(pointsRepository, placesReviewsRepository, reviewsPhototsRepository,
            1,1,1)
        userId = "6b056865-ca6c-4125-988c-f34412e4a880"
        placeId = "ceb98c86-f11e-4e08-8997-82fecccc9e23"
        reviewId = "734fedae-df7b-4968-9006-6362e80e0618"

        points = listOf(
            Points(
                userId,
                1,
                EventType.REVIEW,
                EventDetailType.ADD_CONTENTS,
                placeId,
                reviewId
            ),
            Points(
                userId,
                1,
                EventType.REVIEW,
                EventDetailType.ADD_PHOTOS,
                placeId,
                reviewId
            )
        )
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
    fun `리뷰 등록 시 포인트 적입_글자`() = runBlockingTest {
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
        coVerify { placesReviewsRepository.findByPlaceIdAndDeletedOrderByCreatedAtAsc(placeId, false) }
    }

    @Test
    fun `리뷰 등록 시 포인트 적입_사진`() = runBlockingTest {
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
        coVerify { placesReviewsRepository.findByPlaceIdAndDeletedOrderByCreatedAtAsc(placeId, false) }
    }

    @Test
    fun `UUID`() = runBlockingTest {
        println(UUID.randomUUID())
        println(UUID.randomUUID())
        println(UUID.randomUUID())
    }

    private fun mockingRepository(userId: String, type: String) {

        val pointsContentAndFirst = mutableListOf<Points>(
            Points(
                userId,
                1,
                EventType.REVIEW,
                EventDetailType.ADD_CONTENTS,
                placeId,
                reviewId
            )
        )

        if(type == "withPhotoAndFirst") {
            pointsContentAndFirst.add(Points(
                userId,
                1,
                EventType.REVIEW,
                EventDetailType.ADD_PHOTOS,
                placeId,
                reviewId
            ))
            pointsContentAndFirst.add(Points(
                userId,
                1,
                EventType.REVIEW,
                EventDetailType.ADD_FIRST_REVIEW,
                placeId,
                reviewId
            ))
        }

        if(type == "withFirst") {
            pointsContentAndFirst.add(Points(
                userId,
                1,
                EventType.REVIEW,
                EventDetailType.ADD_FIRST_REVIEW,
                placeId,
                reviewId
            ))
        }

        val placesReviews = PlacesReviewsMapping(placeId, reviewId, false)

        coEvery { pointsRepository.findAllByUserId(userId) } returns points.asFlow()
        coEvery { pointsRepository.saveAll(pointsContentAndFirst) } returns pointsContentAndFirst.asFlow()
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_CONTENTS) } returns 0L
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_PHOTOS) } returns 0L
        coEvery { pointsRepository.countByUserIdAndPlaceIdAndDetailType(userId, placeId, EventDetailType.ADD_FIRST_REVIEW) } returns 0L
        coEvery { placesReviewsRepository.findByPlaceIdAndDeletedOrderByCreatedAtAsc(placeId, false) } returns placesReviews
    }
}