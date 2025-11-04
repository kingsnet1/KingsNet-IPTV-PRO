package com.kingsnet.iptvpro.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kingsnet.iptvpro.R
import com.kingsnet.iptvpro.data.ChannelRepository
import com.kingsnet.iptvpro.data.db.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelAdapter(
    private var items: List<ChannelEntity>,
    private val onClick: (ChannelEntity) -> Unit
) : RecyclerView.Adapter<ChannelAdapter.VH>() {

    inner class VH(view: View): RecyclerView.ViewHolder(view) {
        val thumb: ImageView = view.findViewById(R.id.thumb)
        val title: TextView = view.findViewById(R.id.title)
        val group: TextView = view.findViewById(R.id.group)
        val favBtn: ImageButton = view.findViewById(R.id.favBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ch = items[position]
        holder.title.text = ch.name
        holder.group.text = ch.groupTitle ?: ""
        if (!ch.tvgLogo.isNullOrEmpty()) {
            holder.thumb.load(ch.tvgLogo) {
                placeholder(android.R.drawable.ic_media_play)
                error(android.R.drawable.ic_menu_report_image)
            }
        } else {
            holder.thumb.setImageResource(android.R.drawable.ic_media_play)
        }
        holder.itemView.setOnClickListener { onClick(ch) }
        holder.favBtn.setImageResource(if (ch.favorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
        holder.favBtn.setOnClickListener {
            val repo = ChannelRepository(holder.itemView.context.applicationContext)
            val updated = ch.copy(favorite = !ch.favorite)
            val act = holder.itemView.context as? androidx.fragment.app.FragmentActivity
            act?.lifecycleScope?.launchWhenStarted {
                withContext(Dispatchers.IO) {
                    repo.update(updated)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<ChannelEntity>) {
        items = newList
        notifyDataSetChanged()
    }
}
