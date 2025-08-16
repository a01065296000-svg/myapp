package com.aceto.taroapp

// 타로카드 데이터 클래스
data class TarotCard(
    val name: String,
    val meaningUpright: String,
    val meaningReversed: String,
    val imageRes: Int = 0,
    var isReversed: Boolean = false
)

// 타로카드 데이터 매니저
object TarotCardManager {
    val cards = listOf(
        TarotCard(
            "바보(The Fool)",
            "새로운 시작, 순수한 모험",
            "경솔함, 무모한 일"
        ),
        TarotCard(
            "마술사(The Magician)",
            "의지력, 창조력, 집중",
            "허영, 조작, 오만"
        ),
        TarotCard(
            "여황제(The High Priestess)",
            "직관, 잠재력, 신비",
            "무지, 현실 부정, 히스테리"
        ),
        TarotCard(
            "여황(The Empress)",
            "풍요로움, 모성, 창조",
            "의존, 과잉보호, 창조력 부족"
        ),
        TarotCard(
            "황제(The Emperor)",
            "권위, 안정적 리더십",
            "독재, 경직된 권력남용"
        ),
        TarotCard(
            "교황(The Hierophant)",
            "전통, 학습, 영적 지도",
            "독단주의, 체념주의, 영적 무지"
        ),
        TarotCard(
            "연인(The Lovers)",
            "사랑, 조화, 선택",
            "관계문제, 가치관 충돌, 잘못된 선택"
        ),
        TarotCard(
            "전차(The Chariot)",
            "의지, 승리, 통제력",
            "자제력 부족, 방향성 상실, 혼돈"
        ),
        TarotCard(
            "힘(Strength)",
            "용기, 인내, 정신력",
            "허약함, 자제력 부족, 의심"
        ),
        TarotCard(
            "은자(The Hermit)",
            "성찰, 탐구, 영적 깨달음",
            "고립, 외로움, 성찰 과도"
        ),
        TarotCard(
            "운명의 수레바퀴(Wheel of Fortune)",
            "변화, 운명, 기회",
            "불운, 통제 불능, 좌절"
        ),
        TarotCard(
            "정의(Justice)",
            "균형, 공정, 진실",
            "불균형, 편견, 진실 왜곡"
        ),
        TarotCard(
            "교수형(The Hanged Man)",
            "희생, 새로운 관점, 참을성",
            "쓸데없는 희생, 지연, 진전 부족"
        ),
        TarotCard(
            "죽음(Death)",
            "변화, 종료, 재생",
            "저항, 정체, 변화 거부"
        ),
        TarotCard(
            "절제(Temperance)",
            "균형, 인내심, 조화",
            "불균형, 과도함, 부조화"
        ),
        TarotCard(
            "악마(The Devil)",
            "유혹, 속박, 물질주의",
            "해방, 자유, 깨달음"
        ),
        TarotCard(
            "탑(The Tower)",
            "갑작스러운 변화, 깨달음, 파괴",
            "점진적 변화, 개인적 변화, 두려움"
        ),
        TarotCard(
            "별(The Star)",
            "희망, 직관, 영감",
            "절망, 목표 상실, 비관주의"
        ),
        TarotCard(
            "달(The Moon)",
            "환상, 혼란, 잠재의식",
            "명확함, 진실 발견, 직관"
        ),
        TarotCard(
            "태양(The Sun)",
            "기쁨, 활력, 긍정",
            "자신감 부족, 실패, 우울"
        ),
        TarotCard(
            "심판(Judgement)",
            "각성, 부활, 새로운 시작",
            "자기 의심, 후회, 잘못된 판단"
        ),
        TarotCard(
            "세계(The World)",
            "완성, 성취, 통합",
            "미완성, 지연, 목표 부족"
        )
    )

    fun getRandomCards(count: Int): List<TarotCard> {
        return cards.shuffled().take(count).map { card ->
            card.copy(isReversed = (0..1).random() == 1)
        }
    }
}