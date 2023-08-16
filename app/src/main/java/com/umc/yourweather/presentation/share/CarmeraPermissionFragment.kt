import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.yourweather.databinding.FragmentCarmeraPermissionBinding

class CarmeraPermissionFragment : Fragment() {
    private lateinit var binding: FragmentCarmeraPermissionBinding

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

        binding.tvCarmeraPermissionNo.setOnClickListener {
            // 해당 프래그먼트를 종료하고 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }
    }
}
