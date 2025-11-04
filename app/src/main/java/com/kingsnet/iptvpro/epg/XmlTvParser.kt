package com.kingsnet.iptvpro.epg

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

data class EpgProgramme(val channel: String, val start: Date, val stop: Date, val title: String, val desc: String?)

class XmlTvParser {
    fun parse(xml: String): List<EpgProgramme> {
        val list = mutableListOf<EpgProgramme>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            val xp = factory.newPullParser()
            xp.setInput(StringReader(xml))
            var event = xp.eventType
            var currentProgrammeChannel: String? = null
            var currentStart: String? = null
            var currentStop: String? = null
            var currentTitle: String? = null
            var currentDesc: String? = null

            while (event != XmlPullParser.END_DOCUMENT) {
                when (event) {
                    XmlPullParser.START_TAG -> {
                        when (xp.name) {
                            "programme" -> {
                                currentProgrammeChannel = xp.getAttributeValue(null, "channel")
                                currentStart = xp.getAttributeValue(null, "start")
                                currentStop = xp.getAttributeValue(null, "stop")
                                currentTitle = null
                                currentDesc = null
                            }
                            "title" -> currentTitle = xp.nextText()
                            "desc" -> currentDesc = xp.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (xp.name == "programme" && currentProgrammeChannel != null && currentTitle != null) {
                            val sdf = SimpleDateFormat("yyyyMMdd'T'HHmmssZ", Locale.US)
                            val startStr = currentStart?.replace("Z", "+0000")
                            val stopStr = currentStop?.replace("Z", "+0000")
                            try {
                                val start = sdf.parse(startStr)
                                val stop = sdf.parse(stopStr)
                                if (start != null && stop != null) {
                                    list.add(EpgProgramme(currentProgrammeChannel, start, stop, currentTitle, currentDesc))
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
                event = xp.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun nowNext(epg: List<EpgProgramme>, channelId: String): Pair<EpgProgramme?, EpgProgramme?> {
        val now = Date()
        val programmes = epg.filter { it.channel == channelId }.sortedBy { it.start }
        val current = programmes.find { it.start <= now && it.stop > now }
        val next = programmes.firstOrNull { it.start > now }
        return Pair(current, next)
    }
}
