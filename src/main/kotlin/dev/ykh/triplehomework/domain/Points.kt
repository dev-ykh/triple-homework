package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.domain.common.EventDetailType
import dev.ykh.triplehomework.domain.common.EventType
import dev.ykh.triplehomework.utils.getUUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import java.util.*

@Table("points")
data class Points(
    val userId: String,

    var point: Int,

    val type: EventType,

    var detailType: EventDetailType,

    val placeId: String,

    val reviewId: String
) {

    @Id
    var id: String = getUUID()

    var createdAt: ZonedDateTime? = null
}