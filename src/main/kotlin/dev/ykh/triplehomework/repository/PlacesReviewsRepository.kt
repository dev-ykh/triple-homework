package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.PlacesReviewsMapping
import dev.ykh.triplehomework.domain.Points
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlacesReviewsRepository: CoroutineCrudRepository<PlacesReviewsMapping, Long> {

    fun findAllByUserId(userId: UUID): Flow<Points>?

    fun countByPlaceId(placeId: UUID): Long
}