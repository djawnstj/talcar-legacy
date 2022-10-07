package com.lx.talcar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.lx.talcar.adapter.ChatAdapter
import com.lx.talcar.api.ChatClient
import com.lx.talcar.databinding.FragmentChatBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.ChatResponse
import com.lx.talcar.response.MemResponse
import com.lx.talcar.util.AppData
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FragmentChat : Fragment() {

    private  var _binding: FragmentChatBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity
    var chatAdapter: ChatAdapter? = null

    var memId = AppData.memId
    var recApp:String? = null

    companion object {
        var requestQueue: RequestQueue? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        var shMem = arguments?.getString("shMem")
        println("shMem : $shMem")

        mainActivity = context as MainActivity

        initRecyclerView()
        getRecMem(shMem!!)
        getChat(shMem)

        requestQueue = Volley.newRequestQueue(mainActivity.applicationContext)

        println("recApp : $recApp")

        _binding?.chatSendButton?.setOnClickListener {
            var contents = _binding?.chatInput?.getText().toString()
            val now = System.currentTimeMillis()
            var time = SimpleDateFormat("yyy-MM-dd HH:mm", Locale.KOREAN).format(now)
            if(contents!=null&&!contents.equals("")) {
                sendMessage(AppData.memId, contents, time, recApp, shMem)
            }
        }

        return binding.root
    }

    fun getChat(shMem:String?) {
        ChatClient.api.getChat(
            shMem = memId,
            memId = shMem,
            shMem2 = memId,
            memId2 = shMem
        ).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                var items = response.body()?.data
                items?.apply {
                    chatAdapter?.items = this as ArrayList<ChatResponse.Data>
                    chatAdapter?.notifyDataSetChanged()
                    _binding?.chatList?.scrollToPosition(chatAdapter?.items?.size!! -1)
                }
                println("채팅 추가 : ${items?.size}")
            }
            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

    fun getRecMem(shMem:String): String {
        var recId = ""
        ChatClient.api.getRecMem(
            shMem = shMem
        ).enqueue(object : Callback<MemResponse> {
            override fun onResponse(call: Call<MemResponse>,response: Response<MemResponse>) {
                if(response.body()?.data?.get(0)?.memApp!=null) {
                    sample(response.body()?.data?.get(0)?.memApp!!)
                }
            }
            override fun onFailure(call: Call<MemResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
        return recId
    }

    fun sample(recId:String) {
        recApp = recId
        println("sample 호출됨 : $recApp, $recId")
    }

    fun receiveChat(user: String, contents: String, time:String) {
        var chatResponseData = ChatResponse.Data(
            chId = 0,
            chat = contents,
            chMem = user,
            chCounterpart = AppData.memId,
            chTime = time
        )

        chatAdapter?.items?.add(chatResponseData)
        chatAdapter?.notifyItemRangeChanged(chatAdapter?.items?.size!!, 1)
        _binding?.chatList?.scrollToPosition(chatAdapter?.items!!.size!! -1)
    }

    fun setChat(user: String, contents: String, time:String) {
        var chatResponseData = ChatResponse.Data(
            chId = 0,
            chat = contents,
            chMem = AppData.memId,
            chCounterpart = user,
            chTime = time
        )

        chatAdapter?.items?.add(chatResponseData)
        chatAdapter?.notifyItemRangeChanged(chatAdapter?.items?.size!!, 1)
        _binding?.chatList?.scrollToPosition(chatAdapter?.items!!.size!! -1)
    }

    fun initRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.chatList.layoutManager = layoutManager

        chatAdapter = ChatAdapter()

        _binding?.chatList?.adapter = chatAdapter
    }

    fun sendMessage(user:String, contents:String, time:String, recId:String?, shMem:String) {
        setChat(user, contents, time)
        _binding?.chatInput?.setText("")

        val requestData = JSONObject()
        requestData.put("priority", "high")

        val dataObj = JSONObject()
        dataObj.put("user", user)
        dataObj.put("contents", contents)
        dataObj.put("time", time)
        requestData.put("data", dataObj)

        val idArray = JSONArray()
        idArray.put(0, recId)
        requestData.put("registration_ids", idArray)

        println("appKey : ${AppData.appKey}")

        val url = "https://fcm.googleapis.com/fcm/send"
        val request = object: JsonObjectRequest(
            Request.Method.POST,
            url,
            requestData,
            {
                println("메세지 송신 : $it")
            },
            {
                println("메세지 송신 에 : $it")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "key=${AppData.appKey}")
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        request.setShouldCache(false)
        requestQueue?.add(request)

        ChatClient.api.setChat(
            chMem = memId,
            chCounterpart = shMem,
            chat = contents
        ).enqueue(object : Callback<CUDReponse> {
            override fun onResponse(call: Call<CUDReponse>,response: Response<CUDReponse>) {

            }
            override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

}