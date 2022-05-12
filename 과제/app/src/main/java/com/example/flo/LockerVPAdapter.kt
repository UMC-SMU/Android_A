package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // when은 switch와 비슷하다.
        return when(position){
            0 -> SavedFragment()
            1 -> MusicfilesFragment() // 1
            else -> SavedAlbumFragment()
        }
    }


}