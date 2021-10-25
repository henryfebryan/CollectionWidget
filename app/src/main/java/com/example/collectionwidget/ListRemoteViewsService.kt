package com.example.collectionwidget

import android.content.Intent
import android.widget.RemoteViewsService

class ListRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListRemoteViewsFactory(this.applicationContext, intent)
    }
}