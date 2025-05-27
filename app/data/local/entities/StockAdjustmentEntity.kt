import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_adjustments")
data class StockAdjustmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val adjustmentAmount: Int,
    val reason: String,
    val timestamp: Long
)