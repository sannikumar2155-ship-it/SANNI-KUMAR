package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.model.VipTier
import com.example.ui.theme.*

@Composable
fun AvatarWithFrame(
    user: UserProfile,
    size: Dp = 56.dp,
    showOnlineDot: Boolean = true,
    isSpeaking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speakingPulse"
    )

    val frameBorder = when (user.activeFrameId) {
        "frame_neon_heart" -> Brush.sweepGradient(listOf(PassionPink, PassionPinkLight, Color.White, PassionPink))
        "frame_royal_crown" -> Brush.sweepGradient(listOf(CoinGold, CoinGoldLight, Color.White, CoinGold))
        "frame_cyber_dragon" -> Brush.sweepGradient(listOf(DiamondCyan, ElectricViolet, DiamondCyanDark, DiamondCyan))
        else -> null
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(if (isSpeaking) size * pulseScale else size)
    ) {
        // Speaking ring ripple
        if (isSpeaking) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(PassionPink.copy(alpha = 0.25f))
            )
        }

        // Frame / Avatar Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .then(
                    if (frameBorder != null) {
                        Modifier
                            .border(3.dp, frameBorder, CircleShape)
                            .padding(3.dp)
                    } else if (user.vipTier != VipTier.NONE) {
                        Modifier
                            .border(2.dp, Color(user.vipTier.badgeColor), CircleShape)
                            .padding(2.dp)
                    } else {
                        Modifier
                    }
                )
                .clip(CircleShape)
                .background(Color(user.avatarColor))
        ) {
            Text(
                text = user.avatarInitial,
                color = Color.White,
                fontSize = (size.value * 0.42f).sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Online status dot
        if (showOnlineDot && user.isOnline && !user.hideOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.26f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(SuccessGreen)
                    .border(2.dp, LoveraDeepNight, CircleShape)
            )
        }

        // Verified check
        if (user.verified && !showOnlineDot) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified",
                tint = DiamondCyan,
                modifier = Modifier
                    .size(size * 0.32f)
                    .align(Alignment.BottomEnd)
                    .background(LoveraDeepNight, CircleShape)
            )
        }
    }
}
