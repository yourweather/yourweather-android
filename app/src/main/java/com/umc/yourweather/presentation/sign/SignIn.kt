package com.umc.yourweather.presentation.sign

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.umc.yourweather.BuildConfig
import com.umc.yourweather.R
import com.umc.yourweather.databinding.ActivitySignInBinding
import com.umc.yourweather.presentation.BottomNavi
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import com.umc.yourweather.util.SignUtils.Companion.KAKAOTAG
import com.umc.yourweather.util.SignUtils.Companion.NAVERTAG

class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var resultLauncherGoogle: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 비밀번호 찾기로 이동
        binding.tvSigninBtnfindpw.setOnClickListener {
            val mIntent = Intent(this, FindPw::class.java)
            startActivity(mIntent)
        }

        // 회원가입으로 이동
        binding.tvSigninBtnsignup.setOnClickListener {
            // customToast()
            val mIntent = Intent(this, SignUp::class.java)
            startActivity(mIntent)
        }

        // 로그인 버튼 클릭
        binding.btnSigninSignin.setOnClickListener {
            val mIntent = Intent(this, BottomNavi::class.java)
            startActivity(mIntent)
        }

        // 카카오 소셜로그인
        binding.btnSigninKakao.setOnClickListener {
            kakaoSignIn()
        }

        // 네이버 소셜로그인
        binding.btnSigninNaver.setOnClickListener {
            naverSignIn()
        }

        binding.btnSigninGoogle.setOnClickListener {
            googleSignIn()
        }

        resultLauncherGoogle =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleSignInResult(task)
                }
            }
    }

    fun customToast() {
        val inflater = LayoutInflater.from(this)
        val layout =
            inflater.inflate(R.layout.toast_signin, findViewById(R.id.ll_signin_toast), false)

        val textViewMessage = layout.findViewById<TextView>(R.id.tv_signin_toast)
        textViewMessage.text = "이메일 또는 비밀번호를 다시 입력해주세요"
        val toast = Toast(applicationContext)

        val params = WindowManager.LayoutParams()
        params.width = dpToPx(this@SignIn, 297).toInt()
        params.height = dpToPx(this@SignIn, 40).toInt()
        params.gravity = Gravity.CENTER

        toast.view?.layoutParams = params
        toast.duration = Toast.LENGTH_SHORT // 메시지 표시 시간
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view = layout
        toast.show()
    }

    private fun kakaoSignIn() { // 카카오 소셜로그인
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d(KAKAOTAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.d(KAKAOTAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                val mIntent = Intent(this@SignIn, SocialSignCheck::class.java)
                mIntent.putExtra("SIGN", "KAKAO")
                startActivity(mIntent)
                finish()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.d(KAKAOTAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this@SignIn, callback = callback)
                } else if (token != null) {
                    Log.i(KAKAOTAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this@SignIn, callback = callback)
        }
    }

    private fun naverSignIn() { // 네이버 소셜로그인
        Log.d(NAVERTAG, "네이버 로그인 : ${BuildConfig.NAVER_CLIENT_ID}")

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공시 정보 받아옴
//                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
//                    override fun onSuccess(result: NidProfileResponse) {
//                        // email = result.profile?.email.toString()
//                        // result.profile?.id
//                        Log.d(NAVERTAG, "네이버 로그인 : ${result.profile?.email}")
//                        Log.d(NAVERTAG, "네이버 로그인한 유저 정보 - 이메일 : ${result.profile?.id}")
//                    }
//
//                    override fun onError(errorCode: Int, message: String) {
//                        //
//                    }
//
//                    override fun onFailure(httpStatus: Int, message: String) {
//                        //
//                    }
//                })
                val mIntent = Intent(this@SignIn, SocialSignCheck::class.java)
                mIntent.putExtra("SIGN", "NAVER")
                startActivity(mIntent)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@SignIn, "errorCode:$errorCode, errorDesc:$errorDescription", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this@SignIn, oauthLoginCallback)
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        resultLauncherGoogle.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
//            val email = account?.email.toString()
//            val id = account?.id.toString()

            val mIntent = Intent(this@SignIn, SocialSignCheck::class.java)
            mIntent.putExtra("email", account?.email.toString())
            mIntent.putExtra("id", account?.id.toString())
            mIntent.putExtra("SIGN", "GOOGLE")
            startActivity(mIntent)
            finish()
        } catch (e: ApiException) {
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }
}
