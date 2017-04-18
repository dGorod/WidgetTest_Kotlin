package example.dgorod.widget.data.api;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import example.dgorod.widget.BuildConfig;
import example.dgorod.widget.common.Const;
import example.dgorod.widget.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by:
 * @author Dmytro Gorodnytskyi
 *         on 18-Aug-16.
 */
public abstract class ApiService {

    private static ApiInterface instance;

    private ApiService() {
    }

    public static synchronized ApiInterface getInstance(@NonNull Context context) {
        if (instance == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(getLoggingInterceptor())
                    .connectTimeout(Const.Network.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Const.Network.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Const.Network.WRITE_TIMEOUT, TimeUnit.SECONDS);

            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_host))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            instance = restAdapter.create(ApiInterface.class);
        }

        return instance;
    }

    private static Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? BASIC : NONE);
        return logging;
    }
}
