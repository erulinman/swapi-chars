package info.erulinman.swapichars

import android.app.Application
import info.erulinman.swapichars.di.AppComponent
import info.erulinman.swapichars.di.DaggerAppComponent
import info.erulinman.swapichars.di.DatabaseModule

class App : Application() {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent get() = _appComponent!!

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(this))
            .build()
    }
}