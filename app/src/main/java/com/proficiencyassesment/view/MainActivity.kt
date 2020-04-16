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
import com.proficiencyassesment.viewmodel.MainViewModel
import com.proficiencyassesment.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkConnectionLiveData: LiveData<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(this@MainActivity,Constants.APIKEY)
        mainViewModel = ViewModelProviders.of(this@MainActivity, viewModelFactory)
            .get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    private fun init(){
        networkConnectionLiveData=NetworkAvailabilityCheck(this)
        recyclerview.layoutManager =LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)

        mainViewModel.mFactsData.observe(this, Observer<Facts>{})
        mainViewModel.error.observe(
            this, Observer<String> {
            }
        )
    }
}
