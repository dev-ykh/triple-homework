package dev.ykh.triplehomework.web.v1.response

import dev.ykh.triplehomework.domain.common.EventDetailType
import java.time.ZonedDateTime

data class PointsView(
    val userId: String,
) {
    var totalPoints: Int = 0
    val pointList: MutableList<PointsSubView> = mutableListOf()
}

data class PointsSubView(
    val point: Int = 0,
    val type: EventDetailType? = null,
    var createdAt: ZonedDateTime? = null
)
