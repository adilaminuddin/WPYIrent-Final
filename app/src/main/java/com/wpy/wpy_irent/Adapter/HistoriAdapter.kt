package com.wpy.wpy_irent.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.wpy.wpy_irent.Model.Histori.Histori
import com.wpy.wpy_irent.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class HistoriAdapter(private val c: Context, histori: ArrayList<Histori>) : RecyclerView.Adapter<HistoriAdapter.ViewHolder>() {

    private val histori: List<Histori>

    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    var convetDateFormat = SimpleDateFormat("hh:mm")
    var finalconf = SimpleDateFormat("dd-MM-yyyy")

    var date: Date? = null
    var date2: Date? = null

    class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val hNamapkt: TextView
        val hNamaPnsl: TextView
        val hJamMul: TextView
        val hTglMul: TextView
        val hJamSel: TextView
        val hTglSel: TextView
        val hTarif: TextView
        val hTgl: TextView

        private var itemClickListener: ItemClickListener? = null
        override fun onClick(v: View?) {
            itemClickListener!!.onItemClick(this.layoutPosition)
        }
        fun setItemClickListener(itemClickListener: ItemClickListener?) {
            this.itemClickListener = itemClickListener
        }
        init {
            hNamapkt = itemView.findViewById(R.id.hNamaPkt)
            hNamaPnsl = itemView.findViewById(R.id.hNamaPon)
            hJamMul = itemView.findViewById(R.id.hJamMul)
            hTglMul = itemView.findViewById(R.id.hdTglMul)
            hJamSel = itemView.findViewById(R.id.hJamSel)
            hTglSel = itemView.findViewById(R.id.hdTglSel)
            hTarif = itemView.findViewById(R.id.hTarif)
            hTgl = itemView.findViewById(R.id.tglhis)
            itemView.setOnClickListener(this)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(c).inflate(R.layout.hmodel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return histori.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s: Histori = histori[position]
        holder.hNamapkt.text = s.nama_paket
        date = simpleDateFormat.parse(s.waktu_mulai)
        holder.hTglMul.text = convetDateFormat.format(date)
        date2 = simpleDateFormat.parse(s.waktu_berakhir)
        holder.hTglSel.text = convetDateFormat.format(date2)
        //holder.hTglMul.text = s.waktu_mulai
        //holder.hTglSel.text = s.waktu_berakhir
        holder.hNamaPnsl.text = s.tipe_ponsel
        holder.hTarif.text = "Tarif : Rp."+s.harga_paket
        holder.hTgl.text = "Tgl :  "+finalconf.format(date)
        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {

            }
        })
    }
    init {
        this.histori = histori
    }
    interface ItemClickListener {
        fun onItemClick(pos: Int)
    }
}