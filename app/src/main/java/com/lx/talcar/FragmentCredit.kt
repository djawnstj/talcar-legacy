package com.lx.talcar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentCreditBinding
import com.lx.talcar.databinding.FragmentHomeBinding
import com.lx.talcar.response.ReservationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FragmentCredit : Fragment() {

    private  var _binding: FragmentCreditBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle:Bundle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreditBinding.inflate(inflater, container, false)

        var shAddress = arguments?.getString("shAddress")
        var shModel = arguments?.getString("shModel")
        var shCarNumber = arguments?.getString("shCarNumber")
        var shMem = arguments?.getString("shMem")

        mainActivity = context as MainActivity

        _binding?.creditAddress?.text = shAddress
        _binding?.creditModel?.text = shModel
        _binding?.creditCarNumber?.text = shCarNumber
        _binding?.creditShMem?.text = shMem

        showCarImage("/images/benz.jpeg")

        _binding?.creditCheckbutton?.setOnClickListener {
            activity?.let{
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

    fun showCarImage(path:String?) {
        var url = "http://14.55.65.168/image.do?image=${path}"
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .into(_binding?.imageView4!!)
    }

}