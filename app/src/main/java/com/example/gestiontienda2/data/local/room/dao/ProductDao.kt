package com.example.gestiontienda2.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestiontienda2.data.local.room.entities.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("UPDATE products SET stockQuantity = :newStock WHERE id = :productId")
    suspend fun updateStockQuantity(productId: Long, newStock: Int)

    @Query("UPDATE products SET reservedStockQuantity = :newReservedStock WHERE id = :productId")
    suspend fun updateReservedStockQuantity(productId: Long, newReservedStock: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Long): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    // Assuming this is the blocking call used in repositories
    @Query("SELECT * FROM products")
    fun getAllProductsBlocking(): List<ProductEntity>
}