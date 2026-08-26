package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import com.example.ui.GharFixViewModel
import com.example.ui.model.AppRole
import com.example.ui.theme.GharFixBackground
import com.example.ui.theme.GharFixCardStroke
import com.example.ui.theme.GharFixEmerald
import com.example.ui.theme.GharFixNavy
import com.example.ui.theme.GharFixTeal
import com.example.ui.theme.GharFixTealContainer
import com.example.ui.theme.GharFixTealDark
import com.example.ui.theme.GharFixTextMuted
import com.example.ui.theme.GharFixTextPrimary
import com.example.ui.theme.GharFixTextSecondary

@Composable
fun ChatScreen(
    bookingId: String,
    viewModel: GharFixViewModel,
    onBack: () -> Unit
) {
    val allBookings by viewModel.allBookings.collectAsState()
    val booking = allBookings.find { it.id == bookingId }
    val currentRole by viewModel.currentRole.collectAsState()

    val messagesFlow = viewModel.getMessagesForBooking(bookingId)
    val messages by messagesFlow.collectAsState(initial = emptyList())

    var messageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickChips = if (currentRole == AppRole.PROVIDER) {
        listOf(
            "I have reached the location",
            "Please share flat/door number",
            "Starting inspection now",
            "Job is completed, please share OTP"
        )
    } else {
        listOf(
            "Where have you reached?",
            "Door bell is working, 1st floor",
            "Please bring extra wiring/tape",
            "I have shared the OTP"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_chat")
    ) {
        // Chat Header with Contact Info
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GharFixTealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (currentRole == AppRole.PROVIDER) Icons.Default.Person else Icons.Default.Engineering,
                            contentDescription = null,
                            tint = GharFixTealDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (currentRole == AppRole.PROVIDER) (booking?.customerName ?: "Customer") else (booking?.providerName ?: "Technician"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = GharFixEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "${booking?.serviceName} • Booking ${booking?.bookingNumber}",
                            fontSize = 11.sp,
                            color = GharFixTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GharFixTealContainer,
                    modifier = Modifier.clickable {
                        viewModel.showMessage("Connecting call to +91 7172 250000...")
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = GharFixTealDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GharFixTealDark)
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isMe = when (currentRole) {
                    AppRole.PROVIDER -> msg.senderRole == "PROVIDER"
                    AppRole.ADMIN -> msg.senderRole == "ADMIN"
                    else -> msg.senderRole == "CUSTOMER"
                }

                ChatBubble(message = msg, isMe = isMe)
            }
        }

        // Quick Message Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(quickChips) { chip ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GharFixBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke),
                    modifier = Modifier.clickable {
                        viewModel.sendMessage(bookingId, chip)
                    }
                ) {
                    Text(
                        text = chip,
                        fontSize = 11.sp,
                        color = GharFixTextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Input Field Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    placeholder = { Text("Type a message...", fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_chat_message"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GharFixTeal,
                        unfocusedBorderColor = GharFixCardStroke,
                        focusedContainerColor = GharFixBackground,
                        unfocusedContainerColor = GharFixBackground
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageInput.isNotBlank()) {
                            viewModel.sendMessage(bookingId, messageInput)
                            messageInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(GharFixTeal)
                        .testTag("btn_send_message")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageEntity, isMe: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isMe) 14.dp else 2.dp,
                bottomEnd = if (isMe) 2.dp else 14.dp
            ),
            color = if (isMe) GharFixTeal else Color.White,
            border = if (!isMe) androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke) else null,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (!isMe) {
                    Text(
                        text = "${message.senderName} (${message.senderRole})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTealDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (isMe) Color.White else GharFixTextPrimary,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = message.timestampText,
                    fontSize = 9.sp,
                    color = if (isMe) Color.White.copy(alpha = 0.7f) else GharFixTextMuted,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
