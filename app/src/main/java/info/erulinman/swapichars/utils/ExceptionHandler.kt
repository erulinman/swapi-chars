package info.erulinman.swapichars.utils

import android.content.res.Resources
import info.erulinman.swapichars.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandler @Inject constructor(private val resources: Resources) {

    operator fun <T : Exception> invoke(exception: T) = when (exception) {
        is IOException -> resources.getString(R.string.error_bad_connect)
        is HttpException -> resources.getString(R.string.error_http)
        else -> "${resources.getString(R.string.error_unknown)}: $exception"
    }
}