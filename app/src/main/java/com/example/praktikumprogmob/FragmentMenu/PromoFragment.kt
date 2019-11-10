package com.example.praktikumprogmob.FragmentMenu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.praktikumprogmob.R

class PromoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_promo, container, false)

    companion object {
        fun newInstance(): PromoFragment = PromoFragment()
    }
}