package studio.bz_soft.testzimad.data.http

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.CacheControl
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

const val HEADER_PRAGMA = "Pragma"
const val HEADER_CACHE_CONTROL = "Cache-Control"

fun cacheInterceptor(): Interceptor = Interceptor { chain ->
    val response = chain.proceed(chain.request())

    val cacheControl = CacheControl.Builder()
            .maxAge(60, TimeUnit.SECONDS)
            .build()

    return@Interceptor response.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .build()
}

fun offlineCacheInterceptor(context: Context): Interceptor = Interceptor { chain ->
    var request = chain.request()
    if (!hasNetwork(context)) {
        val cacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(30, TimeUnit.DAYS)
                .build()

        request = request.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl)
                .build()
    }
    chain.proceed(request)
}

private fun hasNetwork(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return (activeNetwork != null && activeNetwork.isConnected)
}
