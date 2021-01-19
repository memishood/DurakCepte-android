package net.memish.durakcepte.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import net.memish.durakcepte.R

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runOnHandler {
            Intent(this, BaseActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.also {
                startActivity(it)
            }.run {
                finish()
            }
        }
    }

    private fun runOnHandler(action: () -> Unit) {
        Handler(mainLooper).postDelayed(action, 700)
    }
}