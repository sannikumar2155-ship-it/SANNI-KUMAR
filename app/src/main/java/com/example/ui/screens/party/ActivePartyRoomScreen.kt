package com.example.ui.screens.party

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PartyRoom
import com.example.model.RoomSeat
import com.example.model.UserProfile
import com.example.model.VipTier
import com.example.ui.components.AvatarWithFrame
import com.example.ui.components.GiftSelectionSheet
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel
import kotlinx.coroutines.launch

@Composable
fun ActivePartyRoomScreen(
    room: PartyRoom,
    viewModel: LoveraViewModel,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val roomReactions by viewModel.roomReactions.collectAsState()
    val chatListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var messageText by remember { mutableStateOf("") }
    var showGiftSheetForUser by remember { mutableStateOf<UserProfile?>(null) }
    var showSoundboard by remember { mutableStateOf(false) }

    // Intercept hardware/system back button to leave room cleanly
    BackHandler {
        viewModel.leaveRoom()
    }

    // Auto-scroll chat to latest message
    LaunchedEffect(room.messages.size) {
        if (room.messages.isNotEmpty()) {
            chatListState.animateScrollToItem(room.messages.size - 1)
        }
    }

    val mySeat = room.seats.find { it.user?.id == currentUser?.id }
    val isOnMic = mySeat != null

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        LoveraDeepNight,
                        LoveraSurface,
                        LoveraDeepNight
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Top Bar: Room Title, Viewers, Leave Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = { viewModel.leaveRoom() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Leave Room", tint = TextWhite)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = room.title,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Host: ${room.host.name} • 💎 ${room.totalGiftPoints}",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Viewer count & Leave button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LoveraSurfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "👀 ${room.viewerCount}",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { viewModel.leaveRoom() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DangerRed.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Exit", tint = DangerRed, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stage Area: 8 Microphone Seats in 2 Rows of 4
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LoveraSurface.copy(alpha = 0.85f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LoveraCardBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Row 1: Seats 0 to 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (i in 0..3) {
                            val seat = room.seats.getOrNull(i) ?: RoomSeat(i)
                            MicSeatItem(
                                seat = seat,
                                isHostSeat = i == 0,
                                isMe = seat.user?.id == currentUser?.id,
                                onSeatClick = {
                                    if (seat.user == null) {
                                        viewModel.takeSeat(i)
                                    } else if (seat.user.id != currentUser?.id) {
                                        showGiftSheetForUser = seat.user
                                    } else {
                                        viewModel.leaveSeat()
                                    }
                                }
                            )
                        }
                    }

                    // Row 2: Seats 4 to 7
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (i in 4..7) {
                            val seat = room.seats.getOrNull(i) ?: RoomSeat(i)
                            MicSeatItem(
                                seat = seat,
                                isHostSeat = false,
                                isMe = seat.user?.id == currentUser?.id,
                                onSeatClick = {
                                    if (seat.user == null) {
                                        viewModel.takeSeat(i)
                                    } else if (seat.user.id != currentUser?.id) {
                                        showGiftSheetForUser = seat.user
                                    } else {
                                        viewModel.leaveSeat()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Live Chat Messages Stream
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    state = chatListState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(room.messages) { msg ->
                        if (msg.isSystem) {
                            // System banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LoveraSurfaceVariant.copy(alpha = 0.6f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = msg.content,
                                    color = ElectricVioletLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else if (msg.isGiftNotice) {
                            // Rich Gift Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                PassionPink.copy(alpha = 0.3f),
                                                CoinGold.copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                    .border(1.dp, CoinGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = msg.giftIcon ?: "🎁", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${msg.senderName}: ${msg.content}",
                                        color = CoinGoldLight,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            // Regular User chat
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LoveraSurfaceVariant.copy(alpha = 0.7f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                if (msg.vipTier != VipTier.NONE) {
                                    Text(
                                        text = "👑 ",
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = "${msg.senderName}: ",
                                    color = PassionPinkLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = msg.content,
                                    color = TextWhite,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // Floating Reactions stream
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    roomReactions.takeLast(4).forEach { emoji ->
                        Text(text = emoji, fontSize = 28.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Soundboard quick row if opened
            AnimatedVisibility(visible = showSoundboard) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LoveraSurfaceHighlight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf(
                            "Applause 👏" to "👏",
                            "Cheer 🥳" to "🥳",
                            "DJ Drop 🎧" to "🎧",
                            "Airhorn 🎺" to "🎺"
                        ).forEach { (label, emoji) ->
                            TextButton(onClick = {
                                viewModel.playSoundEffect(label)
                                viewModel.sendRoomReaction(emoji)
                            }) {
                                Text(text = "$emoji $label", fontSize = 12.sp, color = TextWhite)
                            }
                        }
                    }
                }
            }

            // Bottom Action Bar: Mic control, Chat input, Gifts, Reactions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mic button: toggle mute if on mic, or take mic if open
                IconButton(
                    onClick = {
                        if (isOnMic) {
                            viewModel.toggleSeatMute()
                        } else {
                            val firstEmpty = room.seats.indexOfFirst { it.user == null }
                            if (firstEmpty != -1) viewModel.takeSeat(firstEmpty)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isOnMic) PassionPink else LoveraSurfaceVariant)
                ) {
                    Icon(
                        imageVector = if (isOnMic && mySeat?.isMuted == true) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = Color.White
                    )
                }

                // Chat Input Field
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Chat in room...", color = TextSubtle, fontSize = 13.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PassionPink,
                        unfocusedBorderColor = LoveraCardBorder
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("room_chat_input")
                )

                // Send chat message
                if (messageText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            viewModel.sendRoomMessage(messageText)
                            messageText = ""
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PassionPink)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                // Soundboard toggle
                IconButton(
                    onClick = { showSoundboard = !showSoundboard },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LoveraSurfaceVariant)
                ) {
                    Text(text = "🔊", fontSize = 18.sp)
                }

                // Send Gift Button
                IconButton(
                    onClick = { showGiftSheetForUser = room.host },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(CoinGold, PassionPink)))
                        .testTag("room_send_gift_button")
                ) {
                    Text(text = "🎁", fontSize = 20.sp)
                }

                // Quick Heart Reaction
                IconButton(
                    onClick = { viewModel.sendRoomReaction("❤️") },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LoveraSurfaceVariant)
                ) {
                    Text(text = "❤️", fontSize = 18.sp)
                }
            }
        }
    }

    // Gift Sheet for Room Host or Seated User
    showGiftSheetForUser?.let { targetUser ->
        GiftSelectionSheet(
            userCoins = currentUser?.coins ?: 0,
            onDismiss = { showGiftSheetForUser = null },
            onSendGift = { gift ->
                viewModel.sendRoomGift(gift, targetUser)
                showGiftSheetForUser = null
            },
            onTopUpClick = {
                showGiftSheetForUser = null
                onTopUpClick()
            }
        )
    }
}

@Composable
fun MicSeatItem(
    seat: RoomSeat,
    isHostSeat: Boolean,
    isMe: Boolean,
    onSeatClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSeatClick() }
            .padding(4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (seat.user != null) {
                AvatarWithFrame(
                    user = seat.user,
                    size = 52.dp,
                    showOnlineDot = false,
                    isSpeaking = seat.isSpeaking && !seat.isMuted
                )

                // Host Crown
                if (isHostSeat) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-10).dp)
                    ) {
                        Text(text = "👑", fontSize = 16.sp)
                    }
                }

                // Mute indicator
                if (seat.isMuted) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(DangerRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MicOff, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                    }
                }
            } else {
                // Empty Mic Seat
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(LoveraSurfaceVariant)
                        .border(1.dp, LoveraCardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Take Mic",
                        tint = TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = when {
                seat.user != null && isMe -> "You"
                seat.user != null -> seat.user.name.split(" ").first()
                isHostSeat -> "Host Mic"
                else -> "Mic ${seat.seatIndex + 1}"
            },
            color = if (isMe) PassionPinkLight else TextWhite,
            fontSize = 11.sp,
            fontWeight = if (isMe) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
