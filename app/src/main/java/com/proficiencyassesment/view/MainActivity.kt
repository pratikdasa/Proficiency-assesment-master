package com.proficiencyassesment.view

import android.os.Bundle
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

        mainViewModel.mFactsData.observe(this, Observer<Facts> { Facts -> updateListData(Facts) })
        mainViewModel.error.observe(
            this, Observer<String> {
                showErrorDialog(it)
            }
        )
    }

    private fun showErrorDialog(message: String){
        PopupUtils.showAlertDialog(this, getString(R.string.app_name), message)

    }
    private fun updateListData(facts: Facts) {
        txtToolbarTitle.text = facts.title
        val data = facts.rows?.filter { it.title != null }?.filter { it.description != null }
            ?.filter { it.imageHref != null }
        recyclerViewAdapter = RecyclerViewAdapter(data)
        recyclerview.adapter = recyclerViewAdapter
    }
}
