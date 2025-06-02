import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration_X_Y :
    Migration(X, Y) { // Replace X and Y with your actual old and new database versions
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add reservedStockQuantity column to products table
        database.execSQL("ALTER TABLE products ADD COLUMN reservedStockQuantity INTEGER NOT NULL DEFAULT 0")

        // Add status column to orders table
        database.execSQL("ALTER TABLE orders ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING'")
    }
}