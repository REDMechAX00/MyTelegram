package com.redmechax00.mytelegram.ui.screens

import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.*
import com.redmechax00.mytelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername:String

    override fun onResume() {
        super.onResume()
        settings_text_input_username.setText(UserModel.username)
    }

    override fun change(){
        mNewUsername = settings_text_input_username.text.toString().lowercase(Locale.getDefault())
        if(mNewUsername.isEmpty()){
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewUsername)){
                        showToast("Такой пользователь уже существует")
                    } else {
                        changeUsername(mNewUsername)
                    }
                })
        }
    }

}