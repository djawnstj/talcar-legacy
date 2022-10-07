package com.lx.talcar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.lx.talcar.adapter.TalcarRecyclerAdapter
import com.lx.talcar.api.ChatClient
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.api.TmapClient
import com.lx.talcar.databinding.FragmentHomeBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.MemResponse
import com.lx.talcar.response.TalcarListResponse
import com.lx.talcar.response.TmapSearchResponse
import com.lx.talcar.util.MyItem
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentHome : Fragment() {

    private  var _binding: FragmentHomeBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var map: GoogleMap
    lateinit var locationClient: FusedLocationProviderClient
    var talcarRecyclerAdapter: TalcarRecyclerAdapter? = null
    lateinit var currentLatLng: LatLng
    private lateinit var mClusterManager: ClusterManager<MyItem>

    lateinit var bundle: Bundle

    lateinit var reserveDate:String
    lateinit var reserveTime:String
    lateinit var returnDate:String
    lateinit var returnTime:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity
        val slidePanel = _binding?.homeSlidePanel

        initRecyclerView()

        val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)

        try {
            MapsInitializer.initialize(mainActivity.applicationContext)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        mapFragment.getMapAsync {
            map = it
            requestLocation()
        }

        _binding?.searchText?.setOnClickListener {

            // Dialog만들기
            var mDialogView = LayoutInflater.from(mainActivity).inflate(R.layout.custom_dialog, null)
            val mBuilder = AlertDialog.Builder(mainActivity)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()

            var reservTimePickerBtn = mAlertDialog.findViewById<TextView>(R.id.reservTimePickerBtn)
            var returnTimePickerBtn = mAlertDialog.findViewById<TextView>(R.id.returnTimePickerBtn)
            var reservationDate = mAlertDialog.findViewById<TextView>(R.id.reservationDate)
            var returnDateInput = mAlertDialog.findViewById<TextView>(R.id.returnDate)

            val nowDate = LocalDateTime.now()

            reservTimePickerBtn?.setOnClickListener {
                val cal = Calendar.getInstance()

                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)

                        var time:String = ""
                        if(SimpleDateFormat("mm").format(cal.time).toInt()<30) {
                            time = SimpleDateFormat("HH").format(cal.time) + ":00"
                        } else if(SimpleDateFormat("mm").format(cal.time).toInt()>=30) {
                            time = SimpleDateFormat("HH").format(cal.time) + ":30"
                        }

                        reservTimePickerBtn?.setText(time)
                        setReserveTime(time!!)
                    }
                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }

            returnTimePickerBtn?.setOnClickListener {
                val cal = Calendar.getInstance()

                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)

                    var time:String? = null
                    if(SimpleDateFormat("mm").format(cal.time).toInt()<30) {
                        time = SimpleDateFormat("HH").format(cal.time) + ":00"
                    } else if(SimpleDateFormat("mm").format(cal.time).toInt()>=30) {
                        time = SimpleDateFormat("HH").format(cal.time) + ":30"
                    }

                    returnTimePickerBtn?.setText(time)
                    setReturnTime(time!!)
                }
                TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }


            reservationDate?.setOnClickListener {
                val listener =
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var date:String = year.toString() + "-" + (monthOfYear.toInt()+1).toString() + "-" + dayOfMonth
                        reservationDate.text = date
                        setReserveDate(date)
                    }
                val dialog = DatePickerDialog(mainActivity, listener, nowDate.year, nowDate.monthValue-1, nowDate.dayOfMonth)
                dialog.show()
            }

            returnDateInput?.setOnClickListener {
                val listener =
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var date:String = year.toString() + "-" + (monthOfYear.toInt()+1).toString() + "-" + dayOfMonth
                        returnDateInput.text = date
                        setReturnDate(date)
                    }
                val dialog = DatePickerDialog(mainActivity, listener, nowDate.year, nowDate.monthValue-1, nowDate.dayOfMonth)
                dialog.show()
            }

            var searchConditionButton = mAlertDialog.findViewById<Button>(R.id.searchConditionButton)
            var searchAddressInput = mAlertDialog.findViewById<EditText>(R.id.dialogAddressInput)

            searchConditionButton?.setOnClickListener {
                TmapClient.api.getSearchLocation(
                    keyword = searchAddressInput?.getText().toString(),
                    currentLon = currentLatLng.longitude.toString(),
                    currentLat = currentLatLng.latitude.toString(),
                ).enqueue(object: Callback<TmapSearchResponse> {
                    override fun onResponse(call: Call<TmapSearchResponse>, response: Response<TmapSearchResponse>) {
                        var data = response.body()?.searchPoiInfo?.pois?.poi
                        if(data!!.size>0) {
                            TalcarClient.api.getTalcarList(
                                point = "POINT(${data.get(0).noorLon} ${data.get(0).noorLat})",
                                reserveDate1 = reserveDate,
                                reserveDate2 = reserveDate,
                                reserveDate3 = reserveDate,
                                reserveDate4 = reserveDate,
                                reserveTime1 = reserveTime,
                                reserveTime2 = reserveTime,
                                reserveTime3 = reserveTime,
                                reserveTime4 = reserveTime,
                                returnDate1 = returnDate,
                                returnDate2 = returnDate,
                                returnDate3 = returnDate,
                                returnDate4 = returnDate,
                                returnTime1 = returnTime,
                                returnTime2 = returnTime,
                                returnTime3 = returnTime,
                                returnTime4 = returnTime
                            ).enqueue(object: Callback<TalcarListResponse> {
                                override fun onResponse(call: Call<TalcarListResponse>, response: Response<TalcarListResponse>) {
                                    var items = response.body()?.data
                                    items?.apply {
                                        talcarRecyclerAdapter?.items = this as ArrayList<TalcarListResponse.Data>
                                        talcarRecyclerAdapter?.notifyDataSetChanged()

                                        showTalcar()
                                    }
                                }
                                override fun onFailure(call: Call<TalcarListResponse>, t: Throwable) {
                                    Log.d("오류", "$t")
                                }
                            })
                        }

                    }
                    override fun onFailure(call: Call<TmapSearchResponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })

                _binding?.searchText?.text = "$reserveDate $reserveTime ~ $returnDate $returnTime"
                mAlertDialog.dismiss()
            }

            var dialogCloseBtn = mAlertDialog.findViewById<ImageView>(R.id.ratingCloseBtn)
            dialogCloseBtn?.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        talcarRecyclerAdapter?.setOnItemClickListener(object: TalcarRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: TalcarListResponse.Data, pos: Int) {
                _binding?.slideShId?.text = data.shId.toString()
                _binding?.slideTitle?.text = "제목 : ${data.shTitle}"
                _binding?.slideShMem?.text = "아이디 : ${data.memId}"
                _binding?.slidePost?.text = "내용 : \n${data.shPost}"
                _binding?.slideModel?.text = "차량(번호) : ${data.shModel}"
                _binding?.slideCarNumber?.text = "(${data.shCarNumber})"
                _binding?.slideAddress?.text = "주소 : ${data.shAddress}"
                _binding?.slideDate?.text = "기간 : $reserveDate $reserveTime ~ $returnDate $returnTime"
                showCarImage("/images/benz.jpeg")

                var returnArray = returnTime.split(":")
                var reserveArray = reserveTime.split(":")
                var time = (returnArray[0].toInt()*60 + returnArray[1].toInt()) - (reserveArray[0].toInt()*60 + reserveArray[1].toInt())
                _binding?.slidePrice?.text = "금액(시간당) : ${data.shPrice}"
                slidePanel?.panelState = SlidingUpPanelLayout.PanelState.ANCHORED

                _binding?.moveChatButton?.setOnClickListener {
                    bundle = Bundle()
                    var fragmentChat=FragmentChat()
                    var shMem:String = data.memId
                    bundle.putString("shMem", shMem)
                    fragmentChat.arguments = bundle
                    mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentChat).addToBackStack(null).commit()
                }

                _binding?.creditButton?.setOnClickListener {
                    setReservation(data.shId, data.memId, (time/60*data.shPrice), data.shModel, data.shCarNumber)
                    var fragmentCredit = FragmentCredit()
                    bundle = Bundle()
                    bundle.putString("shAddress", data.shAddress)
                    bundle.putString("shModel", data.shModel)
                    bundle.putString("shCarNumber", data.shCarNumber)
                    bundle.putString("shMem", data.memId)
                    fragmentCredit.arguments = bundle
                    mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentCredit).addToBackStack(null).commit()
                }

            }
        })

        return binding.root
    }

    fun setReservation(reShId:Int, reShMem:String, rePrice:Int, reModel:String, reCarNumber:String) {
        TalcarClient.api.setReservation(
            reShId = reShId,
            reShMem = reShMem,
            reserveDate = reserveDate,
            returnDate = returnDate,
            reserveTime = reserveTime,
            returnTime = returnTime,
            rePrice = rePrice,
            reModel = reModel,
            reCarNumber = reCarNumber
        ).enqueue(object: Callback<CUDReponse> {
            override fun onResponse(call: Call<CUDReponse>, response: Response<CUDReponse>) {
                var items = response.body()?.data
            }
            override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

    @JvmName("setReserveDate1")
    fun setReserveDate(reserveDate:String) {
        this.reserveDate = reserveDate
    }

    @JvmName("setReserveTime1")
    fun setReserveTime(reserveTime:String) {
        this.reserveTime = reserveTime
    }

    @JvmName("setReturnDate1")
    fun setReturnDate(returnDate:String) {
        this.returnDate = returnDate
    }

    @JvmName("setReturnTime1")
    fun setReturnTime(returnTime:String) {
        this.returnTime = returnTime
    }

    fun getRecMem(shMem:String): String {
        lateinit var recId:String
        ChatClient.api.getRecMem(
            shMem = shMem
        ).enqueue(object : Callback<MemResponse> {
            override fun onResponse(call: Call<MemResponse>, response: Response<MemResponse>) {
                recId = response.body()?.data?.get(0)?.memApp!!
            }
            override fun onFailure(call: Call<MemResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
        return recId
    }

    fun showTalcar() {
        map.clear()
        if(talcarRecyclerAdapter?.items?.size!!>0) {
            talcarRecyclerAdapter?.items?.apply {
                var latlng = LatLng(this[0].latitude, this[0].longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17.0f))
                for (item in this) {
                    val constellationMaker = MarkerOptions()
                    with(constellationMaker) {
                        position(LatLng(item.latitude!!, item.longitude!!))
                        title(item.shTitle)
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
                        map.addMarker(this)
                    }
                }
            }
        }
    }

    fun initRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.homeTalcarList.layoutManager = layoutManager

        talcarRecyclerAdapter = TalcarRecyclerAdapter()

        _binding?.homeTalcarList?.adapter = talcarRecyclerAdapter
    }

    fun requestLocation() {
        // 위치 클라이언트 가져오기
        locationClient = LocationServices.getFusedLocationProviderClient((activity as MainActivity))
        try {
            // 최근 위치 확인하기
            locationClient?.lastLocation?.addOnSuccessListener {
                println("최근 위치 : ${it?.latitude}, ${it?.longitude}")
            }?.addOnFailureListener {
                println("최근 위치 확인 시 에러 : ${it.message}")
            }

            // 현재 위치 확인하기
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                interval = 1000 * 1000
            }

            val locationCallback = object: LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    println("현재 위치 확인")
                    result.run {
                        for ((index, location) in locations.withIndex()) {
                            var nowLatLng = LatLng(location.latitude, location.longitude)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLatLng, 17.0f))

                            setLatLng(location.latitude, location.longitude)
                        }
                    }
                }
            }

            locationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        } catch(e:SecurityException) {
            e.printStackTrace()
        }
    }

    fun setLatLng(latitude:Double, longitude:Double) {
        this.currentLatLng = LatLng(latitude, longitude)
        println("setLatLng : $currentLatLng")

        val now = System.currentTimeMillis()
        val nowDate = SimpleDateFormat("yyy-MM-dd", Locale.KOREAN).format(now)

        var exReservetime:String = ""
        var exReturnTime:String = ""
        if(SimpleDateFormat("mm", Locale.KOREAN).format(now).toInt()<30) {
            exReservetime = SimpleDateFormat("HH", Locale.KOREAN).format(now) + ":00"
            exReturnTime = (SimpleDateFormat("HH", Locale.KOREAN).format(now).toInt()+2).toString() + ":00"
        } else if(SimpleDateFormat("mm", Locale.KOREAN).format(now).toInt()>=30) {
            exReservetime = SimpleDateFormat("HH", Locale.KOREAN).format(now) + ":30"
            exReturnTime = (SimpleDateFormat("HH", Locale.KOREAN).format(now).toInt()+2).toString() + ":30"
        }

        _binding?.searchText?.text = "$nowDate $exReservetime ~ $nowDate $exReturnTime"

        TalcarClient.api.getTalcarList(
            point = "POINT(${currentLatLng.longitude} ${currentLatLng.latitude})",
            reserveDate1 = nowDate,
            reserveDate2 = nowDate,
            reserveDate3 = nowDate,
            reserveDate4 = nowDate,
            reserveTime1 = exReservetime,
            reserveTime2 = exReservetime,
            reserveTime3 = exReservetime,
            reserveTime4 = exReservetime,
            returnDate1 = nowDate,
            returnDate2 = nowDate,
            returnDate3 = nowDate,
            returnDate4 = nowDate,
            returnTime1 = exReturnTime,
            returnTime2 = exReturnTime,
            returnTime3 = exReturnTime,
            returnTime4 = exReturnTime
        ).enqueue(object: Callback<TalcarListResponse> {
            override fun onResponse(call: Call<TalcarListResponse>, response: Response<TalcarListResponse>) {
                var items = response.body()?.data
                items?.apply {
                    talcarRecyclerAdapter?.items = this as ArrayList<TalcarListResponse.Data>
                    talcarRecyclerAdapter?.notifyDataSetChanged()

                    showTalcar()
                }
            }
            override fun onFailure(call: Call<TalcarListResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })

    }

    private fun setUpClusterer() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))
        mClusterManager = ClusterManager(mainActivity, map)
        map.setOnCameraIdleListener(mClusterManager)
        map.setOnMarkerClickListener(mClusterManager)
        addItems()
    }

    private fun addItems() {
        var lat = 51.5145160
        var lng = -0.1270060
        for (i in 0..9) {
            val offset = i / 60.0
            lat = lat + offset
            lng = lng + offset
            val title = "This is the title"
            val snippet = "and this is the snippet."
            val offsetItem = MyItem(lat, lng, title, snippet)
            mClusterManager.addItem(offsetItem)
        }
    }

    fun showCarImage(path:String?) {
        var url = "http://14.55.65.168/image.do?image=${path}"
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .into(_binding?.imageView3!!)
    }

    fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(
            mainActivity, "Info window clicked",
            Toast.LENGTH_SHORT
        ).show()
    }
}