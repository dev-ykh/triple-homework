package dev.ykh.triplehomework.web.response.v1

import dev.ykh.triplehomework.domain.Points
import java.time.ZonedDateTime
import java.util.*

data class PointsView(
    val userId: UUID,
) {
    var totalPoints: Int = 0
    val pointList: MutableList<PointsSubView> = mutableListOf()
}

data class PointsSubView(
    val point: Int = 0,
    var createdAt: ZonedDateTime? = null
)
