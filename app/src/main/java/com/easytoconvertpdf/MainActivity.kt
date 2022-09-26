package com.easytoconvertpdf

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.easytoconvertpdf.adapter.ImageDocument
import com.easytoconvertpdf.databinding.ActivityMainBinding
import com.easytoconvertpdf.fragment.ViewPagerAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE = 999
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var isReadPermissionGranted = false
    private val READ_REQUEST_CODE = 42
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_COLLAGE = 265

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EasyToConvertPDF)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.BottomNavigationView.menu.getItem(3).isEnabled = false
        binding.viewpager2.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager2.isUserInputEnabled = false
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissions ->
                if (permissions) {
                    isReadPermissionGranted = true
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                    } else {
                        val snackbar = Snackbar.make(
                            binding.root,
                            "Storage Permission is required to store Image to the gallery",
                            Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("Permission Snackbar") {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", this.packageName, null)
                            intent.data = uri
                            this.startActivity(intent)
                        }
                        snackbar.show()
                    }
                }
            }
        requestPermission()

        binding.BottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.viewpager2.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.doc -> {
                    binding.viewpager2.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.new_create -> {
                    binding.viewpager2.currentItem = 2
                    return@setOnItemSelectedListener true
                }
                else -> {
                    binding.viewpager2.currentItem = 0
                    return@setOnItemSelectedListener true
                }
            }
        }
        binding.BottomNavigationView.selectedItemId = R.id.home
    }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT > 23) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //main code
                } else {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                }
            } else {
                Toast.makeText(applicationContext, "Already", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.clipData != null) {
                    val count: Int = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                        val document = ImageDocument(imageUri, this)
                        addToDataStore(document)
                    }
                } else if (data.getData() != null) {
                    val imageUri: Uri = data.data!!
                    val document = ImageDocument(imageUri, this)
                    addToDataStore(document)
                }
            }
        }
    }

    private fun addToDataStore(item: ImageDocument) {
        utils.documents?.add(item)
        Log.d("CLEAR","documents ${utils.documents!!.size}")
        startActivity(Intent(this, ImgToPdfActivity::class.java))
    }

    private fun requestPermission() {

        val isWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isReadPermissionGranted = isWritePermission || sdkCheck()

        val permissionRequest = mutableListOf<String>()
        if (!isReadPermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun sdkCheck(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }


}