package net.memish.durakcepte.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import es.dmoral.toasty.Toasty
import net.memish.durakcepte.R
import net.memish.durakcepte.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var exit = false

    private val message
        get() = getString(R.string.activity_base_back_press)

    private val icon
        get() = ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_24)

    private val backgroundColor
        get() = ContextCompat.getColor(this,  R.color.purple_700)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->  }
        val binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(binding.toolbar, navController)
    }

    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            handleExit()
        }
    }

    private fun handleExit() {
        when (exit) {
            true -> finishAffinity()
            else -> Toasty.custom(this, message, icon, backgroundColor, Color.WHITE, Toasty.LENGTH_SHORT, false, true).show()
        }
        exit = true
        Handler(Looper.myLooper() ?: Looper.getMainLooper())
            .postDelayed({ exit = false }, 1500)
    }
}