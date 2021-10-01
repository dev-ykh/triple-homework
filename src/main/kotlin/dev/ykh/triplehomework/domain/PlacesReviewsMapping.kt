package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.domain.common.EventType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import java.util.*

@Table("places_reviews_mapping")
data class PlacesReviewsMapping(
    val placeId: UUID,
    val reviewId: UUID
) {

    @Id
    var id: UUID? = null

    var createdAt: ZonedDateTime? = null
}