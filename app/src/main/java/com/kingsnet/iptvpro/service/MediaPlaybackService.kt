package com.kingsnet.iptvpro.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat

class MediaPlaybackService : Service() {
    private var mediaSession: MediaSessionCompat? = null

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "kingsnet_iptv_service")
        mediaSession?.isActive = true
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        mediaSession?.isActive = false
        mediaSession?.release()
        super.onDestroy()
    }
}
