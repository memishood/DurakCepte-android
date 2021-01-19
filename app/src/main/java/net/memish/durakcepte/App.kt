package net.memish.durakcepte

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import es.dmoral.toasty.Toasty

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Toasty.Config.getInstance()
            .setToastTypeface(ResourcesCompat.getFont(applicationContext, R.font.magic) ?: return)
            .apply()
    }
}