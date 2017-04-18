package example.dgorod.widget.list;

import android.content.Intent;
import android.widget.RemoteViewsService;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * @author Dmytro Gorodnytskyi
 *         on 14-Apr-17.
 */

public class GistService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        final int widgetID = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        return new GistFactory(getApplicationContext(), widgetID);
    }
}
