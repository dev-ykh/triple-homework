package dev.ykh.triplehomework.config.r2dbc.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "r2dbc.pool")
data class R2dbcPoolProperties(
    val initialSize: Int,
    val maxSize: Int,
    val maxLife: Long,
    val maxCreateConnectionTime: Long,
    val maxIdleTime: Long,
    val host: String,
    val port: Int,
    val db: String,
    val username: String,
    val password: String
)