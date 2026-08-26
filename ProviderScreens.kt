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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.data.local.BookingEntity
import com.example.data.local.ProviderEntity
import com.example.ui.GharFixViewModel
import com.example.ui.components.BrandLogoLayout
import com.example.ui.components.BrandTheme
import com.example.ui.components.GharFixLogo
import com.example.ui.components.LogoSize
import com.example.ui.components.StatusBadge
import com.example.ui.model.ProviderTab
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
fun ProviderDashboardScreen(
    viewModel: GharFixViewModel,
    onJobClick: (String) -> Unit,
    onChatClick: (String) -> Unit
) {
    val currentProviderId by viewModel.currentProviderId.collectAsState()
    val allProviders by viewModel.allProviders.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val settings by viewModel.platformSettings.collectAsState()

    val provider = allProviders.find { it.id == currentProviderId } ?: allProviders.firstOrNull()
    val providerBookings = allBookings.filter { it.providerId == currentProviderId || (it.status == "PENDING" && it.categoryName.equals(provider?.primaryCategory, true)) }

    val pendingRequests = allBookings.filter { it.status == "PENDING" }
    val activeJobs = providerBookings.filter { it.status in listOf("ACCEPTED", "ON_THE_WAY", "STARTED") }
    val completedJobs = providerBookings.filter { it.status == "COMPLETED" }

    var isOnline by remember { mutableStateOf(provider?.isOnline ?: true) }

    // Dialog state for adding extra work / completing job
    var activeJobForExtraWork by remember { mutableStateOf<BookingEntity?>(null) }
    var extraAmountInput by remember { mutableStateOf("") }
    var extraNotesInput by remember { mutableStateOf("") }

    var activeJobForCompletion by remember { mutableStateOf<BookingEntity?>(null) }
    var enteredOtp by remember { mutableStateOf("") }
    var completionPhotoNotes by remember { mutableStateOf("") }

    // Dialog for sending quote
    var quoteRequestBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var quoteLabourInput by remember { mutableStateOf("") }
    var quoteMaterialInput by remember { mutableStateOf("") }
    var quoteDurationInput by remember { mutableStateOf("1 Day") }
    var quoteNotesInput by remember { mutableStateOf("") }

    val totalGrossEarnings = completedJobs.sumOf { it.totalPrice }
    val commissionPercent = settings?.commissionPercentage ?: 15.0
    val netEarnings = totalGrossEarnings * (1.0 - commissionPercent / 100.0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_provider_dashboard"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Online / Offline Status Toggle Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOnline) GharFixNavy else Color(0xFF334155)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GharFixLogo(
                            size = LogoSize.SMALL,
                            layout = BrandLogoLayout.HORIZONTAL,
                            theme = BrandTheme.DARK,
                            showTagline = true
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = GharFixEmeraldContainer
                        ) {
                            Text(
                                text = "Partner Network",
                                color = GharFixEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (isOnline) GharFixEmerald else Color(0xFF94A3B8))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isOnline) "You are ONLINE" else "You are OFFLINE",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = if (isOnline)
                                    "Accepting live orders in ${provider?.serviceAreas ?: "Chandrapur"}"
                                else
                                    "Switch on to receive new job alerts in Chandrapur",
                                fontSize = 11.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }

                        Switch(
                            checked = isOnline,
                            onCheckedChange = {
                                isOnline = it
                                viewModel.toggleProviderOnline(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GharFixAmber,
                                checkedTrackColor = GharFixAmberContainer
                            ),
                            modifier = Modifier.testTag("switch_provider_online")
                        )
                    }
                }
            }
        }

        // Metrics Grid (4 Cards)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Active Jobs",
                        value = "${activeJobs.size}",
                        subtitle = "In progress",
                        icon = Icons.Default.Assignment,
                        color = GharFixTeal,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Net Earnings",
                        value = "₹${netEarnings.toInt()}",
                        subtitle = "After 15% comm.",
                        icon = Icons.Default.AccountBalanceWallet,
                        color = GharFixEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Rating",
                        value = "${provider?.rating ?: 4.9} ★",
                        subtitle = "${provider?.reviewCount ?: 28} reviews",
                        icon = Icons.Default.Star,
                        color = GharFixAmberDark,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Completed",
                        value = "${completedJobs.size}",
                        subtitle = "Total verified jobs",
                        icon = Icons.Default.CheckCircle,
                        color = GharFixBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 1. Pending Booking / Quote Requests in Chandrapur
        item {
            Text(
                text = "New Job Opportunities in Chandrapur (${pendingRequests.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )
        }

        if (pendingRequests.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No pending requests right now. New bookings in Chandrapur will appear here instantly.",
                        fontSize = 12.sp,
                        color = GharFixTextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(pendingRequests) { booking ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = booking.bookingNumber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixTealDark
                            )
                            StatusBadge(status = booking.status)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = booking.serviceName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharFixTextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = GharFixTextMuted, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${booking.area}, ${booking.city} • ${booking.scheduledDate} (${booking.scheduledTimeSlot})",
                                fontSize = 12.sp,
                                color = GharFixTextSecondary
                            )
                        }

                        if (booking.problemDescription.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Notes: ${booking.problemDescription}",
                                fontSize = 11.sp,
                                color = GharFixAmberDark
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = GharFixCardStroke)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (booking.bookingType == "GET_QUOTE") "Quotation Project" else "Payout: ₹${(booking.totalPrice * 0.85).toInt()}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixNavy
                            )

                            if (booking.bookingType == "GET_QUOTE") {
                                Button(
                                    onClick = {
                                        quoteRequestBooking = booking
                                        quoteLabourInput = ""
                                        quoteMaterialInput = ""
                                        quoteNotesInput = ""
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GharFixAmber),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text("Send Quotation", color = Color(0xFF451A03), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            } else {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            viewModel.updateBookingStatus(booking.id, "ACCEPTED")
                                            viewModel.showMessage("Job accepted! Customer notified.")
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Text("Accept Job", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Active Jobs Section (With Stage Actions)
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "My Ongoing Jobs (${activeJobs.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )
        }

        if (activeJobs.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No ongoing jobs at the moment.",
                        fontSize = 12.sp,
                        color = GharFixTextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(activeJobs) { job ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = job.customerName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GharFixTextPrimary
                                )
                                Text(
                                    text = "${job.serviceName} • ${job.area}",
                                    fontSize = 11.sp,
                                    color = GharFixTextSecondary
                                )
                            }
                            StatusBadge(status = job.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Address: ${job.customerAddress}",
                            fontSize = 12.sp,
                            color = GharFixTextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stage progression buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (job.status) {
                                "ACCEPTED" -> {
                                    Button(
                                        onClick = {
                                            viewModel.updateBookingStatus(job.id, "ON_THE_WAY")
                                            viewModel.showMessage("Marked On The Way. Customer notified.")
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GharFixBlue),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Mark On The Way", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                "ON_THE_WAY" -> {
                                    Button(
                                        onClick = {
                                            viewModel.startJob(job.id, "Technician arrived on site, started work inspection")
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF854D0E)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Start Job (Arrived)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                "STARTED" -> {
                                    OutlinedButton(
                                        onClick = {
                                            activeJobForExtraWork = job
                                            extraAmountInput = ""
                                            extraNotesInput = ""
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+ Add Extra Cost", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = {
                                            activeJobForCompletion = job
                                            enteredOtp = ""
                                            completionPhotoNotes = ""
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GharFixEmerald),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Complete Job (OTP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            IconButton(
                                onClick = { onChatClick(job.id) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GharFixTealContainer)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat", tint = GharFixTealDark)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Add Additional Work (During Service)
    if (activeJobForExtraWork != null) {
        val job = activeJobForExtraWork!!
        AlertDialog(
            onDismissRequest = { activeJobForExtraWork = null },
            title = { Text("Add Extra Material / Labour", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "If extra parts or wiring were required during work, enter the cost to update the customer invoice transparently.",
                        fontSize = 12.sp,
                        color = GharFixTextSecondary
                    )
                    OutlinedTextField(
                        value = extraAmountInput,
                        onValueChange = { extraAmountInput = it },
                        label = { Text("Additional Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = extraNotesInput,
                        onValueChange = { extraNotesInput = it },
                        label = { Text("Description (e.g. Extra 5m copper pipe)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = extraAmountInput.toDoubleOrNull() ?: 0.0
                        if (amount > 0) {
                            viewModel.addAdditionalWork(
                                bookingId = job.id,
                                extraAmount = amount,
                                extraNotes = extraNotesInput.ifBlank { "Additional materials used" }
                            )
                        }
                        activeJobForExtraWork = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal)
                ) {
                    Text("Add to Bill")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeJobForExtraWork = null }) { Text("Cancel") }
            }
        )
    }

    // Modal: Complete Job with OTP
    if (activeJobForCompletion != null) {
        val job = activeJobForCompletion!!
        AlertDialog(
            onDismissRequest = { activeJobForCompletion = null },
            title = { Text("Verify Completion OTP", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Ask customer for the 4-digit completion code shown on their tracking screen.",
                        fontSize = 12.sp,
                        color = GharFixTextSecondary
                    )
                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { enteredOtp = it },
                        label = { Text("4-digit OTP (e.g. ${job.completionOtp})") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = completionPhotoNotes,
                        onValueChange = { completionPhotoNotes = it },
                        label = { Text("Completion Notes / Evidence") },
                        placeholder = { Text("e.g. AC cooling verified, no leaks") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredOtp.trim() == job.completionOtp) {
                            viewModel.completeJob(
                                bookingId = job.id,
                                otp = enteredOtp.trim(),
                                completionNotes = completionPhotoNotes.ifBlank { "Service completed and verified" }
                            )
                            activeJobForCompletion = null
                        } else {
                            viewModel.showMessage("Invalid OTP! Please check code with customer.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixEmerald)
                ) {
                    Text("Verify & Complete Job")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeJobForCompletion = null }) { Text("Cancel") }
            }
        )
    }

    // Modal: Send Quotation Proposal
    if (quoteRequestBooking != null) {
        val req = quoteRequestBooking!!
        val labourVal = quoteLabourInput.toDoubleOrNull() ?: 0.0
        val materialVal = quoteMaterialInput.toDoubleOrNull() ?: 0.0
        val totalQuote = labourVal + materialVal
        val netProQuote = totalQuote * (1.0 - commissionPercent / 100.0)

        AlertDialog(
            onDismissRequest = { quoteRequestBooking = null },
            title = { Text("Send Custom Quotation", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Project: ${req.serviceName} (${req.area}, Chandrapur)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTealDark
                    )
                    Text(
                        text = "Requirement: ${req.problemDescription}",
                        fontSize = 11.sp,
                        color = GharFixTextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = quoteLabourInput,
                        onValueChange = { quoteLabourInput = it },
                        label = { Text("Labour Cost (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quoteMaterialInput,
                        onValueChange = { quoteMaterialInput = it },
                        label = { Text("Material Cost (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quoteDurationInput,
                        onValueChange = { quoteDurationInput = it },
                        label = { Text("Estimated Duration (e.g. 2 Days)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quoteNotesInput,
                        onValueChange = { quoteNotesInput = it },
                        label = { Text("Proposal Notes / Brand details") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GharFixTealContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Total Proposal: ₹${totalQuote.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GharFixTealDark)
                            Text(text = "Your Net Payout (after 15% fee): ₹${netProQuote.toInt()}", fontSize = 11.sp, color = GharFixTextSecondary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (totalQuote > 0) {
                            viewModel.sendQuote(
                                bookingId = req.id,
                                labourCost = labourVal,
                                materialCost = materialVal,
                                duration = quoteDurationInput.ifBlank { "1 Day" },
                                notes = quoteNotesInput.ifBlank { "Complete installation with warranty" }
                            )
                        }
                        quoteRequestBooking = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixAmber)
                ) {
                    Text("Submit Quote", color = Color(0xFF451A03), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { quoteRequestBooking = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = GharFixTextSecondary)
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = GharFixNavy)
            Text(text = subtitle, fontSize = 10.sp, color = GharFixTextMuted)
        }
    }
}

@Composable
fun ProviderEarningsScreen(viewModel: GharFixViewModel) {
    val allBookings by viewModel.allBookings.collectAsState()
    val currentProviderId by viewModel.currentProviderId.collectAsState()
    val settings by viewModel.platformSettings.collectAsState()

    val completedJobs = allBookings.filter { it.providerId == currentProviderId && it.status == "COMPLETED" }
    val commissionPercent = settings?.commissionPercentage ?: 15.0

    val gross = completedJobs.sumOf { it.totalPrice }
    val commission = gross * (commissionPercent / 100.0)
    val net = gross - commission

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_provider_earnings"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Balance Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GharFixNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Available Payout Balance", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "₹${net.toInt()}", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "UPI: suresh.chauhan@okhdfcbank (Auto-payout every Monday)", fontSize = 11.sp, color = GharFixAmber)
                }
            }
        }

        // Commission Breakdown Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Financial Summary", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Gross Customer Billing", fontSize = 13.sp, color = GharFixTextSecondary)
                        Text("₹${gross.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("GharFix Platform Fee ($commissionPercent%)", fontSize = 13.sp, color = GharFixRed)
                        Text("- ₹${commission.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GharFixRed)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GharFixCardStroke)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Net Provider Earnings", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("₹${net.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GharFixEmerald)
                    }
                }
            }
        }

        // Completed Jobs Settlement Log
        item {
            Text(text = "Job Settlement History (${completedJobs.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        items(completedJobs) { job ->
            val jobGross = job.totalPrice
            val jobNet = jobGross * (1.0 - commissionPercent / 100.0)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = job.serviceName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "${job.customerName} • ${job.area}", fontSize = 11.sp, color = GharFixTextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "+ ₹${jobNet.toInt()}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = GharFixEmerald)
                        Text(text = "Gross ₹${jobGross.toInt()}", fontSize = 10.sp, color = GharFixTextMuted)
                    }
                }
            }
        }
    }
}
