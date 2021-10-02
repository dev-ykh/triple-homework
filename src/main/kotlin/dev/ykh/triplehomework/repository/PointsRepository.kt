package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.common.EventDetailType
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PointsRepository: CoroutineCrudRepository<Points, Long> {

    suspend fun countByUserIdAndPlaceIdAndDetailType(userId: String, placeId: String, detailType: EventDetailType): Long

    fun findAllByUserId(userId: String): Flow<Points>?

    fun findAllByUserIdAndPlaceId(userId: String, placeId: String): Flow<Points>?
}