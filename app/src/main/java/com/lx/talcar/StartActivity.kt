package com.lx.talcar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lx.talcar.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(supportFragmentManager.beginTransaction()) {
            val startFragment = StartFragment()
            replace(R.id.startView, startFragment)
            commit()
        }

    }
}