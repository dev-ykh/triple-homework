package dev.ykh.triplehomework.config.r2dbc.converter

import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.convert.WritingConverter

@ReadingConverter
class JsonReadConverter(private val objectMapper: ObjectMapper) : Converter<String?, HashMap<String, Any?>?> {

    override fun convert(json: String): HashMap<String, Any?>? {
        return runCatching {
            objectMapper.readValue(json, object: TypeReference<HashMap<String, Any?>>() {})
        }.getOrElse { HashMap() }
    }
}

@WritingConverter
class JsonWriteConverter(private val objectMapper: ObjectMapper) : Converter<HashMap<String, Any?>?, String?> {
    override fun convert(map: HashMap<String, Any?>): String? {
        return runCatching {
            objectMapper.writeValueAsString(map)
        }.getOrNull()
    }
}