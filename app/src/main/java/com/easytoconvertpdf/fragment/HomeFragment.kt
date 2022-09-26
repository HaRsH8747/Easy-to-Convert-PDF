package com.easytoconvertpdf.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.easytoconvertpdf.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var imageView: ImageView
    private val READ_REQUEST_CODE = 42
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_COLLAGE = 265

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

//        val file = File(Environment.getExternalStorageDirectory(), "Report.pdf")
//        val path = Uri.fromFile(file)
//        val pdfOpenintent = Intent(Intent.ACTION_VIEW)
//        pdfOpenintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        pdfOpenintent.setDataAndType(path, "application/pdf")
//        try {
//            startActivity(pdfOpenintent)
//        } catch (e: ActivityNotFoundException) {
//        }
        binding.btnImage.setOnClickListener {
            performFileSearch()
        }

        return binding.root
    }

    fun performFileSearch() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/jpeg"
        val mimetypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        requireActivity().startActivityForResult(intent,READ_REQUEST_CODE)
    }

}




