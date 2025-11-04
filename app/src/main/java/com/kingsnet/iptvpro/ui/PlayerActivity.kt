package com.kingsnet.iptvpro.ui

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kingsnet.iptvpro.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.TrackSelectionParameters
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util

class PlayerActivity : AppCompatActivity() {
    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView
    private var mediaSession: MediaSessionCompat? = null
    private var trackSelector: DefaultTrackSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playerView = findViewById(R.id.playerView)

        val title = intent.getStringExtra("title")
        val stream = intent.getStringExtra("stream")
        supportActionBar?.title = title ?: "Player"

        initializePlayer(stream)
    }

    private fun initializePlayer(streamUrl: String?) {
        if (player == null) {
            trackSelector = DefaultTrackSelector(this)
            trackSelector?.parameters = TrackSelectionParameters.Builder(this).build()
            player = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector!!).build()
            playerView.player = player

            mediaSession = MediaSessionCompat(this, "kingsnet_iptv_pro")
            mediaSession?.isActive = true
        }

        if (!streamUrl.isNullOrEmpty()) {
            try {
                val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl))
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.playWhenReady = true
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to play stream", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
        mediaSession?.isActive = false
        mediaSession?.release()
        mediaSession = null
    }
}
