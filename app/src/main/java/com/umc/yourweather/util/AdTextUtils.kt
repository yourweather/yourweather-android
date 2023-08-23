package com.umc.yourweather.util

class AdTextUtils {
    companion object {
        // RandomAd리스트
        private val AdList = listOf(
            "힘들고 지칠 땐 노래로 달래보아요. '로이'킴의 괜찮을거야",
            "'에디'의 개발자를 위한 감정 조절 에세이, 404도 괜찮아",
            "기분 좋은 날, '리유'랑 운동해요! 헬스장 회원권 할인 중",
            "일요일은 내가 요리사,'진'심을 담아♡ ",
            "'프로도' 실수 하는 법 ! 기죽지 말자 !",
            "'카야'토스트와 커피 한 잔, 지금이 딱 이야 !",
            "'션'선한 바람이 부는 오늘, 피크닉 어때요?",
            "지금 이 순간, 노래 한 곡 추천 ! 넌 '이즈' 뭔들~",
        )

        fun getRandomAd(): String {
            val randomIndex = (0 until AdList.size).random()
            return AdList[randomIndex]
        }
    }
}
