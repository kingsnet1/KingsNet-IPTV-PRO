package com.kingsnet.iptvpro.data.parser

import com.kingsnet.iptvpro.data.db.ChannelEntity
import java.io.BufferedReader
import java.io.StringReader
import java.util.regex.Pattern

class M3UParser {
    private val extinfPattern = Pattern.compile("#EXTINF:-?\d+\s*(.*),\s*(.*)")

    fun parseToEntities(input: String): List<ChannelEntity> {
        val channels = mutableListOf<ChannelEntity>()
        val reader = BufferedReader(StringReader(input))
        var line: String? = reader.readLine()
        var lastExt: String? = null

        while (line != null) {
            val trimmed = line.trim()
            if (trimmed.startsWith("#EXTINF")) {
                lastExt = trimmed
            } else if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                val title = parseTitle(lastExt) ?: trimmed
                val attrs = parseAttributes(lastExt)
                val url = trimmed
                val group = attrs["group-title"] ?: attrs["group"]
                val tvgLogo = attrs["tvg-logo"] ?: attrs["tvg_logo"]
                val tvgId = attrs["tvg-id"] ?: attrs["tvg_id"]
                channels.add(ChannelEntity(name = title, url = url, groupTitle = group, tvgLogo = tvgLogo, tvgId = tvgId))
                lastExt = null
            }
            line = reader.readLine()
        }
        return channels
    }

    private fun parseTitle(extinf: String?): String? {
        if (extinf == null) return null
        val matcher = extinfPattern.matcher(extinf)
        if (matcher.find()) {
            val title = matcher.group(2)
            return title
        }
        return null
    }

    private fun parseAttributes(extinf: String?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (extinf == null) return map
        val attrPattern = Pattern.compile("(\S+?)\s*=\s*"([^"]*)"")
        val matcher = attrPattern.matcher(extinf)
        while (matcher.find()) {
            val key = matcher.group(1)
            val value = matcher.group(2)
            map[key] = value
        }
        return map
    }
}
