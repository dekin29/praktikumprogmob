package com.example.praktikumprogmob.FragmentMenu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.praktikumprogmob.APIHelper.BaseApiService
import com.example.praktikumprogmob.APIHelper.UtilsApi
import com.example.praktikumprogmob.Auth.LoginActivity
import com.example.praktikumprogmob.EditProfile
import com.example.praktikumprogmob.GantiPassword
import com.example.praktikumprogmob.Main2Activity
import com.example.praktikumprogmob.Model.User
import com.example.praktikumprogmob.Model.UserResult
import com.example.praktikumprogmob.R
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ProfileFragment : Fragment() {
    internal lateinit var mApiService: BaseApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val tvNama = view.findViewById<TextView>(R.id.tv_name)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit_profile)
        val btnPassword = view.findViewById<Button>(R.id.btn_ganti_password)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        val profile = this.activity!!.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val token = "Bearer "+profile.getString("access_token",null)

        mApiService = UtilsApi.getAPIService()

        val user = mApiService.getUser(token)
        user.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val nama = response!!.body()!!.name
                val email = response!!.body()!!.email
                tvNama.setText(nama)
                tvEmail.setText(email)
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("debug","GAGAL")
            }
        })

        btnEdit.setOnClickListener {
            val intent = Intent (getActivity(), EditProfile::class.java)
            activity!!.startActivity(intent)
        }

        btnPassword.setOnClickListener {
            val intent = Intent (getActivity(), GantiPassword::class.java)
            activity!!.startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val profile = this.activity!!.getSharedPreferences("profile", Context.MODE_PRIVATE)
            val profileEditor = profile.edit()
            profileEditor.clear()
            profileEditor.commit()
            val intent = Intent (getActivity(), LoginActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.finish()
        }

        return view
    }


    companion object {
        fun newInstance(): ProfileFragment {
            val fragment = ProfileFragment()
//            val bundle = Bundle().apply {
//            }
//            fragment.arguments = bundle

            return fragment
        }

    }


}