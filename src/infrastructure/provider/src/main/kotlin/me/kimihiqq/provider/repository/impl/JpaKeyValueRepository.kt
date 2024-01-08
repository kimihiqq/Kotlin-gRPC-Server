package me.kimihiqq.provider.repository.impl

import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import me.kimihiqq.provider.config.EntityManagerConfig

class JpaKeyValueRepository : KeyValueRepository {

    override fun save(keyValue: KeyValue): KeyValue {
        val entityManager = EntityManagerConfig.createEntityManager()
        return try {
            entityManager.transaction.begin()
            entityManager.persist(keyValue)
            entityManager.transaction.commit()
            keyValue
        } catch (e: Exception) {
            entityManager.transaction.rollback()
            throw e
        } finally {
            entityManager.close()
        }
    }

    override fun findByKey(key: String): KeyValue? {
        val entityManager = EntityManagerConfig.createEntityManager()
        return try {
            entityManager.find(KeyValue::class.java, key)
        } catch (e: Exception) {
            throw e
        } finally {
            entityManager.close()
        }
    }

    override fun deleteByKey(key: String): Boolean {
        val entityManager = EntityManagerConfig.createEntityManager()
        return try {
            val keyValue = entityManager.find(KeyValue::class.java, key) ?: return false
            entityManager.transaction.begin()
            entityManager.remove(keyValue)
            entityManager.transaction.commit()
            true
        } catch (e: Exception) {
            entityManager.transaction.rollback()
            throw e
        } finally {
            entityManager.close()
        }
    }
}
