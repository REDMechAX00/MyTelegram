package com.redmechax00.mytelegram.ui.message_recycler_view.views

interface MessageView {

    val id: String
    val from: String
    val timeStamp: String
    val fileUrl: String
    val text: String

    companion object {
        val MESSAGE_TEXT: Int
            get() = 0
        val MESSAGE_IMAGE: Int
            get() = 1
        val MESSAGE_VOICE: Int
            get() = 2
    }

    fun getTypeView():Int
}