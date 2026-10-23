package com.example.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.model.VipTier
import com.example.ui.components.AvatarWithFrame
import com.example.ui.components.GiftSelectionSheet
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: LoveraViewModel,
    onNavigateToChat: (UserProfile) -> Unit,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val discoverProfiles by viewModel.discoverProfiles.collectAsState()
    val currentIndex by viewModel.currentCardIndex.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var showProfileDetail by remember { mutableStateOf<UserProfile?>(null) }
    var showGiftSheetForUser by remember { mutableStateOf<UserProfile?>(null) }

    val currentProfile = discoverProfiles.getOrNull(currentIndex)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LoveraDeepNight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Lovera",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PassionPink
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricViolet.copy(alpha = 0.3f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Dating", color = ElectricVioletLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Coins balance pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(LoveraSurfaceVariant)
                        .clickable { onTopUpClick() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "🪙", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentUser?.coins ?: 0}",
                        color = CoinGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Quick Category Filters
            var selectedFilter by remember { mutableStateOf("All") }
            val filterOptions = listOf("All", "Online Now", "Nearby (5km)", "Top Stars ⭐", "VIP Members")

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PassionPink,
                            selectedLabelColor = Color.White,
                            containerColor = LoveraSurfaceVariant,
                            labelColor = TextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) PassionPink else LoveraCardBorder
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // Main Dating Card or Empty State
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (currentProfile != null) {
                    // Dating Match Card
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = LoveraSurface),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp)
                            .shadow(12.dp, RoundedCornerShape(28.dp))
                            .border(1.dp, LoveraCardBorder, RoundedCornerShape(28.dp))
                            .clickable { showProfileDetail = currentProfile }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                        ) {
                            // Header: Avatar, Name, Age, Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarWithFrame(
                                    user = currentProfile,
                                    size = 72.dp,
                                    showOnlineDot = true
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = currentProfile.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${currentProfile.age}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextMuted
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Gender & Location
                                        Text(
                                            text = "${currentProfile.gender} • ${currentProfile.location}",
                                            color = TextMuted,
                                            fontSize = 13.sp
                                        )
                                    }

                                    if (currentProfile.vipTier != VipTier.NONE) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(currentProfile.vipTier.badgeColor))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "👑 ${currentProfile.vipTier.title}",
                                                color = Color(currentProfile.vipTier.badgeTextColor),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Bio Box
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = LoveraSurfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = currentProfile.bio,
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Occupation & Distance
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = LoveraSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Work, contentDescription = null, tint = PassionPinkLight, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(currentProfile.occupation, color = TextWhite, fontSize = 12.sp, maxLines = 1)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = LoveraSurfaceVariant,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Place, contentDescription = null, tint = ElectricVioletLight, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("${currentProfile.distanceKm} km away", color = TextWhite, fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Interests chips
                            Text("Interests & Hobbies", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                currentProfile.interests.forEach { tag ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(LoveraSurfaceHighlight)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "#$tag", color = TextWhite, fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Tap for more info hint
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = TextSubtle, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tap card for full profile & compatibility", color = TextSubtle, fontSize = 11.sp)
                            }
                        }
                    }
                } else {
                    // Empty state when swiped through all cards
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(text = "💫", fontSize = 54.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "That's all for now!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You've checked out all active singles nearby. Join a Voice Party Room or reshuffle to see them again!",
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.resetDiscoverCards() },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                            modifier = Modifier.testTag("reset_discover_cards_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reshuffle Matches", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Bottom Action Controls (Pass, Super Like, Gift, Like)
            if (currentProfile != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pass (Dislike)
                    IconButton(
                        onClick = { viewModel.passCurrentProfile() },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(LoveraSurfaceVariant)
                            .border(1.dp, DangerRed.copy(alpha = 0.5f), CircleShape)
                            .testTag("dating_pass_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Pass", tint = DangerRed, modifier = Modifier.size(26.dp))
                    }

                    // Send Direct Gift
                    IconButton(
                        onClick = { showGiftSheetForUser = currentProfile },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(LoveraSurfaceVariant)
                            .border(1.dp, CoinGold.copy(alpha = 0.5f), CircleShape)
                            .testTag("dating_gift_button")
                    ) {
                        Text(text = "🎁", fontSize = 20.sp)
                    }

                    // Super Like
                    IconButton(
                        onClick = { viewModel.superLikeCurrentProfile() },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(ElectricViolet, ElectricVioletDark)))
                            .testTag("dating_superlike_button")
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Super Like", tint = Color.White, modifier = Modifier.size(28.dp))
                    }

                    // Like Button (Heart)
                    IconButton(
                        onClick = { viewModel.likeCurrentProfile() },
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(PassionPink, PassionPinkDark)))
                            .shadow(8.dp, CircleShape)
                            .testTag("dating_like_button")
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color.White, modifier = Modifier.size(34.dp))
                    }

                    // Instant Direct Chat
                    IconButton(
                        onClick = { onNavigateToChat(currentProfile) },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(LoveraSurfaceVariant)
                            .border(1.dp, DiamondCyan.copy(alpha = 0.5f), CircleShape)
                            .testTag("dating_instant_chat_button")
                    ) {
                        Icon(Icons.Default.ChatBubble, contentDescription = "Direct Chat", tint = DiamondCyan, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    }

    // Full Profile Bottom Sheet Modal
    showProfileDetail?.let { profile ->
        ModalBottomSheet(
            onDismissRequest = { showProfileDetail = null },
            containerColor = LoveraSurface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarWithFrame(user = profile, size = 80.dp, showOnlineDot = true)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("${profile.name}, ${profile.age}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("${profile.gender} • ${profile.occupation}", color = TextMuted, fontSize = 14.sp)
                        Text("📍 ${profile.location} (${profile.distanceKm} km)", color = ElectricVioletLight, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = LoveraCardBorder)
                Spacer(modifier = Modifier.height(16.dp))

                Text("About Me", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(profile.bio, color = TextMuted, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(14.dp))
                Text("Zodiac & Details", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("${profile.zodiac} • Active ${if (profile.isOnline) "Now" else "Recently"}", color = TextMuted, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            showProfileDetail = null
                            showGiftSheetForUser = profile
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LoveraSurfaceHighlight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Send Gift 🎁")
                    }
                    Button(
                        onClick = {
                            showProfileDetail = null
                            onNavigateToChat(profile)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Chat Now 💬")
                    }
                }
            }
        }
    }

    // Gift Selection Bottom Sheet
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
