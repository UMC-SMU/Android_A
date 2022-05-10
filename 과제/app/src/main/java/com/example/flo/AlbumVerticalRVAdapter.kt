package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumVerticalBinding

class AlbumVerticalRVAdapter(): RecyclerView.Adapter<AlbumVerticalRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener //이 아이템이 눌렸을 때 리스너 동작
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumVerticalRVAdapter.ViewHolder { // 처음 화면에 보일 몇 개의 아이템 생성 시 호출
        val binding: ItemAlbumVerticalBinding = ItemAlbumVerticalBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])

        holder.binding.homeSavedMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) { // 좋아요된 노래
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemAlbumVerticalBinding): RecyclerView.ViewHolder(binding.root){
        // 아이템 뷰 객체를 재활용하기 위해 객체를 담는 그릇
        fun bind(song: Song){
            // 받은 album을 표시
            binding.homeSavedAlbumTitleTv.text = song.title
            binding.homeSavedAlbumSingerTv.text = song.singer
            binding.savedAlbumIv.setImageResource(song.coverImg!!)
        }
    }
}