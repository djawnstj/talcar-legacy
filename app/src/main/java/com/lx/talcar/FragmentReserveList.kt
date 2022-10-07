package com.lx.talcar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.talcar.adapter.RequestShareAdapter
import com.lx.talcar.adapter.RequestSubRecyclerAdapter
import com.lx.talcar.adapter.ReserveAdapter
import com.lx.talcar.adapter.UsingShareAdapter
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentReserveListBinding
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.response.SubscriptionsResponse
import com.lx.talcar.response.TalcarListResponse
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentReserveList : Fragment() {

    private  var _binding: FragmentReserveListBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle:Bundle

    lateinit var locationManager: LocationManager
    private var REQUEST_CODE_LOCATION:Int = 2

    var reserveAdapter: ReserveAdapter? = null
    var usingShareAdapter: UsingShareAdapter? = null
    var requestShareAdapter: RequestShareAdapter? = null
    var requestSubRecyclerAdapter: RequestSubRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReserveListBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity
        //사용자의 위치 수신을 위한 세팅
        locationManager =
            mainActivity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        initReserveAdapterRecyclerView()

        val reserveCategory = resources.getStringArray(R.array.reserve_category)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, reserveCategory)
        _binding?.reserveCategory?.adapter = adapter

        _binding?.reserveCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("선택해주세요")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                //예약 등록, 요청, 이용 중
                when(position) {
                    0 -> {
                        initReserveAdapterRecyclerView()
                        _binding?.reserveListTitle?.text = "공유 등록 내역"
                    }
                    1 -> {
                        println("")
                        initRequestShareAdapterRecyclerView()
                        _binding?.reserveListTitle?.text = "이용 요청 내역"

                        requestShareAdapter?.setOnItemClickListener(object: RequestShareAdapter.OnItemClickListener {
                            override fun onItemClick(v: View, data: ReservationResponse.Data, pos: Int) {
                                var fragmentReserveDetail = FragmentReserveDetail()
                                bundle = Bundle()
                                bundle.putInt("reId", data.reId)
                                fragmentReserveDetail.arguments = bundle
                                mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentReserveDetail).addToBackStack(null).commit()
                            }

                        })
                    }
                    2 -> {
                        initSubAdapterRecyclerView()
                        _binding?.reserveListTitle?.text = "구독 요청 내역"

                        requestSubRecyclerAdapter?.setOnItemClickListener(object: RequestSubRecyclerAdapter.OnItemClickListener {
                            override fun onItemClick(v: View, data: SubscriptionsResponse.Data, pos: Int) {
                                bundle = Bundle()
                                bundle.putInt("subId", data.subId)
                                val dialog = FragmentSubscriptionDetail()
                                dialog.setArguments(bundle)
                                dialog.show(childFragmentManager, "FragmentRating")
                            }

                        })
                    }
                }
            }

        }

        return binding.root
    }

    fun initSubAdapterRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.reserveList.layoutManager = layoutManager

        requestSubRecyclerAdapter = RequestSubRecyclerAdapter()

        _binding?.reserveList?.adapter = requestSubRecyclerAdapter
        getSubList()
    }

    fun initReserveAdapterRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.reserveList.layoutManager = layoutManager

        reserveAdapter = ReserveAdapter()

        _binding?.reserveList?.adapter = reserveAdapter
        getReserveList()
    }

    fun initUsingShereRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.reserveList.layoutManager = layoutManager

        usingShareAdapter = UsingShareAdapter()

        _binding?.reserveList?.adapter = usingShareAdapter
        getRegiReserve()
    }

    fun initRequestShareAdapterRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.reserveList.layoutManager = layoutManager

        requestShareAdapter = RequestShareAdapter()

        _binding?.reserveList?.adapter = requestShareAdapter
        getRegiShare()
    }

    fun getSubList() {
        var currentLatLng = getMyLocation()

        TalcarClient.api.getRequestSub(
            //point = "POINT(${currentLatLng?.longitude} ${currentLatLng?.latitude})"
        ).enqueue(object: Callback<SubscriptionsResponse> {
            override fun onResponse(call: Call<SubscriptionsResponse>, response: Response<SubscriptionsResponse>) {
                var items = response.body()?.data
                items?.apply {
                    requestSubRecyclerAdapter?.items = this as ArrayList<SubscriptionsResponse.Data>
                    requestSubRecyclerAdapter?.notifyDataSetChanged()
                }

            }
            override fun onFailure(call: Call<SubscriptionsResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
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

    fun getReserveList() {
        TalcarClient.api.getReserveList(
            memId = AppData.memId
        ).enqueue(object: Callback<TalcarListResponse> {
            override fun onResponse(call: Call<TalcarListResponse>, response: Response<TalcarListResponse>) {
                var items = response.body()?.data
                items?.apply {
                    reserveAdapter?.items = this as ArrayList<TalcarListResponse.Data>
                    reserveAdapter?.notifyDataSetChanged()
                }

            }
            override fun onFailure(call: Call<TalcarListResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

    fun getRegiShare() {
        TalcarClient.api.getRegiShare(
            reMem = AppData.memId
        ).enqueue(object: Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                var items = response.body()?.data
                items?.apply {
                    requestShareAdapter?.items = this as ArrayList<ReservationResponse.Data>
                    requestShareAdapter?.notifyDataSetChanged()
                }

            }
            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

    private fun getMyLocation(): Location? {
        var currentLocation: Location? = null
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(
                mainActivity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mainActivity!!, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("////////////사용자에게 권한을 요청해야함")
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            getMyLocation() //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {
            println("////////////권한요청 안해도됨")

            // 수동으로 위치 구하기
            val locationProvider = LocationManager.GPS_PROVIDER
            println("locationProvider : $locationProvider")
            currentLocation = locationManager.getLastKnownLocation(locationProvider)
            if (currentLocation != null) {
                val lng = currentLocation.longitude
                val lat = currentLocation.latitude
            }
        }
        println("currentLocation : $currentLocation")
        return currentLocation
    }

}