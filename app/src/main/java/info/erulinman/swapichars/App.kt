package info.erulinman.swapichars

import android.app.Application
import info.erulinman.swapichars.core.di.AppComponent
import info.erulinman.swapichars.core.di.AppModule
import info.erulinman.swapichars.core.di.DaggerAppComponent

class App : Application() {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent get() = _appComponent!!

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}