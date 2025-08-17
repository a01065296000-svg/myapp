package com.taropicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 타로카드 데이터 클래스
data class TarotCard(
    val name: String,
    val number: String,
    val emoji: String,
    val description: String,
    val imageResId: Int? = null,
    val isReversed: Boolean = false
)

// 질문과 카드 세트
data class TarotReading(
    val question: String,
    val cards: List<TarotCard>,
    val timestamp: String
)

// 완전한 78장 타로카드 덱 (이모지와 설명 포함)
object TarotDeck {
    // 메이저 아르카나 22장 (실제 타로카드 그림 스타일)
    private val majorArcana = listOf(
        TarotCard("THE FOOL", "0", "🌟🎒🐕", "새로운 시작, 순수함, 모험", R.drawable.major_arcana_fool),
        TarotCard("THE MAGICIAN", "I", "🎩⚡🔮", "의지력, 창조, 능력", R.drawable.major_arcana_magician),
        TarotCard("THE HIGH PRIESTESS", "II", "🌙👑📚", "직감, 신비, 잠재의식", R.drawable.major_arcana_priestess),
        TarotCard("THE EMPRESS", "III", "👑🌺💎", "풍요, 모성, 창조력", R.drawable.major_arcana_empress),
        TarotCard("THE EMPEROR", "IV", "👑🏛️⚔️", "권위, 안정, 질서", R.drawable.major_arcana_emperor),
        TarotCard("THE HIEROPHANT", "V", "⛪🗝️📿", "전통, 교육, 영성", R.drawable.major_arcana_hierophant),
        TarotCard("THE LOVERS", "VI", "💕👫🌈", "사랑, 선택, 조화", R.drawable.major_arcana_lovers),
        TarotCard("THE CHARIOT", "VII", "🏹🐎⚡", "의지, 승리, 통제", R.drawable.major_arcana_chariot),
        TarotCard("STRENGTH", "VIII", "🦁💪🌹", "힘, 용기, 인내", R.drawable.major_arcana_strength),
        TarotCard("THE HERMIT", "IX", "🔦🏔️⭐", "지혜, 성찰, 고독", R.drawable.major_arcana_hermit),
        TarotCard("WHEEL OF FORTUNE", "X", "🎡⚡🔄", "운명, 변화, 순환", R.drawable.major_arcana_fortune),
        TarotCard("JUSTICE", "XI", "⚖️🗡️👁️", "정의, 균형, 진실", R.drawable.major_arcana_justice),
        TarotCard("THE HANGED MAN", "XII", "🙃🌳💧", "희생, 깨달음, 기다림", R.drawable.major_arcana_hanged),
        TarotCard("DEATH", "XIII", "💀🌹🦋", "변화, 종료, 재생", R.drawable.major_arcana_death),
        TarotCard("TEMPERANCE", "XIV", "👼💧🌈", "절제, 조화, 균형", R.drawable.major_arcana_temperance),
        TarotCard("THE DEVIL", "XV", "😈🔗🔥", "유혹, 속박, 욕망", R.drawable.major_arcana_devil),
        TarotCard("THE TOWER", "XVI", "🗼⚡💥", "파괴, 급변, 해방", R.drawable.major_arcana_tower),
        TarotCard("THE STAR", "XVII", "⭐💧🕊️", "희망, 영감, 치유", R.drawable.major_arcana_star),
        TarotCard("THE MOON", "XVIII", "🌙🐺🦞", "환상, 불안, 직감", R.drawable.major_arcana_moon),
        TarotCard("THE SUN", "XIX", "☀️🌻👶", "기쁨, 성공, 활력", R.drawable.major_arcana_sun),
        TarotCard("JUDGEMENT", "XX", "📯👼☁️", "각성, 부활, 심판", R.drawable.major_arcana_judgement),
        TarotCard("THE WORLD", "XXI", "🌍💃🦅", "완성, 성취, 만족", R.drawable.major_arcana_world)
    )

    // 마이너 아르카나 56장
    private val minorArcana = mutableListOf<TarotCard>().apply {
        // 컵 (감정, 사랑)
        val cupsEmojis = listOf("💧", "💝", "🎉", "😔", "🏆", "👶", "🌈", "🎭", "😌", "🎪", "⚔️", "🏇", "👸", "👑")
        val cupsCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        val cupsImages = listOf(
            R.drawable.minor_arcana_cups_ace, R.drawable.minor_arcana_cups_2, R.drawable.minor_arcana_cups_3,
            R.drawable.minor_arcana_cups_4, R.drawable.minor_arcana_cups_5, R.drawable.minor_arcana_cups_6,
            R.drawable.minor_arcana_cups_7, R.drawable.minor_arcana_cups_8, R.drawable.minor_arcana_cups_9,
            R.drawable.minor_arcana_cups_10, R.drawable.minor_arcana_cups_page, R.drawable.minor_arcana_cups_knight,
            R.drawable.minor_arcana_cups_queen, R.drawable.minor_arcana_cups_king
        )
        cupsCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF CUPS", (index + 1).toString(), cupsEmojis[index], "감정과 사랑의 영역", cupsImages[index]))
        }

        // 완드 (정열, 창조)
        val wandsEmojis = listOf("🔥", "🏠", "🚢", "🎊", "⚔️", "🏆", "🛡️", "⚡", "🎯", "📦", "🧙", "🏇", "👸", "👑")
        val wandsCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        val wandsImages = listOf(
            R.drawable.minor_arcana_wands_ace, R.drawable.minor_arcana_wands_2, R.drawable.minor_arcana_wands_3,
            R.drawable.minor_arcana_wands_4, R.drawable.minor_arcana_wands_5, R.drawable.minor_arcana_wands_6,
            R.drawable.minor_arcana_wands_7, R.drawable.minor_arcana_wands_8, R.drawable.minor_arcana_wands_9,
            R.drawable.minor_arcana_wands_10, R.drawable.minor_arcana_wands_page, R.drawable.minor_arcana_wands_knight,
            R.drawable.minor_arcana_wands_queen, R.drawable.minor_arcana_wands_king
        )
        wandsCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF WANDS", (index + 1).toString(), wandsEmojis[index], "정열과 창조의 영역", wandsImages[index]))
        }

        // 소드 (지성, 갈등)
        val swordsEmojis = listOf("⚔️", "🤝", "💔", "😴", "🏃", "🚤", "⚔️", "🔒", "😰", "🗡️", "🧙", "🏇", "👸", "👑")
        val swordsCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        val swordsImages = listOf(
            R.drawable.minor_arcana_swords_ace, R.drawable.minor_arcana_swords_2, R.drawable.minor_arcana_swords_3,
            R.drawable.minor_arcana_swords_4, R.drawable.minor_arcana_swords_5, R.drawable.minor_arcana_swords_6,
            R.drawable.minor_arcana_swords_7, R.drawable.minor_arcana_swords_8, R.drawable.minor_arcana_swords_9,
            R.drawable.minor_arcana_swords_10, R.drawable.minor_arcana_swords_page, R.drawable.minor_arcana_swords_knight,
            R.drawable.minor_arcana_swords_queen, R.drawable.minor_arcana_swords_king
        )
        swordsCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF SWORDS", (index + 1).toString(), swordsEmojis[index], "지성과 갈등의 영역", swordsImages[index]))
        }

        // 펜타클 (물질, 현실)
        val pentaclesEmojis = listOf("💰", "🤹", "👷", "💵", "⛪", "🤝", "🌱", "🔨", "🏡", "💎", "🧙", "🏇", "👸", "👑")
        val pentaclesCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        val pentaclesImages = listOf(
            R.drawable.minor_arcana_pentacles_ace, R.drawable.minor_arcana_pentacles_2, R.drawable.minor_arcana_pentacles_3,
            R.drawable.minor_arcana_pentacles_4, R.drawable.minor_arcana_pentacles_5, R.drawable.minor_arcana_pentacles_6,
            R.drawable.minor_arcana_pentacles_7, R.drawable.minor_arcana_pentacles_8, R.drawable.minor_arcana_pentacles_9,
            R.drawable.minor_arcana_pentacles_10, R.drawable.minor_arcana_pentacles_page, R.drawable.minor_arcana_pentacles_knight,
            R.drawable.minor_arcana_pentacles_queen, R.drawable.minor_arcana_pentacles_king
        )
        pentaclesCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF PENTACLES", (index + 1).toString(), pentaclesEmojis[index], "물질과 현실의 영역", pentaclesImages[index]))
        }
    }

    // 전체 78장 카드
    val allCards = majorArcana + minorArcana

    fun drawRandomCards(count: Int): List<TarotCard> {
        return allCards.shuffled().take(count).map { card ->
            card.copy(
                isReversed = Random.nextBoolean(),
                emoji = card.emoji
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarotApp()
        }
    }
}

@Composable
fun TarotApp() {
    var currentScreen by remember { mutableStateOf("question") }
    var currentReading by remember { mutableStateOf<TarotReading?>(null) }
    var readingHistory by remember { mutableStateOf<List<TarotReading>>(emptyList()) }
    var questionText by remember { mutableStateOf("") }
    var isPremiumPurchased by remember { mutableStateOf(false) } // 실제 결제 상태
    var showPaymentDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0f0f23))
            .padding(16.dp)
    ) {
        when (currentScreen) {
            "question" -> QuestionScreen(
                questionText = questionText,
                onQuestionChange = { questionText = it },
                onConfirm = { currentScreen = "select" },
                onShowHistory = { currentScreen = "history" },
                isPremiumPurchased = isPremiumPurchased,
                onRequestPremium = { showPaymentDialog = true }
            )
            "select" -> SelectScreen(
                onDrawCards = { count ->
                    val cards = TarotDeck.drawRandomCards(count)
                    val reading = TarotReading(
                        question = questionText.ifEmpty { "질문 없음" },
                        cards = cards,
                        timestamp = java.text.SimpleDateFormat("MM/dd HH:mm", java.util.Locale.getDefault())
                            .format(java.util.Date())
                    )
                    currentReading = reading
                    readingHistory = (listOf(reading) + readingHistory).take(5)
                    currentScreen = "cards"
                },
                onBack = { currentScreen = "question" }
            )
            "cards" -> CardsScreen(
                reading = currentReading!!,
                onBack = {
                    questionText = ""
                    currentScreen = "question"
                },
                isPremium = isPremiumPurchased
            )
            "history" -> HistoryScreen(
                history = readingHistory,
                onBack = { currentScreen = "question" },
                onSelectHistory = { reading ->
                    currentReading = reading
                    currentScreen = "cards"
                }
            )
        }

        // 결제 다이얼로그
        if (showPaymentDialog) {
            PaymentDialog(
                onDismiss = { showPaymentDialog = false },
                onPurchaseSuccess = {
                    isPremiumPurchased = true
                    showPaymentDialog = false
                }
            )
        }
    }
}

@Composable
fun PaymentDialog(
    onDismiss: () -> Unit,
    onPurchaseSuccess: () -> Unit
) {
    var isProcessing by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2a2d47)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🌟 프리미엄 업그레이드",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFffd700)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "실제 타로카드 이미지와\n더 정확한 해석을 경험하세요!",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "₩3,900 (일회성 결제)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFffd700)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isProcessing) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFffd700)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "결제 처리 중...",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text("취소")
                        }

                        Button(
                            onClick = {
                                // 임시 결제 처리 시뮬레이션
                                isProcessing = true
                                // 2초 후 결제 완료
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(2000)
                                    onPurchaseSuccess()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFffd700),
                                contentColor = Color.Black
                            )
                        ) {
                            Text("💳 결제하기")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "※ 테스트 버전: 2초 후 자동 활성화",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun QuestionScreen(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onShowHistory: () -> Unit,
    isPremiumPurchased: Boolean,
    onRequestPremium: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 여백
        Spacer(modifier = Modifier.height(60.dp))

        // 앱 제목
        Text(
            text = "🔮 TarotApp",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // 질문 입력란
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2a2d47)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "상담할 내용을 깊이생각해보시고,아래에 써보세요",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp),
                    lineHeight = 20.sp
                )

                OutlinedTextField(
                    value = questionText,
                    onValueChange = onQuestionChange,
                    placeholder = {
                        Text(
                            "주제를 쓰세요",
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFffd700),
                        unfocusedBorderColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm() }
                    ),
                    minLines = 2
                )
            }
        }

        // 확인 버튼 (글씨쓰는곳 바로 밑에)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .width(200.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFffd700),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "✨ 확인",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 여백 (자판이 올라와도 버튼이 보이도록)
        Spacer(modifier = Modifier.weight(1f))

        // 프리미엄 상태 표시 (하단)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (!isPremiumPurchased) onRequestPremium() },
            colors = CardDefaults.cardColors(
                containerColor = if (isPremiumPurchased) Color(0xFF2d5016) else Color(0xFF2a2d47)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isPremiumPurchased) "🌟 프리미엄 모드" else "⭐ 무료 모드",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isPremiumPurchased) "실제 타로카드 이미지" else "이모지 카드 (업그레이드 가능)",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                if (!isPremiumPurchased) {
                    Button(
                        onClick = onRequestPremium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFffd700),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("업그레이드", fontSize = 12.sp)
                    }
                } else {
                    Text(
                        text = "✅",
                        fontSize = 24.sp,
                        color = Color.Green
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 히스토리 버튼 (하단)
        TarotButton(
            text = "📚 이전 기록",
            onClick = onShowHistory,
            isSecondary = true
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SelectScreen(
    onDrawCards: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 뒤로가기 버튼 (상단)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2a2d47),
                    contentColor = Color.White
                )
            ) {
                Text("← 뒤로")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 제목
        Text(
            text = "🎴 카드를 선택하세요",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // 1장 뽑기 버튼
        TarotButton(
            text = "🃏 카드 1장 뽑기",
            onClick = { onDrawCards(1) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3장 뽑기 버튼
        TarotButton(
            text = "🎴 카드 3장 뽑기",
            onClick = { onDrawCards(3) }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TarotButton(
    text: String,
    onClick: () -> Unit,
    isSecondary: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(240.dp)
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSecondary) Color(0xFF2a2d47) else Color(0xFF4a4e69),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CardsScreen(
    reading: TarotReading,
    onBack: () -> Unit,
    isPremium: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // 뒤로가기 버튼 - 상단 고정
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2a2d47),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .zIndex(10f) // 다른 요소들보다 위에 표시
        ) {
            Text("← 뒤로")
        }

        // 질문 표시
        if (reading.question != "질문 없음") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2a2d47)
                )
            ) {
                Text(
                    text = "💭 ${reading.question}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // 카드 표시 영역 - 남은 공간 사용
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-14).dp) // 총 5mm(14dp) 위로 올리기
                .padding(top = 16.dp) // 뒤로 버튼과의 안전 거리 확보
        ) {
            when (reading.cards.size) {
                1 -> SingleCardLayout(reading.cards[0], isPremium = isPremium)
                3 -> ThreeCardVerticalLayout(reading.cards, isPremium = isPremium)
            }
        }
    }
}

@Composable
fun SingleCardLayout(card: TarotCard, isPremium: Boolean = false) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TarotCardView(card, large = true, isPremium = isPremium)
    }
}

@Composable
fun ThreeCardVerticalLayout(cards: List<TarotCard>, isPremium: Boolean = false) {
    // 세로로 3장 배치 (스크롤 가능) - 모든 카드 위치 통일
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 8.dp,     // 상단 여백 최소화
            bottom = 32.dp, // 하단 여백 확보
            start = 16.dp,
            end = 16.dp
        )
    ) {
        items(cards) { card ->
            TarotCardView(card, large = false, isPremium = isPremium)
        }
    }
}

@Composable
fun TarotCardView(
    card: TarotCard,
    large: Boolean = false,
    isPremium: Boolean = false
) {
    val cardWidth = if (large) 300.dp else 220.dp
    val cardHeight = if (large) 450.dp else 320.dp
    val emojiSize = if (large) 180.sp else 120.sp
    val numberSize = if (large) 14.sp else 10.sp
    val nameSize = if (large) 12.sp else 9.sp

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .border(2.dp, Color(0xFFffd700), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 배경 그라디언트
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF5F5DC),
                                Color(0xFFE6E6FA)
                            )
                        )
                    )
            )

            // 프리미엄 모드: 실제 이미지 표시
            if (isPremium && card.imageResId != null) {
                Image(
                    painter = painterResource(id = card.imageResId),
                    contentDescription = card.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(if (card.isReversed) Modifier.rotate(180f) else Modifier),
                    contentScale = ContentScale.Fit
                )

                // 카드 이름 오버레이
                Text(
                    text = card.name,
                    fontSize = nameSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            } else {
                // 무료 버전: 이모지 방식

                // 왼쪽 위 모서리 숫자
                Text(
                    text = card.number,
                    fontSize = numberSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )

                // 오른쪽 아래 모서리 숫자 (뒤집어서)
                Text(
                    text = card.number,
                    fontSize = numberSize,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .rotate(180f)
                )

                // 중앙 - 거대한 타로카드 이모지
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 타로카드 메인 이모지 (매우 크게)
                    Text(
                        text = when(card.name) {
                            "THE SUN" -> "☀️"
                            "THE FOOL" -> "🃏"
                            "THE MAGICIAN" -> "🧙‍♂️"
                            "THE HIGH PRIESTESS" -> "🔮"
                            "THE EMPRESS" -> "👸"
                            "THE EMPEROR" -> "🤴"
                            "THE HIEROPHANT" -> "⛪"
                            "THE LOVERS" -> "💕"
                            "THE CHARIOT" -> "🏇"
                            "STRENGTH" -> "🦁"
                            "THE HERMIT" -> "🔍"
                            "WHEEL OF FORTUNE" -> "🎡"
                            "JUSTICE" -> "⚖️"
                            "THE HANGED MAN" -> "🙃"
                            "DEATH" -> "💀"
                            "TEMPERANCE" -> "👼"
                            "THE DEVIL" -> "😈"
                            "THE TOWER" -> "🏰"
                            "THE STAR" -> "⭐"
                            "THE MOON" -> "🌙"
                            "JUDGEMENT" -> "📯"
                            "THE WORLD" -> "🌍"
                            else -> when {
                                card.name.contains("KING") -> "👑"
                                card.name.contains("QUEEN") -> "👸"
                                card.name.contains("KNIGHT") -> "🏇"
                                card.name.contains("PAGE") -> "📜"
                                card.name.contains("CUPS") -> "🏆"
                                card.name.contains("WANDS") -> "🔥"
                                card.name.contains("SWORDS") -> "⚔️"
                                card.name.contains("PENTACLES") -> "💰"
                                else -> "🌟"
                            }
                        },
                        fontSize = emojiSize,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = if (card.isReversed) Modifier.rotate(180f) else Modifier
                    )

                    // 장식 무늬
                    Text(
                        text = "✦ ❈ ✦",
                        fontSize = if (large) 20.sp else 16.sp,
                        color = Color(0xFF8B4513),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // 하단 카드 이름
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = card.name,
                        fontSize = nameSize,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )

                    if (card.isReversed) {
                        Text(
                            text = "REVERSED",
                            fontSize = (nameSize.value - 2).sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    history: List<TarotReading>,
    onBack: () -> Unit,
    onSelectHistory: (TarotReading) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 뒤로가기 버튼
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2a2d47),
                contentColor = Color.White
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("← 뒤로")
        }

        Text(
            text = "📚 이전 기록 (최근 5번)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "아직 뽑은 카드가 없습니다",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history.withIndex().toList()) { (index, reading) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectHistory(reading) },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2a2d47)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${index + 1}번째 - ${reading.cards.size}장",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = reading.timestamp,
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "💭 ${reading.question}",
                                color = Color(0xFFffd700),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = reading.cards.joinToString(" | ") {
                                    "${it.emoji} ${it.name}${if (it.isReversed) " (역)" else ""}"
                                },
                                color = Color(0xFFcccccc),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}