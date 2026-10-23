package com.example.ui.screens.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.ChatMessage
import com.example.model.Conversation
import com.example.ui.components.AvatarWithFrame
import com.example.ui.components.GiftSelectionSheet
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@Composable
fun DirectChatScreen(
    conversation: Conversation,
    viewModel: LoveraViewModel,
    onBack: () -> Unit,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var showGiftSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    BackHandler {
        onBack()
    }

    LaunchedEffect(conversation.messages.size) {
        if (conversation.messages.isNotEmpty()) {
            listState.animateScrollToItem(conversation.messages.size - 1)
        }
    }

    val icebreakers = listOf(
        "What's your dream vacation? 🌴",
        "Coffee or late night drinks? ☕🍸",
        "Favorite music genre right now? 🎶",
        "Truth or Dare in a party room? 🎲"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LoveraDeepNight)
    ) {
        // Chat Header
        Surface(
            color = LoveraSurface,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }

                AvatarWithFrame(
                    user = conversation.otherUser,
                    size = 42.dp,
                    showOnlineDot = true
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = conversation.otherUser.name,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = if (conversation.otherUser.isOnline) "Active Now" else "Offline",
                        color = if (conversation.otherUser.isOnline) SuccessGreen else TextMuted,
                        fontSize = 11.sp
                    )
                }

                // Voice Call Button
                IconButton(
                    onClick = { viewModel.startCall(conversation.otherUser, isVideo = false) },
                    modifier = Modifier.testTag("chat_voice_call_button")
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Voice Call", tint = PassionPinkLight)
                }

                // Video Call Button
                IconButton(
                    onClick = { viewModel.startCall(conversation.otherUser, isVideo = true) },
                    modifier = Modifier.testTag("chat_video_call_button")
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = ElectricVioletLight)
                }
            }
        }

        // Message Stream
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            items(conversation.messages) { message ->
                MessageBubble(message = message)
            }
        }

        // Icebreakers suggestions
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            items(icebreakers) { icebreaker ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = LoveraSurfaceVariant,
                    modifier = Modifier.clickable {
                        viewModel.sendChatMessage(icebreaker)
                    }
                ) {
                    Text(
                        text = icebreaker,
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Bottom Input Row
        Surface(
            color = LoveraSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Send Gift Button
                IconButton(
                    onClick = { showGiftSheet = true },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(LoveraSurfaceVariant)
                        .testTag("chat_gift_button")
                ) {
                    Text(text = "🎁", fontSize = 18.sp)
                }

                // Voice Note record simulation
                IconButton(
                    onClick = { viewModel.sendVoiceNote(7) },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(LoveraSurfaceVariant)
                        .testTag("chat_voice_note_button")
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice Note", tint = ElectricVioletLight)
                }

                // Text Input
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Type a message...", color = TextSubtle, fontSize = 14.sp) },
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = PassionPink,
                        unfocusedBorderColor = LoveraCardBorder,
                        focusedContainerColor = LoveraSurfaceVariant,
                        unfocusedContainerColor = LoveraSurfaceVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("chat_text_input")
                )

                // Send Button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendChatMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PassionPink)
                        .testTag("chat_send_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }

    if (showGiftSheet) {
        GiftSelectionSheet(
            userCoins = currentUser?.coins ?: 0,
            onDismiss = { showGiftSheet = false },
            onSendGift = { gift ->
                viewModel.sendChatGift(gift)
                showGiftSheet = false
            },
            onTopUpClick = {
                showGiftSheet = false
                onTopUpClick()
            }
        )
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isMe = message.isFromMe

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        if (message.isGift) {
            // Virtual Gift Message Bubble
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LoveraSurfaceVariant),
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(PassionPink, CoinGold)),
                        RoundedCornerShape(18.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = message.giftIcon ?: "🎁", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = message.giftName ?: "Virtual Gift",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🪙", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${message.giftValueCoins} Coins",
                                color = CoinGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else if (message.isVoiceNote) {
            // Voice note bubble
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMe) PassionPink else LoveraSurfaceVariant
                ),
                modifier = Modifier.widthIn(max = 220.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = " ▂▃▅▆▇▆▅▃▂ ", color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "0:${message.voiceDurationSec.toString().padStart(2, '0')}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            // Text Message Bubble
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isMe) 16.dp else 4.dp,
                    bottomEnd = if (isMe) 4.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMe) PassionPink else LoveraSurfaceVariant
                ),
                modifier = Modifier.widthIn(max = 270.dp)
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = message.timestamp,
            color = TextSubtle,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
