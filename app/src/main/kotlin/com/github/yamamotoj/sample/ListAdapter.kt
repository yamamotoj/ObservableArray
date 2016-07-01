package com.github.yamamotoj.sample

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.github.yamamotoj.observablearray.ObservableArray


class ListAdapter(
        private val array: ObservableArray<String>,
        private val onClickListener: OnClickListener)
: RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    init {
        array.updates().subscribe {
            when (it) {
                is ObservableArray.Event.DataSetChanged -> notifyDataSetChanged()
                is ObservableArray.Event.ItemInserted -> notifyItemInserted(it.position)
                is ObservableArray.Event.ItemChanged -> notifyItemChanged(it.position)
                is ObservableArray.Event.ItemRemoved -> notifyItemRemoved(it.position)
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) =
            holder?.let {
                it.bind(array[position])
            } ?: Unit

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder?
            = parent?.let {
        ListViewHolder(it, onClickListener)
    }


    override fun getItemCount(): Int {
        val i = array.size
        return i
    }

    interface OnClickListener {
        fun onClick(string: String)
    }

    class ListViewHolder(val parent: ViewGroup, val onClickListener: OnClickListener)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)) {

        private val textView by lazy { itemView.findViewById(R.id.text) as TextView }

        fun bind(item: String) {
            textView.text = item
            textView.setOnClickListener { onClickListener.onClick(item) }
        }
    }
}
