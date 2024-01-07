package me.kimihiqq.provider.config

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

object EntityManagerConfig {
    private lateinit var entityManagerFactory: EntityManagerFactory

    fun initialize(persistenceUnitName: String) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName)
    }

    fun createEntityManager(): EntityManager {
        if (!::entityManagerFactory.isInitialized) {
            throw IllegalStateException("EntityManagerFactory is not initialized")
        }
        return entityManagerFactory.createEntityManager()
    }
}
