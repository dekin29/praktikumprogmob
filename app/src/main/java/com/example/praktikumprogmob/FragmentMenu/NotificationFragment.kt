package com.example.praktikumprogmob.FragmentMenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praktikumprogmob.R

class NotificationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_notification, container, false)

    companion object {
        fun newInstance(): NotificationFragment = NotificationFragment()
    }
}