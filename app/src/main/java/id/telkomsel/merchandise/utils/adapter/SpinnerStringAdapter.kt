package id.telkomsel.merchandise.utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import id.telkomsel.merchandise.R
import java.util.*

class SpinnerStringAdapter(private val context: Context?,
                           private val dataSpinner: ArrayList<String>,
                           private val disableFirst: Boolean) : BaseAdapter(),
    SpinnerAdapter {
    override fun getCount(): Int {
        return dataSpinner.size
    }
    override fun getItem(position: Int): Any {
        return dataSpinner[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.spinner_layout, null)
        val textView: TextView = view.findViewById(R.id.textNama)
        textView.text = dataSpinner[position]

        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.spinner_layout, null)
        val textView: TextView = view.findViewById(R.id.textNama)
        textView.text = dataSpinner[position]

        if (position == 0 && disableFirst) {
            view.visibility = View.GONE
        }
        else{
            view.visibility = View.VISIBLE
        }
        return view
    }

}