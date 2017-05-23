package example.dgorod.widget.data.api

import android.content.Context
import example.dgorod.widget.BuildConfig
import example.dgorod.widget.R
import example.dgorod.widget.common.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 23-May-17.
 */
object ApiService {
// https://medium.com/@andrey.breslav/why-not-simply-use-object-instead-of-class-8e6f132a0bc7

    val instance = fun (context: Context): ApiInterface {

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .connectTimeout(Const.Network.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Const.Network.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Const.Network.WRITE_TIMEOUT, TimeUnit.SECONDS)

        val restAdapter = Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_host))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient.build())
                .build()

        return restAdapter.create(ApiInterface::class.java)
    }

    private fun getLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) Level.BASIC else Level.NONE
        return logging
    }
}