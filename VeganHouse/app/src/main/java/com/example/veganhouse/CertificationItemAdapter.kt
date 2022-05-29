package com.example.veganhouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.model.Certification

class CertificationItemAdapter(private val certifications: List<Certification>) :
    RecyclerView.Adapter<CertificationItemAdapter.CertificationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.certification_item, parent, false)
        return CertificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertificationViewHolder, position: Int) {
        holder.bind(certifications[position])
    }

    override fun getItemCount(): Int {
        return certifications.size
    }

    class CertificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: Certification) {
            with(itemView) {

                val imgCertification = findViewById<ImageView>(R.id.iv_certified_image)
                val nameCertification = findViewById<TextView>(R.id.tv_certified_name)

                imgCertification.setImageResource(R.drawable.selo1)
                nameCertification.text = model.name

            }
        }
    }
}