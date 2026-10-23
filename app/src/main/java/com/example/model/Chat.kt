package com.example.model

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val isGift: Boolean = false,
    val giftName: String? = null,
    val giftIcon: String? = null,
    val giftValueCoins: Int = 0,
    val isVoiceNote: Boolean = false,
    val voiceDurationSec: Int = 0
)

data class Conversation(
    val id: String,
    val otherUser: UserProfile,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
)
