package dev.ykh.triplehomework.web.v1.request

import dev.ykh.triplehomework.domain.common.ActionType
import dev.ykh.triplehomework.domain.common.EventType

data class PointsRequest(
    val type: EventType,
    val action: ActionType,
    val placeId: String,
    val content: String,
    val attachedPhotoIds: Array<String>,
    val reviewId: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointsRequest

        if (placeId != other.placeId) return false
        if (content != other.content) return false
        if (!attachedPhotoIds.contentEquals(other.attachedPhotoIds)) return false
        if (reviewId != other.reviewId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = placeId.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + attachedPhotoIds.contentHashCode()
        result = 31 * result + reviewId.hashCode()
        return result
    }
}