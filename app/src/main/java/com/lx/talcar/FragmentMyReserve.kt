package com.lx.talcar

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.talcar.adapter.UsingShareAdapter
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentMyReserveBinding
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentMyReserve : Fragment() {

    private  var _binding: FragmentMyReserveBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle:Bundle

    lateinit var locationManager: LocationManager
    var usingShareAdapter: UsingShareAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyReserveBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity
        //사용자의 위치 수신을 위한 세팅
        locationManager =
            mainActivity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        initUsingShereRecyclerView()

        return binding.root
    }


    fun initUsingShereRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.myReserveList.layoutManager = layoutManager

        usingShareAdapter = UsingShareAdapter()

        _binding?.myReserveList?.adapter = usingShareAdapter
        getRegiReserve()
    }

    fun getRegiReserve() {
        TalcarClient.api.getRegiReserve(
            reMem = AppData.memId
        ).enqueue(object: Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                var items = response.body()?.data
                items?.apply {
                    usingShareAdapter?.items = this as ArrayList<ReservationResponse.Data>
                    usingShareAdapter?.notifyDataSetChanged()
                }

            }
            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

}