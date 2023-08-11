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
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.umc.yourweather.BuildConfig
import com.umc.yourweather.R
import com.umc.yourweather.data.remote.request.LoginRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.TokenResponse
import com.umc.yourweather.data.service.LoginService
import com.umc.yourweather.databinding.ActivitySignInBinding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.presentation.BottomNavi
import com.umc.yourweather.util.CalendarUtils.Companion.dpToPx
import com.umc.yourweather.util.SignUtils.Companion.KAKAOTAG
import com.umc.yourweather.util.SignUtils.Companion.NAVERTAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var resultLauncherGoogle: ActivityResultLauncher<Intent>
    var userEmail: String? = null
    var userPw: String? = null

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
            userEmail = binding.etSigninEmail.text.toString()
            userPw = binding.etSigninPw.text.toString()
            SignInApi(userEmail!!, userPw!!)
            // startActivity(mIntent)
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

    private fun SignInApi(userEmail: String, userPw: String) {
        val service = RetrofitImpl.nonRetrofit.create(LoginService::class.java)

        val LoginInfo = LoginRequest(userEmail, userPw)

        service.logIn(LoginInfo).enqueue(object : Callback<BaseResponse<TokenResponse>> {

            override fun onResponse(
                call: Call<BaseResponse<TokenResponse>>,
                response: Response<BaseResponse<TokenResponse>>,
            ) {
                val code = response.body()?.code

                if (response.isSuccessful) {
                    if (code == 200) {
                        val mIntent = Intent(this@SignIn, BottomNavi::class.java)
                        Log.d("SignInDebug", "로그인 성공~ : " + response.headers().toString())
//                        MySharedPreferences.setUserId(this@SignIn, userEmail)
//                        MySharedPreferences.setUserPw(this@SignIn, userPw) // 자동로그인
                        App.token_prefs.accessToken = response.body()!!.result?.accessToken
                        App.token_prefs.refreshToken = response.body()!!.result?.refreshToken
                        startActivity(mIntent)
                    } else {
                        Log.d(
                            "SignInDebug",
                            "아이디 비번 틀림",
                        )
                        customToast()
                    }
                } else {
                    Log.d(
                        "SignInDebug",
                        "onResponse 오류: ${response?.toString()}",
                    )
                }
            }
            override fun onFailure(call: Call<BaseResponse<TokenResponse>>, t: Throwable) {
                Log.d("SignInDebug", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun kakaoSignIn() { // 카카오 소셜로그인
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d(KAKAOTAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.d(KAKAOTAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                kakaoInfo()
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
                    kakaoInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this@SignIn, callback = callback)
        }
    }

    fun kakaoInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d(KAKAOTAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                userEmail = user.kakaoAccount?.email
                userPw = user.id?.toString()
                // val mIntent = Intent(this, SignUp::class.java)
                // SignInApi(userEmail, userPw)

                // startActivity(mIntent)
                Toast.makeText(this@SignIn, "email : $userEmail pw : $userPw", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun naverSignIn() { // 네이버 소셜로그인
        Log.d(NAVERTAG, "네이버 로그인 : ${BuildConfig.NAVER_CLIENT_ID}")

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공시 정보 받아옴
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        userEmail = result.profile?.email.toString()
                        userPw = result.profile?.id
                        Toast.makeText(this@SignIn, "email : $userEmail pw : $userPw", Toast.LENGTH_LONG).show()
                    }
                    override fun onError(errorCode: Int, message: String) {
                        //
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        //
                    }
                })
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
            userEmail = account?.email.toString()
            userPw = account?.id.toString()
            Toast.makeText(this@SignIn, "email : $userEmail pw : $userPw", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }
}
