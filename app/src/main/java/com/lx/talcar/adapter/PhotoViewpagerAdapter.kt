package com.lx.talcar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lx.talcar.*

class PhotoViewpagerAdapter(manager: Fragment): FragmentStateAdapter(manager) {
    // 화면에 표시할 Fragment 리스트
    val pageList = listOf(
        FragmentPhoto1(),
        FragmentPhoto2(),
        FragmentPhoto3(),
        FragmentPhoto4()
    )

    override fun getItemCount(): Int {
        return pageList.size
    }

    override fun createFragment(position: Int): Fragment {
        return pageList[position]
    }
}