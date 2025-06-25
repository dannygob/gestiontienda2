package com.your_app_name.data.local.database.migrations // Make sure this package matches your project structure

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Migration from version 6 to 7
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the 'reservedStockQuantity' column to the 'products' table
        // Default value 0 to handle existing products
        database.execSQL("ALTER TABLE products ADD COLUMN reservedStockQuantity INTEGER NOT NULL DEFAULT 0")

        // Add the 'status' column to the 'orders' table
        // You might need to decide on a default value or handle existing orders
        // For simplicity, we'll add it as nullable initially or with a default status
        // Let's add it with a default value, assuming an 'PENDING' status represented by a string
        // Adjust the DEFAULT value and data type if your status is represented differently (e.g., INTEGER enum)
        database.execSQL("ALTER TABLE orders ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING'")

        // Create the 'order_items' table if it doesn't exist
        // Define the columns and foreign key constraint
        // Adjust column types and constraints based on your OrderItemEntity definition
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS order_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                orderId INTEGER NOT NULL,
                productId INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                price REAL NOT NULL,
                FOREIGN KEY(orderId) REFERENCES orders(id) ON DELETE CASCADE
            )
        """.trimIndent())

        // If you need to create indexes for performance (optional but recommended for foreign keys)
        database.execSQL("CREATE INDEX IF NOT EXISTS index_order_items_orderId ON order_items(orderId)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_order_items_productId ON order_items(productId)")

        // You might need to add logic here if you had existing data in a different format
        // that needs to be migrated into the new 'order_items' table.
        // Based on our previous discussion, it seems the order items weren't stored separately before,
        // so creating the table is likely sufficient for new orders.
    }
}
