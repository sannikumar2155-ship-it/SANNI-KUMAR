package com.example.ui.screens.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import com.example.data.MockData
import com.example.model.UserProfile
import com.example.model.VipTier
import com.example.model.VirtualItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreBottomSheet(
    viewModel: LoveraViewModel,
    currentUser: UserProfile,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val ownedFrames by viewModel.ownedFrames.collectAsState()
    val ownedRides by viewModel.ownedRides.collectAsState()

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
            // Header & User Coins Wallet
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Store & Virtual Items",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text("Top up coins, unlock frames & VIP status", color = TextMuted, fontSize = 12.sp)
                }

                // Balance
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(LoveraSurfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "🪙", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentUser.coins}",
                        color = CoinGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tabs: Coins / Virtual Items / VIP
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = LoveraSurfaceVariant,
                contentColor = PassionPink,
                modifier = Modifier.clip(RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Coins Packs 🪙", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Virtual Items 👑", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("VIP Club ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // Coin Packages
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.heightIn(max = 380.dp)
                    ) {
                        items(MockData.coinPackages) { pkg ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = LoveraSurfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🪙", fontSize = 28.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "${pkg.coins} Coins",
                                                    color = TextWhite,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                )
                                                if (pkg.bonusCoins > 0) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(PassionPink)
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text("+${pkg.bonusCoins}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                            if (pkg.tag != null) {
                                                Text(pkg.tag, color = CoinGold, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                            }
                                        }
                                    }

                                    Button(
                                        onClick = { viewModel.buyCoins(pkg) },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                                        modifier = Modifier.testTag("buy_coins_${pkg.id}")
                                    ) {
                                        Text(pkg.priceUsd, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Virtual Items (Frames & Entrance Rides)
                    val allItems = MockData.avatarFrames + MockData.entranceRides
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.heightIn(max = 380.dp)
                    ) {
                        items(allItems) { item ->
                            val isOwned = ownedFrames.contains(item.id) || ownedRides.contains(item.id)
                            val isEquipped = currentUser.activeFrameId == item.id || currentUser.activeRideId == item.id

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = LoveraSurfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.radialGradient(
                                                        listOf(Color(item.previewGradientStart), Color(item.previewGradientEnd))
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = item.iconEmoji, fontSize = 22.sp)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = item.name,
                                                color = TextWhite,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = item.description,
                                                color = TextMuted,
                                                fontSize = 11.sp,
                                                maxLines = 1
                                            )
                                            if (!isOwned) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "🪙", fontSize = 11.sp)
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(
                                                        text = "${item.priceCoins} Coins",
                                                        color = CoinGold,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (isEquipped) {
                                        OutlinedButton(
                                            onClick = { },
                                            shape = RoundedCornerShape(12.dp),
                                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(SuccessGreen, SuccessGreen)))
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Equipped", color = SuccessGreen, fontSize = 12.sp)
                                        }
                                    } else if (isOwned) {
                                        Button(
                                            onClick = {
                                                if (item.type == com.example.model.VirtualItemType.AVATAR_FRAME) {
                                                    viewModel.equipAvatarFrame(item.id)
                                                } else {
                                                    viewModel.buyAndEquipItem(item)
                                                }
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                                        ) {
                                            Text("Equip", fontSize = 12.sp, color = Color.White)
                                        }
                                    } else {
                                        Button(
                                            onClick = { viewModel.buyAndEquipItem(item) },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                                            modifier = Modifier.testTag("buy_item_${item.id}")
                                        ) {
                                            Text("Buy", fontSize = 12.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // VIP Subscriptions
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(
                            Triple(VipTier.SILVER, "$4.99 / mo", listOf("Silver badge", "5 Free Super Likes / day", "Stealth visitor mode")),
                            Triple(VipTier.GOLD, "$9.99 / mo", listOf("Gold Crown badge", "Unlimited Likes", "Custom Entrance Ride", "Access to VIP Lounges")),
                            Triple(VipTier.DIAMOND, "$19.99 / mo", listOf("Diamond Halo badge", "Top Star discovery boost", "Free 500 Coins monthly", "Private Party Room encryption"))
                        ).forEach { (tier, price, perks) ->
                            val isCurrentTier = currentUser.vipTier == tier && currentUser.paidServiceActive

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = LoveraSurfaceVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (isCurrentTier) Modifier.border(2.dp, Color(tier.badgeColor), RoundedCornerShape(16.dp))
                                        else Modifier
                                    )
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = tier.title,
                                                color = Color(tier.badgeColor),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Text(price, color = TextWhite, fontSize = 13.sp)
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.toggleVipSubscription(tier, !isCurrentTier)
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isCurrentTier) DangerRed else PassionPink
                                            ),
                                            modifier = Modifier.testTag("vip_action_${tier.name}")
                                        ) {
                                            Text(
                                                text = if (isCurrentTier) "Cancel VIP" else "Join VIP",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    perks.forEach { perk ->
                                        Text(
                                            text = "• $perk",
                                            color = TextMuted,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
