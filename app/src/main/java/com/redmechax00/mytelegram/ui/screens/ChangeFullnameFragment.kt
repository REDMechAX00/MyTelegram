package com.redmechax00.mytelegram.ui.screens

import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.*
import com.redmechax00.mytelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_name.*

class ChangeFullnameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullNameList = UserModel.fullname.split(DATA_SEPARATOR)
        if (fullNameList.size > 1) {
            settings_text_input_name.setText(fullNameList[0])
            settings_text_input_surname.setText(fullNameList[1])
        } else {
            settings_text_input_name.setText(fullNameList[0])
        }
    }


    override fun change() {
        val name = settings_text_input_name.text.toString()
        val surname = settings_text_input_surname.text.toString()
        if(name.isEmpty()){
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else{
            val fullname = "$name$DATA_SEPARATOR$surname"
            setFullnameToDB(fullname)
        }
    }
}