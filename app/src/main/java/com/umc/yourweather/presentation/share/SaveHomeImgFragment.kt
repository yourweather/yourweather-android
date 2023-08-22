package com.umc.yourweather.presentation.share

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.umc.yourweather.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class SaveHomeImgFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_save_home_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        view.findViewById<View>(R.id.ll_save_home_download)?.setOnClickListener {
            parentFragmentManager.popBackStack()

            // 홈화면 갤러리 저장 함수 호출
            saveScreenshot()

            // 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }
    }

    private fun saveScreenshot() {
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)

        // 해당 뷰들을 숨김 처리
        view?.findViewById<View>(R.id.ll_save_home1)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ll_save_home_download)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ic_download)?.visibility = View.GONE
        view?.findViewById<View>(R.id.tv_save_home_download)?.visibility = View.GONE

        sharedViewModel.hideViews() // HomeFragment에서 해당 함수를 실행
        Log.d("SaveHomeImgFragment", "캡쳐 전 뷰 숨기기 성공")

        lifecycleScope.launch {
            val screenShot = takeScreenshot(rootView)
            screenShot?.let {
                val imageUri = saveScreenshotToMediaStore(it)
                imageUri?.let {
                }
            }
        }
        showSaveSuccessMessage()
    }

    private suspend fun takeScreenshot(view: View?): File? {
        view?.isDrawingCacheEnabled = true
        val screenBitmap = view?.drawingCache
        val filename = "screenshot${System.currentTimeMillis()}.png"
        val file = File(requireContext().getExternalFilesDir(null), filename)
        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(file)
            screenBitmap?.compress(Bitmap.CompressFormat.PNG, 90, os)
            os?.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            view?.isDrawingCacheEnabled = false
        }
        return file
    }

    private suspend fun saveScreenshotToMediaStore(file: File): Uri? = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures")
        }
        val resolver = requireContext().contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        imageUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                FileInputStream(file).use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        file.delete()

        return@withContext imageUri
    }

    private fun showSaveSuccessMessage() {
        Log.d("SaveHomeImgFragment", "showSaveSuccessMessage() 호출됨") // 추가된 로그
        sharedViewModel.showViews() // HomeFragment에서 해당 함수를 실행
        Log.d("SaveHomeImgFragment", "캡쳐 후 뷰 나타내기 성공")
        Toast.makeText(requireContext(), "갤러리에 저장 성공", Toast.LENGTH_SHORT).show()
    }
}
