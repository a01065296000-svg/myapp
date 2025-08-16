package com.taropicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageView
import android.graphics.Color
import android.view.Gravity
import android.graphics.BitmapFactory
import java.net.URL
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    data class TarotCard(
        val name: String,
        val meaning: String,
        val description: String,
        val imageUrl: String
    )

    private val tarotCards = listOf(
        TarotCard("바보(The Fool)", "새로운 시작과 모험",
            "새로운 여행을 시작하려는 의지와 용기를 보여줍니다.",
            "https://upload.wikimedia.org/wikipedia/commons/9/90/RWS_Tarot_00_Fool.jpg"),

        TarotCard("마술사(The Magician)", "의지력과 창조력",
            "무한한 가능성과 창조력을 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/d/de/RWS_Tarot_01_Magician.jpg"),

        TarotCard("여황제(The High Priestess)", "직관과 신비",
            "내면의 지혜와 직감을 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/8/88/RWS_Tarot_02_High_Priestess.jpg"),

        TarotCard("여황(The Empress)", "풍요와 모성",
            "창조력과 풍요로움을 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/d/d2/RWS_Tarot_03_Empress.jpg"),

        TarotCard("황제(The Emperor)", "권위와 안정",
            "강한 의지와 리더십을 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/c/c3/RWS_Tarot_04_Emperor.jpg"),

        TarotCard("교황(The Hierophant)", "전통과 학습",
            "전통적인 지혜와 영적 가르침을 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/8/8d/RWS_Tarot_05_Hierophant.jpg"),

        TarotCard("연인(The Lovers)", "사랑과 조화",
            "사랑과 선택을 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/3/3a/TheLovers.jpg"),

        TarotCard("전차(The Chariot)", "의지와 승리",
            "강한 의지력으로 장애물을 극복하고 승리를 얻는 것을 의미합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/9/9b/RWS_Tarot_07_Chariot.jpg"),

        TarotCard("힘(Strength)", "용기와 인내",
            "내면의 용기와 인내력을 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/f/f5/RWS_Tarot_08_Strength.jpg"),

        TarotCard("은자(The Hermit)", "성찰과 깨달음",
            "혼자만의 시간을 통해 내면의 지혜를 찾는 것을 의미합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/4/4d/RWS_Tarot_09_Hermit.jpg"),

        TarotCard("운명의 수레바퀴", "변화와 운명",
            "삶의 순환과 변화를 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/3/3c/RWS_Tarot_10_Wheel_of_Fortune.jpg"),

        TarotCard("정의(Justice)", "균형과 공정",
            "균형과 공정함을 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/e/e0/RWS_Tarot_11_Justice.jpg"),

        TarotCard("교수형(The Hanged Man)", "희생과 깨달음",
            "자발적인 희생과 새로운 관점을 얻는 것을 의미합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/2/2b/RWS_Tarot_12_Hanged_Man.jpg"),

        TarotCard("죽음(Death)", "변화와 재생",
            "끝과 새로운 시작을 의미합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/d/d7/RWS_Tarot_13_Death.jpg"),

        TarotCard("절제(Temperance)", "균형과 조화",
            "모든 것의 균형과 조화를 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/f/f8/RWS_Tarot_14_Temperance.jpg"),

        TarotCard("악마(The Devil)", "유혹과 속박",
            "물질적 욕망과 속박을 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/5/55/RWS_Tarot_15_Devil.jpg"),

        TarotCard("탑(The Tower)", "파괴와 변화",
            "갑작스러운 변화와 기존 구조의 붕괴를 의미합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/5/53/RWS_Tarot_16_Tower.jpg"),

        TarotCard("별(The Star)", "희망과 영감",
            "희망과 꿈을 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/d/db/RWS_Tarot_17_Star.jpg"),

        TarotCard("달(The Moon)", "환상과 직관",
            "무의식과 직관, 때로는 착각과 환상을 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/7/7f/RWS_Tarot_18_Moon.jpg"),

        TarotCard("태양(The Sun)", "기쁨과 성공",
            "기쁨, 성공, 생명력을 상징합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/1/17/RWS_Tarot_19_Sun.jpg"),

        TarotCard("심판(Judgement)", "각성과 부활",
            "깨달음과 새로운 부활을 의미합니다.",
            "https://upload.wikimedia.org/wikipedia/commons/d/dd/RWS_Tarot_20_Judgement.jpg"),

        TarotCard("세계(The World)", "완성과 성취",
            "완성과 성취를 나타냅니다.",
            "https://upload.wikimedia.org/wikipedia/commons/f/ff/RWS_Tarot_21_World.jpg")
    )

    private lateinit var resultLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.setPadding(30, 50, 30, 30)
        mainLayout.setBackgroundColor(Color.parseColor("#0f0f23"))

        // 제목
        val title = TextView(this)
        title.text = "🔮 실제 타로카드 🔮"
        title.textSize = 24f
        title.setTextColor(Color.parseColor("#FFE55C"))
        title.gravity = Gravity.CENTER
        title.setPadding(0, 0, 0, 30)
        mainLayout.addView(title)

        // 결과 영역
        resultLayout = LinearLayout(this)
        resultLayout.orientation = LinearLayout.VERTICAL
        resultLayout.setPadding(20, 20, 20, 20)
        resultLayout.setBackgroundColor(Color.parseColor("#1E1B4B"))

        val welcomeText = TextView(this)
        welcomeText.text = "실제 타로카드 이미지를 보여드립니다!"
        welcomeText.textSize = 16f
        welcomeText.setTextColor(Color.parseColor("#FFFFFF"))
        welcomeText.gravity = Gravity.CENTER
        welcomeText.setPadding(20, 40, 20, 40)
        resultLayout.addView(welcomeText)

        mainLayout.addView(resultLayout)

        // 카드 뽑기 버튼
        val drawButton = Button(this)
        drawButton.text = "🌟 실제 카드 뽑기"
        drawButton.textSize = 18f
        drawButton.setTextColor(Color.WHITE)
        drawButton.setBackgroundColor(Color.parseColor("#6366F1"))
        drawButton.setPadding(0, 40, 0, 40)
        drawButton.setOnClickListener {
            drawRealCard()
        }
        mainLayout.addView(drawButton)

        setContentView(mainLayout)
    }

    private fun drawRealCard() {
        resultLayout.removeAllViews()

        val randomCard = tarotCards.random()

        // 로딩 메시지
        val loadingText = TextView(this)
        loadingText.text = "실제 타로카드를 불러오는 중..."
        loadingText.textSize = 16f
        loadingText.setTextColor(Color.parseColor("#B8B5FF"))
        loadingText.gravity = Gravity.CENTER
        loadingText.setPadding(20, 40, 20, 40)
        resultLayout.addView(loadingText)

        // 코루틴으로 이미지 다운로드
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val imageView = ImageView(this@MainActivity)
                imageView.layoutParams = LinearLayout.LayoutParams(400, 600)
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                imageView.setPadding(20, 20, 20, 20)
                imageView.setBackgroundColor(Color.parseColor("#FFFFFF"))

                // 백그라운드에서 이미지 다운로드
                val bitmap = withContext(Dispatchers.IO) {
                    val url = URL(randomCard.imageUrl)
                    BitmapFactory.decodeStream(url.openConnection().getInputStream())
                }

                // UI 업데이트
                resultLayout.removeAllViews()
                resultLayout.addView(imageView)
                imageView.setImageBitmap(bitmap)

                // 카드 정보 추가
                addCardInfo(randomCard)

                Toast.makeText(this@MainActivity, "실제 타로카드를 뽑았습니다!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                resultLayout.removeAllViews()
                val errorText = TextView(this@MainActivity)
                errorText.text = "이미지를 불러올 수 없습니다.\n인터넷 연결을 확인해주세요."
                errorText.textSize = 16f
                errorText.setTextColor(Color.parseColor("#FF6B6B"))
                errorText.gravity = Gravity.CENTER
                errorText.setPadding(20, 40, 20, 40)
                resultLayout.addView(errorText)
            }
        }
    }

    private fun addCardInfo(card: TarotCard) {
        // 카드 이름
        val cardName = TextView(this)
        cardName.text = card.name
        cardName.textSize = 20f
        cardName.setTextColor(Color.parseColor("#FFE55C"))
        cardName.gravity = Gravity.CENTER
        cardName.setPadding(10, 20, 10, 10)
        resultLayout.addView(cardName)

        // 카드 의미
        val cardMeaning = TextView(this)
        cardMeaning.text = "✨ ${card.meaning} ✨"
        cardMeaning.textSize = 16f
        cardMeaning.setTextColor(Color.parseColor("#B8B5FF"))
        cardMeaning.gravity = Gravity.CENTER
        cardMeaning.setPadding(10, 10, 10, 15)
        resultLayout.addView(cardMeaning)

        // 카드 설명
        val cardDescription = TextView(this)
        cardDescription.text = card.description
        cardDescription.textSize = 14f
        cardDescription.setTextColor(Color.parseColor("#FFFFFF"))
        cardDescription.gravity = Gravity.CENTER
        cardDescription.setPadding(15, 10, 15, 20)
        resultLayout.addView(cardDescription)
    }
}