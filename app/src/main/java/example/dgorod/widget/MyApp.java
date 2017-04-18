package example.dgorod.widget;

import android.app.Application;

import timber.log.Timber;

/**
 * @author Dmytro Gorodnytskyi
 *         on 18-Apr-17.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
