package com.redmechax00.mytelegram.ui.screens


import android.view.View
import androidx.fragment.app.Fragment
import com.redmechax00.mytelegram.utilits.APP_ACTIVITY


open class BaseFragment (private val layout: Int) : Fragment(layout) {

    private lateinit var mRootView: View

    override fun onStart() {
        super.onStart()
            APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

}