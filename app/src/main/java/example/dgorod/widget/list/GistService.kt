package example.dgorod.widget.list

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.widget.RemoteViewsService

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 23-May-17.
 */
class GistService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val widgetID = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
        return GistFactory(applicationContext, widgetID)
    }
}