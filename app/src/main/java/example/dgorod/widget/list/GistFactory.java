package example.dgorod.widget.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import example.dgorod.widget.R;
import example.dgorod.widget.common.Const;
import example.dgorod.widget.data.api.ApiService;
import example.dgorod.widget.data.api.dto.GistDto;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Dmytro Gorodnytskyi
 *         on 14-Apr-17.
 */

public class GistFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final int widgetID;
    private List<GistDto> data;

    public GistFactory(@NonNull Context context, int widgetID) {
        this.context = context;
        this.widgetID = widgetID;
    }

    @Override
    public void onCreate() {
        data = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        // nothing to clean
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).hashCode();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        GistDto item = data.get(position);
        String filename = item.getFiles().entrySet().iterator().next().getKey();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_gist);
        views.setTextViewText(R.id.name, filename);
        views.setTextViewText(R.id.link, item.getUrl());
        views.setOnClickFillInIntent(R.id.item_container, makeClickIntent(position, item.getUrl()));

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        data.clear();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString(Const.PrefKey.WIDGET_LOGIN + widgetID, "");

        ApiService.getInstance(context).getGists(username)
                .subscribe(result -> data.addAll(result), Timber::e);
    }

    private Intent makeClickIntent(int position, @NonNull String url) {
        Intent intent = new Intent();
        intent.putExtra(Const.Extra.ITEM_POSITION, position);
        intent.putExtra(Const.Extra.ITEM_URL, url);
        return intent;
    }
}
