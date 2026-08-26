package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookingEntity
import com.example.data.local.QuoteEntity
import com.example.ui.GharFixViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GharFixAmber
import com.example.ui.theme.GharFixAmberContainer
import com.example.ui.theme.GharFixAmberDark
import com.example.ui.theme.GharFixBackground
import com.example.ui.theme.GharFixCardStroke
import com.example.ui.theme.GharFixEmerald
import com.example.ui.theme.GharFixEmeraldContainer
import com.example.ui.theme.GharFixNavy
import com.example.ui.theme.GharFixTeal
import com.example.ui.theme.GharFixTealContainer
import com.example.ui.theme.GharFixTealDark
import com.example.ui.theme.GharFixTextMuted
import com.example.ui.theme.GharFixTextPrimary
import com.example.ui.theme.GharFixTextSecondary

@Composable
fun CustomerQuotesScreen(
    viewModel: GharFixViewModel,
    onViewBooking: (String) -> Unit,
    onChatClick: (String) -> Unit
) {
    val allBookings by viewModel.allBookings.collectAsState()
    val quoteBookings = allBookings.filter { it.bookingType == "GET_QUOTE" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_customer_quotes"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GharFixAmberContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.RequestQuote,
                        contentDescription = null,
                        tint = GharFixAmberDark,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Quotation Comparison Hub",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = "Compare transparent labour & material bids from verified Chandrapur pros",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }

        if (quoteBookings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.RequestQuote, contentDescription = null, tint = GharFixTextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No open quote requests",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = GharFixTextSecondary
                        )
                    }
                }
            }
        } else {
            items(quoteBookings) { booking ->
                QuoteBookingGroupCard(
                    booking = booking,
                    viewModel = viewModel,
                    onViewBooking = { onViewBooking(booking.id) },
                    onChatClick = { onChatClick(booking.id) }
                )
            }
        }
    }
}

@Composable
fun QuoteBookingGroupCard(
    booking: BookingEntity,
    viewModel: GharFixViewModel,
    onViewBooking: () -> Unit,
    onChatClick: () -> Unit
) {
    val quotesFlow = viewModel.getQuotesForBooking(booking.id)
    val quotes by quotesFlow.collectAsState(initial = emptyList())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.bookingNumber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTealDark
                    )
                    Text(
                        text = booking.serviceName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary
                    )
                }
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Requirement: ${booking.problemDescription}",
                fontSize = 12.sp,
                color = GharFixTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Quotations Received (${quotes.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (quotes.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GharFixBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Broadcasting to local Chandrapur pros... quotes will appear here shortly.",
                        fontSize = 12.sp,
                        color = GharFixTextMuted,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    quotes.forEach { quote ->
                        val isAccepted = quote.status == "ACCEPTED" || booking.providerId == quote.providerId
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isAccepted) GharFixEmeraldContainer.copy(alpha = 0.5f) else GharFixBackground,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isAccepted) GharFixEmerald else GharFixCardStroke
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Engineering, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = quote.providerName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = GharFixTextPrimary
                                        )
                                    }
                                    Text(
                                        text = "₹${quote.totalPrice.toInt()}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GharFixTealDark
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(text = "Labour: ₹${quote.labourCost.toInt()}", fontSize = 11.sp, color = GharFixTextSecondary)
                                    Text(text = "Material: ₹${quote.materialCost.toInt()}", fontSize = 11.sp, color = GharFixTextSecondary)
                                    Text(text = "Est. Time: ${quote.estimatedDuration}", fontSize = 11.sp, color = GharFixAmberDark, fontWeight = FontWeight.SemiBold)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = quote.notes,
                                    fontSize = 11.sp,
                                    color = GharFixTextPrimary,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                if (isAccepted) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GharFixEmerald
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Quote Accepted ✓", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else if (booking.status == "PENDING") {
                                    Button(
                                        onClick = { viewModel.acceptQuote(booking.id, quote) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Text("Accept This Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
