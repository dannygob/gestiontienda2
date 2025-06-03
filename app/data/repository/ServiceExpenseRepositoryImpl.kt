package com.yourcompany.app.data.repository

import com.yourcompany.app.data.local.dao.ServiceExpenseDao
// Assuming you have a DateConverter, if not, this import might be unused or problematic
// import com.yourcompany.app.data.local.database.DateConverter
import com.yourcompany.app.data.local.entities.ServiceExpenseEntity // Assuming this path is correct
import com.yourcompany.app.domain.models.ServiceExpense
import com.yourcompany.app.domain.repository.ServiceExpenseRepository
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

    override fun getServiceExpensesByDateRange(
        startDate: Long?,
        endDate: Long?
    ): Flow<List<ServiceExpense>> {
        return serviceExpenseDao.getServiceExpensesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}

// Mapper functions (you might want to put these in a separate mapping file)
// Ensure ServiceExpenseEntity has fields: id, type, amount, date, description
// ServiceExpense domain model requires: id, type, description, date, amount, category
fun ServiceExpense.toEntity(): ServiceExpenseEntity {
    return ServiceExpenseEntity(
        id = id,
        type = type,
        amount = amount,
        date = date,
        description = description
        // category and notes are not mapped to entity
    )
}

fun ServiceExpenseEntity.toDomain(): ServiceExpense {
    return ServiceExpense(
        id = id,
        type = type,
        description = description, // Assuming description is present in ServiceExpenseEntity
        date = date,
        amount = amount,
        category = "", // Placeholder for missing category
        notes = null    // Placeholder for missing notes
    )
}