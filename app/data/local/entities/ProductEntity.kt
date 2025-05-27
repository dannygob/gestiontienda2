package com.your_app_name.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.your_app_name.domain.models.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val barcode: String,
    val purchasePrice: Double,
    val salePrice: Double,
    val category: String,
    val stock: Int,
    val providerId: Int
)

fun ProductEntity.toDomainModel(): Product {
    return Product(
        id = this.id,
        nombre = this.name,
        codigoDeBarras = this.barcode,
        precioCompra = this.purchasePrice,
        precioVenta = this.salePrice,
        categoria = this.category,
        stock = this.stock,
        proveedorID = this.providerId
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id, name = this.nombre, barcode = this.codigoDeBarras, purchasePrice = this.precioCompra,
        salePrice = this.precioVenta, category = this.categoria, stock = this.stock, providerId = this.proveedorID
    )
}