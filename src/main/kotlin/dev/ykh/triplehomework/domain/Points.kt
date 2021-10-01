package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.domain.common.EventType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import java.util.*

@Table("points")
data class Points(
    val userId: UUID,

    val point: Int,

    val type: EventType,

    val isDeleted: Boolean = false,

    val reviewId: UUID  //포인트 획득은 리뷰만으로 되지는 않을텐데;;;
) {

    @Id
    var id: UUID? = null

    var createdAt: ZonedDateTime? = null

    var updatedAt: ZonedDateTime? = null
}