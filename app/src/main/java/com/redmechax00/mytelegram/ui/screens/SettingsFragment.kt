package com.redmechax00.mytelegram.ui.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.redmechax00.mytelegram.R
import com.redmechax00.mytelegram.database.*
import com.redmechax00.mytelegram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        initFields()
        setHasOptionsMenu(true)
    }

    fun initFields() {
        settings_text_bio.text = UserModel.bio
        settings_text_fullname.text = UserModel.fullname.replace(DATA_SEPARATOR, " ")
        settings_text_phone_number.text = UserModel.phone
        settings_text_user_status.text = UserModel.state
        settings_text_username.text = UserModel.username
        settings_user_photo.downloadAndSetImage(UserModel.photoUrl)
        settings_btn_change_username.setOnClickListener { replaceFragment(ChangeUsernameFragment(), true) }
        settings_btn_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment(), true) }
        settings_btn_change_photo.setOnClickListener { changePhotoUser() }
    }

    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {

            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE).child(CURRENT_UID)

            putFileToStorage(uri, path){
                getFileUrlFromStorage(path){ imageUrl ->
                    putImageUrlToDatabase(imageUrl){
                        settings_user_photo.downloadAndSetImage(imageUrl)
                        showToast(getString(R.string.toast_data_update))
                        UserModel.photoUrl = imageUrl
                        showToast(imageUrl)
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_btn_change_name -> {
                replaceFragment(ChangeFullnameFragment(), true)
            }

            R.id.settings_btn_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
        }
        return true
    }
}