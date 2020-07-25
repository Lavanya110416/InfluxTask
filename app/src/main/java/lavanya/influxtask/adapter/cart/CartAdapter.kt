package lavanya.influxtask.adapter.cart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import lavanya.influxtask.R
import lavanya.influxtask.base.BaseViewHolder
import lavanya.influxmodel.FnblistItem

class CartAdapter(var foodList: List<FnblistItem>): RecyclerView.Adapter<BaseViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_cart_item,
                parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position, foodList[position])
    }

    fun addItems(fnbListItem: List<FnblistItem>) {
        this.foodList = fnbListItem
        notifyDataSetChanged()
    }

}