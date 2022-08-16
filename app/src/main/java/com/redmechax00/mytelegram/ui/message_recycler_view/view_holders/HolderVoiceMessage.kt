package com.redmechax00.mytelegram.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.mytelegram.database.CURRENT_UID
import com.redmechax00.mytelegram.ui.message_recycler_view.views.MessageView
import com.redmechax00.mytelegram.utilits.AppVoicePlayer
import com.redmechax00.mytelegram.utilits.asTime
import kotlinx.android.synthetic.main.message_item_voice.view.*

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view),MessageHolder {

    private val mAppVoicePlayer = AppVoicePlayer()

    private val blockUserVoiceMessage: ConstraintLayout = view.block_user_voice_message
    private val chatUserVoiceMessageTime: TextView = view.chat_user_voice_message_time
    private val blockReceivedVoiceMessage: ConstraintLayout = view.block_received_voice_message
    private val chatReceivedVoiceMessageTime: TextView = view.chat_received_voice_message_time

    private val chatUserBtnPlay: ImageView = view.chat_user_btn_play
    private val chatReceivedBtnPlay: ImageView = view.chat_received_btn_play
    private val chatUserBtnStop: ImageView = view.chat_user_btn_stop
    private val chatReceivedBtnStop: ImageView = view.chat_received_btn_stop

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserVoiceMessage.visibility = View.VISIBLE
            blockReceivedVoiceMessage.visibility = View.GONE
            chatUserVoiceMessageTime.text = view.timeStamp.asTime()
        } else {
            blockReceivedVoiceMessage.visibility = View.VISIBLE
            blockUserVoiceMessage.visibility = View.GONE
            chatReceivedVoiceMessageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if(view.from == CURRENT_UID){
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnStop.visibility = View.VISIBLE
                chatUserBtnStop.setOnClickListener {
                    stop {
                        chatUserBtnStop.setOnClickListener(null)
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnStop.visibility = View.GONE
                    }
                }
                play(view){
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnStop.visibility = View.GONE
                }
            }
        } else{
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnStop.visibility = View.VISIBLE
                chatReceivedBtnStop.setOnClickListener {
                    stop {
                        chatReceivedBtnStop.setOnClickListener(null)
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnStop.visibility = View.GONE
                    }
                }
                play(view){
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnStop.visibility = View.GONE
                }
            }
        }
    }

    override fun onDetach() {
        chatUserBtnPlay.setOnClickListener(null)
        chatReceivedBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.release()
    }

    private fun play(view:MessageView, onSuccess:()->Unit){
        mAppVoicePlayer.play(view.id,view.fileUrl){
            onSuccess()
        }
    }

    private fun stop(onSuccess:()->Unit){
        mAppVoicePlayer.stop{
            onSuccess()
        }
    }
}