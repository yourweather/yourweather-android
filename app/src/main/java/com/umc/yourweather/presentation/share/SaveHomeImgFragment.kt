package com.umc.yourweather.presentation.share

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.umc.yourweather.util.AlertDialogTwoBtn
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        view.findViewById<View>(R.id.ll_save_home_instagram)?.setOnClickListener {
            parentFragmentManager.popBackStack()

            // 인스타그램 스토리 공유 함수 호출
            lifecycleScope.launch {
                val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
                val screenShot = takeScreenshot(rootView)
                screenShot?.let {
                    val imageUri = saveScreenshotToMediaStore(it)

                    imageUri?.let {
                        shareToInstagram(imageUri)
                    }
                }

                // 이전 화면으로 돌아가기
                parentFragmentManager.popBackStack()
            }
        }
    }

    private suspend fun shareToInstagram(imageUri: Uri) {
        val instagramIntent = Intent("com.instagram.share.ADD_TO_STORY")
        instagramIntent.type = "image/*"
        instagramIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        instagramIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        try {
            requireContext().startActivity(instagramIntent)
        } catch (e: Exception) {
            goToInstallInstagramDialog() //인스타그램이 설치되지 않은 경우
            Log.d("Instagram","인스타 미설치 다이얼로그 생성 성공")
        }
    }
    private fun goToInstallInstagramDialog() {
        val alertDialog = AlertDialogTwoBtn(requireContext())

        alertDialog.setTitle("Instagram 설치를 위해 Google Play로 이동합니다.")

        alertDialog.setNegativeButton("취소") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        alertDialog.setPositiveButton("확인") { dialogInterface, _ ->
            dialogInterface.dismiss()

            // 인스타그램 설치 페이지로 이동
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.instagram.android"))
            startActivity(intent)

            parentFragmentManager.popBackStack()
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun saveScreenshot() {
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)

        // 해당 뷰들을 숨김 처리
        view?.findViewById<View>(R.id.ll_save_home1)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ll_save_home_instagram)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ic_instagram)?.visibility = View.GONE
        view?.findViewById<View>(R.id.tv_save_home_instagram)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ll_save_home_download)?.visibility = View.GONE
        view?.findViewById<View>(R.id.ic_download)?.visibility = View.GONE
        view?.findViewById<View>(R.id.tv_save_home_download)?.visibility = View.GONE

        sharedViewModel.hideViews() // HomeFragment에서 해당 함수를 실행
        Log.d("SaveHomeImgFragment","캡쳐 전 뷰 숨기기 성공")

        lifecycleScope.launch {
            val screenShot = takeScreenshot(rootView)
            screenShot?.let {
                val imageUri = saveScreenshotToMediaStore(it)
                imageUri?.let {
                    shareToInstagram(imageUri)
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
        Log.d("SaveHomeImgFragment","캡쳐 후 뷰 나타내기 성공")
        Toast.makeText(requireContext(), "갤러리에 저장 성공", Toast.LENGTH_SHORT).show()
    }
}
