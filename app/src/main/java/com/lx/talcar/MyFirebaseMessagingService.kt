package com.lx.talcar

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lx.talcar.adapter.ChatAdapter
import com.lx.talcar.response.ChatResponse
import com.lx.talcar.util.AppData

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token:String) {
        super.onNewToken(token)

        println("onNewToken 호출됨 : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        println("onMessageReceived 호출됨 : $message")

//        println("수신 데이터 : ${message.from}, ${message.data["title"]}, ${message.data["contents"]}")
        sendToActivity(message.data["user"], message.data["contents"], message.data["time"])
    }

    fun sendToActivity(user:String?, contents:String?, time:String?) {
        println("sendToActivity 호출")
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("contents", contents)
        intent.putExtra("time", time)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK +
                        Intent.FLAG_ACTIVITY_SINGLE_TOP +
                        Intent.FLAG_ACTIVITY_CLEAR_TOP)
        applicationContext.startActivity(intent)
    }
}