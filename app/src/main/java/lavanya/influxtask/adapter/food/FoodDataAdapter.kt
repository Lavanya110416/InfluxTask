package lavanya.influxtask.adapter.food

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lavanya.influxtask.base.BaseViewHolder
import lavanya.influxtask.databinding.ViewHolderFoodItemBinding
import lavanya.influxmodel.FnblistItem

class FoodDataAdapter(var foodList: List<FnblistItem>, val listener: View.OnClickListener)
    : RecyclerView
.Adapter<BaseViewHolder>(){

    override fun onCreateViewHolder(container: ViewGroup, position: Int): BaseViewHolder {
        val view = ViewHolderFoodItemBinding.inflate(LayoutInflater.from(container.context),
                container, false)
        return FoodListViewHolder(view, listener)
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