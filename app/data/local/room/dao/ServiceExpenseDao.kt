package com.your_app_name.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.your_app_name.data.local.room.entities.ServiceExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceExpenseDao {
    @Query("SELECT * FROM serviceexpenses")
    fun getAllServiceExpenses(): Flow<List<ServiceExpenseEntity>>

    @Query("SELECT * FROM serviceexpenses WHERE id = :expenseId")
    suspend fun getServiceExpenseById(expenseId: Int): ServiceExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServiceExpense(expense: ServiceExpenseEntity): Long

    @Update
    suspend fun updateServiceExpense(expense: ServiceExpenseEntity)

    @Delete
    suspend fun deleteServiceExpense(expense: ServiceExpenseEntity)
}