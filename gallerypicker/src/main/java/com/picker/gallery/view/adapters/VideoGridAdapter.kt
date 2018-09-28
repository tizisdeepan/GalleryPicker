package com.picker.gallery.view.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.detailedimage.*
import kotlinx.android.synthetic.main.grid_item.view.*
import org.jetbrains.anko.doAsync
import kotlin.collections.ArrayList
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.picker.gallery.R
import com.picker.gallery.model.GalleryData
import com.picker.gallery.utils.DateUtil
import com.picker.gallery.utils.MLog
import com.picker.gallery.utils.RunOnUiThread
import com.picker.gallery.utils.scroll.FastScrollRecyclerView

class VideoGridAdapter() : RecyclerView.Adapter<VideoGridAdapter.MyViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    lateinit var ctx: Context
    private var mimageList: ArrayList<GalleryData> = ArrayList()
    private var fullimagelist: ArrayList<GalleryData> = ArrayList()
    var THRESHOLD = 1

    constructor(imageList: ArrayList<GalleryData> = ArrayList(), filter: Int = 0, threshold: Int = 4) : this() {
        fullimagelist = imageList
        THRESHOLD = threshold
        if (filter == 0) mimageList = imageList
        else imageList.filter { it.albumId == filter }.forEach { mimageList.add(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        ctx = parent.context
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (THRESHOLD != 0) {
            if (getSelectedCount() >= THRESHOLD) mimageList.filterNot { it.isSelected }.forEach { it.isEnabled = false }
            else mimageList.forEach { it.isEnabled = true }
        }

        doAsync {
            RunOnUiThread(ctx).safely {
                try {
                    val requestListener: RequestListener<Drawable> = object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            MLog.e("error", "error")
                            holder.image.alpha = 0.3f
                            holder.image.isEnabled = false
                            holder.checkbox.visibility = View.INVISIBLE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                    }
                    Glide.with(ctx).load(mimageList[holder.adapterPosition].photoUri).apply(RequestOptions().centerCrop().override(150, 150)).transition(DrawableTransitionOptions.withCrossFade()).listener(requestListener).into(holder.image)
                } catch (e: Exception) {
                }
            }
        }

        if (mimageList[holder.adapterPosition].isEnabled) {
            holder.frame.alpha = 1.0f
            holder.image.isEnabled = true
            holder.checkbox.visibility = View.VISIBLE
        } else {
            holder.frame.alpha = 0.3f
            holder.image.isEnabled = false
            holder.checkbox.visibility = View.INVISIBLE
        }

        if (mimageList[holder.adapterPosition].mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            holder.durationFrame.visibility = View.VISIBLE
            holder.durationLabel.text = DateUtil().millisToTime(mimageList[holder.adapterPosition].duration.toLong())
        } else holder.durationFrame.visibility = View.GONE

        if (mimageList[holder.adapterPosition].isSelected) holder.checkbox.setImageResource(R.drawable.tick)
        else holder.checkbox.setImageResource(R.drawable.round)

        holder.image.setOnClickListener {
            if (THRESHOLD != 0) {
                when {
                    getSelectedCount() <= THRESHOLD -> {
                        if (mimageList[holder.adapterPosition].isSelected) {
                            mimageList[holder.adapterPosition].isSelected = false
                            holder.checkbox.setImageResource(R.drawable.round)
                            if (getSelectedCount() == (THRESHOLD - 1) && !mimageList[holder.adapterPosition].isSelected) {
                                mimageList.forEach { it.isEnabled = true }
                                for ((index, item) in mimageList.withIndex()) {
                                    if (item.isEnabled && !item.isSelected) notifyItemChanged(index)
                                }
                            }
                        } else {
                            mimageList[holder.adapterPosition].isSelected = true
                            holder.checkbox.setImageResource(R.drawable.tick)
                            if (getSelectedCount() == THRESHOLD && mimageList[holder.adapterPosition].isSelected) {
                                mimageList.filterNot { it.isSelected }.forEach { it.isEnabled = false }
                                for ((index, item) in mimageList.withIndex()) {
                                    if (!item.isEnabled) notifyItemChanged(index)
                                }
                            }
                        }
                    }
                    getSelectedCount() > THRESHOLD -> {
                        for (image in mimageList) {
                            mimageList.filter { it.isSelected && !it.isEnabled }.forEach { it.isSelected = false }
                        }
                    }
                    else -> {
                    }
                }
            } else {
                if (mimageList[holder.adapterPosition].isSelected) {
                    mimageList[holder.adapterPosition].isSelected = false
                    holder.checkbox.setImageResource(R.drawable.round)
                    notifyItemChanged(holder.adapterPosition)
                } else {
                    mimageList[holder.adapterPosition].isSelected = true
                    holder.checkbox.setImageResource(R.drawable.tick)
                    notifyItemChanged(holder.adapterPosition)
                }
            }
        }

        var dialog: Dialog? = null
        holder.image.setOnLongClickListener {
            dialog = Dialog(ctx)
            if (dialog != null) {
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.setContentView(R.layout.detailedimage)
                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                doAsync {
                    RunOnUiThread(ctx).safely {
                        Glide.with(ctx).load(mimageList[holder.adapterPosition].photoUri).into(dialog?.bigimage!!)
                    }
                }
                dialog?.show()
            }
            true
        }

        holder.image.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP || event.action == DragEvent.ACTION_DROP) dialog?.dismiss()
            false
        }
    }

    private fun getSelectedCount(): Int = fullimagelist.count { it.isSelected }

    override fun getItemCount(): Int = mimageList.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image = view.image
        var checkbox = view.checkbox
        var frame = view.frame
        val durationFrame = view.durationFrame
        val durationLabel = view.durationLabel
    }

    override fun getSectionName(position: Int): String = DateUtil().getMonthAndYearString(mimageList[position].dateAdded.toLong() * 1000)
}