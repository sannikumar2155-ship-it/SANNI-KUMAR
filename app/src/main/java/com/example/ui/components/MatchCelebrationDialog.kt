package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun MatchCelebrationDialog(
    matchedUser: UserProfile,
    currentUser: UserProfile,
    onSendMessage: () -> Unit,
    onKeepSwiping: () -> Unit
) {
    Dialog(
        onDismissRequest = onKeepSwiping,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LoveraDeepNight.copy(alpha = 0.95f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Text(
                    text = "🎉 IT'S A MATCH! 🎉",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PassionPink,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "You and ${matchedUser.name} liked each other! Break the ice or invite them to a voice party.",
                    color = TextMuted,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Overlapping avatar circles
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarWithFrame(
                        user = currentUser,
                        size = 110.dp,
                        showOnlineDot = false
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = (-16).dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(PassionPink, ElectricViolet))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "❤️", fontSize = 18.sp)
                    }
                    Box(modifier = Modifier.offset(x = (-32).dp)) {
                        AvatarWithFrame(
                            user = matchedUser,
                            size = 110.dp,
                            showOnlineDot = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = onSendMessage,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PassionPink),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("match_send_message_button")
                ) {
                    Text(
                        text = "Say Hi to ${matchedUser.name} 💬",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onKeepSwiping,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Keep Swiping", fontSize = 15.sp)
                }
            }
        }
    }
}
