package example.dgorod.widget

import android.app.Application
import timber.log.Timber

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 22-May-17.
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}