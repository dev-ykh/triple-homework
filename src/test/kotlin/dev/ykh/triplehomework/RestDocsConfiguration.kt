package dev.ykh.triplehomework

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsWebTestClientConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.operation.preprocess.Preprocessors.*

@TestConfiguration
class RestDocsConfiguration {
    @Bean
    fun restDocsWebTestClientConfigurationCustomizer(): RestDocsWebTestClientConfigurationCustomizer {
        return RestDocsWebTestClientConfigurationCustomizer {
            it.operationPreprocessors()
                    .withRequestDefaults(
                            modifyUris()
                                    .scheme("https")
                                    .host("api.ykh.com")
                                    .removePort(),
                            prettyPrint()
                    )
                    .withResponseDefaults(prettyPrint())
        }
    }
}