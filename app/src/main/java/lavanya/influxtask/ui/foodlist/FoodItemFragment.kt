package lavanya.influxtask.ui.foodlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lavanya.influxtask.BR
import lavanya.influxtask.R
import lavanya.influxtask.adapter.food.FoodDataAdapter
import lavanya.influxtask.database.FoodDatabase
import lavanya.influxtask.databinding.FragmentFoodBinding
import lavanya.influxtask.ui.home.HomeActivity
import lavanya.influxtask.util.BundleKeys
import lavanya.influxtask.util.DBWorkerThread
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_food.*
import lavanya.influxmodel.FnblistItem
import lavanya.influxmodel.FoodListItem


class FoodItemFragment: Fragment(), View.OnClickListener {


    private lateinit var viewModel: FoodItemViewModel
    private var dataBinding: FragmentFoodBinding? = null
    private var adapter: FoodDataAdapter ?= null
    private var foodDb: FoodDatabase ?= null
    private var dbWorkerThread: DBWorkerThread ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false)
        viewModel = ViewModelProviders.of(this).get(FoodItemViewModel::class.java)
        dataBinding?.setVariable(BR.foodItemViewModel, viewModel)

        dbWorkerThread= DBWorkerThread("dbWorkerThread")
        dbWorkerThread?.start()

        foodDb = FoodDatabase.getInstance(activity!!)

        return dataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FoodDataAdapter(ArrayList<FnblistItem>(), this)
        setupRecyclerView()
        viewModel.foodItemData.observe(this, Observer {
            if (it != null){
                viewModel.updateFoodList(it)
            }
        })
        loadFoodItem()
    }

    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val animator = recyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun loadFoodItem(){
        val arguments = getArguments()
        if (arguments != null){
            val foodItemCategory = Gson().fromJson(arguments.getString(BundleKeys.foodItemKey),
                    FoodListItem::class.java)
            viewModel.foodItemData.value = foodItemCategory.fnblist
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnSubItem -> {
                val rootPosition = view.getTag(R.id.imgFoodItem) as Int
                val subItemPosition = view.getTag(R.id.subItems) as Int
                val foodItem = viewModel.foodItemData.value!![rootPosition]
                foodItem.itemPrice = foodItem.subitems!![subItemPosition].subitemPrice
                for (i in 0 until foodItem.subitems!!.size){
                    foodItem.subitems!![i].isSelected = false
                }
                foodItem.subitems!![subItemPosition].isSelected = true
                viewModel.foodItemData.value!![rootPosition] = foodItem
                adapter?.notifyItemChanged(rootPosition, foodItem)
            }
            R.id.imgAdd -> {
                val rootPosition = view.getTag(R.id.imgFoodItem) as Int
                val foodItem = viewModel.foodItemData.value!![rootPosition]
                foodItem.cartCount = foodItem.cartCount + 1
                viewModel.foodItemData.value!![rootPosition] = foodItem
                adapter?.notifyItemChanged(rootPosition, foodItem)
                updateCart(foodItem)
            }
            R.id.imgRemove -> {
                val rootPosition = view.getTag(R.id.imgFoodItem) as Int
                val foodItem = viewModel.foodItemData.value!![rootPosition]
                if (foodItem.cartCount > 0){
                    foodItem.cartCount = foodItem.cartCount - 1
                    viewModel.foodItemData.value!![rootPosition] = foodItem
                    adapter?.notifyItemChanged(rootPosition, foodItem)
                    updateCart(foodItem)
                }
            }
        }
    }

    fun updateCart(foodItem: FnblistItem){
        val task = Runnable {
            if (foodDb?.foodDataDao()?.selectFoodItem(foodItem.vistaFoodItemId) != null &&
                    foodDb?.foodDataDao()?.selectFoodItem(foodItem.vistaFoodItemId)!!.isNotEmpty()){
                foodDb?.foodDataDao()?.update(foodItem.cartCount, foodItem.vistaFoodItemId,
                        foodItem.itemPrice)
            }else{
                foodDb?.foodDataDao()?.insert(foodItem)
            }
        }
        dbWorkerThread?.postTask(task)
        (activity as HomeActivity).updateCartTotal()

    }

    override fun onDestroy() {
        super.onDestroy()
        dbWorkerThread?.quit()
        foodDb?.destroyInstance()
    }

}