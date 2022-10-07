package com.lx.talcar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lx.talcar.databinding.FragmentHomeBinding

class FragmentEx : Fragment() {

    private  var _binding: FragmentHomeBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity


        return binding.root
    }

}