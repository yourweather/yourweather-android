package com.umc.yourweather.presentation.sign

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.NidOAuthPreferencesManager.code
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.umc.yourweather.BuildConfig
import com.umc.yourweather.data.remote.request.LoginRequest
import com.umc.yourweather.data.remote.response.BaseResponse
import com.umc.yourweather.data.remote.response.TokenResponse
import com.umc.yourweather.data.service.LoginService
import com.umc.yourweather.databinding.ActivitySignInBinding
import com.umc.yourweather.di.App
import com.umc.yourweather.di.RetrofitImpl
import com.umc.yourweather.di.UserSharedPreferences
import com.umc.yourweather.presentation.BottomNavi
import com.umc.yourweather.util.SignUtils.Companion.ALERT_TEXT_SIGN_IN
import com.umc.yourweather.util.SignUtils.Companion.KAKAOTAG
import com.umc.yourweather.util.SignUtils.Companion.NAVERTAG
import com.umc.yourweather.util.SignUtils.Companion.customSingInPopupWindow
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

        if (UserSharedPreferences.getUserEmail(this).isNullOrBlank() ||
            UserSharedPreferences.getUserPw(this).isNullOrBlank()
        ) {
            Log.d("sh 확인", "로그인 필요")
        } else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
            Log.d("sh 확인 이메일", UserSharedPreferences.getUserEmail(this).toString())
            Log.d("sh 확인 비밀번호", UserSharedPreferences.getUserPw(this).toString())
            val intent = Intent(this, BottomNavi::class.java)
            startActivity(intent)
            finish()
        }

//        if (App.token_prefs.accessToken != null) {
//            val mIntent = Intent(this@SignIn, BottomNavi::class.java)
//            startActivity(mIntent)
//            Log.d("토큰 확인 액세스", App.token_prefs.accessToken.toString())
//            Log.d("토큰 확인 리프래시", App.token_prefs.refreshToken.toString())
//            finish()
//        }

        // 비밀번호 찾기로 이동
        binding.tvSigninBtnfindpw.setOnClickListener {
            val mIntent = Intent(this, FindPw::class.java)
            startActivity(mIntent)
        }

        // 회원가입으로 이동
        binding.tvSigninBtnsignup.setOnClickListener {
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
    }
    private fun SignInApi(userEmail: String, userPw: String) {
        val service = RetrofitImpl.nonRetrofit.create(LoginService::class.java)

        val LoginInfo = LoginRequest(userEmail, userPw)

        service.logIn(LoginInfo).enqueue(object : Callback<BaseResponse<TokenResponse>> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<BaseResponse<TokenResponse>>,
                response: Response<BaseResponse<TokenResponse>>,
            ) {
                val code = response.body()?.code

                if (response.isSuccessful) {
                    if (code == 200) {
                        // val mIntent = Intent(this@SignIn, BottomNavi::class.java)
                        Log.d("SignInDebug", "로그인 성공~ : " + response.headers().toString())
                        UserSharedPreferences.setUserPwToStar(this@SignIn, userPw)

                        // 자동로그인
                        UserSharedPreferences.setUserEmail(this@SignIn, userEmail)
                        UserSharedPreferences.setUserPw(this@SignIn, userPw)
                        //

                        // 토큰저장
                        App.token_prefs.accessToken = response.body()!!.result?.accessToken
                        App.token_prefs.refreshToken = response.body()!!.result?.refreshToken

                        Log.d("로그인 sh 확인 로그 이메일", UserSharedPreferences.getUserEmail(this@SignIn))
                        Log.d("로그인 sh 확인 로그 비번", UserSharedPreferences.getUserPw(this@SignIn))
                        Log.d("로그인 sh 확인 로그 서버에서 온 액세스 토큰", App.token_prefs.accessToken.toString())
                        Log.d("로그인 sh 확인 로그 서버에서 온 리프래시 토큰", App.token_prefs.refreshToken.toString())
                        Log.d("로그인 sh 확인 로그 액세스 토큰", App.token_prefs.accessToken.toString())
                        Log.d("로그인 sh 확인 로그 리프래시 토큰", App.token_prefs.refreshToken.toString())

                        // startActivity(mIntent)
                        moveToHome()
                    } else {
                        Log.d(
                            "SignInDebug",
                            "아이디 비번 틀림",
                        )
                        customSingInPopupWindow(this@SignIn, ALERT_TEXT_SIGN_IN, binding.root, binding.btnSigninSignin)
                    }
                } else {
                    Log.d(
                        "SignInDebug",
                        "onResponse 오류: ${response?.toString()}",
                    )
                    customSingInPopupWindow(this@SignIn, ALERT_TEXT_SIGN_IN, binding.root, binding.btnSigninSignin)
                }
            }
            override fun onFailure(call: Call<BaseResponse<TokenResponse>>, t: Throwable) {
                Log.d("SignInDebug", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    private fun socialSignInApi(userEmail: String, userPw: String, platform: String) {
        val service = RetrofitImpl.nonRetrofit.create(LoginService::class.java)

        val LoginInfo = LoginRequest(userEmail, userPw)
        service.oauthLogIn(LoginInfo).enqueue(object : Callback<BaseResponse<TokenResponse>> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<BaseResponse<TokenResponse>>,
                response: Response<BaseResponse<TokenResponse>>,
            ) {
                val code = response.body()?.code

                if (response.isSuccessful) {
                    if (code == 200) {
                        // val mIntent = Intent(this@SignIn, BottomNavi::class.java)
                        Log.d("SignInDebug", "소셜 로그인 성공~ : " + response.headers().toString())
                        App.token_prefs.accessToken = response.body()!!.result?.accessToken
                        App.token_prefs.refreshToken = response.body()!!.result?.refreshToken

                        moveToHome()
                        // startActivity(mIntent)
                    } else {
                        Log.d(
                            "SignInDebug",
                            "아이디 비번 틀림",
                        )
                        customSingInPopupWindow(
                            this@SignIn,
                            ALERT_TEXT_SIGN_IN,
                            binding.root,
                            binding.btnSigninSignin,
                        )
                    }
                } else {
                    Log.d(
                        "SignInDebug",
                        "소셜로그인, 정보 존재하지 않음. 회원가입 필요한경우임 ${response?.toString()}",
                    )
                    val mIntent = Intent(this@SignIn, Nickname::class.java)
                    mIntent.putExtra("email", userEmail)
                    mIntent.putExtra("password", userPw)
                    mIntent.putExtra("platform", platform)
                    startActivity(mIntent)
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
                socialSignInApi(userEmail!!, userPw!!, "KAKAO")
                // Toast.makeText(this@SignIn, "email : $userEmail pw : $userPw", Toast.LENGTH_LONG).show()
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
                        // Toast.makeText(this@SignIn, "email : $userEmail pw : $userPw", Toast.LENGTH_LONG).show()
                        socialSignInApi(userEmail!!, userPw!!, "NAVER")
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

    private fun moveToHome() {
        val mIntent = Intent(this@SignIn, BottomNavi::class.java)
        startActivity(mIntent)
        finish()
    }
}
