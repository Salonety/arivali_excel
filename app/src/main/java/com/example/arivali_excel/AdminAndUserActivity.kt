package com.example.arivali_excel

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arivali_excel.Util.Constant.EXCELSHEET_URL
import com.example.arivali_excel.Util.Constant.SHARED_PREF
import com.example.arivali_excel.Util.Constant.USER_TYPE
import com.example.arivali_excel.adapter.UserDetailAdapter
import com.example.arivali_excel.database.Student
import com.example.arivali_excel.databinding.ActivityAdminAndUserBinding
import com.example.arivali_excel.viewmodel.AdminAndUserViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.FileAsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.read.biff.BiffException
import java.io.File
import java.io.IOException

class AdminAndUserActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var query: String
    private val userDetailAdapter = UserDetailAdapter()
    private lateinit var sharedPreferences: SharedPreferences
    private var isInsert: Boolean = false
    private lateinit var binding: ActivityAdminAndUserBinding
    private var userType: String? = null
    private var studentList: MutableList<Student>? = null
    private lateinit var viewModel: AdminAndUserViewModel
    private lateinit var client: AsyncHttpClient
    private lateinit var workbook: Workbook
    private var name: String? = null
    private var gender: String? = null
    private var city: String? = null
    private var status: String? = null
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAndUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        dialog = Dialog(this)
        sharedPreferences = this.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[AdminAndUserViewModel::class.java]


        binding.progressbar.visibility = View.GONE
        studentList = mutableListOf()
        isInsert = sharedPreferences.getBoolean("insert_key", false)
        binding.rvExcelData.apply {
            adapter = userDetailAdapter
            layoutManager =
                LinearLayoutManager(this@AdminAndUserActivity, LinearLayoutManager.VERTICAL, false)
        }

        if (intent != null) {
            userType = intent.getStringExtra(USER_TYPE)!!
        }
        if (userType.equals("admin")) {
            if (!isInsert) {
                binding.topTv.text = getString(R.string.upload_excel_sheet)
                binding.btnUpload.visibility = View.VISIBLE
                binding.rvExcelData.visibility = View.GONE
            } else {
                binding.topTv.text = getString(R.string.excel_sheet)
                binding.btnUpload.visibility = View.GONE
                binding.progressbar.visibility = View.VISIBLE
                binding.rvExcelData.visibility = View.VISIBLE
                uploadUserDetails()
            }
        } else {
            binding.topTv.text = getString(R.string.excel_sheet)
            binding.btnUpload.visibility = View.GONE
            binding.rvExcelData.visibility = View.VISIBLE
            binding.progressbar.visibility = View.VISIBLE
            uploadUserDetails()
        }


        binding.btnUpload.setOnClickListener {
            binding.progressbar.visibility = View.VISIBLE
            readExcelFile()
        }

        userDetailAdapter.itemClickListener {
            viewModel.updateStudent(Student(it.id, it.name, it.gender, it.city, "Active"))
            Toast.makeText(this, "${it.name} Updated Active", Toast.LENGTH_SHORT).show()
            binding.progressbar.visibility = View.VISIBLE
            finish()
            startActivity(intent)

        }
        


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        // here you get error but don't worry
        searchView?.setOnQueryTextListener(this)
        return true
    }
    private fun readExcelFile() {
        client = AsyncHttpClient()
        client[EXCELSHEET_URL, object : FileAsyncHttpResponseHandler(this) {
            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                throwable: Throwable,
                file: File
            ) {
                binding.progressbar.visibility = View.GONE
                Log.e("====", "Error is : " + throwable.message)
                Toast.makeText(
                    this@AdminAndUserActivity,
                    "Fail to import Excel file",
                    Toast.LENGTH_SHORT
                ).show()
            }


            override fun onSuccess(statusCode: Int, headers: Array<Header>, file: File) {
                binding.progressbar.visibility = View.GONE
                val ws = WorkbookSettings()
                ws.gcDisabled = true
                try {
                    workbook = Workbook.getWorkbook(file)
                    val sheet = workbook.getSheet(0)
                    for (i in 0 until sheet.rows) {
                        val row = sheet.getRow(i)
                        name = row[0].contents
                        gender = row[1].contents
                        city = row[2].contents
                        status = row[3].contents
                        studentList?.add(Student(0L, name!!, gender!!, city!!, status!!))
                    }
                    studentList?.let { insertStudentData(it) }

                } catch (e: IOException) {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this@AdminAndUserActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                } catch (e: BiffException) {
                    binding.progressbar.visibility = View.GONE
                    e.printStackTrace()
                    Toast.makeText(this@AdminAndUserActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }]

    }




    private fun insertStudentData(mutableList: MutableList<Student>) {
        viewModel.insertStudent(mutableList)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("insert_key", true)
        editor.apply()
        editor.commit()

        dialog.setContentView(R.layout.custom_layout_dialog)
        val no = dialog.findViewById<Button>(R.id.tv_no)
        val yes = dialog.findViewById<Button>(R.id.tv_yes)

        no.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        yes.setOnClickListener {
            dialog.dismiss()
            binding.topTv.text = getString(R.string.excel_sheet)
            binding.btnUpload.visibility = View.GONE
            binding.progressbar.visibility = View.VISIBLE
            binding.rvExcelData.visibility = View.VISIBLE
            uploadUserDetails()
        }

        dialog.show()
    }


    private fun uploadUserDetails() {
        viewModel.allStudent.observe(this) {
            it.let {
                binding.progressbar.visibility = View.GONE
                studentList?.addAll(it)
                studentList?.let { it1 ->
                    userDetailAdapter.setContentList(it1, userType!!)

                }


            }
        }

    }


    override fun onQueryTextSubmit(query: String?): Boolean {

     return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchDatabase(query)
        return true

    }


    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        viewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                studentList?.addAll(it)
            }
        }

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }



}

