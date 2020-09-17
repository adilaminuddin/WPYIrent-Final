package com.wpy.wpy_irent.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wpy.wpy_irent.Auth.Utils
import com.wpy.wpy_irent.DetailActivity
import com.wpy.wpy_irent.Model.Paket.Paket
import com.wpy.wpy_irent.R
import java.util.*
import kotlin.collections.ArrayList


class PaketAdapter(private val c: Context, paket: ArrayList<Paket>) : RecyclerView.Adapter<PaketAdapter.ViewHolder>(){
    private val paket: List<Paket>

    class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val pkNama: TextView
        val pkDeskripsi: TextView
        val pkDurasi: TextView
        val pkHarga: TextView
        private var itemClickListener: ItemClickListener? = null
        override fun onClick(v: View?) {
            itemClickListener!!.onItemClick(this.layoutPosition)
        }
        fun setItemClickListener(itemClickListener: ItemClickListener?) {
            this.itemClickListener = itemClickListener
        }
        init {
            pkNama = itemView.findViewById(R.id.pNama)
            pkDurasi = itemView.findViewById(R.id.pDurasi)
            pkHarga = itemView.findViewById(R.id.pHarga)
            pkDeskripsi = itemView.findViewById(R.id.pDeskripsi)
            itemView.setOnClickListener(this)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(c).inflate(R.layout.model, parent, false)
            return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paket.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get current scientist
        val s: Paket = paket[position]
        //bind data to widgets
        holder.pkNama.setText(s.nama_pak)
        holder.pkDurasi.setText(s.durasi.toString())
        holder.pkHarga.setText("Rp."+s.harga_pak)
        holder.pkDeskripsi.setText(s.ket)

        //get name and harga
//        val nama: String = s.nama_pak!!.toLowerCase(Locale.getDefault())
//        val harga: String = s.harga_pak!!.toLowerCase(Locale.getDefault())

        //open detail activity when clicked
        holder.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(pos: Int) {
                Utils.sendPaketToActivity(
                    c, s,
                    DetailActivity::class.java
                )
            }
        })
    }

    interface ItemClickListener {
        fun onItemClick(pos: Int)
    }

    /**
     * Our MyAdapter's costructor
     */
    init {
        this.paket = paket
    }

}