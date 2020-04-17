package com.proficiencyassesment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.proficiencyassesment.di.ApiClient
import com.proficiencyassesment.model.Facts
import com.proficiencyassesment.utils.Constants
import com.proficiencyassesment.view.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class MainViewModelTest {


    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkConnectionLiveData: LiveData<Boolean>

    @Mock
    lateinit var mainActivity: MainActivity

    @Mock
    private lateinit var mConstants: Constants

    private val mainThreadTest = newSingleThreadContext("Main UI thread")

    @Mock
    private lateinit var apiClient: ApiClient


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadTest)
        apiClient = ApiClient()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadTest.close()
    }

    val mFactsData: MutableLiveData<Facts> by lazy {
        MutableLiveData<Facts>().also {
            networkConnectionLiveData.observe(mainActivity, Observer<Boolean> {
                if (it == true) {
                    loadData_WithKey()
                } else {
                    println("Error " + mainActivity.getString(R.string.error))
                }
            })
        }
    }

    @Test
    fun loadData_WithKey() {

        CoroutineScope(Dispatchers.Main).launch() {
            try {
                withContext(Dispatchers.IO) {
                    var resultDeferred: Deferred<Response<Facts>> =
                        getDataFromServer(mConstants.APIKEY)
                    try {
                        var result: Response<Facts> = resultDeferred.await()
                        Assert.assertEquals(200, result.code())

                    } catch (ex: Exception) {
                        resultDeferred.getCompletionExceptionOrNull()?.let {
                            println(resultDeferred.getCompletionExceptionOrNull()!!.message)
                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Test
    fun loadData_WithoutKey() {

        CoroutineScope(Dispatchers.Main).launch() {
            try {
                withContext(Dispatchers.IO) {
                    var resultDeferred: Deferred<Response<Facts>> =
                        getDataFromServer("null")
                    try {
                        var result: Response<Facts> = resultDeferred.await()
                        Assert.assertEquals(404, result.code())

                    } catch (ex: Exception) {
                        resultDeferred.getCompletionExceptionOrNull()?.let {
                            println(resultDeferred.getCompletionExceptionOrNull()!!.message)
                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getDataFromServer(mApiKey: String) = withContext(Dispatchers.IO) {
        apiClient.providersApiService(
            apiClient.providesRetrofit(
                apiClient.providesMyOkHttpClient(
                    apiClient.providesHttpLoggingInterceptor()
                )
            )
        ).getFacts(mApiKey)
    }


}
