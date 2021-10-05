package dev.ykh.triplehomework.domain

import dev.ykh.triplehomework.utils.getUUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.ZonedDateTime

@Table("photots")
data class Photots(
    val phototUrl: String,

    val isDeleted: Boolean = false,
) {

    @Id
    var id: String? = getUUID()

    var createdAt: ZonedDateTime? = null

    var updatedAt: ZonedDateTime? = null
}