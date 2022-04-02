package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbumImgIv1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
        }

        val bannerAdaper = BannerVPAdapter(this)
        bannerAdaper.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdaper.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdaper //뷰페이저와 어뎁터 연결
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL    // 뷰페이저가 좌우로 스크롤 가능

        val pannelAdapter = PannelVPAdapter(this)
        binding.homeContentVp.adapter = pannelAdapter

        return binding.root
    }
}