package example.dgorod.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.RemoteViews
import example.dgorod.widget.common.Const
import example.dgorod.widget.list.GistService
import example.dgorod.widget.ui.WidgetConfigActivity
import timber.log.Timber

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 22-May-17.
 */
class GistWidgetProvider : AppWidgetProvider() {

    private val INVALID_POSITION = -1

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action.contentEquals(Const.Action.LIST_ITEM_CLICK)) {
            if (intent.extras.getInt(Const.Extra.ITEM_POSITION) != INVALID_POSITION) {
                openWebLink(context, intent.extras.getString(Const.Extra.ITEM_URL, ""))
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val sp = PreferenceManager.getDefaultSharedPreferences(context)

        Timber.i("Widgets: onUpdate")

        for (id in appWidgetIds) {
            update(context, sp, appWidgetManager, id)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        for (id in appWidgetIds) {
            editor.remove(Const.PrefKey.WIDGET_LOGIN + id)
        }
        editor.apply()
    }

    private fun openWebLink(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun update(context: Context, preferences: SharedPreferences, manager: AppWidgetManager, id: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_gists)

            setHeader(context, views, preferences, id)
            setList(context, views, id)
            setListItemClick(context, views)

            manager.updateAppWidget(id, views)
            manager.notifyAppWidgetViewDataChanged(id, R.id.gists)
        }

        private fun setHeader(context: Context, views: RemoteViews, preferences: SharedPreferences, id: Int) {
            views.setTextViewText(R.id.username, preferences.getString(Const.PrefKey.WIDGET_LOGIN + id, ""))
            views.setOnClickPendingIntent(R.id.refresh, makeUpdateIntent(context, id))
            views.setOnClickPendingIntent(R.id.configure, makeConfigActivityIntent(context, id))
            views.setEmptyView(R.id.gists, R.id.no_data)
        }

        private fun setList(context: Context, views: RemoteViews, id: Int) {
            val adapter = Intent(context, GistService::class.java)
            adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            adapter.data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME))
            views.setRemoteAdapter(R.id.gists, adapter)
        }

        private fun setListItemClick(context: Context, views: RemoteViews) {
            views.setPendingIntentTemplate(R.id.gists, makeListItemClickIntent(context))
        }

        private fun makeUpdateIntent(context: Context, id: Int): PendingIntent {
            val intent = Intent(context, GistWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, listOf(id).toIntArray())
            return PendingIntent.getBroadcast(context, id, intent, 0)
        }

        private fun makeConfigActivityIntent(context: Context, id: Int): PendingIntent {
            val intent = Intent(context, WidgetConfigActivity::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            return PendingIntent.getActivity(context, id, intent, 0)
        }

        private fun makeListItemClickIntent(context: Context): PendingIntent {
            val intent = Intent(context, GistWidgetProvider::class.java)
            intent.action = Const.Action.LIST_ITEM_CLICK
            return PendingIntent.getBroadcast(context, 0, intent, 0)
        }
    }
}