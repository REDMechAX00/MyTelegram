package com.redmechax00.mytelegram.utilits

import com.redmechax00.mytelegram.database.*

enum class AppStates (val state: String) {
    ONLINE ("в сети"),
    OFFLINE ("был недавно"),
    TYPING ("печатает");

    companion object{
        fun updateState(appStates: AppStates){
            if(AUTH.currentUser!=null) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener { UserModel.state = appStates.state }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }
        }
    }
}