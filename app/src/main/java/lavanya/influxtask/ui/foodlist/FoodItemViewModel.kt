package lavanya.influxtask.ui.foodlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import lavanya.influxmodel.FnblistItem
import lavanya.influxmodel.SubitemsItem

class FoodItemViewModel: ViewModel() {

    var foodItemData = MutableLiveData<MutableList<FnblistItem>>()
    var observeFoodItemData = ObservableField<List<FnblistItem>>()

    fun updateFoodList(fnblistItem: List<FnblistItem>){
        observeFoodItemData.set(fnblistItem)
    }

    fun foodSubItemSelected(subitemsItem: SubitemsItem, rootPosition: Int, subItemPosition: Int){
        val foodItem = foodItemData.value!![rootPosition]
        foodItem.itemPrice = subitemsItem.subitemPrice
        for (i in 0 until foodItem.subitems!!.size){
            foodItem.subitems!![i].isSelected = false
        }
        foodItem.subitems!![subItemPosition].isSelected = true
        foodItemData.value!![rootPosition] = foodItem
    }

}