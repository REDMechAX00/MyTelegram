package com.redmechax00.mytelegram.ui.screens


import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.*
import kotlinx.android.synthetic.main.fragment_change_bio.*

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_text_input_bio.setText(UserModel.bio)
    }

    override fun change() {
        super.change()
        val mNewBio = settings_text_input_bio.text.toString()
        setBioToDB(mNewBio)
    }

}