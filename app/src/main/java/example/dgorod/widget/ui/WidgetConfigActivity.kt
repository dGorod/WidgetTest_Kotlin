package example.dgorod.widget.ui

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import example.dgorod.widget.GistWidgetProvider
import example.dgorod.widget.R
import example.dgorod.widget.common.Const
import kotlinx.android.synthetic.main.activity_config.*

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 22-May-17.
 */

class WidgetConfigActivity : AppCompatActivity() {

    private var widgetID: Int = INVALID_APPWIDGET_ID
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)

        if (intent == null || intent.extras == null) {
            finish()
        }

        widgetID = intent.extras.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (widgetID == INVALID_APPWIDGET_ID) {
            finish()
        }

        setContentView(R.layout.activity_config)
    }

    override fun onContentChanged() {
        super.onContentChanged()

        confirm.setOnClickListener { _ -> addWidget() }

        val username = preferences.getString(Const.PrefKey.WIDGET_LOGIN + widgetID, "")
        login.hint = username
    }

    private fun addWidget() {
        var text: CharSequence = login.text

        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(login.hint)) {
            Toast.makeText(this, R.string.error_no_username, Toast.LENGTH_LONG).show()
            return
        } else if (TextUtils.isEmpty(text)) {
            text = login.hint
        }

        preferences.edit().putString(Const.PrefKey.WIDGET_LOGIN + widgetID, text.toString()).commit()

        // Widget update method made static to manually update widget after setting config data.
        // Known bug: https://issuetracker.google.com/issues/36908882
        GistWidgetProvider.update(this, preferences, AppWidgetManager.getInstance(this), widgetID)

        val result = Intent()
        result.putExtra(EXTRA_APPWIDGET_ID, widgetID)
        setResult(RESULT_OK, result)
        finish()
    }
}