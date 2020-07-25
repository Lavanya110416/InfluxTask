package lavanya.influxtask.ui.home

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import lavanya.influxtask.BR
import lavanya.influxtask.R
import lavanya.influxtask.adapter.FoodCategoryAdapter
import lavanya.influxtask.adapter.cart.CartAdapter
import lavanya.influxtask.base.BaseActivity
import lavanya.influxtask.database.FoodDatabase
import lavanya.influxtask.databinding.ActivityHomeBinding
import lavanya.influxtask.ui.foodlist.FoodItemFragment
import lavanya.influxtask.util.BundleKeys
import lavanya.influxtask.util.DBWorkerThread
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import lavanya.influxmodel.FnblistItem
import android.support.design.widget.TabLayout
import android.graphics.Typeface
import android.widget.TextView






class HomeActivity: BaseActivity(), View.OnClickListener{

    private lateinit var viewModel: HomeItemViewModel
    private lateinit var adapter: FoodCategoryAdapter
    private var dataBinding: ActivityHomeBinding? = null
    private var foodDb: FoodDatabase?= null
    private var dbWorkerThread: DBWorkerThread?= null
    private var cartItemAdapter: CartAdapter ? = null

    override fun getContentView(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableBackButton()
        setTitle("F & B")
        viewModel = HomeItemViewModel(application)
        viewModel.foodItemData.observe(this, Observer {
            if (it != null){
                viewModel.updateFoodCategory(it)
            }
        })

        dataBinding = DataBindingUtil.setContentView(this, getContentView())
        dataBinding?.setVariable(BR.homeViewModel, viewModel)
        dataBinding?.executePendingBindings()
        adapter = FoodCategoryAdapter(supportFragmentManager)

        dbWorkerThread= DBWorkerThread("dbWorkerThread")
        dbWorkerThread?.start()

        foodDb = FoodDatabase.getInstance(this)

        val task = Runnable {
            foodDb?.foodDataDao()?.deleteAll()
        }
        dbWorkerThread?.postTask(task)

        lblPay.setOnClickListener(this)

        loadFoodCategory()
        cartItemAdapter = CartAdapter(ArrayList<FnblistItem>())
        setupRecyclerView()
        applyBottomSheet()
        tabSelectedBold()
    }

    private fun tabSelectedBold() {

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.typeface = Typeface.DEFAULT_BOLD
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.typeface = Typeface.DEFAULT
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView = tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.typeface = Typeface.DEFAULT_BOLD
            }
        })

    }

    private fun loadCartItems(){
        if (viewModel.fnbListItemData != null && viewModel.fnbListItemData!!.isNotEmpty()){
            cartItemAdapter?.addItems(viewModel.fnbListItemData!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_skip, menu)
        return true
    }

    private fun loadFoodCategory(){
        val foodListItem = viewModel.foodItemData.value
        if ( foodListItem != null){
            for (pos in 0 until foodListItem.size){
                val fragment = FoodItemFragment()
                val arguments = Bundle()
                arguments.putString(BundleKeys.foodItemKey, Gson().toJson(foodListItem[pos]).toString())
                fragment.arguments = arguments
                adapter.addFragment(fragment, foodListItem[pos].tabName.toUpperCase() )
            }
            pager.adapter = adapter
            tabs.setupWithViewPager(pager)
        }
    }

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout> ?= null

    private fun applyBottomSheet(){
        bottomSheetBehavior =  BottomSheetBehavior.from(lnrBottomSheet)
        bottomSheetBehavior?.setBottomSheetCallback(object: BottomSheetBehavior
        .BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        loadCartItems()
                        lnrSheetContents.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        lnrSheetContents.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        bottomSheetBehavior?.peekHeight = 130
    }

    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = cartItemAdapter
        val animator = recyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.imgToggleBottomSheet -> {
                if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }else if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            R.id.lblPay -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbWorkerThread?.quit()
        foodDb?.destroyInstance()
    }

    fun updateCartTotal(){
        var totalPrice: Float ?= null
        val task = Runnable {
            totalPrice = foodDb?.foodDataDao()?.getTotalPrice()
            runOnUiThread {
                lblCartPrice.text = if (totalPrice != null) "AED $totalPrice" else "AED 0.0"
            }
        }
        dbWorkerThread?.postTask(task)
        viewModel.getDataFromDataBase()
    }

}