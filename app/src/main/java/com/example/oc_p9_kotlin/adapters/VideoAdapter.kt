package com.example.oc_p9_kotlin.adapters

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oc_p9_kotlin.databinding.ItemVideosBinding
import com.example.oc_p9_kotlin.models.Media
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import java.io.File


class VideoAdapter(

    var videoList: MutableList<Media>,
    private var isEditing: Boolean = false,
    private var onDataUpdate: () -> Unit,

    ) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    companion object {
        var TAG = "VideoAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideosBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        val media = videoList[position]

        holder.itemVideosBinding.itemVideoTitle.text = media.name

        val defaultDataSourceFactory = DefaultDataSource.Factory(holder.itemView.context)

        val mediaSource = ProgressiveMediaSource.Factory(
            defaultDataSourceFactory,
            DefaultExtractorsFactory()
        )

        val player = ExoPlayer.Builder(holder.itemVideosBinding.root.context)
            .setMediaSourceFactory(mediaSource)
            .build()

        holder.itemVideosBinding.itemVideoPlayerView.player = player

        if (videoList.isNullOrEmpty())
            return

        val mediaItem = MediaItem.fromUri(media.uri)

        player.addMediaItem(mediaItem)
        player.prepare()
        player.play()

        if (isEditing) {
            holder.itemVideosBinding.itemVideoDelete.visibility = View.VISIBLE
            holder.itemVideosBinding.itemVideoDelete.setOnClickListener {
                removeData(position)
            }
        } else {
            holder.itemVideosBinding.itemVideoDelete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    fun updateData(newList: MutableList<Media>) {
        this.videoList = newList
        notifyDataSetChanged()
        onDataUpdate()
    }

    fun addData(media: Media) {
        this.videoList.add(media)
        notifyItemInserted(videoList.size)
        onDataUpdate()
    }

    public fun removeData(position: Int) {
        this.videoList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        onDataUpdate()
    }

    class VideoViewHolder(val itemVideosBinding: ItemVideosBinding) :
        RecyclerView.ViewHolder(itemVideosBinding.root)
}