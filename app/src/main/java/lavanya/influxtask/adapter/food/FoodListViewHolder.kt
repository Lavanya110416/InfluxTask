package lavanya.influxtask.adapter.food

import android.net.Uri
import android.support.design.chip.Chip
import android.support.design.chip.ChipDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import lavanya.influxtask.R
import lavanya.influxtask.base.BaseViewHolder
import lavanya.influxtask.databinding.ViewHolderFoodItemBinding
import lavanya.influxtask.util.glide.RoundedCornerTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import lavanya.influxmodel.FnblistItem

class FoodListViewHolder(val binding: ViewHolderFoodItemBinding,
                         val listener: View.OnClickListener):
        BaseViewHolder(binding.root){

    private var foodItemModel: FoodItemModel ?= null

    override fun onBind(position: Int, any: Any) {
        val foodItem = any as FnblistItem
        foodItemModel = FoodItemModel(foodItem)
        binding.foodItemVM = foodItemModel
        binding.subItems.visibility = if (foodItem.subitems != null  && foodItem.subitems!!.isNotEmpty()) View.VISIBLE else View.GONE
        binding.subItems.removeAllViews()
        if (foodItem.subitems != null){
            for (i in 0 until foodItem.subitems!!.size){
                val subItem = foodItem.subitems!![i]
                val chipRoot = LayoutInflater.from(binding.subItems.context).inflate(R.layout
                        .view_holder_sub_item, null)

                val chip: Chip = chipRoot.findViewById(R.id.btnSubItem)
                chip.text = subItem.name
                chip.textStartPadding = binding.root.context.resources.getDimension(R.dimen.dp10)
                chip.textEndPadding = binding.root.context.resources.getDimension(R.dimen.dp10)
                if (subItem.isSelected){
                    val chipDrawable = ChipDrawable.createFromResource(binding.root.context, R.xml.chip_selected)
                    chip.setChipDrawable(chipDrawable)
                    chip.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.black))
                }else{
                    val chipDrawable = ChipDrawable.createFromResource(binding.root.context, R.xml.chip)
                    chip.setChipDrawable(chipDrawable)
                    chip.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_color_grey))
                }
                chip.setTag(R.id.subItems, i)
                chip.setTag(R.id.imgFoodItem, position)
                chip.setOnClickListener(listener)
                binding.subItems.addView(chipRoot)
            }

        }
        binding.imgAdd.setTag(R.id.imgFoodItem, position)
        binding.imgAdd.setOnClickListener(listener)
        binding.imgRemove.setTag(R.id.imgFoodItem, position)
        binding.imgRemove.setOnClickListener(listener)

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCornerTransformation(16.0f, 1.0f))

        Glide.with(binding.root.context)
                .load(Uri.parse(foodItem.imageUrl))
                .apply(requestOptions)
                .into(binding.imgFoodItem)
    }


}