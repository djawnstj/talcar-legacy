package com.lx.talcar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.lx.talcar.api.PostFileUploadClient
import com.lx.talcar.databinding.FragmentHomeBinding
import com.lx.talcar.databinding.FragmentPhoto2Binding
import com.lx.talcar.response.PostFileUploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FragmentPhoto2 : Fragment() {

    private  var _binding: FragmentPhoto2Binding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    val outputFilename = "photo.jpg"
    lateinit var outputFile: File

    lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    lateinit var pickAlbumLauncher: ActivityResultLauncher<Intent>
    var fileName:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPhoto2Binding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

// 사진찍기 인텐트 등록
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.apply {
                    handleCaptureResult()
                }
            }
        }

        _binding?.backPhotoTab?.setOnClickListener {
            takePicture()
        }


        return binding.root
    }

    //사진찍기
    fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { captureIntent ->
            outputFile = getPhotoFile()

            val providerFile = FileProvider.getUriForFile(context as MainActivity, BuildConfig.APPLICATION_ID, outputFile)
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)

            captureIntent.resolveActivity(mainActivity.packageManager)?.also {
                takePictureLauncher.launch(captureIntent)
            } ?:run {
                Log.e("Main", "Cannot open camera app.")
            }
        }
    }

    fun handleCaptureResult() {

//       loadImageFromFile(outputFile.absolutePath)

        // 파일 업로드 테스트
        uploadFile(outputFile.absolutePath)
    }

    @TargetApi(19)
    fun handlePickResult(data: Intent) {
        var imagePath: String? = null
        val uri = data.data

        if (uri != null) {
            if (DocumentsContract.isDocumentUri(context as MainActivity, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                if (uri.authority == "com.android.providers.media.documents") {
                    val id = docId.split(":")[1]
                    val selection = MediaStore.Images.Media._ID + "=" + id
                    imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection
                    )
                } else if (uri.authority == "com.android.providers.downloads.documents") {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse(
                            "content://downloads/public_downloads"
                        ), docId.toLong()
                    )
                    imagePath = getImagePath(contentUri, null)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                imagePath = getImagePath(uri, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                imagePath = uri.path
            }

            imagePath?.apply {
                loadImageFromFile(this)
                uploadFile(this)
            }
        }
    }

    private fun uploadFile(filePath: String) {
        val outExtension = filePath.substringAfterLast(".").uppercase()
        val outFile = File(filePath)
        println("outExtension : $outExtension")

        var mediaTypeString:String = "image/*"
        when (outExtension) {
            "JPG", "JPEG" -> mediaTypeString = "image/jpg"
            "PNG" -> mediaTypeString = "image/png"
            "GIF" -> mediaTypeString = "image/gif"
            "TIF", "TIFF" -> mediaTypeString = "image/tif"
        }

        val filePart = MultipartBody.Part.createFormData(
            "photo",
            outputFilename,
            RequestBody.create(mediaTypeString.toMediaTypeOrNull(), outFile)
        )

        val params = hashMapOf<String,String>()
        params["userid"] = "102010"
        params["username"] = "김준수"

        PostFileUploadClient.api.uploadFile(
            file=filePart,
            params=params
        ).enqueue(object : Callback<PostFileUploadResponse> {
            override fun onResponse(call: Call<PostFileUploadResponse>, response: Response<PostFileUploadResponse>) {
                println("onResponse called : ${response.body().toString()}")
                fileName = response.body()?.output?.filename.toString()
            }

            override fun onFailure(call: Call<PostFileUploadResponse>, t: Throwable) {
                println("onFailure called : ${t.message}")

            }
        })

        println("uploadFile called.")

    }

    private fun loadImageFromFile(imagePath: String) {

        val targetWidth: Int = _binding?.backPhotoTab?.width!!
        val targetHeight: Int = _binding?.backPhotoTab?.height!!

        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true

            val photoWidth: Int = outWidth
            val photoHeight: Int = outHeight

            val scaleFactor: Int = Math.min(photoWidth / targetWidth, photoHeight / targetHeight)

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        BitmapFactory.decodeFile(imagePath, bmOptions)?.also { bitmap ->
            binding.backPhotoTab.setImageBitmap(bitmap)
        }

    }

    @SuppressLint("Range")
    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = activity?.contentResolver?.query(uri, null, selection, null, null)
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }

        return path
    }

    private fun getPhotoFile(): File {
        val directoryStorage = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val file = File(directoryStorage, outputFilename)

        try {
            if (file.exists()) {
                file.delete()
            }
        } catch(e:Exception) {
            e.printStackTrace()
        }

        return file
    }

}