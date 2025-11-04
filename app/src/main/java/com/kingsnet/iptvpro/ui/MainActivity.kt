package com.kingsnet.iptvpro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingsnet.iptvpro.R
import com.kingsnet.iptvpro.data.ChannelRepository
import com.kingsnet.iptvpro.data.db.ChannelEntity
import com.kingsnet.iptvpro.data.parser.M3UParser
import com.kingsnet.iptvpro.epg.XmlTvParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var playlistUrl: EditText
    private lateinit var loadBtn: Button
    private lateinit var channelListView: androidx.recyclerview.widget.RecyclerView
    private lateinit var epgNowNext: TextView

    private lateinit var repo: ChannelRepository
    private val parser = M3UParser()
    private val epgParser = XmlTvParser()
    private var epgData: List<com.kingsnet.iptvpro.epg.EpgProgramme> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playlistUrl = findViewById(R.id.playlistUrl)
        loadBtn = findViewById(R.id.loadBtn)
        channelListView = findViewById(R.id.channelList)
        epgNowNext = findViewById(R.id.epgNowNext)
        channelListView.layoutManager = LinearLayoutManager(this)

        repo = ChannelRepository(applicationContext)

        loadBtn.setOnClickListener {
            val url = playlistUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                fetchPlaylist(url)
            }
        }

        lifecycleScope.launchWhenStarted {
            repo.getAll().collect { list ->
                runOnUiThread {
                    val adapter = ChannelAdapter(list) { ch ->
                        val i = Intent(this@MainActivity, PlayerActivity::class.java)
                        i.putExtra("title", ch.name)
                        i.putExtra("stream", ch.url)
                        i.putExtra("tvgId", ch.tvgId)
                        startActivity(i)
                    }
                    channelListView.adapter = adapter
                }
            }
        }
    }

    private fun fetchPlaylist(url: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val req = Request.Builder().url(url).build()
                val resp = client.newCall(req).execute()
                val body = resp.body?.string()
                if (body != null) {
                    val entities = parser.parseToEntities(body)
                    repo.clear()
                    repo.insertAll(entities)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
