package com.gestiontienda2.data.repository

import com.gestiontienda2.data.local.dao.ServiceExpenseDao
import com.gestiontienda2.data.local.entities.ServiceExpenseEntity
import com.gestiontienda2.domain.models.ServiceExpense
import com.gestiontienda2.domain.repository.ServiceExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServiceExpenseRepositoryImpl @Inject constructor(
    private val serviceExpenseDao: ServiceExpenseDao
) : ServiceExpenseRepository {

    override suspend fun insertServiceExpense(serviceExpense: ServiceExpense) {
        serviceExpenseDao.insertServiceExpense(serviceExpense.toEntity())
    }

    override fun getAllServiceExpenses(): Flow<List<ServiceExpense>> {
        return serviceExpenseDao.getAllServiceExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getServiceExpenseById(id: Int): Flow<ServiceExpense?> {
        return serviceExpenseDao.getServiceExpenseById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun updateServiceExpense(serviceExpense: ServiceExpense) {
        serviceExpenseDao.updateServiceExpense(serviceExpense.toEntity())
    }

    override suspend fun deleteServiceExpense(serviceExpense: ServiceExpense) {
        serviceExpenseDao.deleteServiceExpense(serviceExpense.toEntity())
    }

    override suspend fun getTotalServiceExpenseAmount(startDate: Long?, endDate: Long?): Double {
        return serviceExpenseDao.getTotalServiceExpenseAmount(startDate, endDate) ?: 0.0
    }
}

override fun getServiceExpensesByDateRange(
    startDate: Long?,
    endDate: Long?
): Flow<List<ServiceExpense>> {
    return serviceExpenseDao.getServiceExpensesByDateRange(startDate, endDate).map { entities ->
        entities.map { it.toDomain() }
    }
}

// Mapper functions (you might want to put these in a separate mapping file)
fun ServiceExpense.toEntity(): ServiceExpenseEntity {
    return ServiceExpenseEntity(
        id = id,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}

fun ServiceExpenseEntity.toDomain(): ServiceExpense {
    return ServiceExpense(
        id = id,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}