package com.example.model

enum class VirtualItemType {
    AVATAR_FRAME,
    RIDE_VEHICLE,
    CHAT_BUBBLE,
    GIFT
}

data class VirtualItem(
    val id: String,
    val name: String,
    val type: VirtualItemType,
    val priceCoins: Int,
    val iconEmoji: String,
    val description: String,
    val previewGradientStart: Long,
    val previewGradientEnd: Long,
    val isEquipped: Boolean = false,
    val isOwned: Boolean = false
)

data class CoinPackage(
    val id: String,
    val coins: Int,
    val bonusCoins: Int = 0,
    val priceUsd: String,
    val tag: String? = null
)

data class PaidSubscription(
    val id: String,
    val tier: VipTier,
    val title: String,
    val priceMonthly: String,
    val features: List<String>,
    val isActive: Boolean = false,
    val renewalDate: String = "2026-11-23"
)
