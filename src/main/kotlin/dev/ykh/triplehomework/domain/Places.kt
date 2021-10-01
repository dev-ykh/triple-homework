package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.domain.common.EventType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime
import java.util.*

@Table("places")
data class Places(
    val placeName: String,

    val isDeleted: Boolean = false,
) {

    @Id
    var id: UUID? = null

    var createdAt: ZonedDateTime? = null

    var updatedAt: ZonedDateTime? = null
}