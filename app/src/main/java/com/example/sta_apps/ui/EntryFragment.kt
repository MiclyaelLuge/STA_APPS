package com.example.sta_apps.ui

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sta_apps.R
import com.example.sta_apps.data.DatabaseKaryawan
import com.example.sta_apps.data.KaryawanHelper
import com.example.sta_apps.databinding.FragmentEntryBinding
import com.example.sta_apps.databinding.FragmentShowBinding


class EntryFragment : Fragment() {
    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!
    private lateinit var karyawanHelper: KaryawanHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = FragmentEntryBinding.inflate(layoutInflater)
        karyawanHelper = KaryawanHelper.getInstance(requireContext())
        karyawanHelper.open()

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding.btSearch.setOnClickListener {
                val id = Integer.parseInt(binding.ID.text.toString())
                val name = binding.namaKaryawan.text.toString()
                val usia = Integer.parseInt(binding.usia.text.toString())
                val tgl = binding.tglkerja.let {
                    "${it.year}-${it.month + 1}-${it.dayOfMonth}"
                }
                var values = ContentValues()
                values.put(DatabaseKaryawan.KaryawanColumn.ID,id)
                values.put(DatabaseKaryawan.KaryawanColumn.NAMA,name)
                values.put(DatabaseKaryawan.KaryawanColumn.USIA,usia)
                values.put(DatabaseKaryawan.KaryawanColumn.TGL_MASUK_KERJA,tgl)
                karyawanHelper.insert(values)
        }

        return binding.root
    }

    companion object {

    }
}