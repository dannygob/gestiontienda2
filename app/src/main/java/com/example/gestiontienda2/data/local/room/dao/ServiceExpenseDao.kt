package com.example.gestiontienda2.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.room.entities.entity.ServiceExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceExpenseDao {

    // Aquí asegúrate que el nombre de la tabla coincida con el de la entidad
    @Query("SELECT * FROM service_expenses")
    fun getAllServiceExpenses(): Flow<List<ServiceExpenseEntity>>

    @Query("SELECT * FROM service_expenses WHERE id = :expenseId")
    suspend fun getServiceExpenseById(expenseId: Long): ServiceExpenseEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertServiceExpense(expense: ServiceExpenseEntity): Long

    @Update
    suspend fun updateServiceExpense(expense: ServiceExpenseEntity)

    @Delete
    suspend fun deleteServiceExpense(expense: ServiceExpenseEntity)
}
