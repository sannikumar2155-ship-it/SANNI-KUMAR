package com.example.model

enum class RoomCategory(val displayName: String, val icon: String) {
    ALL("All", "🔥"),
    DATING("Dating & Flirt", "💘"),
    SINGING("Music & Sing", "🎤"),
    CHILL("Late Night Chill", "🌙"),
    GAMES("Fun & Games", "🎮"),
    VIP("VIP Lounge", "👑")
}

data class RoomSeat(
    val seatIndex: Int,
    val user: UserProfile? = null,
    val isMuted: Boolean = false,
    val isSpeaking: Boolean = false
)

data class RoomMessage(
    val id: String,
    val senderName: String,
    val content: String,
    val isSystem: Boolean = false,
    val isGiftNotice: Boolean = false,
    val giftIcon: String? = null,
    val vipTier: VipTier = VipTier.NONE,
    val timestamp: String = "Just now"
)

data class PartyRoom(
    val id: String,
    val title: String,
    val category: RoomCategory,
    val host: UserProfile,
    val coverEmoji: String = "✨",
    val tag: String = "English / Hindi",
    val viewerCount: Int = 42,
    val totalGiftPoints: Int = 18500,
    val seats: List<RoomSeat> = emptyList(),
    val messages: List<RoomMessage> = emptyList(),
    val isVipOnly: Boolean = false
)
