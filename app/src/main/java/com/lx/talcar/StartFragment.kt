package com.lx.talcar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lx.talcar.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private  var _binding: FragmentStartBinding? =null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        var startActivity = context as StartActivity

        showTitleImage()

        _binding?.loginViewButton?.setOnClickListener {
            startActivity.supportFragmentManager.beginTransaction().replace(R.id.startView, LoginFragment()).addToBackStack(null).commit()
        }

        return binding.root
    }

    fun showTitleImage() {
        Glide.with(this)
            .load(R.raw.talcar_title)
            .into(_binding?.imageView9!!)
    }

}