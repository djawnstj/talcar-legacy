package com.lx.talcar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentConfirmBinding
import com.lx.talcar.databinding.FragmentHomeBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.ReservationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentConfirm : Fragment() {

    private  var _binding: FragmentConfirmBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle:Bundle

    var mode = "check"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        showCarImage("/images/benz.jpeg")

        var reId = arguments?.getInt("reId")
        var reShId = arguments?.getInt("reShId")
        var reShMem = arguments?.getString("reShMem")
        var reReserveDate = arguments?.getString("reReserveDate")
        var reReturnDate = arguments?.getString("reReturnDate")
        var reReserveTime = arguments?.getString("reReserveTime")
        var reReturnTime = arguments?.getString("reReturnTime")
        var reModel = arguments?.getString("reModel")
        var reCarNumber = arguments?.getString("reCarNumber")

        checkRe(reId!!)

        _binding?.reIdOutput?.text = "예약번호." + reId.toString()
        _binding?.reShMemOutput?.text = reShMem
        _binding?.reCarOutput?.text = reModel + "(" + reCarNumber + ")"
        _binding?.reDateOutput?.text = reReserveDate + " " + reReserveTime + " ~ " + reReturnDate + " " + reReturnTime

        _binding?.reReadyButton?.setOnClickListener {
            var fragmentReady = FragmentReady()
            bundle = Bundle()
            bundle.putInt("reId", reId!!)
            bundle.putInt("reShId", reShId!!)
            bundle.putString("reShMem", reShMem!!)
            bundle.putString("reReserveDate", reReserveDate!!)
            bundle.putString("reReturnDate", reReturnDate!!)
            bundle.putString("reReserveTime", reReserveTime!!)
            bundle.putString("reReturnTime", reReturnTime!!)
            bundle.putString("reModel", reModel!!)
            bundle.putString("reCarNumber", reCarNumber!!)
            fragmentReady.arguments = bundle
            mainActivity.supportFragmentManager.beginTransaction().replace(R.id.fragmentView, fragmentReady).addToBackStack(null).commit()
        }

        _binding?.reChatButton?.setOnClickListener {

            bundle = Bundle()
            var fragmentChat=FragmentChat()
            bundle.putString("shMem", reShMem)
            fragmentChat.arguments = bundle
            mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentChat).addToBackStack(null).commit()

        }

        return binding.root
    }

    fun checkRe(reId:Int) {
        TalcarClient.api.checkReReturn(
            reId = reId
        ).enqueue(object: Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                var data = response.body()?.data
                if(data?.get(0)?.reCheck!=null) {
                    _binding?.reReadyButton?.setText("반납하기")
                    getMode("return")
                }
            }
            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

    fun getMode(mode:String) {
        this.mode = mode
    }

    fun showCarImage(path:String?) {
        var url = "http://14.55.65.168/image.do?image=${path}"
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .into(_binding?.imageView6!!)
    }

}