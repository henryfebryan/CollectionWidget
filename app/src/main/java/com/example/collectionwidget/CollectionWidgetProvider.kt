package com.example.collectionwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast

const val TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION"
const val EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM"

class CollectionWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TOAST_ACTION) {
            val viewIndex: Int = intent.getIntExtra(EXTRA_ITEM, 0)
            Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Set up the intent that starts the ListRemoteViewsService, which will provide the views for this collection.
    val intent = Intent(context, ListRemoteViewsService::class.java).apply {
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        // When intents are compared, the extras are ignored, so we need to embed the extras into
        // the data so that the extras will not be ignored.
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }

    // Instantiate the RemoteViews object for the widget layout.
    val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {
        setRemoteAdapter(R.id.list_view, intent)
        setEmptyView(R.id.list_view, R.id.empty_view)
    }

    // This section makes it possible for items to have individualized behavior.
    val toastPendingIntent: PendingIntent =
        Intent(context, CollectionWidgetProvider::class.java).run {
            action = TOAST_ACTION
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    views.setPendingIntentTemplate(R.id.list_view, toastPendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}