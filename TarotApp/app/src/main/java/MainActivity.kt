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
import kotlin.random.Random

// 타로카드 데이터 클래스
data class TarotCard(
    val name: String,
    val number: String,
    val emoji: String,
    val description: String,
    val imageResId: Int? = null, // 이미지 리소스 ID 추가 (나중에 사용)
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
        TarotCard("THE FOOL", "0", "🌟🎒🐕", "새로운 시작, 순수함, 모험"),
        TarotCard("THE MAGICIAN", "I", "🎩⚡🔮", "의지력, 창조, 능력"),
        TarotCard("THE HIGH PRIESTESS", "II", "🌙👑📚", "직감, 신비, 잠재의식"),
        TarotCard("THE EMPRESS", "III", "👑🌺💎", "풍요, 모성, 창조력"),
        TarotCard("THE EMPEROR", "IV", "👑🏛️⚔️", "권위, 안정, 질서"),
        TarotCard("THE HIEROPHANT", "V", "⛪🗝️📿", "전통, 교육, 영성"),
        TarotCard("THE LOVERS", "VI", "💕👫🌈", "사랑, 선택, 조화"),
        TarotCard("THE CHARIOT", "VII", "🏹🐎⚡", "의지, 승리, 통제"),
        TarotCard("STRENGTH", "VIII", "🦁💪🌹", "힘, 용기, 인내"),
        TarotCard("THE HERMIT", "IX", "🔦🏔️⭐", "지혜, 성찰, 고독"),
        TarotCard("WHEEL OF FORTUNE", "X", "🎡⚡🔄", "운명, 변화, 순환"),
        TarotCard("JUSTICE", "XI", "⚖️🗡️👁️", "정의, 균형, 진실"),
        TarotCard("THE HANGED MAN", "XII", "🙃🌳💧", "희생, 깨달음, 기다림"),
        TarotCard("DEATH", "XIII", "💀🌹🦋", "변화, 종료, 재생"),
        TarotCard("TEMPERANCE", "XIV", "👼💧🌈", "절제, 조화, 균형"),
        TarotCard("THE DEVIL", "XV", "😈🔗🔥", "유혹, 속박, 욕망"),
        TarotCard("THE TOWER", "XVI", "🗼⚡💥", "파괴, 급변, 해방"),
        TarotCard("THE STAR", "XVII", "⭐💧🕊️", "희망, 영감, 치유"),
        TarotCard("THE MOON", "XVIII", "🌙🐺🦞", "환상, 불안, 직감"),
        TarotCard("THE SUN", "XIX", "☀️🌻👶", "기쁨, 성공, 활력"),
        TarotCard("JUDGEMENT", "XX", "📯👼☁️", "각성, 부활, 심판"),
        TarotCard("THE WORLD", "XXI", "🌍💃🦅", "완성, 성취, 만족")
    )

    // 마이너 아르카나 56장
    private val minorArcana = mutableListOf<TarotCard>().apply {
        // 컵 (감정, 사랑)
        val cupsEmojis = listOf("💧", "💝", "🎉", "😔", "🏆", "👶", "🌈", "🎭", "😌", "🎪", "⚔️", "🏇", "👸", "👑")
        val cupsCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        cupsCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF CUPS", (index + 1).toString(), cupsEmojis[index], "감정과 사랑의 영역"))
        }

        // 완드 (정열, 창조)
        val wandsEmojis = listOf("🔥", "🏠", "🚢", "🎊", "⚔️", "🏆", "🛡️", "⚡", "🎯", "📦", "🧙", "🏇", "👸", "👑")
        val wandsCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        wandsCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF WANDS", (index + 1).toString(), wandsEmojis[index], "정열과 창조의 영역"))
        }

        // 소드 (지성, 갈등)
        val swordsEmojis = listOf("⚔️", "🤝", "💔", "😴", "🏃", "🚤", "⚔️", "🔒", "😰", "🗡️", "🧙", "🏇", "👸", "👑")
        val swordsCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        swordsCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF SWORDS", (index + 1).toString(), swordsEmojis[index], "지성과 갈등의 영역"))
        }

        // 펜타클 (물질, 현실)
        val pentaclesEmojis = listOf("💰", "🤹", "👷", "💵", "⛪", "🤝", "🌱", "🔨", "🏡", "💎", "🧙", "🏇", "👸", "👑")
        val pentaclesCards = listOf("ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "PAGE", "KNIGHT", "QUEEN", "KING")
        pentaclesCards.forEachIndexed { index, name ->
            add(TarotCard("$name OF PENTACLES", (index + 1).toString(), pentaclesEmojis[index], "물질과 현실의 영역"))
        }
    }

    // 전체 78장 카드
    val allCards = majorArcana + minorArcana

    fun drawRandomCards(count: Int): List<TarotCard> {
        return allCards.shuffled().take(count).map { card ->
            // 매번 새로운 객체를 생성해서 강제로 화면 업데이트
            card.copy(
                isReversed = Random.nextBoolean(),
                emoji = card.emoji // 이모지도 새로 설정
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
    var isPremium by remember { mutableStateOf(false) }
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
                isPremium = isPremium,
                onPremiumChange = { isPremium = it }
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
                isPremium = isPremium
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
    }
}

@Composable
fun QuestionScreen(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onShowHistory: () -> Unit,
    isPremium: Boolean,
    onPremiumChange: (Boolean) -> Unit
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

        // 프리미엄 토글 (하단)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isPremium) "🌟 프리미엄 모드 (실제 타로카드)" else "⭐ 무료 모드 (이모지)",
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = isPremium,
                onCheckedChange = onPremiumChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFffd700),
                    checkedTrackColor = Color(0xFFffd700).copy(alpha = 0.5f)
                )
            )
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

        // 질문 표시
        if (reading.question != "질문 없음") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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

        // 카드 표시 - 세로 배치
        when (reading.cards.size) {
            1 -> SingleCardLayout(reading.cards[0], isPremium = isPremium)
            3 -> ThreeCardVerticalLayout(reading.cards, isPremium = isPremium)
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
    // 세로로 3장 배치 (스크롤 가능)
    LazyColumn(
        modifier = Modifier.fillMaxSize().offset(y = (-48).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
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
    val emojiSize = if (large) 180.sp else 120.sp // 큰 이모지
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

            // 프리미엄 모드: 나중에 이미지 추가할 예정
            // 현재는 무료 버전과 동일하게 이모지 표시

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