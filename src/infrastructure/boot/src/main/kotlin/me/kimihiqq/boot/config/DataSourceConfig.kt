package me.kimihiqq.boot.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.*
import javax.sql.DataSource


class DataSourceConfig {
    fun createDataSource(): DataSource {
        val properties = Properties()
        properties.load(javaClass.getResourceAsStream("/application.properties"))

        val config = HikariConfig()
        config.jdbcUrl = properties.getProperty("database.url")
        config.username = properties.getProperty("database.user")
        config.password = properties.getProperty("database.password")

        config.maximumPoolSize = 10

        return HikariDataSource(config)
    }
}
