package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.ui.GharFixViewModel
import com.example.ui.components.BrandLogoLayout
import com.example.ui.components.BrandTheme
import com.example.ui.components.GharFixAboutCard
import com.example.ui.components.GharFixAuthModal
import com.example.ui.components.GharFixLogo
import com.example.ui.components.LogoSize
import com.example.ui.model.AppRole
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
fun CustomerProfileAndSupportScreen(
    viewModel: GharFixViewModel,
    onRoleSwitchClick: () -> Unit
) {
    val location by viewModel.selectedLocation.collectAsState()
    val settings by viewModel.platformSettings.collectAsState()

    var showAuthDialog by remember { mutableStateOf(false) }
    var showTicketDialog by remember { mutableStateOf(false) }
    var ticketSubject by remember { mutableStateOf("") }
    var ticketMessage by remember { mutableStateOf("") }

    var expandedFaqIndex by remember { mutableStateOf<Int?>(null) }

    val faqs = listOf(
        Pair("What makes GharFix professionals trustworthy in Chandrapur?", "Every professional on GharFix undergoes 100% in-person background checks, Aadhaar verification, and skill assessment before onboarding."),
        Pair("How does the 30-Day GharFix Service Protection work?", "If you face any issues with the repair within 30 days of completion, our verified technician will re-inspect and fix it for free."),
        Pair("What is the difference between Fixed Price and Get Quote?", "Fixed Price is for quick standardized repairs (e.g. Fan repair ₹199, AC jet wash ₹499) with instant confirmation. Get Quote is for large custom projects (e.g. Solar 5kW, house wiring) where local pros send itemized bids for you to compare."),
        Pair("Are there any extra hidden charges?", "None. All base labor charges and taxes are displayed upfront. If additional parts or wiring are required on-site, the technician must record them transparently in the app before billing.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_customer_profile"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(GharFixTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GharFixTealDark, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Sunil Sharma", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GharFixTextPrimary)
                                Text(text = "+91 98220 12345 • sunil@example.com", fontSize = 12.sp, color = GharFixTextSecondary)
                                Surface(shape = RoundedCornerShape(4.dp), color = GharFixEmeraldContainer, modifier = Modifier.padding(top = 2.dp)) {
                                    Text("Verified Customer ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GharFixEmerald, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }

                        IconButton(onClick = { showAuthDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = GharFixTeal)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = GharFixCardStroke)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Saved Address: Plot 42, Anand Nagar, ${location.area}, ${location.city} - ${location.pincode}",
                            fontSize = 12.sp,
                            color = GharFixTextSecondary
                        )
                    }
                }
            }
        }

        // Role Switcher Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRoleSwitchClick() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GharFixAmberContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = GharFixAmberDark)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Switch Application Role", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF78350F))
                            Text(text = "Test Customer, Provider, and Admin dashboards", fontSize = 11.sp, color = Color(0xFF92400E))
                        }
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = GharFixAmberDark) {
                        Text("Switch", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                    }
                }
            }
        }

        // Support & Helpline Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Help & Support in Chandrapur", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GharFixNavy)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.showMessage("Connecting helpline call to ${settings?.helplineNumber ?: "+91 7172 250000"}...") },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Helpline", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { showTicketDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.HeadsetMic, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Raise Ticket", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // About GharFix Startup Brand Card
        item {
            GharFixAboutCard(cityName = location.city)
        }

        // FAQs Accordion
        item {
            Text(text = "Frequently Asked Questions", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GharFixNavy)
        }

        items(faqs.indices.toList()) { index ->
            val (q, a) = faqs[index]
            val isExpanded = expandedFaqIndex == index

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedFaqIndex = if (isExpanded) null else index },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = q, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GharFixTextPrimary, modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = GharFixTextSecondary
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = GharFixCardStroke.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = a, fontSize = 12.sp, color = GharFixTextSecondary, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }

    // Support Ticket Dialog
    if (showTicketDialog) {
        AlertDialog(
            onDismissRequest = { showTicketDialog = false },
            title = { Text("Raise Support Request", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ticketSubject,
                        onValueChange = { ticketSubject = it },
                        label = { Text("Issue Subject (e.g. Technician delayed)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ticketMessage,
                        onValueChange = { ticketMessage = it },
                        label = { Text("Details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (ticketSubject.isNotBlank()) {
                            viewModel.raiseSupportTicket(ticketSubject, ticketMessage.ifBlank { "Need help with booking" })
                        }
                        showTicketDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal)
                ) {
                    Text("Submit Ticket")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTicketDialog = false }) { Text("Cancel") }
            }
        )
    }

    // OTP / Mobile Auth Dialog
    if (showAuthDialog) {
        GharFixAuthModal(
            onDismiss = { showAuthDialog = false },
            onSuccessLogin = { mobile ->
                viewModel.showMessage("Successfully verified +91 $mobile! Welcome to GharFix.")
                showAuthDialog = false
            }
        )
    }
}
