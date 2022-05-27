package com.example.veganhouse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.veganhouse.R
import com.example.veganhouse.model.Certification
import com.squareup.picasso.Picasso

class CertificationAdapter(private val certifications: ArrayList<Certification>) :
    RecyclerView.Adapter<CertificationAdapter.CertificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_certification, parent, false)
        return CertificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertificationViewHolder, position: Int) {
        holder.bind(certifications[position])
    }

    override fun getItemCount(): Int {
        return certifications.size
    }

    class CertificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: Certification) {
            with(itemView) {

                val imgCertification = findViewById<ImageView>(R.id.iv_certified_image)
                val nameCertification = findViewById<TextView>(R.id.tv_certified_name)

                Picasso.get().load(data.url).into(imgCertification);
                //imgCertification.setImageURI(data.url)
                nameCertification.text = data.name

            }
        }
    }
}