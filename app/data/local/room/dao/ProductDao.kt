package com.your_app_name.data.local.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.your_app_name.data.local.room.entities.ProductEntity // Assuming ProductEntity exists

@Dao
interface ProductDao {
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