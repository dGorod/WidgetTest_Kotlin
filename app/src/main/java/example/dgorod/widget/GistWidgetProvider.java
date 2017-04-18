package example.dgorod.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import example.dgorod.widget.common.Const;
import example.dgorod.widget.list.GistService;
import example.dgorod.widget.ui.WidgetConfigActivity;
import timber.log.Timber;

/**
 * @author Dmytro Gorodnytskyi
 *         on 14-Apr-17.
 */

public class GistWidgetProvider extends AppWidgetProvider {

    private static final int INVALID_POSITION = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equalsIgnoreCase(Const.Action.LIST_ITEM_CLICK)) {
            Bundle extras = intent.getExtras();

            if (extras != null && extras.getInt(Const.Extra.ITEM_POSITION) != INVALID_POSITION) {
                openWebLink(context, extras.getString(Const.Extra.ITEM_URL, ""));
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        Timber.i("Widgets: onUpdate");

        for (int id : appWidgetIds) {
            update(context, sp, appWidgetManager, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        for (int id : appWidgetIds) {
            editor.remove(Const.PrefKey.WIDGET_LOGIN + id);
        }
        editor.apply();
    }

    public static void update(@NonNull Context context, @NonNull SharedPreferences preferences,
                              @NonNull AppWidgetManager manager, int id) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_gists);

        setHeader(context, views, preferences, id);
        setList(context, views, id);
        setListItemClick(context, views);

        manager.updateAppWidget(id, views);
        manager.notifyAppWidgetViewDataChanged(id, R.id.gists);
    }

    private static void setHeader(@NonNull Context context, @NonNull RemoteViews views,
                                  @NonNull SharedPreferences preferences, int id) {
        views.setTextViewText(R.id.username, preferences.getString(Const.PrefKey.WIDGET_LOGIN + id, ""));
        views.setOnClickPendingIntent(R.id.refresh, makeUpdateIntent(context, id));
        views.setOnClickPendingIntent(R.id.configure, makeConfigActivityIntent(context, id));
        views.setEmptyView(R.id.gists, R.id.no_data);
    }

    private static void setList(@NonNull Context context, @NonNull RemoteViews views, int id) {
        Intent adapter = new Intent(context, GistService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        views.setRemoteAdapter(R.id.gists, adapter);
    }

    private static void setListItemClick(@NonNull Context context, @NonNull RemoteViews views) {
        views.setPendingIntentTemplate(R.id.gists, makeListItemClickIntent(context));
    }

    private static PendingIntent makeUpdateIntent(@NonNull Context context, int id) {
        Intent intent = new Intent(context, GistWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { id });
        return PendingIntent.getBroadcast(context, id, intent, 0);
    }

    private static PendingIntent makeConfigActivityIntent(@NonNull Context context, int id) {
        Intent intent = new Intent(context, WidgetConfigActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        return PendingIntent.getActivity(context, id, intent, 0);
    }

    private static PendingIntent makeListItemClickIntent(@NonNull Context context) {
        Intent intent = new Intent(context, GistWidgetProvider.class);
        intent.setAction(Const.Action.LIST_ITEM_CLICK);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void openWebLink(@NonNull Context context, @NonNull String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
