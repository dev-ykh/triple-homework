package dev.ykh.triplehomework.config.r2dbc.converter

import org.joda.time.DateTimeZone
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.joda.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@ReadingConverter
class ZonedDateTimeReadConverter : Converter<java.time.LocalDateTime?, ZonedDateTime?> {
    override fun convert(date: java.time.LocalDateTime): ZonedDateTime {
        return date.atZone(ZoneOffset.UTC)
    }
}

@WritingConverter
class ZonedDateTimeWriteConverter : Converter<ZonedDateTime?, LocalDateTime?> {
    override fun convert(zonedDateTime: ZonedDateTime): LocalDateTime {
        val dateTimeZone: DateTimeZone = DateTimeZone.forTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
        return LocalDateTime(zonedDateTime.toInstant().toEpochMilli(), dateTimeZone)
    }
}