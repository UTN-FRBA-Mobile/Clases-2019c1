package ar.edu.utn.frba.mobile.a2019c1

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Intent
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.IsNot.not
import java.lang.NullPointerException


@RunWith(AndroidJUnit4::class)

class MainActivityTest {

    @Rule @JvmField
    var activityRule = ActivityTestRule<MainActivity>(
        MainActivity::class.java
    )

    @Test
    fun botonesDesactivadosSinTexto (){
        onView(withId(R.id.temperaturaIngresadaC)).check(matches(withText("")))
        onView(withId(R.id.botonCelsius)).check(matches(not(isEnabled())))
    }
}