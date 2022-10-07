package com.lx.talcar

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.lx.talcar.api.ChatClient
import com.lx.talcar.databinding.ActivityMainBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.util.AppData
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.afollestad.assent.askForPermissions
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.response.ReservationResponse
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val channelId = "noti"
    val channelName = "noti"
    val description = "noti test"

    var notiManager: NotificationManagerCompat? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerView: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        drawerView = findViewById(R.id.drawer)

        binding.menuButton.setOnClickListener {
            drawerLayout.openDrawer(drawerView)
        }
        var homeText = findViewById<TextView>(R.id.homeTextView)
        var userTextLayout = findViewById<LinearLayout>(R.id.userTextLayout)
        var userTextArrow = findViewById<TextView>(R.id.userTextArrow)
        var userMenu = findViewById<LinearLayout>(R.id.userMenu)
        var reserveCheckText = findViewById<TextView>(R.id.reserveCheckText)
        var subscriptionText = findViewById<TextView>(R.id.subscriptionText)
        var airportReserveText = findViewById<TextView>(R.id.airportReserveText)
        var lenderText = findViewById<TextView>(R.id.lenderText)
        var chatText = findViewById<TextView>(R.id.chatText)

        homeText.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentHome()).commit()
            drawerLayout.closeDrawer(drawerView)
            userMenu.setVisibility(View.GONE)
            userTextArrow.text = " ▼"
        }

        userTextLayout.setOnClickListener {
            if(userTextArrow.text.equals(" ▲")) {
                userMenu.setVisibility(View.GONE)
                userTextArrow.text = " ▼"
            } else if(userTextArrow.text.equals(" ▼")) {
                userMenu.setVisibility(View.VISIBLE)
                userTextArrow.text = " ▲"
            }
        }

        subscriptionText.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentSubscription()).commit()
            drawerLayout.closeDrawer(drawerView)
            userMenu.setVisibility(View.GONE)
            userTextArrow.text = " ▼"

        }

        reserveCheckText.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentMyReserve()).commit()
            drawerLayout.closeDrawer(drawerView)
            userMenu.setVisibility(View.GONE)
            userTextArrow.text = " ▼"
        }

        airportReserveText.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentAirportList()).commit()
            drawerLayout.closeDrawer(drawerView)
            userMenu.setVisibility(View.GONE)
            userTextArrow.text = " ▼"
        }

        lenderText.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentReserveList()).commit()
            drawerLayout.closeDrawer(drawerView)
            userMenu.setVisibility(View.GONE)
            userTextArrow.text = " ▼"
        }

        chatText.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentChatList()).commit()
            drawerLayout.closeDrawer(drawerView)
            userMenu.setVisibility(View.GONE)
            userTextArrow.text = " ▼"
        }

        notiManager = NotificationManagerCompat.from(applicationContext)
        createNotificationChannel()

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            AppData.appId = task.result

            if(AppData.memId!=null&&!AppData.memId.equals("")) {
                ChatClient.api.setAppId(
                    memId = AppData.memId,
                    memApp = AppData.appId
                ).enqueue(object : Callback<CUDReponse> {
                    override fun onResponse(call: Call<CUDReponse>, response: Response<CUDReponse>) {

                    }
                    override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })
            }
        })

        var bundle = intent?.extras
        if(bundle!=null) {
            val chMem:String = bundle?.get("chMem") as String
            var fragmentChat=FragmentChat()
            bundle.putString("shMem", chMem)
            fragmentChat.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentChat).addToBackStack(null).commit()
        } else {

            var fragment: Fragment = FragmentHome()

            val now = System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm", Locale.KOREAN).format(now)
            println("현재시간 $simpleDateFormat")
            TalcarClient.api.getReservation(
                reDate1 = simpleDateFormat,
                reDate2 = simpleDateFormat
            ).enqueue(object : Callback<ReservationResponse> {
                override fun onResponse(
                    call: Call<ReservationResponse>,
                    response: Response<ReservationResponse>
                ) {
                    var data = response.body()?.data
                    if (data?.size!! > 0) {
                        println("사이즈 0보다 큼")
                        if (data[0].reConfirm.equals("o")) {
                            println("확인 됨 : ${data[0].reConfirm}")
                            if (!data[0].reReturn.equals("o")) {
                                println("반납 안함 : ${data[0].reReturn}")
                                fragment = FragmentConfirm()
                                var bundle = Bundle()
                                bundle.putInt("reId", data[0].reId)
                                bundle.putInt("reShId", data[0].reShId)
                                bundle.putString("reShMem", data[0].reShMem)
                                bundle.putString("reReserveDate", data[0].reReserveDate)
                                bundle.putString("reReturnDate", data[0].reReturnDate)
                                bundle.putString("reReserveTime", data[0].reReserveTime)
                                bundle.putString("reReturnTime", data[0].reReturnTime)
                                bundle.putString("reModel", data[0].reModel)
                                bundle.putString("reCarNumber", data[0].reCarNumber)
                                fragment.arguments = bundle
                                supportFragmentManager.beginTransaction()
                                    .add(R.id.fragmentView, fragment).commit()
                            } else {
                                println("반납 함")
                                fragment = FragmentHome()
                                supportFragmentManager.beginTransaction()
                                    .add(R.id.fragmentView, fragment).commit()
                            }
                        } else {
                            println("확인 안함(${data[0].reId}) : ${data[0].reConfirm}")
                            fragment = FragmentHome()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragmentView, fragment)
                                .commit()
                        }
                    } else {
                        println("사이즈 0보다 크지 않음")
                        fragment = FragmentHome()
                        supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragment)
                            .commit()
                    }
                }

                override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                    Log.d("오류", "$t")
                }
            })
        }

        // 위험 권한 부여하기
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "모든 권한이 부여됨", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "권한 중에 거부된 권한들 : $deniedList", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerView)) {
            //binding.drawerLayout.isDrawerOpen(GravityCompat.START)
            //binding.drawerLayout.closeDrawer(GravityCompat.START)
            drawerLayout.closeDrawer(drawerView)
        } else {
            super.onBackPressed()
        }
    }

    fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val bundle = intent?.extras
        val user:String = bundle?.get("user") as String
        val contents:String = bundle?.get("contents") as String
        val time:String = bundle?.get("time") as String

        var fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fragmentView)

        if(fragment is FragmentChat) {
            fragment.receiveChat(user, contents, time)
        } else {
            showNoti(user, contents)
            println("알림옴")
        }
    }

    fun showNoti(user:String?, contents:String?) {
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        resultIntent.putExtra("chMem", user)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, resultIntent, 0)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("$user 님")
            .setContentText(contents)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setChannelId(channelId)
            .setContentIntent(pendingIntent)
            .build()

        notiManager?.notify(101, notification)
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            notiManager?.createNotificationChannel(channel)
        }

    }
}