package com.kingsnet.iptvpro.ui.tv

import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.fragment.app.FragmentActivity
import com.kingsnet.iptvpro.R

class TvMainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_main)
    }
}
