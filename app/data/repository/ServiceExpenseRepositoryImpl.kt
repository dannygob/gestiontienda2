package com.your_app_name.data.repository

import com.your_app_name.data.local.room.dao.ServiceExpenseDao
// Assuming you have a DateConverter, if not, this import might be unused or problematic
// import com.your_app_name.data.local.database.DateConverter
import com.your_app_name.data.local.room.entities.ServiceExpenseEntity // Assuming this path is correct
import com.your_app_name.domain.models.ServiceExpense
import com.your_app_name.domain.repository.ServiceExpenseRepository
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
// Ensure ServiceExpenseEntity has fields: id, type, amount, date, description, category, notes
// ServiceExpense domain model requires: id, type, description, date, amount, category, notes
fun ServiceExpense.toEntity(): ServiceExpenseEntity {
    return ServiceExpenseEntity(
        id = id,
        // type = type, // Domain 'type' is not mapped to entity 'type' which is usually an Int or String code.
        description = description,
        date = date,
        amount = amount,
        category = category,
        notes = notes
    )
}

fun ServiceExpenseEntity.toDomain(): ServiceExpense {
    return ServiceExpense(
        id = id,
        type = this.category, // Temporary: using entity's category for domain's type
        description = description,
        date = date,
        amount = amount,
        category = category,
        notes = notes
    )
}