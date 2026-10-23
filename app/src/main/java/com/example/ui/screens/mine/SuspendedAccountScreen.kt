package com.example.ui.screens.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AccountStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

@Composable
fun SuspendedAccountScreen(
    viewModel: LoveraViewModel,
    accountStatus: AccountStatus,
    modifier: Modifier = Modifier
) {
    val isTemporary = accountStatus == AccountStatus.SUSPENDED_TEMPORARY

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LoveraDeepNight)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (isTemporary) CoinGold.copy(alpha = 0.2f) else DangerRed.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PauseCircle,
                    contentDescription = null,
                    tint = if (isTemporary) CoinGold else DangerRed,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isTemporary) "Account Temporarily Suspended" else "Services Terminated",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isTemporary) {
                    "Your profile and voice party rooms are currently paused and hidden from discovery. Your virtual items and coin balance are safe and waiting for you."
                } else {
                    "Your account, virtual items, app currency, and paid services have been successfully terminated per your request."
                },
                color = TextMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isTemporary) {
                Button(
                    onClick = { viewModel.reactivateAccount() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("reactivate_account_button")
                ) {
                    Text("Reactivate My Account Now 🚀", fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                Button(
                    onClick = { viewModel.loginAsGuest() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Create New Account", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
