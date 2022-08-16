package com.redmechax00.mytelegram.utilits

import android.media.MediaPlayer
import com.redmechax00.mytelegram.database.REF_STORAGE_ROOT
import com.redmechax00.mytelegram.database.getFileFromStorage
import java.io.File

class AppVoicePlayer {
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File

    fun play(messageKey: String, fileUrl: String, onSuccess: () -> Unit) {
        mFile = File(APP_ACTIVITY.filesDir, messageKey)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay {
                onSuccess()
            }
        } else {
            mFile.createNewFile()
            getFileFromStorage(mFile,fileUrl){
                startPlay {
                    onSuccess()
                }
            }
        }
    }

    private fun startPlay(onSuccess: () -> Unit) {
        try {
            mMediaPlayer.apply {
                setDataSource(mFile.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    stop {
                        onSuccess()
                    }
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    fun stop(onSuccess: () -> Unit) {
        try {
            mMediaPlayer.apply {
                stop()
                reset()
            }
            onSuccess()
        } catch (e: Exception) {
            showToast(e.message.toString())
            onSuccess()
        }
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init(){
        mMediaPlayer = MediaPlayer()
    }

}