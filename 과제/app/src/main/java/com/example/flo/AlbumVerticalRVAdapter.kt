package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumVerticalBinding

class AlbumVerticalRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumVerticalRVAdapter.ViewHolder>() {

    interface CustomItemClickListener{
        fun onItemClick(album: Album)
    }

    private lateinit var mItemClickListner: CustomItemClickListener
    fun setMyItemClickListner(itemClickListener: CustomItemClickListener){
        mItemClickListner = itemClickListener
    } // 리스너 객체 전달 받기


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumVerticalRVAdapter.ViewHolder { // 처음 화면에 보일 몇 개의 아이템 생성 시 호출
        val binding: ItemAlbumVerticalBinding = ItemAlbumVerticalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumVerticalBinding): RecyclerView.ViewHolder(binding.root){
        // 아이템 뷰 객체를 재활용하기 위해 객체를 담는 그릇
        fun bind(album: Album){
            // 받은 album을 표시
            binding.homeSavedAlbumTitleTv.text = album.title
            binding.homeSavedAlbumSingerTv.text = album.singer
            binding.savedAlbumIv.setImageResource(album.coverImg!!)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // position: index id
        // position을 bind에 던져준다.
        holder.bind(albumList[position])

        // 클릭 이벤트 처리: onBindViewHolder가 position값을 갖고 있기 때문
        holder.itemView.setOnClickListener{
            // 클릭 리스너가 내장되어있지 않으므로 클릭 리스너 인터페이스를 만들어줘야 한다.
            mItemClickListner.onItemClick(albumList[position])
        }
    }
}