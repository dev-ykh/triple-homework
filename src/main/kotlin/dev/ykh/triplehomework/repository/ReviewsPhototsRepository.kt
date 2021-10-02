package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.PlacesReviewsMapping
import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.ReviewsPhototsMapping
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewsPhototsRepository: CoroutineCrudRepository<ReviewsPhototsMapping, Long> {

    suspend fun countByReviewIdAndDeleted(reviewId: String, isDeleted: Boolean): Long
}