package com.example.ui.screens.mine

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.model.VipTier
import com.example.ui.components.AvatarWithFrame
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@Composable
fun MineScreen(
    viewModel: LoveraViewModel,
    onOpenSafetySettings: () -> Unit,
    onOpenStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val user = currentUser ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LoveraDeepNight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card Header
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LoveraSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LoveraCardBorder, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarWithFrame(
                        user = user,
                        size = 76.dp,
                        showOnlineDot = true
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${user.age}",
                                fontSize = 16.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // ID copyable badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LoveraSurfaceVariant)
                                    .clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Lovera ID", user.id))
                                        viewModel.showToast("ID copied: ${user.id}")
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("ID: ${user.id}", color = TextMuted, fontSize = 11.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextMuted, modifier = Modifier.size(10.dp))
                            }

                            // VIP Badge
                            if (user.vipTier != VipTier.NONE) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(user.vipTier.badgeColor))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "👑 ${user.vipTier.title}",
                                        color = Color(user.vipTier.badgeTextColor),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${user.gender} • ${user.location}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    // Edit Profile Pencil Button
                    IconButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LoveraSurfaceVariant)
                            .testTag("open_edit_profile_button")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = PassionPinkLight, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = user.bio,
                    color = TextWhite,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(LoveraSurfaceVariant)
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatCounter("Following", "${user.followingCount}")
                    StatCounter("Followers", "${user.followersCount}")
                    StatCounter("Visitors", "${user.visitorsCount}")
                    StatCounter("Matches", "${user.matchesCount}")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // App Currency Wallet Card ("app currency")
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LoveraSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(CoinGold.copy(alpha = 0.5f), DiamondCyan.copy(alpha = 0.5f))),
                    RoundedCornerShape(20.dp)
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Wallet & App Currency",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Button(
                        onClick = onOpenStore,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("topup_coins_button")
                    ) {
                        Text("Top Up 🪙", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Coins Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = LoveraSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🪙", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "${user.coins}", color = CoinGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(text = "Lovera Coins", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }

                    // Diamonds Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = LoveraSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💎", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "${user.diamonds}", color = DiamondCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(text = "Gift Diamonds", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // VIP & Paid Services Quick Card ("suspend paid services")
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LoveraSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LoveraCardBorder, RoundedCornerShape(20.dp))
                .clickable { onOpenSafetySettings() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(CoinGold, PassionPink))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (user.paidServiceActive) "VIP Membership: Active" else "VIP Membership: Inactive",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (user.paidServiceActive) user.subscriptionPlan ?: "Gold VIP Tier" else "Join to unlock party privileges",
                            color = if (user.paidServiceActive) SuccessGreen else TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Settings & Options List
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LoveraSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LoveraCardBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                MineMenuItem(
                    icon = Icons.Default.Storefront,
                    iconTint = ElectricVioletLight,
                    title = "Store & Virtual Items",
                    subtitle = "Buy frames, entrance cars & coin packs",
                    onClick = onOpenStore,
                    testTag = "menu_store"
                )

                HorizontalDivider(color = LoveraCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 8.dp))

                MineMenuItem(
                    icon = Icons.Default.EditNote,
                    iconTint = PassionPinkLight,
                    title = "Modify & Update Profile",
                    subtitle = "Change photos, bio, interests and age",
                    onClick = { showEditProfileDialog = true },
                    testTag = "menu_edit_profile"
                )

                HorizontalDivider(color = LoveraCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 8.dp))

                MineMenuItem(
                    icon = Icons.Default.Shield,
                    iconTint = CoinGold,
                    title = "Suspend Paid Services & Terminate",
                    subtitle = "Manage VIP subscription, privacy & account deletion",
                    onClick = onOpenSafetySettings,
                    testTag = "menu_safety_settings"
                )

                HorizontalDivider(color = LoveraCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 8.dp))

                MineMenuItem(
                    icon = Icons.Default.Logout,
                    iconTint = DangerRed,
                    title = "Log Out",
                    subtitle = "Sign out of this session",
                    onClick = { viewModel.logout() },
                    testTag = "menu_logout"
                )
            }
        }

        Spacer(modifier = Modifier.height(72.dp))
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            user = user,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, age, gender, bio, occupation, loc, interests ->
                viewModel.updateProfile(name, age, gender, bio, occupation, loc, interests)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
fun StatCounter(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, color = TextMuted, fontSize = 11.sp)
    }
}

@Composable
fun MineMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
        }

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
    }
}
