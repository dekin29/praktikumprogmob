package com.example.praktikumprogmob.FragmentMenu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.praktikumprogmob.APIHelper.BaseApiService
import com.example.praktikumprogmob.LoginActivity
import com.example.praktikumprogmob.R
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ProfileFragment : Fragment() {
    private var profile: SharedPreferences? = null
    val mContext = this
    internal lateinit var mApiService: BaseApiService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val tvNama = view.findViewById<TextView>(R.id.tv_name)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        val id = arguments?.getInt(ARG_ID)
        val profile = this.activity!!.getSharedPreferences("profile", Context.MODE_PRIVATE)
        val id2 = profile.getInt("id",0)

        Log.d("debug","idprof : "+id+"idsf :"+id2)

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
        const val ARG_ID = "id"

        fun newInstance(id: Int): ProfileFragment {
            val fragment = ProfileFragment()
            val bundle = Bundle().apply {
                putInt(ARG_ID, id)
            }

            fragment.arguments = bundle

            return fragment
        }

    }

    private fun getDetail(id: Int?) {
        mApiService.detailUser(id)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            try {
                                val jsonRESULTS = JSONObject(response.body()!!.string())
                                val id = jsonRESULTS.getJSONObject("user").getInt("id")
                                val nama = jsonRESULTS.getJSONObject("user").getString("name")
                                val email = jsonRESULTS.getJSONObject("user").getString("email")

                                Log.d("debug","nama :"+ nama + "email : " + email)

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("debug", "onFailure: ERROR > $t")
                    }
                })


    }


}