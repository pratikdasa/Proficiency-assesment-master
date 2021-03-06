package com.proficiencyassesment.view

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.proficiencyassesment.R
import com.proficiencyassesment.databinding.ActivityMainBinding
import com.proficiencyassesment.model.Facts
import com.proficiencyassesment.utils.Constants
import com.proficiencyassesment.utils.NetworkAvailabilityCheck
import com.proficiencyassesment.utils.PopupUtils
import com.proficiencyassesment.viewmodel.MainViewModel
import com.proficiencyassesment.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkConnectionLiveData: LiveData<Boolean>
    var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var doubleBackToExitPressedOnce = false
    private val mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(this@MainActivity, Constants.APIKEY)
        mainViewModel = ViewModelProviders.of(this@MainActivity, viewModelFactory)
            .get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    private fun init() {
        networkConnectionLiveData = NetworkAvailabilityCheck(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)

        //Swipe-to-refresh code
        swipe_container.setOnRefreshListener {
            getDataFromServer()
            swipe_container.isRefreshing = false
        }
        //Observer for live data to update the data if there any change
        mainViewModel.mFactsData.observe(this, Observer<Facts> { Facts -> updateListData(Facts) })

        //Observer for error live data to to check if there is any error found
        mainViewModel.error.observe(
            this, Observer<String> {
                showErrorDialog(it)
            }
        )
    }

    //Get data from server for swipe to refresh
    private fun getDataFromServer() {
        networkConnectionLiveData.observe(this, Observer<Boolean> {
            if (it == true) {
                mainViewModel.loadData(Constants.APIKEY)
            } else {
                showErrorDialog(this.getString(R.string.error))
            }

        })

    }

    //Code to show error dialog
    private fun showErrorDialog(message: String) {
        PopupUtils.showAlertDialog(this, getString(R.string.app_name), message)
    }

    //The data will be updated here and recyclerview adapter also will be set from this method.
    private fun updateListData(facts: Facts) {
        txtToolbarTitle.text = facts.title
        val data = facts.rows?.filter { it.title != null }?.filter { it.description != null }
            ?.filter { it.imageHref != null }
        recyclerViewAdapter = RecyclerViewAdapter(data)
        recyclerview.adapter = recyclerViewAdapter
    }

    //Code to press back button 2 times when user wants to leave the app.
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.back_press_message), Toast.LENGTH_SHORT).show()
        mHandler.postDelayed(mRunnable, 2000)
    }

    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }


    override fun onDestroy() {
        super.onDestroy()
        //Releasing handler
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable)
        }
    }

}
