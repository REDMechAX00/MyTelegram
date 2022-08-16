package com.redmechax00.mytelegram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.redmechax00.mytelegram.database.AUTH
import com.redmechax00.mytelegram.database.initFirebase
import com.redmechax00.mytelegram.database.initUser
import com.redmechax00.mytelegram.databinding.ActivityMainBinding
import com.redmechax00.mytelegram.ui.screens.MainFragment
import com.redmechax00.mytelegram.ui.screens.register.EnterPhoneNumberFragment
import com.redmechax00.mytelegram.ui.objects.AppDrawer
import com.redmechax00.mytelegram.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        APP_ACTIVITY = this
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFun()
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    private fun initFun() {
        setSupportActionBar(mToolbar)
        if(AUTH.currentUser!=null){
            mAppDrawer.create()
            replaceFragment(MainFragment(), false)
        } else{
            replaceFragment(EnterPhoneNumberFragment(),false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }

}