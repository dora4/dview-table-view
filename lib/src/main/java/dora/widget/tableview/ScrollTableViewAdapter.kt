package dora.widget.tableview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dora.widget.DoraScrollTableView
import dora.widget.tableview.ScrollTableViewAdapter.CallViewHolder

class ScrollTableViewAdapter(private var tableView: DoraScrollTableView) :
    RecyclerView.Adapter<CallViewHolder>() {

    private var list: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        return CallViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.dview_cell, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: CallViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.title.text = list[position]
        holder.itemView.setOnClickListener {
            tableView.listener?.itemClickListener(
                list[position],
                position / tableView.columnCount,
                position % tableView.columnCount
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun itemClickListener(pos: Int)
    }

    private var listener: OnItemClickListener? = null

    init {
        list = ArrayList()
        if (tableView.headers.isNotEmpty()) {
            for (i in 0 until tableView.columnCount) {
                list.add(tableView.headers[i])
            }
        }
        if (tableView.data.isNotEmpty()) {
            for (strings in tableView.data) {
                for (i in 0 until tableView.columnCount) {
                    list.add(strings[i].toString())
                }
            }
        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        listener = itemClickListener
    }

    inner class CallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById<View>(R.id.title) as TextView
        var container: RelativeLayout =
            itemView.findViewById<View>(R.id.container) as RelativeLayout
    }
}