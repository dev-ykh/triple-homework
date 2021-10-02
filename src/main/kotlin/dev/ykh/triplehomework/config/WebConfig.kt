package dev.ykh.triplehomework.config

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import dev.ykh.triplehomework.domain.common.DateFormat.DATE_FORMAT
import dev.ykh.triplehomework.domain.common.DateFormat.DATE_TIME_FORMAT
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.time.format.DateTimeFormatter


@Configuration
@ConfigurationPropertiesScan(basePackages = ["dev.ykh.triplehomework"])
internal class WebConfig : WebFluxConfigurer {

    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer = Jackson2ObjectMapperBuilderCustomizer {
        it.simpleDateFormat(DATE_TIME_FORMAT)
            .serializers(LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)))
            .serializers(LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
            .serializers(ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
    }
}