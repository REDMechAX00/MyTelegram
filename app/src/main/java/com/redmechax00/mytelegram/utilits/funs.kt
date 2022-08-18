package com.redmechax00.mytelegram.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.redmechax00.mytelegram.MainActivity
import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.updatePhonesToDatabase
import com.redmechax00.mytelegram.models.CommonModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment, addToStack: Boolean) {
    if (addToStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container, fragment)
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment)
            .commit()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun AppCompatActivity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(APP_ACTIVITY))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showKeyboard() {
    val imm = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun ImageView.downloadAndSetImage(url: String) {
    if (url.isEmpty()) {
        this.setImageResource(R.drawable.default_photo)
    } else {
        Picasso.get()
            .load(url)
            .fit()
            .placeholder(R.drawable.default_photo)
            .into(this)
    }
}

@SuppressLint("Range")
fun initContacts() {
    if (checkPermission(READ_CONTACTS)){
        var arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while(it.moveToNext()){
                val fullname = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullname
                newModel.phone = phone.replace(Regex("[\\s,-]"),"")
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}