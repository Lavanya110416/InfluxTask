package lavanya.influxtask.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import lavanya.influxmodel.FnblistItem

@Dao
interface FoodDataDao {

    @Query("SELECT * FROM food_data WHERE cart_count > 0")
    fun getAll(): MutableList<FnblistItem>

    @Insert(onConflict = REPLACE)
    fun insert(foodData: FnblistItem)

    @Query("UPDATE food_data SET item_price = :itemPrice, cart_count = :cartCount WHERE vista_food_item_id = :foodItemId")
    fun update(cartCount: Int,foodItemId: String, itemPrice: Float)

    @Query("SELECT * FROM food_data WHERE vista_food_item_id = :foodItemId")
    fun selectFoodItem(foodItemId: String): List<FnblistItem>

    @Query("DELETE FROM food_data")
    fun deleteAll()

    @Query("SELECT SUM(item_price * cart_count) as total FROM food_data")
    fun getTotalPrice(): Float

    @Query("SELECT cart_count FROM food_data WHERE vista_food_item_id = :foodItemId")
    fun getCartCount(foodItemId: String): Int


}