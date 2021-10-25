package com.example.collectionwidget

import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.example.collectionwidget.data.ApiService
import com.example.collectionwidget.data.Coin
import com.example.collectionwidget.data.RequestCoinBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateWidgetService : Service() {

    override fun onStart(intent: Intent, startId: Int) {
        val appContext = this.applicationContext
        val appWidgetManager = AppWidgetManager.getInstance(appContext)
        val allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        for (widgetId in allWidgetIds!!) {
            val apiService: ApiService = ApiService.retrofit.create(ApiService::class.java)
            val call: Call<ArrayList<Coin>> = apiService.coinlist(
                RequestCoinBody(
                    currency = "USD",
                    sort = "rank",
                    order = "ascending",
                    offset = 0,
                    limit = 15,
                    meta = true
                )
            )
            call.enqueue(object : Callback<ArrayList<Coin>?> {
                override fun onResponse(
                    call: Call<ArrayList<Coin>?>?,
                    response: Response<ArrayList<Coin>?>
                ) {
                    Log.d("TAG", "Response: " + response.message())
                    Log.d("TAG", "Body: " + response.body())
                    updateWidget(appContext, appWidgetManager, widgetId, response.body())
                }

                override fun onFailure(call: Call<ArrayList<Coin>?>?, t: Throwable) {
                    Log.d("TAG", "Failure Response: " + t.message)
                    updateWidget(appContext, appWidgetManager, widgetId, null)
                }
            })
        }
        stopSelf()
        super.onStart(intent, startId)
    }

    private fun updateWidget(
        appContext: Context,
        appWidgetManager: AppWidgetManager,
        widgetId: Int,
        body: java.util.ArrayList<Coin>?
    ) {
        // Set up the intent that starts the ListRemoteViewsService, which will provide the views for this collection.
        val intentItemS = Intent(appContext, ListRemoteViewsService::class.java).apply {
            body?.let {
                val companyList: ArrayList<String> = arrayListOf()
                for (company in it) {
                    companyList.add(company.name)
                }
                putStringArrayListExtra("companyList", companyList)
            }
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            // When intents are compared, the extras are ignored, so we need to embed the extras into
            // the data so that the extras will not be ignored.
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }

        // Instantiate the RemoteViews object for the widget layout.
        val viewsS = RemoteViews(appContext.packageName, R.layout.widget_layout).apply {
            setRemoteAdapter(R.id.list_view, intentItemS)
            setEmptyView(R.id.list_view, R.id.empty_view)
        }

        // This section makes it possible for items to have individualized behavior.
        val toastPendingIntent: PendingIntent =
            Intent(appContext, CollectionWidgetProvider::class.java).run {
                action = TOAST_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                PendingIntent.getBroadcast(
                    applicationContext,
                    0,
                    this,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        viewsS.setPendingIntentTemplate(R.id.list_view, toastPendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(widgetId, viewsS)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}