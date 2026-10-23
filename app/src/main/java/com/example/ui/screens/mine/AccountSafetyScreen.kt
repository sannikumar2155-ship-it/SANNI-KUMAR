package com.example.ui.screens.mine

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSafetyScreen(
    viewModel: LoveraViewModel,
    currentUser: UserProfile,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSuspendConfirmDialog by remember { mutableStateOf(false) }
    var showTerminateConfirmDialog by remember { mutableStateOf(false) }

    var ghostMode by remember { mutableStateOf(currentUser.ghostMode) }
    var hideOnline by remember { mutableStateOf(currentUser.hideOnline) }
    var incognitoParty by remember { mutableStateOf(currentUser.incognitoParty) }

    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Safety & Services Control",
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LoveraDeepNight)
            )
        },
        containerColor = LoveraDeepNight,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Paid Services & Subscriptions Control ("suspend paid services")
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LoveraSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LoveraCardBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = CoinGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Paid Services Management",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Current Status: " + if (currentUser.paidServiceActive) "Active (${currentUser.subscriptionPlan ?: "VIP Member"})" else "Suspended / No Active Plan",
                        color = if (currentUser.paidServiceActive) SuccessGreen else DangerRed,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )

                    if (currentUser.subscriptionRenewalDate != null && currentUser.paidServiceActive) {
                        Text(
                            text = "Next Billing Date: ${currentUser.subscriptionRenewalDate}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (currentUser.paidServiceActive) {
                        Button(
                            onClick = { viewModel.suspendPaidServices() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LoveraSurfaceHighlight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("suspend_paid_services_button")
                        ) {
                            Icon(Icons.Default.PauseCircle, contentDescription = null, tint = DangerRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Suspend Paid Services (Cancel Auto-Renew)", color = DangerRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.resumePaidServices() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("resume_paid_services_button")
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Resume Paid Services", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Section 2: Privacy & Online Visibility
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LoveraSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LoveraCardBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = ElectricVioletLight, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Privacy & Visibility",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ghost Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Ghost Mode", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Browse profiles without showing up in their visitors list", color = TextMuted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = ghostMode,
                            onCheckedChange = {
                                ghostMode = it
                                viewModel.updatePrivacySettings(ghostMode, hideOnline, incognitoParty)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = PassionPink, checkedTrackColor = PassionPink.copy(alpha = 0.5f))
                        )
                    }

                    HorizontalDivider(color = LoveraCardBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Hide Online
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Hide Online Status", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Do not show green active indicator to other users", color = TextMuted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = hideOnline,
                            onCheckedChange = {
                                hideOnline = it
                                viewModel.updatePrivacySettings(ghostMode, hideOnline, incognitoParty)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = PassionPink, checkedTrackColor = PassionPink.copy(alpha = 0.5f))
                        )
                    }

                    HorizontalDivider(color = LoveraCardBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Incognito Party Room
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Incognito Party Room", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Suppress entrance car announcements when entering voice rooms", color = TextMuted, fontSize = 12.sp)
                        }
                        Switch(
                            checked = incognitoParty,
                            onCheckedChange = {
                                incognitoParty = it
                                viewModel.updatePrivacySettings(ghostMode, hideOnline, incognitoParty)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = PassionPink, checkedTrackColor = PassionPink.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            // Section 3: Suspend Account Temporarily
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LoveraSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LoveraCardBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Pause, contentDescription = null, tint = CoinGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Temporary Account Suspension",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Need a break? Suspend your account temporarily. Your profile and party rooms will be hidden from everyone, but your coins balance (${currentUser.coins}) and unlocked items are preserved.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { showSuspendConfirmDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CoinGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("suspend_account_button")
                    ) {
                        Text("Suspend Account Temporarily", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Section 4: Terminating Services & Account Deletion ("terminating services")
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LoveraSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DangerRed.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, tint = DangerRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Terminate Services & Delete Account",
                            color = DangerRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Permanent Termination: This will erase your matches, delete conversation history, void remaining app currency (${currentUser.coins} Coins & ${currentUser.diamonds} Diamonds), terminate all virtual items, and cancel recurring paid subscriptions immediately.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showTerminateConfirmDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("terminate_account_button")
                    ) {
                        Text("Terminate All Services & Erase Account", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Suspend Confirmation Dialog
    if (showSuspendConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSuspendConfirmDialog = false },
            title = { Text("Suspend Account?", fontWeight = FontWeight.Bold, color = TextWhite) },
            text = {
                Text(
                    "Your profile will be hidden from matching and voice party rooms. You can reactivate anytime by simply logging back in.",
                    color = TextMuted
                )
            },
            containerColor = LoveraSurface,
            confirmButton = {
                Button(
                    onClick = {
                        showSuspendConfirmDialog = false
                        viewModel.suspendAccountTemporarily()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoinGold)
                ) {
                    Text("Yes, Suspend Account", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuspendConfirmDialog = false }) {
                    Text("Cancel", color = TextWhite)
                }
            }
        )
    }

    // Terminate Confirmation Dialog
    if (showTerminateConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showTerminateConfirmDialog = false },
            title = { Text("Confirm Termination?", fontWeight = FontWeight.Bold, color = DangerRed) },
            text = {
                Text(
                    "Warning: This action is irreversible. All virtual items, coins balance, party room host privileges, and paid subscriptions will be wiped immediately.",
                    color = TextMuted
                )
            },
            containerColor = LoveraSurface,
            confirmButton = {
                Button(
                    onClick = {
                        showTerminateConfirmDialog = false
                        viewModel.terminateAccountAndServices()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Terminate & Erase", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTerminateConfirmDialog = false }) {
                    Text("Cancel", color = TextWhite)
                }
            }
        )
    }
}
