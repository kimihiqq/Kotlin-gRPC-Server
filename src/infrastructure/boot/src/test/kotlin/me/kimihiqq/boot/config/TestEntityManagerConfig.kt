package me.kimihiqq.boot.config

import me.kimihiqq.provider.config.EntityManagerConfig
import org.testcontainers.containers.DockerComposeContainer

class TestEntityManagerConfig(private val environment: DockerComposeContainer<*>) {
    fun initialize() {
        val databaseHost = environment.getServiceHost("local-db", 3306)
        val databasePort = environment.getServicePort("local-db", 3306)
        val jdbcUrl = "jdbc:mysql://$databaseHost:$databasePort/keyvalue_db"

        val properties = HashMap<String, Any>().apply {
            put("javax.persistence.jdbc.url", jdbcUrl)
            put("javax.persistence.jdbc.user", "root")
            put("javax.persistence.jdbc.password", "password")
            put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect")
        }

        EntityManagerConfig.initializeWithProperties(properties)
    }
}
