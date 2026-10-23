package com.example.ui.screens.party

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.RoomCategory
import com.example.model.VipTier
import com.example.ui.components.AvatarWithFrame
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyScreen(
    viewModel: LoveraViewModel,
    modifier: Modifier = Modifier
) {
    val rooms by viewModel.partyRooms.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }

    val filteredRooms = remember(rooms, selectedCategory) {
        if (selectedCategory == RoomCategory.ALL) rooms else rooms.filter { it.category == selectedCategory }
    }

    Scaffold(
        containerColor = LoveraDeepNight,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = PassionPink,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                icon = { Icon(Icons.Default.Mic, contentDescription = null) },
                text = { Text("Create Room", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("create_party_room_fab")
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Voice Party Rooms",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite
                    )
                    Text(
                        text = "Listen, sing, talk and meet new people live 🎧",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }

                // VIP badge or room count
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LoveraSurfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🔴 ${rooms.size} Live",
                        color = DangerRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Categories horizontal scroll
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                items(RoomCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectCategory(category) },
                        label = { Text("${category.icon} ${category.displayName}", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricViolet,
                            selectedLabelColor = Color.White,
                            containerColor = LoveraSurfaceVariant,
                            labelColor = TextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) ElectricVioletLight else LoveraCardBorder
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // Rooms List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredRooms) { room ->
                    PartyRoomCard(
                        room = room,
                        onClick = { viewModel.joinRoom(room) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }

    // Create Room Modal Dialog
    if (showCreateDialog) {
        CreateRoomDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { title, cat, tag ->
                viewModel.createPartyRoom(title, cat, tag)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun PartyRoomCard(
    room: PartyRoom,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LoveraSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LoveraCardBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("party_room_card_${room.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Room Title & Live Viewers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LoveraSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = room.coverEmoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = room.title,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "Host: ${room.host.name} • ${room.tag}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                // Viewer Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LoveraSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = PassionPinkLight, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${room.viewerCount}",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mic Seats Avatars Row (showing who is currently on mic)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-8).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val occupiedSeats = room.seats.filter { it.user != null }.take(4)
                    if (occupiedSeats.isNotEmpty()) {
                        occupiedSeats.forEach { seat ->
                            seat.user?.let { u ->
                                AvatarWithFrame(user = u, size = 38.dp, showOnlineDot = false, isSpeaking = seat.isSpeaking)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LoveraSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }

                    val openSeatsCount = room.seats.count { it.user == null }
                    if (openSeatsCount > 0) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(LoveraSurfaceHighlight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("$openSeatsCount open mics 🎙️", color = TextMuted, fontSize = 11.sp)
                        }
                    }
                }

                // Gift Points
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💎", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${room.totalGiftPoints}",
                        color = DiamondCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, category: RoomCategory, tag: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(RoomCategory.DATING) }
    var tag by remember { mutableStateOf("Music & Romance") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LoveraSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Launch a Voice Party Room",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Anyone can tune in, request mic seats, send gifts, and chat live.",
                color = TextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Room Title", color = TextMuted) },
                placeholder = { Text("e.g. Late Night Flirt & Chill 🌙", color = TextSubtle) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedBorderColor = PassionPink,
                    unfocusedBorderColor = LoveraCardBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_room_title_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Select Category", color = TextMuted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(RoomCategory.values().filter { it != RoomCategory.ALL }) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text("${cat.icon} ${cat.displayName}", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PassionPink,
                            selectedLabelColor = Color.White,
                            containerColor = LoveraSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = tag,
                onValueChange = { tag = it },
                label = { Text("Language / Tag", color = TextMuted) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedBorderColor = PassionPink,
                    unfocusedBorderColor = LoveraCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onCreate(title, selectedCategory, tag) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_create_room_button")
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Party Room Now 🚀", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
