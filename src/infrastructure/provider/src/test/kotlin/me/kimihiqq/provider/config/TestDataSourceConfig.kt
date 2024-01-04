package me.kimihiqq.provider.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.testcontainers.containers.DockerComposeContainer
import java.util.*
import javax.sql.DataSource

class TestDataSourceConfig(private val environment: DockerComposeContainer<*>) {
    fun createDataSource(): DataSource {
        val properties = Properties()
        properties.load(javaClass.getResourceAsStream("/application.properties"))

        val databaseHost = environment.getServiceHost("local-db", 3306)
        val databasePort = environment.getServicePort("local-db", 3306)
        val jdbcUrl = "jdbc:mysql://$databaseHost:$databasePort/keyvalue_db"

        val config = HikariConfig()
        config.jdbcUrl = jdbcUrl
        config.username = properties.getProperty("database.user")
        config.password = properties.getProperty("database.password")

        config.maximumPoolSize = 10

        return HikariDataSource(config)
    }
}
