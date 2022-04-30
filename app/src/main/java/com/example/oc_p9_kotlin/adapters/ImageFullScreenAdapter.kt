package com.example.oc_p9_kotlin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oc_p9_kotlin.R
import com.example.oc_p9_kotlin.databinding.ItemPicFullscreenLayoutBinding
import com.example.oc_p9_kotlin.models.Media
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class ImageFullScreenAdapter(

    var imageList: List<Media>,
) : RecyclerView.Adapter<ImageFullScreenAdapter.ImageViewHolder>() {

    companion object {
        var TAG = "ImageAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemPicFullscreenLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val media = imageList[position]

        holder.itemPicFullscreenLayoutBinding.itemPicFullscreenTitle.text = media.name

            Log.d(TAG, media.toString())

            Glide.with(holder.itemView.context)
                .load(media.uri)
                .error(R.drawable.ic_house)
                .placeholder(R.drawable.ic_house)
                .into(holder.itemPicFullscreenLayoutBinding.itemPicFullscreen)

        /*
        else {

            val defaultDataSourceFactory = DefaultDataSource.Factory(holder.itemView.context)

            var mediaSource = ProgressiveMediaSource.Factory(
                defaultDataSourceFactory,
                DefaultExtractorsFactory()
            )

            val player =
                ExoPlayer.Builder(holder.itemPicFullscreenLayoutBinding.root.context)
                .setMediaSourceFactory(mediaSource)
                .build()

            holder.itemPicFullscreenLayoutBinding.fullscreenPlayerView.player = player

            val mediaItem = MediaItem.fromUri(media.uri)

            player.addMediaItem(mediaItem)
            player.prepare()
            player.play()

            holder.itemPicFullscreenLayoutBinding.itemPicFullscreenTitle.text = media.name

        }


 */

    }

    override fun getItemCount(): Int {
        return imageList.size
    }


    class ImageViewHolder(val itemPicFullscreenLayoutBinding: ItemPicFullscreenLayoutBinding) :
        RecyclerView.ViewHolder(itemPicFullscreenLayoutBinding.root)
}