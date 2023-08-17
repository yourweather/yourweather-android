package com.umc.yourweather.presentation.share


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.yourweather.R
import com.umc.yourweather.presentation.weatherinput.HomeFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SaveHomeImgFragment : Fragment() {

    private var homeFragment: HomeFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_home_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.ll_save_home_download)?.setOnClickListener {
            parentFragmentManager.popBackStack()

            // 저장 기능 호출
            saveScreenshot()

            // 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }
    }

    private fun saveScreenshot() {
        verifyStoragePermission()

        // 해당 뷰들을 숨김 처리
        view?.findViewById<View>(R.id.ll_save_home1)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ll_save_home_instagram)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ic_instagram)?.visibility = View.GONE
        view?.findViewById<View>(R.id.tv_save_home_instagram)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ll_save_home_download)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ic_download)?.visibility = View.GONE
        view?.findViewById<View>(R.id.tv_save_home_download)?.visibility = View.GONE

        homeFragment?.hideViews() // HomeFragment에서 해당 함수를 실행
        Log.d("SaveHomeImgFragment","캡쳐 전 뷰 숨기기 성공")

        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        val screenShot = takeScreenshot(rootView)
        if (screenShot != null) {
            val imageUri = Uri.fromFile(screenShot)
            requireContext().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri))
            Toast.makeText(requireContext(), "갤러리에 저장 성공", Toast.LENGTH_SHORT).show()
        }
        homeFragment?.showViews() // HomeFragment에서 해당 함수를 실행
        Log.d("SaveHomeImgFragment","캡쳐 후 뷰 나타내기 성공")
    }


    private fun takeScreenshot(view: View?): File? {
        view?.isDrawingCacheEnabled = true
        val screenBitmap = view?.drawingCache
        val filename = "screenshot${System.currentTimeMillis()}.png"
        val file = File(
            Environment.getExternalStorageDirectory().toString() + "/Pictures", filename
        )
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
    private fun verifyStoragePermission() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
    }
}