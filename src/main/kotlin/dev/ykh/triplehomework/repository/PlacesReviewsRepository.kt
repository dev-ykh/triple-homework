package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.PlacesReviewsMapping
import dev.ykh.triplehomework.domain.Points
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlacesReviewsRepository: CoroutineCrudRepository<PlacesReviewsMapping, String> {

    suspend fun findTopByPlaceIdAndIsDeletedOrderByCreatedAtAsc(placeId: String, isDeleted: Boolean): PlacesReviewsMapping?

}