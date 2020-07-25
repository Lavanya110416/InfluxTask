package lavanya.influxtask.adapter.cart

import android.databinding.ObservableField
import lavanya.influxmodel.FnblistItem

class CartItemModel(fnblistItem: FnblistItem){
    val cartFoodName: ObservableField<String> = ObservableField(fnblistItem.name+" " +
            "("+fnblistItem.cartCount+")")
    val cartFooPrice: ObservableField<String> = ObservableField((fnblistItem.itemPrice.toInt() * fnblistItem.cartCount).toString())
}