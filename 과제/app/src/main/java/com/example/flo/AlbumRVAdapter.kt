package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {// 어댑터 클래스 상속

    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListner: MyItemClickListener
    fun setMyItemClickListner(itemClickListener: MyItemClickListener){
        mItemClickListner = itemClickListener
    } // 리스너 객체 전달 받기

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged() // 데이터가 변경되는 거를 일일히 알려줘야 한다.
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged() // 데이터가 변경되는 거를 일일히 알려줘야 한다.
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder { // 처음 화면에 보일 몇 개의 아이템 생성 시 호출
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) { // 뷰 홀더에 데이터 바인딩할 때마다, 스크롤 할 때마다
        // position: index id
        // position을 bind에 던져준다.
        holder.bind(albumList[position])

        // 클릭 이벤트 처리: onBindViewHolder가 position값을 갖고 있기 때문
        holder.itemView.setOnClickListener{
            // 클릭 리스너가 내장되어있지 않으므로 클릭 리스너 인터페이스를 만들어줘야 한다.
            mItemClickListner.onItemClick(albumList[position])
        }

        // 타이틀이 클릭되었을 때 삭제
        holder.binding.homeTodayreleaseAlbum1TitleTv.setOnClickListener{
            mItemClickListner.onRemoveAlbum(position)
        }
    }
    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        // 아이템 뷰 객체를 재활용하기 위해 객체를 담는 그릇
        fun bind(album: Album){
            // 받은 album을 표시
            binding.homeTodayreleaseAlbum1TitleTv.text = album.title
            binding.homeTodayreleaseAlbum1SingerTv.text = album.singer
            binding.itemAlbumPlayImgIv.setImageResource(album.coverImg!!)
        }
    }
}