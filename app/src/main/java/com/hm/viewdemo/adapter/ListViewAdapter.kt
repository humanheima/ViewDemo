package com.hm.viewdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hm.viewdemo.R
import com.hm.viewdemo.bean.MyBean
import java.util.*

/**
 * Created by dumingwei on 2020/4/1
 *
 *
 * Desc:
 */
class ListViewAdapter(
        context: Context,
        private val resource: Int,
        lists: ArrayList<MyBean>
) : ArrayAdapter<MyBean>(context, resource, lists) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        val bean = getItem(position)

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false)
            holder = ViewHolder()
            holder.textViewTitle = view.findViewById(R.id.tvTitle)
            holder.textViewDetail = view.findViewById(R.id.tvDetail)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.textViewTitle?.text = bean?.title
        holder.textViewDetail?.text = bean?.detail
        return view
    }

    //内部类，用来提高listView的性能
    private class ViewHolder {
        var textViewTitle: TextView? = null
        var textViewDetail: TextView? = null
    }

}