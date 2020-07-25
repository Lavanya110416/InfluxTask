package lavanya.influxmodel


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Status(@SerializedName("Description")
                  val description: String = "",
                  @SerializedName("Id")
                  val id: String = "")


data class SubitemsItem(@SerializedName("SubitemPrice")
                        val subitemPrice: Float = 0.0F,
                        @SerializedName("VistaSubFoodItemId")
                        val vistaSubFoodItemId: String = "",
                        @SerializedName("PriceInCents")
                        val priceInCents: String = "",
                        @SerializedName("VistaParentFoodItemId")
                        val vistaParentFoodItemId: String = "",
                        @SerializedName("Name")
                        val name: String = "", var isSelected: Boolean = false)


@Entity(tableName = "food_data")
data class FnblistItem(
        @ColumnInfo(name = "veg_class")
        @SerializedName("VegClass")
                       var vegClass: String = "",
        @ColumnInfo(name = "item_price")
                       @SerializedName("ItemPrice")
                       var itemPrice: Float = 0.0F,
        @ColumnInfo(name = "image_url")
                       @Ignore
                       @SerializedName("ImageUrl")
                       var imageUrl: String = "",
        @ColumnInfo(name = "img_url")
                       @Ignore
                       @SerializedName("ImgUrl")
                       var imgUrl: String = "",
        @ColumnInfo(name = "name")
                       @SerializedName("Name")
                       var name: String = "",
        @ColumnInfo(name = "other_experience")
                       @SerializedName("OtherExperience")
                       var otherExperience: String = "",
        @ColumnInfo(name = "cinemaid")
                       @SerializedName("Cinemaid")
                       var cinemaid: String = "",
        @ColumnInfo(name = "price_in_cents")
                       @Ignore
                       @SerializedName("PriceInCents")
                       var priceInCents: String = "",
        @ColumnInfo(name = "subitems")
        @Ignore
                       @SerializedName("subitems")
                       var subitems: MutableList<SubitemsItem>?,
        @ColumnInfo(name = "seven_star_experience")
                       @Ignore
                       @SerializedName("SevenStarExperience")
                       var sevenStarExperience: String = "",
        @ColumnInfo(name = "tab_name")
                       @Ignore
                       @SerializedName("TabName")
                       var tabName: String = "",
        @ColumnInfo(name = "sub_item_count")
                       @Ignore
                       @SerializedName("SubItemCount")
                       var subItemCount: Int = 0,
        @PrimaryKey
        @ColumnInfo(name = "vista_food_item_id")
                       @SerializedName("VistaFoodItemId")
                       var vistaFoodItemId: String = "",
        @ColumnInfo(name = "cart_count")
        var cartCount: Int = 0){
    constructor() : this("", 0.0F, "" ,"", "", "",
            "", "", null, "", "", 0,
            "", 0)
}


data class FoodListItem(@SerializedName("fnblist")
                        val fnblist: MutableList<FnblistItem>?,
                        @SerializedName("TabName")
                        val tabName: String = "")


data class FoodResponse(@SerializedName("Currency")
                        val currency: String = "",
                        @SerializedName("FoodList")
                        val foodList: List<FoodListItem>?,
                        @SerializedName("status")
                        val status: Status)


