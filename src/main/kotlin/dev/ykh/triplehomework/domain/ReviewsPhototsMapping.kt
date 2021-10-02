package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.domain.common.EventType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import java.util.*

@Table("reviews_photots_mapping")
data class ReviewsPhototsMapping(
    val reviewId: Long,

    val photoId: Long,

    val isDeleted: Boolean
) {

    @Id
    var id: String? = null

    var createdAt: ZonedDateTime? = null

    var updatedAt: ZonedDateTime? = null
}