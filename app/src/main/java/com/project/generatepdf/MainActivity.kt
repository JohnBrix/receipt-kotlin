package com.project.generatepdf

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var et_pdf_data : EditText
    private lateinit var btn_generate_pdf : Button
    private val STORAGE_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        et_pdf_data = findViewById(R.id.et_pdf_data)
        btn_generate_pdf = findViewById(R.id.btn_generate_pdf)
        btn_generate_pdf.setOnClickListener{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
                ){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                }else{
                    savePDF()
                }
            }else{
                savePDF()
            }
        }

    }
    private fun savePDF(){
        val mDoc = com.itextpdf.text.Document()
        val mFileName = SimpleDateFormat("yyyMMdd_HHmmss",Locale.getDefault())
            .format(System.currentTimeMillis())

        /*This method saved in:
         *
         * /storage/emulated/0/20220127_224414.pdf*/
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"
        Log.i("MAIN: ","${mFilePath}")
        try {

            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            val data = et_pdf_data.text.toString().trim()
            mDoc.addAuthor("KB CODER")
            mDoc.add(Paragraph(" ********************WELCOME MGA BOBO***************"+" \n Item Name"
                +" \n ${data}"))
            mDoc.close()
            Toast.makeText(this,"$mFileName.pdf\n is create to\n$mFilePath", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(this,""+e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode){
            STORAGE_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF()
                }else {
                    Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}