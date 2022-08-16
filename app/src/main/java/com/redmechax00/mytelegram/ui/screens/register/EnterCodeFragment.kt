package com.redmechax00.mytelegram.ui.screens.register


import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.*
import com.redmechax00.mytelegram.utilits.*
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val phoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id,code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String,Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(UserModel.username).child(uid).get()
                            .addOnSuccessListener { task_get_uid ->

                                if (task_get_uid.value ==null) {
                                    dateMap[CHILD_USERNAME] = uid
                                    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                                        .addOnCompleteListener{ task2 ->
                                            if(task2.isSuccessful) {
                                                REF_DATABASE_ROOT.child(NODE_USERNAMES).child(uid)
                                                    .setValue(uid)
                                                    .addOnCompleteListener{
                                                        if(it.isSuccessful){
                                                            showToast("Добро пожаловать")
                                                            hideKeyboard()
                                                            restartActivity()
                                                        }
                                                    }
                                            } else showToast(task2.exception?.message.toString())
                                        }
                                } else{
                                    showToast("Добро пожаловать снова")
                                    hideKeyboard()
                                    restartActivity()
                                }
                            }
                    }
            } else showToast(task.exception?.message.toString())
        }
    }

}