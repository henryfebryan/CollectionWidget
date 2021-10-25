package com.example.collectionwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.util.*

class ListRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private var widgetItems: MutableList<WidgetItem> = mutableListOf()
    private var mAppWidgetId = 0

    override fun onCreate() {
        mAppWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        Log.d("TAG", "onCreate(): $mAppWidgetId")

        val companyList: ArrayList<String>? = intent.getStringArrayListExtra("companyList")
        companyList?.let { arr ->
            for (name in arr) {
                widgetItems.add(WidgetItem(name))
            }
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        Log.d("TAG", "getViewAt(): $mAppWidgetId")

        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            setTextViewText(R.id.widget_item_id, widgetItems[position].text)

            val fillInIntent = Intent().apply {
                Bundle().also { extras ->
                    extras.putString(EXTRA_ITEM, widgetItems[position].text)
                    putExtras(extras)
                }
            }
            setOnClickFillInIntent(R.id.widget_item_id, fillInIntent)
        }
    }

    override fun onDestroy() {
        Log.d("TAG", "onDestroy(): $mAppWidgetId")
        widgetItems.clear()
    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getLoadingView(): RemoteViews? {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        // called when notifyDataSetChanged() is triggered on the remote adapter.
    }
}