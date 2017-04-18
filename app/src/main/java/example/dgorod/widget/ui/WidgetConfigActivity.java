package example.dgorod.widget.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import example.dgorod.widget.GistWidgetProvider;
import example.dgorod.widget.R;
import example.dgorod.widget.common.Const;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

/**
 * @author Dmytro Gorodnytskyi
 *         on 14-Apr-17.
 */

public class WidgetConfigActivity extends AppCompatActivity {

    private TextInputEditText login;
    private Button confirm;

    private int widgetID = INVALID_APPWIDGET_ID;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        if (getIntent() == null || getIntent().getExtras() == null) {
            finish();
        }

        widgetID = getIntent().getExtras().getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (widgetID == INVALID_APPWIDGET_ID) {
            finish();
        }

        setContentView(R.layout.activity_config);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        login = (TextInputEditText) findViewById(R.id.login);
        confirm = (Button) findViewById(R.id.confirm);

        confirm.setOnClickListener(v -> addWidget());

        String username = preferences.getString(Const.PrefKey.WIDGET_LOGIN + widgetID, "");
        login.setHint(username);
    }

    private void addWidget() {
        CharSequence text = login.getText();
        CharSequence hint = login.getHint();

        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(hint)) {
            Toast.makeText(this, R.string.error_no_username, Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(text)) {
            text = hint;
        }

        preferences.edit().putString(Const.PrefKey.WIDGET_LOGIN + widgetID, text.toString()).commit();

        // Widget update method made static to manually update widget after setting config data.
        // Known bug: https://issuetracker.google.com/issues/36908882
        GistWidgetProvider.update(this, preferences, AppWidgetManager.getInstance(this), widgetID);

        Intent result = new Intent();
        result.putExtra(EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_OK, result);
        finish();
    }
}
