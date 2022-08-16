package com.redmechax00.mytelegram.ui.screens

import androidx.fragment.app.Fragment
import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.utilits.APP_ACTIVITY
import com.redmechax00.mytelegram.utilits.hideKeyboard


class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }

}