package com.example.praktikumprogmob

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBar

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.TextView
import com.example.praktikumprogmob.FragmentMenu.HomeFragment
import com.example.praktikumprogmob.FragmentMenu.NotificationFragment
import com.example.praktikumprogmob.FragmentMenu.ProfileFragment
import com.example.praktikumprogmob.FragmentMenu.PromoFragment
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.praktikumprogmob.APIHelper.BaseApiService
import com.example.praktikumprogmob.APIHelper.UtilsApi
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

//import javax.swing.text.StyleConstants.setIcon



class Main2Activity : AppCompatActivity() {

    val homeFragment = HomeFragment.newInstance()
    val mContext = this
    internal lateinit var mApiService: BaseApiService

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val promoFragment = PromoFragment.newInstance()
                openFragment(promoFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val notificationFragment = NotificationFragment.newInstance()
                openFragment(notificationFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val profileFragment = ProfileFragment.newInstance(1)
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mApiService = UtilsApi.getAPIService() // meng-init yang ada di package apihelper
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close Application")
                .setMessage("Are you sure you want to exit app?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which -> finish() })
                .setNegativeButton("No", null)
                .show()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        supportFragmentManager.popBackStackImmediate()
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun BottomNavigationView.active(position: Int) {
        menu.getItem(position).isChecked = true

    }


}
