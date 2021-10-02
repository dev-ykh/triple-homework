package dev.ykh.triplehomework.config.r2dbc

import com.fasterxml.jackson.databind.ObjectMapper
import dev.ykh.triplehomework.config.r2dbc.converter.JsonReadConverter
import dev.ykh.triplehomework.config.r2dbc.converter.JsonWriteConverter
import dev.ykh.triplehomework.config.r2dbc.converter.ZonedDateTimeReadConverter
import dev.ykh.triplehomework.config.r2dbc.converter.ZonedDateTimeWriteConverter
import dev.ykh.triplehomework.config.r2dbc.properties.R2dbcPoolProperties
import io.r2dbc.pool.PoolingConnectionFactoryProvider
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.core.DatabaseClient
import java.time.Duration
import java.util.*

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableR2dbcRepositories(
    entityOperationsRef = "r2dbcEntityTemplate",
    basePackages = ["dev.ykh.triplehomework.repository"]
)
class BaseDatabaseConfig(
    val r2dbcPoolProperties: R2dbcPoolProperties
) {

    companion object {
        const val DRIVER_NAME = "mysql"
    }

    @Bean
    fun reactiveTransactionManager(@Qualifier("connectionFactory") connectionFactory: ConnectionFactory) =
        R2dbcTransactionManager(connectionFactory)

    @Bean
    fun r2dbcEntityTemplate(
        @Qualifier("connectionFactory") connectionFactory: ConnectionFactory,
        r2dbcConverter: R2dbcConverter
    ): R2dbcEntityOperations {
        val client = DatabaseClient.create(connectionFactory)
        return R2dbcEntityTemplate(client, MySqlDialect.INSTANCE, r2dbcConverter)
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {

        return ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, PoolingConnectionFactoryProvider.POOLING_DRIVER)
                .option(ConnectionFactoryOptions.PROTOCOL, DRIVER_NAME)
                .option(ConnectionFactoryOptions.HOST, r2dbcPoolProperties.host)
                .option(ConnectionFactoryOptions.USER, r2dbcPoolProperties.username)
                .option(ConnectionFactoryOptions.PORT, r2dbcPoolProperties.port)
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcPoolProperties.password)
                .option(ConnectionFactoryOptions.DATABASE, r2dbcPoolProperties.db)
                .option(PoolingConnectionFactoryProvider.MAX_SIZE, r2dbcPoolProperties.maxSize)
                .option(PoolingConnectionFactoryProvider.INITIAL_SIZE, r2dbcPoolProperties.initialSize)
                .option(PoolingConnectionFactoryProvider.MAX_IDLE_TIME, Duration.ofSeconds(r2dbcPoolProperties.maxIdleTime))
                .option(PoolingConnectionFactoryProvider.MAX_CREATE_CONNECTION_TIME, Duration.ofSeconds(r2dbcPoolProperties.maxCreateConnectionTime))
                .option(PoolingConnectionFactoryProvider.MAX_LIFE_TIME, Duration.ofMinutes(r2dbcPoolProperties.maxLife))
                .build()
        )
    }

    @Bean
    fun r2dbcMappingContext(
        namingStrategy: ObjectProvider<NamingStrategy>,
        r2dbcCustomConversions: R2dbcCustomConversions
    ): R2dbcMappingContext? {
        val relationalMappingContext = R2dbcMappingContext(
            namingStrategy.getIfAvailable { NamingStrategy.INSTANCE })
        relationalMappingContext.setSimpleTypeHolder(r2dbcCustomConversions.simpleTypeHolder)
        return relationalMappingContext
    }

    @Bean
    fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters = ArrayList<Any>().apply {
            addAll(MySqlDialect.INSTANCE.converters)
            addAll(R2dbcCustomConversions.STORE_CONVERTERS)
            add(ZonedDateTimeReadConverter())
            add(ZonedDateTimeWriteConverter())
            add(JsonReadConverter(ObjectMapper()))
            add(JsonWriteConverter(ObjectMapper()))
        }
        return R2dbcCustomConversions(converters)
    }

    @Bean
    fun r2dbcConverter(mappingContext: R2dbcMappingContext?, r2dbcCustomConversions: R2dbcCustomConversions?) =
        MappingR2dbcConverter(mappingContext!!, r2dbcCustomConversions!!)
}