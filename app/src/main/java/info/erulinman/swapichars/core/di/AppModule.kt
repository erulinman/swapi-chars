package info.erulinman.swapichars.core.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        DataModule::class
    ]
)
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources
}