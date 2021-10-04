package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.domain.common.EventType
import dev.ykh.triplehomework.utils.getUUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import java.util.*

@Table("places_reviews_mapping")
data class PlacesReviewsMapping(

    val placeId: String,

    val reviewId: String,

    val isDeleted: Boolean
) {

    @Id
    var id: String? = getUUID()

    var createdAt: ZonedDateTime? = null

    var updatedAt: ZonedDateTime? = null
}