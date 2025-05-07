package com.example.sta_apps.ui

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sta_apps.R
import com.example.sta_apps.adapter.DataAdapter
import com.example.sta_apps.data.DataEntity
import com.example.sta_apps.data.DatabaseKaryawan
import com.example.sta_apps.data.KaryawanHelper
import com.example.sta_apps.databinding.FragmentShowBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ShowFragment : Fragment() {
    private var _binding: FragmentShowBinding? = null
    private val binding get() = _binding!!
    val format = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
    private lateinit var entryFragment: EntryFragment
    private lateinit var karyawanHelper: KaryawanHelper
    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentShowBinding.inflate(layoutInflater)
        karyawanHelper = KaryawanHelper.getInstance(requireContext())
        karyawanHelper.open()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        entryFragment = EntryFragment()
        val showRV = binding.rv1
        showRV.layoutManager = LinearLayoutManager(requireContext())
        adapter = DataAdapter(emptyList())
        binding.rv1.adapter = adapter
        binding.rv1.layoutManager = LinearLayoutManager(requireContext())

        binding.btSearch.setOnClickListener {
            val name = binding.namaKaryawan1.text.toString()
            val usia1 =
                if (binding.usia1.text!!.isNotBlank()) Integer.parseInt(binding.usia1.text.toString()) else null
            val usia2 =
                if (binding.usia2.text!!.isNotBlank()) Integer.parseInt(binding.usia2.text.toString()) else null
            val tgl1 = binding.tglkerja1.let {
                "${it.year}-${it.month + 1}-${it.dayOfMonth}"
            }
            val tgl2 = binding.tglkerja2.let {
                "${it.year}-${it.month + 1}-${it.dayOfMonth}"
            }
            Log.d("QueryDebug", "Params: name=$name usia1=$usia1 usia2=$usia2 tgl1=$tgl1 tgl2=$tgl2")

            val fetchAll = karyawanHelper.queryByFields(
                idKaryawan = "",
                nmKaryawan = name,
                usiaMin = usia1,
                usiaMax = usia2,
                tglMasukStart = format.parse(tgl1),
                tglMasukEnd = format.parse(tgl2)
            )
            val todayDate: Date = Calendar.getInstance().time

            val dummyData = listOf(
                DataEntity("1", "John", todayDate, 30),
                DataEntity("2", "Jane", todayDate, 25)
            )
            dataConfigure(fetchAll)

        }

        binding.btClose.setOnClickListener {
            requireActivity().finishAffinity()
        }

        binding.btNew.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.container, entryFragment, EntryFragment.javaClass.simpleName)
                addToBackStack(null)
                    .commit()
            }
        }

        return binding.root


    }

    private fun dataConfigure(item: List<DataEntity>) {
        Log.d("DataConfigure", "Received ${item.size} items")
        adapter.updateData(item)
        adapter.setOnItemClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(
                selectedUser: DataEntity?,
                position: Int
            ) {
                selectedUser ?: return

                AlertDialog.Builder(requireContext())
                    .setTitle("Select Action")
                    .setItems(arrayOf("Edit", "Delete")) { dialog, which ->
                        when (which) {
                            0 -> navigateToEdit(selectedUser)
                            1 -> confirmAndDelete(selectedUser.idKaryawan ?: "")
                        }
                    }
                    .show()
            }

        })

    }

    private fun navigateToEdit(user: DataEntity) {
        binding.namaKaryawan1.setText(user.idKaryawan)
        binding.usia1.setText(user.usia)
        binding.btSearch.text = "Edit"

        val nama = binding.namaKaryawan1.text.toString()
        val usia = Integer.parseInt(binding.usia1.text.toString())
        val tgl = binding.tglkerja1.let {
            "${it.dayOfMonth}-${it.month + 1}-${it.year}"
        }
        val values = ContentValues()
        values.put(DatabaseKaryawan.KaryawanColumn.NAMA, nama)
        values.put(DatabaseKaryawan.KaryawanColumn.USIA, usia)
        values.put(DatabaseKaryawan.KaryawanColumn.TGL_MASUK_KERJA, tgl)

        binding.btSearch.setOnClickListener {
            karyawanHelper.updateById(user.idKaryawan!!, values)
        }


    }

    private fun confirmAndDelete(id: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton("Yes") { _, _ ->
                val result = karyawanHelper.deleteById(id)
                if (result > 0) {
                    Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
    }
}