import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar la columna 'reservedStockQuantity' a la tabla 'products'
        database.execSQL("ALTER TABLE products ADD COLUMN reservedStockQuantity INTEGER NOT NULL DEFAULT 0")

        // Agregar la columna 'status' a la tabla 'orders'
        database.execSQL("ALTER TABLE orders ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING'")
    }
}
