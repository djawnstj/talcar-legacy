package com.lx.talcar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.assent.askForPermissions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lx.talcar.adapter.PhotoViewpagerAdapter
import com.lx.talcar.databinding.FragmentPhotoBinding
import me.relex.circleindicator.CircleIndicator3

class FragmentPhoto : DialogFragment() {

    var _binding: FragmentPhotoBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    private var mPager:ViewPager2? = null
    private var pagerAdapter: FragmentStateAdapter? = null
    private var num_page:Int? = null
    private var mIndicator: CircleIndicator3? = null

    private val pagerAdpater by lazy {
        PhotoViewpagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        // 위험 권한 요청
        askForPermissions(com.afollestad.assent.Permission.READ_EXTERNAL_STORAGE, com.afollestad.assent.Permission.CAMERA) { result  ->
            println("askForPermissions result : ${result.granted().size}")
        }

         _binding?.photoTab?.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
             override fun onTabSelected(tab: TabLayout.Tab?) {

             }

             override fun onTabUnselected(tab: TabLayout.Tab?) {

             }

             override fun onTabReselected(tab: TabLayout.Tab?) {

             }

         })

        _binding?.photoViewPager?.apply {
            this.adapter = pagerAdpater
            isUserInputEnabled = false
        }

        TabLayoutMediator(_binding?.photoTab!!, _binding?.photoViewPager!!) {
            tab, position ->
            when(position) {
                0 -> tab.text = "전면"
                1 -> tab.text = "후면"
                2 -> tab.text = "측면(좌측)"
                3 -> tab.text = "측면(우측)"
            }
        }.attach()

        _binding?.photoViewPager?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position) {
                    0 -> _binding?.dialogCloseBtn?.apply {
                        setEnabled(true)
                        setText("다음")
                        setOnClickListener {
                            _binding?.photoViewPager?.setCurrentItem(1)
                        }
                    }
                    1 -> _binding?.dialogCloseBtn?.apply {
                        setEnabled(true)
                        setText("다음")
                        setOnClickListener {
                            _binding?.photoViewPager?.setCurrentItem(2)
                        }
                    }
                    2 -> _binding?.dialogCloseBtn?.apply {
                        setEnabled(true)
                        setText("다음")
                        setOnClickListener {
                            _binding?.photoViewPager?.setCurrentItem(3)
                        }
                    }
                    3 -> _binding?.dialogCloseBtn?.apply {
                        setEnabled(true)
                        setText("외관에 이상이 없습니다.")
                        setOnClickListener{
                            dismiss()
                        }
                    }
                }
            }
        })

        return binding.root
    }
}

