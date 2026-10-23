package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AccountStatus
import com.example.ui.components.CallingDialog
import com.example.ui.components.MatchCelebrationDialog
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.chat.ChatListScreen
import com.example.ui.screens.chat.DirectChatScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.mine.AccountSafetyScreen
import com.example.ui.screens.mine.MineScreen
import com.example.ui.screens.mine.StoreBottomSheet
import com.example.ui.screens.mine.SuspendedAccountScreen
import com.example.ui.screens.party.ActivePartyRoomScreen
import com.example.ui.screens.party.PartyScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.LoveraViewModel

enum class LoveraTab(val title: String) {
    HOME("Home"),
    PARTY("Party Room"),
    CHAT("Chat"),
    MINE("Mine")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoveraTheme {
                LoveraApp()
            }
        }
    }
}

@Composable
fun LoveraApp(viewModel: LoveraViewModel = viewModel()) {
    val context = LocalContext.current
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val activeRoom by viewModel.activeRoom.collectAsState()
    val activeConversation by viewModel.activeConversation.collectAsState()
    val callingState by viewModel.callingState.collectAsState()
    val matchedUser by viewModel.matchedUser.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    var currentTab by remember { mutableStateOf(LoveraTab.HOME) }
    var showSafetySettings by remember { mutableStateOf(false) }
    var showStoreSheet by remember { mutableStateOf(false) }

    // Toast listener
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    if (!isLoggedIn) {
        AuthScreen(viewModel = viewModel)
        return
    }

    val user = currentUser
    if (user != null && user.accountStatus != AccountStatus.ACTIVE) {
        SuspendedAccountScreen(
            viewModel = viewModel,
            accountStatus = user.accountStatus
        )
        return
    }

    // Full screen sub-destinations
    if (showSafetySettings && user != null) {
        AccountSafetyScreen(
            viewModel = viewModel,
            currentUser = user,
            onBack = { showSafetySettings = false }
        )
        return
    }

    if (activeRoom != null) {
        ActivePartyRoomScreen(
            room = activeRoom!!,
            viewModel = viewModel,
            onTopUpClick = { showStoreSheet = true }
        )
        return
    }

    if (activeConversation != null) {
        DirectChatScreen(
            conversation = activeConversation!!,
            viewModel = viewModel,
            onBack = { viewModel.closeChat() },
            onTopUpClick = { showStoreSheet = true }
        )
        return
    }

    // Main 4-Tab Screen with Material 3 Scaffold and NavigationBar
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = LoveraSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("lovera_bottom_navigation")
            ) {
                // Tab 1: Home (Dating Discovery)
                NavigationBarItem(
                    selected = currentTab == LoveraTab.HOME,
                    onClick = { currentTab = LoveraTab.HOME },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == LoveraTab.HOME) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PassionPink,
                        selectedTextColor = PassionPink,
                        indicatorColor = PassionPink.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                // Tab 2: Party Room (Voice Rooms)
                NavigationBarItem(
                    selected = currentTab == LoveraTab.PARTY,
                    onClick = { currentTab = LoveraTab.PARTY },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == LoveraTab.PARTY) Icons.Filled.Mic else Icons.Outlined.Mic,
                            contentDescription = "Party Room"
                        )
                    },
                    label = { Text("Party Room", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricViolet,
                        selectedTextColor = ElectricViolet,
                        indicatorColor = ElectricViolet.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_party")
                )

                // Tab 3: Chat (Messages)
                NavigationBarItem(
                    selected = currentTab == LoveraTab.CHAT,
                    onClick = { currentTab = LoveraTab.CHAT },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == LoveraTab.CHAT) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Chat"
                        )
                    },
                    label = { Text("Chat", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DiamondCyan,
                        selectedTextColor = DiamondCyan,
                        indicatorColor = DiamondCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_chat")
                )

                // Tab 4: Mine (Profile & Services)
                NavigationBarItem(
                    selected = currentTab == LoveraTab.MINE,
                    onClick = { currentTab = LoveraTab.MINE },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == LoveraTab.MINE) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Mine"
                        )
                    },
                    label = { Text("Mine", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CoinGold,
                        selectedTextColor = CoinGold,
                        indicatorColor = CoinGold.copy(alpha = 0.15f),
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_tab_mine")
                )
            }
        },
        containerColor = LoveraDeepNight,
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                LoveraTab.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToChat = { otherUser ->
                            viewModel.openChat(otherUser)
                        },
                        onTopUpClick = { showStoreSheet = true }
                    )
                }
                LoveraTab.PARTY -> {
                    PartyScreen(viewModel = viewModel)
                }
                LoveraTab.CHAT -> {
                    ChatListScreen(
                        viewModel = viewModel,
                        onOpenChat = { otherUser ->
                            viewModel.openChat(otherUser)
                        }
                    )
                }
                LoveraTab.MINE -> {
                    MineScreen(
                        viewModel = viewModel,
                        onOpenSafetySettings = { showSafetySettings = true },
                        onOpenStore = { showStoreSheet = true }
                    )
                }
            }
        }
    }

    // Store & Virtual Items Sheet
    if (showStoreSheet && user != null) {
        StoreBottomSheet(
            viewModel = viewModel,
            currentUser = user,
            onDismiss = { showStoreSheet = false }
        )
    }

    // Call Modal Simulation
    callingState?.let { state ->
        CallingDialog(
            callingState = state,
            onEndCall = { viewModel.endCall() }
        )
    }

    // Match Celebration Dialog
    matchedUser?.let { matched ->
        if (user != null) {
            MatchCelebrationDialog(
                matchedUser = matched,
                currentUser = user,
                onSendMessage = {
                    viewModel.matchAndStartChat(matched)
                },
                onKeepSwiping = {
                    viewModel.dismissMatchDialog()
                }
            )
        }
    }
}
