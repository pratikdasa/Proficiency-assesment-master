package com.proficiencyassesment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.proficiencyassesment.MainActivityTest.CustomAssertions.Companion.hasItemCount
import com.proficiencyassesment.view.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun itemCount_RecyclerViewAdapters_ReturnsTrue() {
        Thread.sleep(1000)
        onView(withId(R.id.recyclerview))
            .check(hasItemCount(9))
    }
    @Test
    fun itemCount_RecyclerViewAdapters_ReturnsFalse() {
        Thread.sleep(1000)
        onView(withId(R.id.recyclerview))
            .check(hasItemCount(100))
    }

    class CustomAssertions {
        companion object {
            fun hasItemCount(count: Int): ViewAssertion {
                return RecyclerViewItemCountAssertion(count)
            }
        }

        private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

            override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
                if (noViewFoundException != null) {
                    throw noViewFoundException
                }

                if (view !is RecyclerView) {
                    throw IllegalStateException("The asserted view is not RecyclerView")
                }

                if (view.adapter == null) {
                    throw IllegalStateException("No adapter is assigned to RecyclerView")
                }

                ViewMatchers.assertThat(
                    "RecyclerView item count",
                    view.adapter!!.itemCount,
                    CoreMatchers.equalTo(count)
                )
            }
        }
    }
}