package com.proficiencyassesment.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.proficiencyassesment.R
import com.proficiencyassesment.dependencyinjection.ProficiencyAssesment
import com.proficiencyassesment.model.Facts
import com.proficiencyassesment.retrofit.ApiService
import com.proficiencyassesment.utils.NetworkAvailabilityCheck
import com.proficiencyassesment.view.MainActivity
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class MainViewModel(private val mainActivity: MainActivity, private val mApiKey: String) :
    ViewModel() {

    private val networkConnectionLiveData: LiveData<Boolean>

    @Inject
    lateinit var apiService: ApiService

    init {
        //Network Availability Check Instance
        networkConnectionLiveData = NetworkAvailabilityCheck(mainActivity)
        //Injecting and getting apiService instance using dagger
        (mainActivity.application as ProficiencyAssesment).getAppComponent()?.inject(this)
    }

    //Error state to handle with live data
    val error = MutableLiveData<String>()

    val mFactsData: MutableLiveData<Facts> by lazy {
        MutableLiveData<Facts>().also {
            networkConnectionLiveData.observe(mainActivity, Observer<Boolean> {
                if (it == true) {
                    loadData(mApiKey)
                } else {
                    error.postValue(mainActivity.getString(R.string.error))
                }
            })
        }
    }


    fun loadData(mApiKey: String) {

        viewModelScope.launch() {
            try {
                withContext(Dispatchers.IO) {
                    var resultDeferred: Deferred<Response<Facts>> = getDataFromServer(mApiKey)
                    try {
                        var result: Response<Facts> = resultDeferred.await()
                        if (result.isSuccessful) {
                            var response = result.body()
                            response?.let {
                                mFactsData.postValue(response)
                            }
                            Log.e("response", response.toString())
                        } else {
                            error.postValue(mainActivity.getString(R.string.error))
                            Log.e("error", "Error in response")

                        }
                    } catch (ex: Exception) {
                        error.postValue(ex.message)
                        resultDeferred.getCompletionExceptionOrNull()?.let {
                            println(resultDeferred.getCompletionExceptionOrNull()!!.message)
                        }
                    }

                }
            }catch (e: Exception){
                e.printStackTrace()
                error.postValue(e.message)
            }
        }


//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                withContext(Dispatchers.IO) {
//                    var resultDeferred: Deferred<Response<Facts>> = getDataFromServer(mApiKey)
//                    try {
//                        var result: Response<Facts> = resultDeferred.await()
//                        if (result.isSuccessful) {
//                            var response = result.body()
//                            Log.e("response", response.toString())
//                        } else {
//                            Log.e("error", "Error in response")
//
//                        }
//                    } catch (ex: Exception) {
//                        ex.printStackTrace()
//                    }
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//            }
//        }
    }

    private suspend fun getDataFromServer(mApiKey: String) = withContext(Dispatchers.IO) {
        apiService.getFacts(mApiKey)
    }

}