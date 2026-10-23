package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CallingState(
    val otherUser: UserProfile,
    val isVideo: Boolean,
    val isConnected: Boolean = false,
    val durationSeconds: Int = 0
)

class LoveraViewModel : ViewModel() {

    private val defaultUser = UserProfile(
        id = "me_1",
        name = "Alex Rivera",
        age = 25,
        gender = "Non-Binary",
        avatarInitial = "A",
        avatarColor = 0xFFFF2D75,
        bio = "Exploring city vibes, music nights, and meaningful connections. Say hi! ✨",
        location = "Bandra West, Mumbai",
        distanceKm = 1,
        interests = listOf("Music", "Nightlife", "Travel", "Art", "Coffee"),
        occupation = "Product Designer",
        zodiac = "Scorpio ♏",
        isOnline = true,
        verified = true,
        vipTier = VipTier.GOLD,
        coins = 1500,
        diamonds = 620,
        followersCount = 384,
        followingCount = 142,
        visitorsCount = 920,
        matchesCount = 27,
        activeFrameId = "frame_neon_heart",
        activeRideId = "ride_sports_car",
        paidServiceActive = true,
        subscriptionPlan = "Lovera VIP Gold Monthly",
        subscriptionRenewalDate = "Nov 23, 2026",
        loginMethod = "phone",
        phoneNumber = "+91 98765 43210"
    )

    private val _currentUser = MutableStateFlow<UserProfile?>(defaultUser)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Discovery / Dating
    private val _discoverProfiles = MutableStateFlow<List<UserProfile>>(MockData.sampleUsers)
    val discoverProfiles: StateFlow<List<UserProfile>> = _discoverProfiles.asStateFlow()

    private val _currentCardIndex = MutableStateFlow(0)
    val currentCardIndex: StateFlow<Int> = _currentCardIndex.asStateFlow()

    private val _matchedUser = MutableStateFlow<UserProfile?>(null)
    val matchedUser: StateFlow<UserProfile?> = _matchedUser.asStateFlow()

    // Party Rooms
    private val _partyRooms = MutableStateFlow<List<PartyRoom>>(emptyList())
    val partyRooms: StateFlow<List<PartyRoom>> = _partyRooms.asStateFlow()

    private val _selectedCategory = MutableStateFlow(RoomCategory.ALL)
    val selectedCategory: StateFlow<RoomCategory> = _selectedCategory.asStateFlow()

    private val _activeRoom = MutableStateFlow<PartyRoom?>(null)
    val activeRoom: StateFlow<PartyRoom?> = _activeRoom.asStateFlow()

    private val _roomReactions = MutableStateFlow<List<String>>(emptyList())
    val roomReactions: StateFlow<List<String>> = _roomReactions.asStateFlow()

    private val _roomSoundEffect = MutableStateFlow<String?>(null)
    val roomSoundEffect: StateFlow<String?> = _roomSoundEffect.asStateFlow()

    // Chat
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _activeConversation = MutableStateFlow<Conversation?>(null)
    val activeConversation: StateFlow<Conversation?> = _activeConversation.asStateFlow()

    // Calls
    private val _callingState = MutableStateFlow<CallingState?>(null)
    val callingState: StateFlow<CallingState?> = _callingState.asStateFlow()

    // Store & Inventory
    private val _ownedFrames = MutableStateFlow<Set<String>>(setOf("frame_neon_heart"))
    val ownedFrames: StateFlow<Set<String>> = _ownedFrames.asStateFlow()

    private val _ownedRides = MutableStateFlow<Set<String>>(setOf("ride_sports_car"))
    val ownedRides: StateFlow<Set<String>> = _ownedRides.asStateFlow()

    // UI Toast / SnackBar message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        val user = _currentUser.value ?: defaultUser
        _partyRooms.value = MockData.createInitialPartyRooms(user)
        _conversations.value = MockData.createInitialConversations(user.id)
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // --- Authentication ---
    fun loginWithPhone(phoneNumber: String, otp: String) {
        val user = defaultUser.copy(
            loginMethod = "phone",
            phoneNumber = phoneNumber.ifBlank { "+91 98765 43210" },
            accountStatus = AccountStatus.ACTIVE
        )
        _currentUser.value = user
        _isLoggedIn.value = true
        showToast("Logged in with phone: ${user.phoneNumber}")
    }

    fun loginWithGoogle() {
        val user = defaultUser.copy(
            name = "Alex GoogleUser",
            loginMethod = "google",
            email = "alex.rivera@gmail.com",
            accountStatus = AccountStatus.ACTIVE
        )
        _currentUser.value = user
        _isLoggedIn.value = true
        showToast("Signed in with Google Account")
    }

    fun loginWithFacebook() {
        val user = defaultUser.copy(
            name = "Alex FB",
            loginMethod = "facebook",
            email = "alex_fb@facebook.com",
            accountStatus = AccountStatus.ACTIVE
        )
        _currentUser.value = user
        _isLoggedIn.value = true
        showToast("Signed in with Facebook")
    }

    fun loginAsGuest() {
        val user = defaultUser.copy(
            name = "Guest User",
            loginMethod = "guest",
            accountStatus = AccountStatus.ACTIVE
        )
        _currentUser.value = user
        _isLoggedIn.value = true
        showToast("Browsing as Guest")
    }

    fun logout() {
        _isLoggedIn.value = false
        showToast("Logged out successfully")
    }

    // --- Dating Actions ---
    fun likeCurrentProfile() {
        val list = _discoverProfiles.value
        val index = _currentCardIndex.value
        if (index < list.size) {
            val user = list[index]
            _currentCardIndex.value = index + 1
            // 70% chance of reciprocal match for exciting UX
            if (index % 2 == 0) {
                _matchedUser.value = user
                // add to matches count
                _currentUser.value = _currentUser.value?.let {
                    it.copy(matchesCount = it.matchesCount + 1)
                }
            } else {
                showToast("Liked ${user.name} ❤️")
            }
        }
    }

    fun superLikeCurrentProfile() {
        val list = _discoverProfiles.value
        val index = _currentCardIndex.value
        if (index < list.size) {
            val user = list[index]
            _currentCardIndex.value = index + 1
            _matchedUser.value = user
            _currentUser.value = _currentUser.value?.let {
                it.copy(matchesCount = it.matchesCount + 1)
            }
        }
    }

    fun passCurrentProfile() {
        val list = _discoverProfiles.value
        val index = _currentCardIndex.value
        if (index < list.size) {
            _currentCardIndex.value = index + 1
        }
    }

    fun resetDiscoverCards() {
        _currentCardIndex.value = 0
    }

    fun dismissMatchDialog() {
        _matchedUser.value = null
    }

    fun matchAndStartChat(matched: UserProfile) {
        _matchedUser.value = null
        openChat(matched)
    }

    // --- Party Rooms ---
    fun selectCategory(category: RoomCategory) {
        _selectedCategory.value = category
    }

    fun joinRoom(room: PartyRoom) {
        val user = _currentUser.value ?: return
        if (room.isVipOnly && user.vipTier == VipTier.NONE) {
            showToast("This is a VIP Lounge! Upgrade to VIP in Store.")
            return
        }

        // Add user entrance announcement if equipped with ride
        val entranceText = if (user.activeRideId != null) {
            "🚀 ${user.name} zoomed into the room with ${user.activeRideId}!"
        } else {
            "👋 ${user.name} joined the party room!"
        }

        val updatedMessages = room.messages + RoomMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = "System",
            content = entranceText,
            isSystem = true
        )
        val updatedRoom = room.copy(
            viewerCount = room.viewerCount + 1,
            messages = updatedMessages
        )
        _activeRoom.value = updatedRoom
    }

    fun leaveRoom() {
        val current = _activeRoom.value
        val user = _currentUser.value
        if (current != null && user != null) {
            // Free any seat user is on
            val updatedSeats = current.seats.map { seat ->
                if (seat.user?.id == user.id) seat.copy(user = null, isSpeaking = false) else seat
            }
            _activeRoom.value = null
            // update in rooms list
            _partyRooms.value = _partyRooms.value.map {
                if (it.id == current.id) it.copy(seats = updatedSeats, viewerCount = (it.viewerCount - 1).coerceAtLeast(1)) else it
            }
        } else {
            _activeRoom.value = null
        }
    }

    fun takeSeat(seatIndex: Int) {
        val room = _activeRoom.value ?: return
        val user = _currentUser.value ?: return

        val seats = room.seats.toMutableList()
        // If user already on another seat, clear it
        for (i in seats.indices) {
            if (seats[i].user?.id == user.id) {
                seats[i] = seats[i].copy(user = null, isSpeaking = false)
            }
        }
        if (seatIndex in seats.indices) {
            seats[seatIndex] = seats[seatIndex].copy(user = user, isMuted = false, isSpeaking = true)
        }
        val updated = room.copy(seats = seats)
        _activeRoom.value = updated
        showToast("You took Mic #${seatIndex + 1}! 🎙️")
    }

    fun leaveSeat() {
        val room = _activeRoom.value ?: return
        val user = _currentUser.value ?: return
        val seats = room.seats.map { seat ->
            if (seat.user?.id == user.id) seat.copy(user = null, isSpeaking = false) else seat
        }
        _activeRoom.value = room.copy(seats = seats)
        showToast("Stepped down from microphone.")
    }

    fun toggleSeatMute() {
        val room = _activeRoom.value ?: return
        val user = _currentUser.value ?: return
        val seats = room.seats.map { seat ->
            if (seat.user?.id == user.id) seat.copy(isMuted = !seat.isMuted, isSpeaking = seat.isMuted) else seat
        }
        _activeRoom.value = room.copy(seats = seats)
    }

    fun sendRoomMessage(text: String) {
        if (text.isBlank()) return
        val room = _activeRoom.value ?: return
        val user = _currentUser.value ?: return

        val newMsg = RoomMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = user.name,
            content = text.trim(),
            vipTier = user.vipTier
        )
        val updated = room.copy(messages = room.messages + newMsg)
        _activeRoom.value = updated
    }

    fun sendRoomGift(gift: VirtualItem, targetUser: UserProfile?) {
        val user = _currentUser.value ?: return
        val room = _activeRoom.value ?: return

        if (user.coins < gift.priceCoins) {
            showToast("Not enough coins! Top up in the Store.")
            return
        }

        // Deduct coins & add diamonds
        _currentUser.value = user.copy(coins = user.coins - gift.priceCoins)

        val targetName = targetUser?.name ?: room.host.name
        val giftMsg = RoomMessage(
            id = "gift_${System.currentTimeMillis()}",
            senderName = user.name,
            content = "Sent ${gift.name} ${gift.iconEmoji} to $targetName!",
            isGiftNotice = true,
            giftIcon = gift.iconEmoji,
            vipTier = user.vipTier
        )
        val updated = room.copy(
            totalGiftPoints = room.totalGiftPoints + gift.priceCoins * 10,
            messages = room.messages + giftMsg
        )
        _activeRoom.value = updated
        showToast("Sent ${gift.name} ${gift.iconEmoji}!")
    }

    fun createPartyRoom(title: String, category: RoomCategory, tag: String) {
        val user = _currentUser.value ?: return
        val newRoom = PartyRoom(
            id = "room_${System.currentTimeMillis()}",
            title = title.ifBlank { "${user.name}'s Party Lounge" },
            category = category,
            host = user,
            coverEmoji = category.icon,
            tag = tag.ifBlank { "Open Mic" },
            viewerCount = 1,
            totalGiftPoints = 0,
            seats = listOf(
                RoomSeat(0, user, isMuted = false, isSpeaking = true),
                RoomSeat(1, null),
                RoomSeat(2, null),
                RoomSeat(3, null),
                RoomSeat(4, null),
                RoomSeat(5, null),
                RoomSeat(6, null),
                RoomSeat(7, null)
            ),
            messages = listOf(
                RoomMessage("init", "System", "Room opened! Invite friends to join 🥂", isSystem = true)
            )
        )
        _partyRooms.value = listOf(newRoom) + _partyRooms.value
        _activeRoom.value = newRoom
        showToast("Your Party Room is LIVE!")
    }

    fun sendRoomReaction(emoji: String) {
        _roomReactions.value = _roomReactions.value + emoji
        viewModelScope.launch {
            delay(2500)
            if (_roomReactions.value.isNotEmpty()) {
                _roomReactions.value = _roomReactions.value.drop(1)
            }
        }
    }

    fun playSoundEffect(name: String) {
        _roomSoundEffect.value = name
        showToast("Sound Effect: $name 🔊")
        viewModelScope.launch {
            delay(1500)
            _roomSoundEffect.value = null
        }
    }

    // --- Chat ---
    fun openChat(user: UserProfile) {
        val convList = _conversations.value
        val existing = convList.find { it.otherUser.id == user.id }
        if (existing != null) {
            _activeConversation.value = existing
        } else {
            val newConv = Conversation(
                id = "c_${user.id}",
                otherUser = user,
                lastMessage = "You both matched! Say hi!",
                lastMessageTime = "Now",
                unreadCount = 0,
                messages = listOf(
                    ChatMessage("init", "system", "🎉 You matched with ${user.name}! Send an icebreaker.", "Now", isFromMe = false)
                )
            )
            _conversations.value = listOf(newConv) + convList
            _activeConversation.value = newConv
        }
    }

    fun closeChat() {
        _activeConversation.value = null
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val conv = _activeConversation.value ?: return
        val me = _currentUser.value ?: return

        val myMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = me.id,
            text = text.trim(),
            timestamp = "Just now",
            isFromMe = true
        )
        val updatedConv = conv.copy(
            lastMessage = text.trim(),
            lastMessageTime = "Just now",
            messages = conv.messages + myMsg
        )
        _activeConversation.value = updatedConv
        _conversations.value = _conversations.value.map { if (it.id == conv.id) updatedConv else it }

        // Simulated companion auto-reply
        viewModelScope.launch {
            delay(1800)
            val replies = listOf(
                "Aww that's so sweet! 😍 How was your day?",
                "Haha totally agree! Are you going to any party rooms tonight? 🎶",
                "I was just thinking about that! What music are you into lately? 🎧",
                "You have great energy! Let's talk more 💖"
            )
            val replyMsg = ChatMessage(
                id = "reply_${System.currentTimeMillis()}",
                senderId = conv.otherUser.id,
                text = replies.random(),
                timestamp = "Just now",
                isFromMe = false
            )
            val withReply = updatedConv.copy(
                lastMessage = replyMsg.text,
                lastMessageTime = "Just now",
                messages = updatedConv.messages + replyMsg
            )
            _activeConversation.value = withReply
            _conversations.value = _conversations.value.map { if (it.id == conv.id) withReply else it }
        }
    }

    fun sendChatGift(gift: VirtualItem) {
        val user = _currentUser.value ?: return
        val conv = _activeConversation.value ?: return

        if (user.coins < gift.priceCoins) {
            showToast("Not enough coins! Get more in Store.")
            return
        }

        _currentUser.value = user.copy(coins = user.coins - gift.priceCoins)

        val giftMsg = ChatMessage(
            id = "gift_${System.currentTimeMillis()}",
            senderId = user.id,
            text = "Sent ${gift.name} ${gift.iconEmoji}",
            timestamp = "Just now",
            isFromMe = true,
            isGift = true,
            giftName = gift.name,
            giftIcon = gift.iconEmoji,
            giftValueCoins = gift.priceCoins
        )
        val updatedConv = conv.copy(
            lastMessage = "Sent a gift: ${gift.name} ${gift.iconEmoji}",
            lastMessageTime = "Just now",
            messages = conv.messages + giftMsg
        )
        _activeConversation.value = updatedConv
        _conversations.value = _conversations.value.map { if (it.id == conv.id) updatedConv else it }
        showToast("Sent ${gift.name} ${gift.iconEmoji} to ${conv.otherUser.name}!")
    }

    fun sendVoiceNote(durationSec: Int = 5) {
        val conv = _activeConversation.value ?: return
        val me = _currentUser.value ?: return

        val voiceMsg = ChatMessage(
            id = "voice_${System.currentTimeMillis()}",
            senderId = me.id,
            text = "Voice message ($durationSec s)",
            timestamp = "Just now",
            isFromMe = true,
            isVoiceNote = true,
            voiceDurationSec = durationSec
        )
        val updatedConv = conv.copy(
            lastMessage = "Voice message ($durationSec s) 🎙️",
            lastMessageTime = "Just now",
            messages = conv.messages + voiceMsg
        )
        _activeConversation.value = updatedConv
        _conversations.value = _conversations.value.map { if (it.id == conv.id) updatedConv else it }
    }

    // --- Calls ---
    fun startCall(otherUser: UserProfile, isVideo: Boolean) {
        _callingState.value = CallingState(otherUser, isVideo, isConnected = false)
        viewModelScope.launch {
            delay(2000)
            _callingState.value = _callingState.value?.copy(isConnected = true)
        }
    }

    fun endCall() {
        _callingState.value = null
        showToast("Call ended.")
    }

    // --- Mine / Profile Modification ---
    fun updateProfile(
        name: String,
        age: Int,
        gender: String,
        bio: String,
        occupation: String,
        location: String,
        interests: List<String>
    ) {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(
            name = name.ifBlank { user.name },
            age = if (age > 0) age else user.age,
            gender = gender.ifBlank { user.gender },
            bio = bio.ifBlank { user.bio },
            occupation = occupation.ifBlank { user.occupation },
            location = location.ifBlank { user.location },
            interests = if (interests.isNotEmpty()) interests else user.interests
        )
        showToast("Profile updated successfully! ✨")
    }

    // --- Virtual Items & Store ---
    fun buyCoins(pkg: CoinPackage) {
        val user = _currentUser.value ?: return
        val totalAdded = pkg.coins + pkg.bonusCoins
        _currentUser.value = user.copy(coins = user.coins + totalAdded)
        showToast("Purchased ${pkg.coins} (+${pkg.bonusCoins} Bonus) Coins! 🪙")
    }

    fun equipAvatarFrame(frameId: String) {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(activeFrameId = frameId)
        showToast("Avatar frame equipped! 🖼️")
    }

    fun buyAndEquipItem(item: VirtualItem) {
        val user = _currentUser.value ?: return
        if (user.coins < item.priceCoins) {
            showToast("Insufficient coins to purchase ${item.name}!")
            return
        }

        val updatedCoins = user.coins - item.priceCoins
        when (item.type) {
            VirtualItemType.AVATAR_FRAME -> {
                _ownedFrames.value = _ownedFrames.value + item.id
                _currentUser.value = user.copy(coins = updatedCoins, activeFrameId = item.id)
                showToast("Purchased and equipped ${item.name}! 👑")
            }
            VirtualItemType.RIDE_VEHICLE -> {
                _ownedRides.value = _ownedRides.value + item.id
                _currentUser.value = user.copy(coins = updatedCoins, activeRideId = item.id)
                showToast("Purchased and equipped entrance ride: ${item.name}! 🏎️")
            }
            else -> {
                _currentUser.value = user.copy(coins = updatedCoins)
                showToast("Purchased ${item.name}!")
            }
        }
    }

    // --- Paid Services Management (Suspend, Update, Terminate) ---
    fun toggleVipSubscription(tier: VipTier, activate: Boolean) {
        val user = _currentUser.value ?: return
        if (activate) {
            _currentUser.value = user.copy(
                vipTier = tier,
                paidServiceActive = true,
                subscriptionPlan = "Lovera ${tier.title} Monthly",
                subscriptionRenewalDate = "Nov 23, 2026"
            )
            showToast("Activated ${tier.title}! Welcome to the VIP Club.")
        } else {
            _currentUser.value = user.copy(
                paidServiceActive = false,
                subscriptionPlan = null
            )
            showToast("VIP subscription cancelled.")
        }
    }

    fun suspendPaidServices() {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(
            paidServiceActive = false,
            subscriptionPlan = "Suspended (Auto-renew disabled)"
        )
        showToast("Paid recurring services have been suspended.")
    }

    fun resumePaidServices() {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(
            paidServiceActive = true,
            subscriptionPlan = "Lovera VIP Monthly (Active)"
        )
        showToast("Paid services resumed.")
    }

    // --- Account Suspension & Termination ---
    fun suspendAccountTemporarily() {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(accountStatus = AccountStatus.SUSPENDED_TEMPORARY)
        showToast("Account suspended. Your profile is hidden from discovery.")
    }

    fun reactivateAccount() {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(accountStatus = AccountStatus.ACTIVE)
        showToast("Welcome back! Your account has been reactivated.")
    }

    fun terminateAccountAndServices() {
        // Erase data and reset to fresh state
        _currentUser.value = _currentUser.value?.copy(
            accountStatus = AccountStatus.TERMINATED,
            coins = 0,
            diamonds = 0,
            paidServiceActive = false,
            subscriptionPlan = null
        )
        _isLoggedIn.value = false
        showToast("Account and all associated paid services have been terminated.")
    }

    fun updatePrivacySettings(ghostMode: Boolean, hideOnline: Boolean, incognitoParty: Boolean) {
        val user = _currentUser.value ?: return
        _currentUser.value = user.copy(
            ghostMode = ghostMode,
            hideOnline = hideOnline,
            incognitoParty = incognitoParty
        )
        showToast("Privacy settings updated.")
    }
}
