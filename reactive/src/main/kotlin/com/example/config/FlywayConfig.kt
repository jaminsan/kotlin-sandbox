package com.example.config

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(FlywayProperties::class)
class FlywayConfig {

    @Bean(initMethod = "migrate")
    fun flyway(properties: FlywayProperties): Flyway {
        val schemas = properties.schemas.toTypedArray()

        return Flyway(
            Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(properties.url, properties.user, properties.password)
                .schemas(*schemas)
        )
    }
}