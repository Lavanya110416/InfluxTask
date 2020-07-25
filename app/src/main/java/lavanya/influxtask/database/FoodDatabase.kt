package lavanya.influxtask.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import lavanya.influxmodel.FnblistItem

@Database(entities = [FnblistItem::class], version = 1)
abstract class FoodDatabase: RoomDatabase(){

    abstract fun foodDataDao(): FoodDataDao

    companion object {
        val DB_NAME = "foodDatabase.db"
        var instance: FoodDatabase? = null

        fun getInstance(context: Context): FoodDatabase?{
            if (instance == null){
                synchronized(FoodDatabase::class) {
                    instance = Room.databaseBuilder(context, FoodDatabase::class.java, DB_NAME).build()
                }
            }
            return instance
        }
    }

    fun destroyInstance(){
        instance = null
    }

}