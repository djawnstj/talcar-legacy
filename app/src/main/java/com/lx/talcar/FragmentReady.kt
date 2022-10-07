package com.lx.talcar

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.api.TmapClient
import com.lx.talcar.databinding.FragmentReadyBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.response.TmapAddressInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.bumptech.glide.manager.TargetTracker
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class FragmentReady : Fragment() {

    private  var _binding: FragmentReadyBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var locationManager: LocationManager
    private var REQUEST_CODE_LOCATION:Int = 2
    lateinit var bundle: Bundle
    lateinit var returnAddress: String

    lateinit var mode:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReadyBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity
        //사용자의 위치 수신을 위한 세팅

        locationManager =
            mainActivity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var currentLatLng = getMyLocation()
        showCarImage("/images/benz.jpeg")

        var reId = arguments?.getInt("reId")
        var reShId = arguments?.getInt("reShId")
        checkRe(reId!!)
        var reShMem = arguments?.getString("reShMem")
        var reReserveDate = arguments?.getString("reReserveDate")
        var reReturnDate = arguments?.getString("reReturnDate")
        var reReserveTime = arguments?.getString("reReserveTime")
        var reReturnTime = arguments?.getString("reReturnTime")
        var reModel = arguments?.getString("reModel")
        var reCarNumber = arguments?.getString("reCarNumber")



        _binding?.readyPhotoButton?.setOnClickListener {
            val dialog = FragmentPhoto()
            dialog.show(childFragmentManager, "FragmentPhoto")
            _binding?.reCheckButton?.setEnabled(true)
        }

        _binding?.reCheckButton?.setOnClickListener {
            if(mode.equals("check")) {
                TalcarClient.api.insertReCheck(
                    reVal = "o",
                    reId = reId
                ).enqueue(object : Callback<CUDReponse> {
                    override fun onResponse(
                        call: Call<CUDReponse>,
                        response: Response<CUDReponse>
                    ) {
                        var fragmentConfirm = FragmentConfirm()
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
                        fragmentConfirm.arguments = bundle
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentView, fragmentConfirm).commit()
                    }

                    override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })
            } else if(mode.equals("return")) {
                println("currentLatLng : $currentLatLng")
//                TmapClient.api.getReverseGeoCode(
//                    lat = currentLatLng?.getLatitude()!!,
//                    lon = currentLatLng?.getLongitude()!!
//                ).enqueue(object : Callback<TmapAddressInfoResponse> {
//                    override fun onResponse(call: Call<TmapAddressInfoResponse>, response: Response<TmapAddressInfoResponse>) {
//                        response.body()?.addressInfo?.fullAddress?.let { it1 -> setReturnAddress(it1) }
                        TalcarClient.api.insertReReturn(
                            reVal = "o",
                            reId = reId,
                            reReturnAddress = "공간정보아카데미 부근"
                        ).enqueue(object : Callback<CUDReponse> {
                            override fun onResponse(
                                call: Call<CUDReponse>,
                                response: Response<CUDReponse>
                            ) {
                                bundle = Bundle()
                                bundle.putString("reShMem", reShMem)
                                bundle.putString("reModel", reModel)
                                bundle.putString("reCarNumber", reCarNumber)
//                                bundle.putString("reReturnAddress", returnAddress)
                                val dialog = FragmentRating()
                                dialog.setArguments(bundle)
                                dialog.show(childFragmentManager, "FragmentRating")
                            }

                            override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                                Log.d("오류", "$t")
                            }
                        })
//                    }

//                    override fun onFailure(call: Call<TmapAddressInfoResponse>, t: Throwable) {
//                        Log.d("오류", "$t")
//                    }
//                })
            }
        }




        return binding.root
    }

    fun checkRe(reId:Int) {
        TalcarClient.api.checkReReturn(
            reId = reId
        ).enqueue(object: Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                var data = response.body()?.data
                if(!data?.get(0)?.reCheck.equals("o")) {
                    getMode("check")
                    println("mode : $mode")
                } else if(data?.get(0)?.reCheck.equals("o")) {
                    _binding?.reCheckButton?.setText("반납하기")
                    getMode("return")
                    println("mode : $mode")
                }
                setLayout()
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
            .into(_binding?.imageView5!!)
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
            currentLocation = locationManager.getLastKnownLocation(locationProvider)
            if (currentLocation != null) {
                val lng = currentLocation.longitude
                val lat = currentLocation.latitude
            }
        }
        println("currentLocation: $currentLocation")
        return currentLocation
    }

    @JvmName("setReturnAddress1")
    fun setReturnAddress(address:String) {
        this.returnAddress = address
    }

    fun setLayout() {
        if(mode.equals("check")) {
            _binding?.readyReturnCheckText?.visibility=View.GONE
            _binding?.readyCheckLayout?.visibility=View.VISIBLE
            _binding?.readyReturnLayout?.visibility=View.GONE
        } else if(mode.equals("return")) {
            _binding?.readyReturnCheckText?.visibility=View.VISIBLE
            _binding?.readyCheckLayout?.visibility=View.GONE
            _binding?.readyReturnLayout?.visibility=View.VISIBLE
        }
    }

}