package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.ReviewsPhototsMapping
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewsPhototsRepository: CoroutineCrudRepository<ReviewsPhototsMapping, String> {

    suspend fun countByReviewIdAndIsDeleted(reviewId: String, isDeleted: Boolean): Long
}