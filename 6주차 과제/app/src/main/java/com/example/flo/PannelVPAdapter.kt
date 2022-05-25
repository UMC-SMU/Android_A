package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PannelVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return PannelFragment()
    }
}
