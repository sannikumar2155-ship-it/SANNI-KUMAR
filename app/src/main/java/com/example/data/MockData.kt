package com.example.data

import com.example.model.*

object MockData {
    val sampleUsers = listOf(
        UserProfile(
            id = "u1",
            name = "Aanya Sharma",
            age = 23,
            gender = "Female",
            avatarInitial = "A",
            avatarColor = 0xFFFF2D75,
            bio = "Coffee lover & indie music fanatic. Let's sing together in the party room! ☕✨",
            location = "Bandra, Mumbai",
            distanceKm = 2,
            interests = listOf("Singing", "Indie Pop", "Coffee", "Photography", "Travel"),
            occupation = "UI/UX Designer",
            zodiac = "Libra ♎",
            isOnline = true,
            vipTier = VipTier.GOLD,
            followersCount = 480,
            followingCount = 150,
            visitorsCount = 1200,
            matchesCount = 42
        ),
        UserProfile(
            id = "u2",
            name = "Rohan Verma",
            age = 26,
            gender = "Male",
            avatarInitial = "R",
            avatarColor = 0xFF8B5CF6,
            bio = "Guitarist, weekend trekker, and tech nerd. Looking for someone with great banter. 🎸🏔️",
            location = "Andheri, Mumbai",
            distanceKm = 4,
            interests = listOf("Acoustic Guitar", "Trekking", "Coding", "Board Games"),
            occupation = "Software Engineer",
            zodiac = "Sagittarius ♐",
            isOnline = true,
            vipTier = VipTier.SILVER,
            followersCount = 290,
            followingCount = 210,
            visitorsCount = 850,
            matchesCount = 28
        ),
        UserProfile(
            id = "u3",
            name = "Priya Sen",
            age = 24,
            gender = "Female",
            avatarInitial = "P",
            avatarColor = 0xFFEC4899,
            bio = "Dance like nobody's watching, host party rooms like everyone's listening 💃🎉",
            location = "Powai, Mumbai",
            distanceKm = 6,
            interests = listOf("Dancing", "Bollywood", "Foodie", "Fashion"),
            occupation = "Fashion Stylist",
            zodiac = "Gemini ♊",
            isOnline = true,
            vipTier = VipTier.DIAMOND,
            followersCount = 1200,
            followingCount = 310,
            visitorsCount = 3500,
            matchesCount = 94
        ),
        UserProfile(
            id = "u4",
            name = "Kabir Malhotra",
            age = 27,
            gender = "Male",
            avatarInitial = "K",
            avatarColor = 0xFF06B6D4,
            bio = "Fitness enthusiast, foodie, and midnight philosopher. Tell me your favorite movie quote.",
            location = "Juhu, Mumbai",
            distanceKm = 3,
            interests = listOf("Fitness", "Cinema", "Culinary", "Cocktails"),
            occupation = "Fitness Coach",
            zodiac = "Aries ♈",
            isOnline = false,
            vipTier = VipTier.NONE,
            followersCount = 180,
            followingCount = 130,
            visitorsCount = 410,
            matchesCount = 15
        ),
        UserProfile(
            id = "u5",
            name = "Tara Kapoor",
            age = 22,
            gender = "Female",
            avatarInitial = "T",
            avatarColor = 0xFF10B981,
            bio = "Art student, dog mom, and vinyl collector. Always up for late night talks & karaoke! 🐶🎨",
            location = "Colaba, Mumbai",
            distanceKm = 8,
            interests = listOf("Art & Painting", "Pets", "Vinyl", "Late Night Talks"),
            occupation = "Illustrator",
            zodiac = "Pisces ♓",
            isOnline = true,
            vipTier = VipTier.GOLD,
            followersCount = 650,
            followingCount = 190,
            visitorsCount = 1600,
            matchesCount = 53
        )
    )

    val virtualGifts = listOf(
        VirtualItem(
            id = "g_rose",
            name = "Romantic Rose",
            type = VirtualItemType.GIFT,
            priceCoins = 10,
            iconEmoji = "🌹",
            description = "A sweet token of affection.",
            previewGradientStart = 0xFFFF416C,
            previewGradientEnd = 0xFFFF4B2B
        ),
        VirtualItem(
            id = "g_heart_lock",
            name = "Heart Lock",
            type = VirtualItemType.GIFT,
            priceCoins = 50,
            iconEmoji = "🔒❤️",
            description = "Lock your hearts together.",
            previewGradientStart = 0xFFFF5E62,
            previewGradientEnd = 0xFFFF9966
        ),
        VirtualItem(
            id = "g_perfume",
            name = "French Perfume",
            type = VirtualItemType.GIFT,
            priceCoins = 150,
            iconEmoji = "✨🧴",
            description = "Captivating aroma of romance.",
            previewGradientStart = 0xFF8A2387,
            previewGradientEnd = 0xFFE94057
        ),
        VirtualItem(
            id = "g_diamond_ring",
            name = "Diamond Ring",
            type = VirtualItemType.GIFT,
            priceCoins = 400,
            iconEmoji = "💍",
            description = "Sparkling pledge of passion.",
            previewGradientStart = 0xFF00C6FF,
            previewGradientEnd = 0xFF0072FF
        ),
        VirtualItem(
            id = "g_sports_car",
            name = "Neon Supercar",
            type = VirtualItemType.GIFT,
            priceCoins = 1200,
            iconEmoji = "🏎️💨",
            description = "Full screen revving car gift animation!",
            previewGradientStart = 0xFFFF8008,
            previewGradientEnd = 0xFFFFC837
        ),
        VirtualItem(
            id = "g_luxury_castle",
            name = "Royal Castle",
            type = VirtualItemType.GIFT,
            priceCoins = 3500,
            iconEmoji = "🏰✨",
            description = "Majestic palace fit for royalty.",
            previewGradientStart = 0xFF654EA3,
            previewGradientEnd = 0xFFEAAFC8
        )
    )

    val avatarFrames = listOf(
        VirtualItem(
            id = "frame_neon_heart",
            name = "Neon Heart Frame",
            type = VirtualItemType.AVATAR_FRAME,
            priceCoins = 300,
            iconEmoji = "💖",
            description = "Glowing animated neon heart border.",
            previewGradientStart = 0xFFFF2D75,
            previewGradientEnd = 0xFFFF85A2
        ),
        VirtualItem(
            id = "frame_royal_crown",
            name = "Imperial Crown Frame",
            type = VirtualItemType.AVATAR_FRAME,
            priceCoins = 800,
            iconEmoji = "👑",
            description = "Golden royal prestige border.",
            previewGradientStart = 0xFFFFD700,
            previewGradientEnd = 0xFFFFA000
        ),
        VirtualItem(
            id = "frame_cyber_dragon",
            name = "Cyber Aura Frame",
            type = VirtualItemType.AVATAR_FRAME,
            priceCoins = 1500,
            iconEmoji = "⚡",
            description = "Electric neon lightning pulse effect.",
            previewGradientStart = 0xFF00E5FF,
            previewGradientEnd = 0xFF8B5CF6
        )
    )

    val entranceRides = listOf(
        VirtualItem(
            id = "ride_sports_car",
            name = "Ferrari Rosso Ride",
            type = VirtualItemType.RIDE_VEHICLE,
            priceCoins = 1000,
            iconEmoji = "🏎️",
            description = "Vroom sound & golden banner upon entering party rooms.",
            previewGradientStart = 0xFFEF4444,
            previewGradientEnd = 0xFFF97316
        ),
        VirtualItem(
            id = "ride_winged_pegasus",
            name = "Winged Pegasus",
            type = VirtualItemType.RIDE_VEHICLE,
            priceCoins = 2500,
            iconEmoji = "🦄",
            description = "Starfall rainbow entrance in any room.",
            previewGradientStart = 0xFF8B5CF6,
            previewGradientEnd = 0xFFEC4899
        )
    )

    val coinPackages = listOf(
        CoinPackage("cp_1", 300, 0, "$0.99"),
        CoinPackage("cp_2", 1500, 200, "$4.99", "Popular"),
        CoinPackage("cp_3", 3500, 600, "$9.99", "Best Value"),
        CoinPackage("cp_4", 9000, 2500, "$19.99", "VIP Deal")
    )

    fun createInitialPartyRooms(currentUser: UserProfile): List<PartyRoom> {
        val user1 = sampleUsers[0]
        val user2 = sampleUsers[1]
        val user3 = sampleUsers[2]
        val user4 = sampleUsers[4]

        return listOf(
            PartyRoom(
                id = "room_1",
                title = "Late Night Romance & Shayari 🌙❤️",
                category = RoomCategory.DATING,
                host = user1,
                coverEmoji = "💖",
                tag = "Hindi / English",
                viewerCount = 88,
                totalGiftPoints = 24600,
                seats = listOf(
                    RoomSeat(0, user1, isMuted = false, isSpeaking = true),
                    RoomSeat(1, user2, isMuted = false, isSpeaking = false),
                    RoomSeat(2, user3, isMuted = true, isSpeaking = false),
                    RoomSeat(3, null),
                    RoomSeat(4, null),
                    RoomSeat(5, null),
                    RoomSeat(6, null),
                    RoomSeat(7, null)
                ),
                messages = listOf(
                    RoomMessage("m1", "System", "Welcome to Late Night Romance! Be respectful and have fun! 🥂", isSystem = true),
                    RoomMessage("m2", "Rohan Verma", "Hey everyone! Who is singing next?", vipTier = VipTier.SILVER),
                    RoomMessage("m3", "Aanya Sharma", "Welcome all newcomers! Take a seat! 🌹", vipTier = VipTier.GOLD)
                )
            ),
            PartyRoom(
                id = "room_2",
                title = "Acoustic Sing-along & Guitar Jams 🎸",
                category = RoomCategory.SINGING,
                host = user2,
                coverEmoji = "🎤",
                tag = "Music / Indie",
                viewerCount = 54,
                totalGiftPoints = 14200,
                seats = listOf(
                    RoomSeat(0, user2, isMuted = false, isSpeaking = true),
                    RoomSeat(1, user4, isMuted = false, isSpeaking = false),
                    RoomSeat(2, null),
                    RoomSeat(3, null),
                    RoomSeat(4, null),
                    RoomSeat(5, null),
                    RoomSeat(6, null),
                    RoomSeat(7, null)
                ),
                messages = listOf(
                    RoomMessage("m10", "System", "Room started with high-quality audio streaming 🎶", isSystem = true),
                    RoomMessage("m11", "Kabir Malhotra", "Drop the beat! 🥁", vipTier = VipTier.NONE)
                )
            ),
            PartyRoom(
                id = "room_3",
                title = "Truth or Dare Dating Edition 🔥",
                category = RoomCategory.GAMES,
                host = user3,
                coverEmoji = "🎲",
                tag = "18+ / Fun",
                viewerCount = 142,
                totalGiftPoints = 48900,
                seats = listOf(
                    RoomSeat(0, user3, isMuted = false, isSpeaking = true),
                    RoomSeat(1, user1, isMuted = false, isSpeaking = false),
                    RoomSeat(2, null),
                    RoomSeat(3, null),
                    RoomSeat(4, null),
                    RoomSeat(5, null),
                    RoomSeat(6, null),
                    RoomSeat(7, null)
                ),
                messages = listOf(
                    RoomMessage("m20", "Priya Sen", "Who has the courage to pick Dare? 😈", vipTier = VipTier.DIAMOND)
                )
            ),
            PartyRoom(
                id = "room_4",
                title = "Lovera VIP Diamond Lounge 👑🥂",
                category = RoomCategory.VIP,
                host = user3,
                coverEmoji = "💎",
                tag = "VIP Members Only",
                viewerCount = 37,
                totalGiftPoints = 96400,
                isVipOnly = true,
                seats = listOf(
                    RoomSeat(0, user3, isMuted = false, isSpeaking = true),
                    RoomSeat(1, null),
                    RoomSeat(2, null),
                    RoomSeat(3, null),
                    RoomSeat(4, null),
                    RoomSeat(5, null),
                    RoomSeat(6, null),
                    RoomSeat(7, null)
                ),
                messages = listOf(
                    RoomMessage("m30", "System", "Welcome VIP members. Enjoy custom luxury entrance badges.", isSystem = true)
                )
            )
        )
    }

    fun createInitialConversations(myId: String): List<Conversation> {
        val u1 = sampleUsers[0]
        val u3 = sampleUsers[2]
        val u5 = sampleUsers[4]

        return listOf(
            Conversation(
                id = "c1",
                otherUser = u1,
                lastMessage = "I loved your taste in music in the party room!",
                lastMessageTime = "10:32 PM",
                unreadCount = 2,
                messages = listOf(
                    ChatMessage("msg1", u1.id, "Hey there! Saw your profile on Lovera 😍", "10:28 PM", isFromMe = false),
                    ChatMessage("msg2", myId, "Hey Aanya! Glad we matched here!", "10:30 PM", isFromMe = true),
                    ChatMessage("msg3", u1.id, "I loved your taste in music in the party room!", "10:32 PM", isFromMe = false)
                )
            ),
            Conversation(
                id = "c2",
                otherUser = u3,
                lastMessage = "Sent you a Romantic Rose 🌹",
                lastMessageTime = "Yesterday",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("msg4", u3.id, "Hi! Are you joining the Truth or Dare room tonight?", "Yesterday", isFromMe = false),
                    ChatMessage("msg5", myId, "For sure! Let's team up.", "Yesterday", isFromMe = true),
                    ChatMessage("msg6", myId, "Sent you a gift", "Yesterday", isFromMe = true, isGift = true, giftName = "Romantic Rose", giftIcon = "🌹", giftValueCoins = 10)
                )
            ),
            Conversation(
                id = "c3",
                otherUser = u5,
                lastMessage = "Check out this acoustic sketch I made today 🎶",
                lastMessageTime = "Oct 21",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("msg7", u5.id, "Hey! You like vinyl records too?", "Oct 21", isFromMe = false),
                    ChatMessage("msg8", myId, "Yes! Big collector here.", "Oct 21", isFromMe = true),
                    ChatMessage("msg9", u5.id, "Check out this acoustic sketch I made today 🎶", "Oct 21", isFromMe = false, isVoiceNote = true, voiceDurationSec = 14)
                )
            )
        )
    }
}
