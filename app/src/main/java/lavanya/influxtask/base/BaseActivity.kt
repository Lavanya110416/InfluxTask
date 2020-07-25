package lavanya.influxtask.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import lavanya.influxtask.R
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity: AppCompatActivity(){

    abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
    }

    fun setTitle(title: String){
        supportActionBar?.title = title
    }

    fun enableBackButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    fun pushFragment(fragment: Fragment, addToBackStack: Boolean = false, tag: String){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentFrame, fragment)
        if (addToBackStack) fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

}