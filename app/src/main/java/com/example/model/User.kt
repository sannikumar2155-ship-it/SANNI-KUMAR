package com.example.model

enum class VipTier(val title: String, val badgeColor: Long, val badgeTextColor: Long) {
    NONE("Free", 0xFF6B7280, 0xFFFFFFFF),
    SILVER("Silver VIP", 0xFF9CA3AF, 0xFF111827),
    GOLD("Gold VIP", 0xFFFFD700, 0xFF451A03),
    DIAMOND("Diamond VIP", 0xFF38BDF8, 0xFF082F49)
}

enum class AccountStatus {
    ACTIVE,
    SUSPENDED_TEMPORARY,
    TERMINATED
}

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String, // "Female", "Male", "Non-Binary"
    val avatarInitial: String,
    val avatarColor: Long = 0xFFFF2D75,
    val bio: String,
    val location: String = "Mumbai, IN",
    val distanceKm: Int = 3,
    val interests: List<String> = emptyList(),
    val occupation: String = "Designer & Music Lover",
    val zodiac: String = "Leo ♌",
    val isOnline: Boolean = true,
    val verified: Boolean = true,
    val vipTier: VipTier = VipTier.NONE,
    val coins: Int = 1200,
    val diamonds: Int = 450,
    val followersCount: Int = 124,
    val followingCount: Int = 89,
    val visitorsCount: Int = 312,
    val matchesCount: Int = 18,
    val activeFrameId: String? = null,
    val activeRideId: String? = null,
    val ghostMode: Boolean = false,
    val hideOnline: Boolean = false,
    val incognitoParty: Boolean = false,
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,
    val paidServiceActive: Boolean = false,
    val subscriptionPlan: String? = null,
    val subscriptionRenewalDate: String? = null,
    val loginMethod: String = "phone", // "phone", "google", "facebook", "guest"
    val phoneNumber: String = "+91 98765 43210",
    val email: String = "user@lovera.app"
)
