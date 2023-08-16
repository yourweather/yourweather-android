import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.yourweather.databinding.FragmentCarmeraPermissionBinding

class CarmeraPermissionFragment : Fragment() {
    private lateinit var binding: FragmentCarmeraPermissionBinding

    companion object {
        const val REQUEST_PERMISSION_CAMERA = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCarmeraPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 권한설정 창에서 취소 버튼 클릭
        binding.tvCarmeraPermissionNo.setOnClickListener {
            // 해당 프래그먼트를 종료하고 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }
        // 권한설정 창에서 설정이동 버튼 클릭
        binding.tvCarmeraPermissionYes.setOnClickListener {
            val cameraPermission = Manifest.permission.CAMERA

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(requireContext(), cameraPermission) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "카메라 권한이 이미 허용되어 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    requestPermissions(arrayOf(cameraPermission), REQUEST_PERMISSION_CAMERA)
                }
            } else {
                // M 미만 버전에서는 이미 설치할 때부터 권한이 부여됨
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "카메라 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "카메라 권한이 거절되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
