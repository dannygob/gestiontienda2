package com.example.gestiontienda2.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.room.entities.ServiceExpenseEntity

@Dao
interface ServiceExpenseDao {
    @Query("SELECT * FROM serviceexpenses")
    fun getAllServiceExpenses(): Flow<List<ServiceExpenseEntity>>

    @Query("SELECT * FROM serviceexpenses WHERE id = :expenseId")
    suspend fun getServiceExpenseById(expenseId: Long): ServiceExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServiceExpense(expense: ServiceExpenseEntity): Long

    @Update
    suspend fun updateServiceExpense(expense: ServiceExpenseEntity)

    @Delete
    suspend fun deleteServiceExpense(expense: ServiceExpenseEntity)
}