package example.dgorod.widget.list

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import example.dgorod.widget.R
import example.dgorod.widget.common.Const
import example.dgorod.widget.data.api.ApiService
import example.dgorod.widget.data.api.dto.GistDto
import timber.log.Timber

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 23-May-17.
 */
class GistFactory(val context: Context, val widgetID: Int) : RemoteViewsService.RemoteViewsFactory {

    private val data: ArrayList<GistDto> by lazy { ArrayList<GistDto>() }

    override fun onCreate() {
        // data lazy initialized (Kotlin)
    }

    override fun onDestroy() {
        // nothing to clean
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return data[position].hashCode().toLong()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val filename = data[position].files.entries.iterator().next().key
        val url = data[position].url ?: ""

        val views = RemoteViews(context.packageName, R.layout.item_gist)
        views.setTextViewText(R.id.name, filename)
        views.setTextViewText(R.id.link, url)
        views.setOnClickFillInIntent(R.id.item_container, makeClickIntent(position, url))

        return views
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        data.clear()

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val username = preferences.getString(Const.PrefKey.WIDGET_LOGIN + widgetID, "")

        ApiService.instance(context).getGists(username).subscribe( {data.addAll(it)}, {Timber.e(it)} )
    }

    private fun makeClickIntent(position: Int, url: String): Intent {
        val intent = Intent()
        intent.putExtra(Const.Extra.ITEM_POSITION, position)
        intent.putExtra(Const.Extra.ITEM_URL, url)
        return intent
    }
}