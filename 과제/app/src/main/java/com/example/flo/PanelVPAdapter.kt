package com.example.flo;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment){
    // 여러 프래그먼트를 담아둘 공간
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        // 데이터를 몇 개를 전달할 것인가
        return fragmentlist.size
    }

    //override fun getItemCount(): Int = fragmentlist.size

    override fun createFragment(position: Int): Fragment = fragmentlist[position] // getItemCount가 4라면 0, 1, 2, 3 실행

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1) // 뷰 페이저에게 새 값이 추가되었다는 걸 알려준다.
    }
}
