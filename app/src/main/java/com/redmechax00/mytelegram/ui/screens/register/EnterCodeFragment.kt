package com.redmechax00.mytelegram.ui.screens.register


import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.mikepenz.materialdrawer.util.ifNotNull
import com.mikepenz.materialdrawer.util.ifNull
import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.*
import com.redmechax00.mytelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(private val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        showKeyboard()
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential)
            .addOnFailureListener { showToast(it.message.toString()) }
            .addOnSuccessListener {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).child(CHILD_USERNAME).get()
                    .addOnFailureListener { it.message.toString() }
                    .addOnSuccessListener {
                        it.ifNull { dateMap[CHILD_USERNAME] = uid }
                    }

                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERNAMES).child(uid).setValue(uid)
                                    .addOnFailureListener { showToast(it.message.toString()) }
                                    .addOnSuccessListener {
                                        showToast("Добро пожаловать")
                                        hideKeyboard()
                                        restartActivity()
                                    }
                            }
                    }
            }
    }

}