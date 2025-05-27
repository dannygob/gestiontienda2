package com.your_app_name.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.your_app_name.data.local.entities.ServiceExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServiceExpense(serviceExpense: ServiceExpenseEntity)

    @Query("SELECT * FROM service_expenses ORDER BY date DESC")
    fun getAllServiceExpenses(): Flow<List<ServiceExpenseEntity>>

    @Query("SELECT * FROM service_expenses WHERE id = :expenseId")
    suspend fun getServiceExpenseById(expenseId: Int): ServiceExpenseEntity?

    @Update
    suspend fun updateServiceExpense(serviceExpense: ServiceExpenseEntity)

    @Delete
    suspend fun deleteServiceExpense(serviceExpense: ServiceExpenseEntity)
}