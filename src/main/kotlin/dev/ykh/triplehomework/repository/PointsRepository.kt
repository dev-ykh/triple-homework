package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.common.EventDetailType
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PointsRepository: CoroutineCrudRepository<Points, String> {

    @Query(
        value = """
        INSERT INTO points 
            (id, user_id, point, type, detail_type, place_id, review_id)
        VALUES
            (
                :#{#points.id},
                :#{#points.userId},
                :#{#points.point},
                :#{#points.type},
                :#{#points.detailType},
                :#{#points.placeId},
                :#{#points.reviewId}
            )
    """
    )
    suspend fun save(points: Points)

    suspend fun countByUserIdAndPlaceIdAndDetailType(userId: String, placeId: String, detailType: EventDetailType): Long

    fun findAllByUserId(userId: String): Flow<Points>?

    fun findAllByUserIdAndPlaceId(userId: String, placeId: String): Flow<Points>?
}