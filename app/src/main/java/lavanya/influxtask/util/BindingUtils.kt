package lavanya.influxtask.util

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import lavanya.influxtask.adapter.cart.CartAdapter
import lavanya.influxtask.adapter.food.FoodDataAdapter
import lavanya.influxmodel.FnblistItem

@BindingAdapter("foodItemModel")
fun foodItemData(recyclerView: RecyclerView, foodItems: List<FnblistItem>){
    val adapter = recyclerView.adapter as FoodDataAdapter
    adapter.addItems(foodItems)
}

@BindingAdapter("cartItemModel")
fun cartItemData(recyclerView: RecyclerView, foodItems: List<FnblistItem>){
    val adapter = recyclerView.adapter as CartAdapter
    adapter.addItems(foodItems)
}