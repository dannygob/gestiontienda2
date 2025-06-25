package com.example.gestiontienda2.data.repository.impl

import android.R.attr.category
import android.R.attr.description
import android.R.attr.type
import android.R.string
import com.example.gestiontienda2.data.local.dao.ServiceExpenseDao
import com.example.gestiontienda2.data.local.entities.entity.ServiceExpenseEntity
import com.example.gestiontienda2.domain.models.ServiceExpense
import com.example.gestiontienda2.domain.repository.ServiceExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ServiceExpenseRepositoryImpl @Inject constructor(
    private val serviceExpenseDao: ServiceExpenseDao
) : ServiceExpenseRepository {

    override suspend fun insertServiceExpense(serviceExpense: ServiceExpense) {
        withContext(Dispatchers.IO) {
            serviceExpenseDao.insertServiceExpense(serviceExpense.toEntity())
        }
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
        withContext(Dispatchers.IO) {
            serviceExpenseDao.updateServiceExpense(serviceExpense.toEntity())
        }
    }

    override suspend fun deleteServiceExpense(serviceExpense: ServiceExpense) {
        withContext(Dispatchers.IO) {
            serviceExpenseDao.deleteServiceExpense(serviceExpense.toEntity())
        }
    }

    override suspend fun getTotalServiceExpenseAmount(startDate: Long?, endDate: Long?): Double {
        return withContext(Dispatchers.IO) {
            serviceExpenseDao.getTotalServiceExpenseAmount(startDate, endDate) ?: 0.0
        }
    }

    override fun getServiceExpensesByDateRange(
        startDate: Long?,
        endDate: Long?,
    ): Flow<List<ServiceExpense>> {
        return serviceExpenseDao.getServiceExpensesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}

// Mapper functions
fun ServiceExpense.toEntity(): ServiceExpenseEntity {
    return ServiceExpenseEntity(
        id = id,
        type = type,
        amount = amount,
        date = string,
        description = description.toString(),
        notes = notes,
        category = category,


    )
}


// Mapper functions
fun ServiceExpenseEntity.toDomain(): ServiceExpense {
    return ServiceExpense(
        id = id,
        type = type,
        amount = amount,
        date = date,
        description = description,
        saleId = TODO(),
        productId = TODO(),
        quantity = TODO(),
        unitPrice = TODO()
    )
}
