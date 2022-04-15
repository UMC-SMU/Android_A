package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var currentPosition = 0
    val handler = Handler(Looper.getMainLooper()){
        setPage()
        true
    }

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
        val thread = Thread(PagerRunnable())
        thread.start()

        return binding.root
    }

    fun setPage(){
        if(currentPosition ==5)
            currentPosition =0
        binding.homeContentVp.setCurrentItem(currentPosition,true)
        currentPosition+=1
    }

    inner class PagerRunnable : Runnable{
        override fun run() {
            while(true){
                try{
                    Thread.sleep(2000)
                    handler.sendEmptyMessage(0)
                }catch (e:InterruptedException){
                    Log.d("interrupt","interrupt 발생")
                }
            }
        }
    }

}