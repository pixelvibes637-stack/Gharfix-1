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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookingEntity
import com.example.ui.GharFixViewModel
import com.example.ui.components.CategoryIcon
import com.example.ui.components.StatusBadge
import com.example.ui.model.AppScreen
import com.example.ui.theme.GharFixAmber
import com.example.ui.theme.GharFixAmberContainer
import com.example.ui.theme.GharFixAmberDark
import com.example.ui.theme.GharFixBackground
import com.example.ui.theme.GharFixBlue
import com.example.ui.theme.GharFixBlueContainer
import com.example.ui.theme.GharFixCardStroke
import com.example.ui.theme.GharFixEmerald
import com.example.ui.theme.GharFixEmeraldContainer
import com.example.ui.theme.GharFixNavy
import com.example.ui.theme.GharFixRed
import com.example.ui.theme.GharFixRedContainer
import com.example.ui.theme.GharFixTeal
import com.example.ui.theme.GharFixTealContainer
import com.example.ui.theme.GharFixTealDark
import com.example.ui.theme.GharFixTextMuted
import com.example.ui.theme.GharFixTextPrimary
import com.example.ui.theme.GharFixTextSecondary

@Composable
fun CustomerBookingsScreen(
    viewModel: GharFixViewModel,
    onBookingClick: (String) -> Unit,
    onChatClick: (String) -> Unit
) {
    val allBookings by viewModel.allBookings.collectAsState()
    val currentCustomerId by viewModel.currentCustomerId.collectAsState()

    val customerBookings = allBookings.filter { it.customerId == currentCustomerId }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Active, 1: Quotes, 2: History

    val activeBookings = customerBookings.filter { it.status !in listOf("COMPLETED", "CANCELLED") && it.bookingType == "FIXED" }
    val quoteBookings = customerBookings.filter { it.bookingType == "GET_QUOTE" }
    val pastBookings = customerBookings.filter { it.status in listOf("COMPLETED", "CANCELLED") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_customer_bookings")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = GharFixTeal,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GharFixTeal
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Active (${activeBookings.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.testTag("tab_active_bookings")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Quotes (${quoteBookings.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.testTag("tab_quotes_bookings")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        text = "History (${pastBookings.size})",
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.testTag("tab_past_bookings")
            )
        }

        val displayList = when (selectedTab) {
            0 -> activeBookings
            1 -> quoteBookings
            else -> pastBookings
        }

        if (displayList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (selectedTab == 1) Icons.Default.RequestQuote else Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = GharFixTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = when (selectedTab) {
                            0 -> "No active direct bookings in Chandrapur"
                            1 -> "No active quote requests"
                            else -> "No completed bookings yet"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextSecondary
                    )
                    Text(
                        text = "Book an Electrician, AC service, Solar quote or RO repair anytime",
                        fontSize = 12.sp,
                        color = GharFixTextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayList) { booking ->
                    BookingListItemCard(
                        booking = booking,
                        onClick = { onBookingClick(booking.id) },
                        onChatClick = { onChatClick(booking.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun BookingListItemCard(
    booking: BookingEntity,
    onClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("booking_card_${booking.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Booking # & Status Badge
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
                        text = if (booking.bookingType == "GET_QUOTE") "Quotation Request" else "Fixed Price",
                        fontSize = 10.sp,
                        color = GharFixTextMuted
                    )
                }
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service Name
            Text(
                text = booking.serviceName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = GharFixTextMuted, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${booking.scheduledDate} • ${booking.scheduledTimeSlot}",
                    fontSize = 12.sp,
                    color = GharFixTextSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = GharFixTextMuted, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${booking.area}, ${booking.city}",
                    fontSize = 12.sp,
                    color = GharFixTextSecondary
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = GharFixCardStroke)

            // Provider Info & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(GharFixTealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Engineering, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = booking.providerName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GharFixTextPrimary
                        )
                        if (booking.status in listOf("ON_THE_WAY", "STARTED")) {
                            Text(text = "Live on location", fontSize = 10.sp, color = GharFixEmerald, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (booking.totalPrice > 0) {
                        Text(
                            text = "₹${booking.totalPrice.toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = GharFixNavy
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onChatClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GharFixTealContainer)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Chat",
                            tint = GharFixTealDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingDetailTrackingScreen(
    bookingId: String,
    viewModel: GharFixViewModel,
    onChatClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val allBookings by viewModel.allBookings.collectAsState()
    val booking = allBookings.find { it.id == bookingId }

    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewRating by remember { mutableStateOf(5.0) }
    var reviewComment by remember { mutableStateOf("") }
    var showCancelDialog by remember { mutableStateOf(false) }

    if (booking == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GharFixTeal)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_booking_tracking"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Status Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (booking.status) {
                        "COMPLETED" -> GharFixNavy
                        "STARTED" -> Color(0xFF78350F)
                        "ON_THE_WAY" -> Color(0xFF1E3A8A)
                        "CANCELLED" -> Color(0xFF7F1D1D)
                        else -> GharFixTeal
                    }
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = booking.bookingNumber,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        StatusBadge(status = booking.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = booking.serviceName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Scheduled: ${booking.scheduledDate} (${booking.scheduledTimeSlot})",
                        fontSize = 12.sp,
                        color = Color(0xFFE2E8F0)
                    )
                }
            }
        }

        // Live Stepper Tracker
        item {
            LiveBookingStepper(status = booking.status)
        }

        // Completion OTP Card (Important for security!)
        if (booking.status in listOf("ACCEPTED", "ON_THE_WAY", "STARTED")) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GharFixAmberContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = GharFixAmberDark)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Completion Security OTP",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF78350F)
                                )
                                Text(
                                    text = "Share with pro ONLY after job completion",
                                    fontSize = 11.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = booking.completionOtp,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = GharFixAmberDark,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Provider Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Assigned Professional",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(GharFixTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Engineering, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = booking.providerName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GharFixTextPrimary
                                )
                                Text(
                                    text = "Verified Professional in Chandrapur",
                                    fontSize = 11.sp,
                                    color = GharFixEmerald,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onChatClick(booking.id) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Chat", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Service & Photo Notes (Before/After)
        if (booking.beforePhotoNotes.isNotBlank() || booking.completionPhotoNotes.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Work Evidence & Notes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharFixTextPrimary
                        )

                        if (booking.beforePhotoNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = GharFixAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Before Work: ${booking.beforePhotoNotes}", fontSize = 12.sp, color = GharFixTextSecondary)
                            }
                        }

                        if (booking.completionPhotoNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GharFixEmerald, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Completion Notes: ${booking.completionPhotoNotes}", fontSize = 12.sp, color = GharFixTextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // Transparent Bill Breakdown
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Payment & Invoice Breakdown",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (booking.basePrice > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Base Labour Charges", fontSize = 13.sp, color = GharFixTextSecondary)
                            Text("₹${booking.basePrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (booking.additionalWorkAmount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Additional Work (${booking.additionalWorkNotes})", fontSize = 13.sp, color = GharFixTextSecondary)
                            Text("+ ₹${booking.additionalWorkAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (booking.discountAmount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Coupon Discount (${booking.couponCode})", fontSize = 13.sp, color = GharFixEmerald)
                            Text("- ₹${booking.discountAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GharFixEmerald)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GharFixCardStroke)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Amount", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Payment Mode: ${booking.paymentMethod}", fontSize = 11.sp, color = GharFixTextMuted)
                        }
                        Text(
                            text = if (booking.totalPrice > 0) "₹${booking.totalPrice.toInt()}" else "Awaiting Quote",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = GharFixTeal
                        )
                    }
                }
            }
        }

        // Actions: Rate & Review (If Completed) / Cancel
        item {
            if (booking.status == "COMPLETED") {
                if (booking.customerReviewRating > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GharFixEmeraldContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(booking.customerReviewRating.toInt()) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = GharFixAmber, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Your Review", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GharFixEmerald)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "\"${booking.customerReviewComment}\"", fontSize = 12.sp, color = GharFixTextPrimary)
                        }
                    }
                } else {
                    Button(
                        onClick = { showReviewDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GharFixAmber)
                    ) {
                        Icon(Icons.Default.RateReview, contentDescription = null, tint = Color(0xFF451A03))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rate & Review Technician", color = Color(0xFF451A03), fontWeight = FontWeight.Bold)
                    }
                }
            } else if (booking.status in listOf("PENDING", "ACCEPTED")) {
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GharFixRed),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixRed)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null, tint = GharFixRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel Booking", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Review Dialog
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Rate & Review Service", fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("How was your experience with ${booking.providerName} in Chandrapur?")
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { reviewRating = star.toDouble() }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star star",
                                    tint = if (star <= reviewRating) GharFixAmber else GharFixTextMuted,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        placeholder = { Text("Write your feedback (e.g. Arrived on time, neat work)...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitReview(
                            bookingId = booking.id,
                            providerName = booking.providerName,
                            serviceName = booking.serviceName,
                            rating = reviewRating,
                            comment = reviewComment.ifBlank { "Great service!" }
                        )
                        showReviewDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal)
                ) {
                    Text("Submit Review")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Cancel Dialog
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Booking?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to cancel this booking? There is no cancellation fee.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateBookingStatus(booking.id, "CANCELLED")
                        showCancelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixRed)
                ) {
                    Text("Confirm Cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) { Text("Keep Booking") }
            }
        )
    }
}

@Composable
fun LiveBookingStepper(status: String) {
    val steps = listOf(
        Pair("PENDING", "Booking Received"),
        Pair("ACCEPTED", "Pro Assigned"),
        Pair("ON_THE_WAY", "On The Way"),
        Pair("STARTED", "Work In Progress"),
        Pair("COMPLETED", "Completed")
    )

    val currentStepIndex = when (status) {
        "PENDING" -> 0
        "ACCEPTED" -> 1
        "ON_THE_WAY" -> 2
        "STARTED" -> 3
        "COMPLETED" -> 4
        else -> 0
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Live Tracking Status",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixTextPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            steps.forEachIndexed { index, (stepKey, stepTitle) ->
                val isDone = index <= currentStepIndex
                val isCurrent = index == currentStepIndex

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isDone -> GharFixEmerald
                                    else -> GharFixBackground
                                }
                            )
                            .border(
                                1.5.dp,
                                if (isDone) GharFixEmerald else GharFixCardStroke,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = stepTitle,
                        fontSize = 13.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isCurrent -> GharFixTealDark
                            isDone -> GharFixTextPrimary
                            else -> GharFixTextMuted
                        }
                    )
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 11.dp, top = 2.dp, bottom = 2.dp)
                            .width(2.dp)
                            .height(14.dp)
                            .background(if (index < currentStepIndex) GharFixEmerald else GharFixCardStroke)
                    )
                }
            }
        }
    }
}
