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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.data.local.LocationEntity
import com.example.data.local.ProviderEntity
import com.example.data.local.ServiceEntity
import com.example.data.local.SupportTicketEntity
import com.example.ui.GharFixViewModel
import com.example.ui.components.BrandLogoLayout
import com.example.ui.components.BrandTheme
import com.example.ui.components.CategoryIcon
import com.example.ui.components.GharFixLogo
import com.example.ui.components.LogoSize
import com.example.ui.components.StatusBadge
import com.example.ui.model.AdminTab
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
fun AdminPanelScreen(
    viewModel: GharFixViewModel,
    onViewBooking: (String) -> Unit
) {
    val adminTab by viewModel.currentAdminTab.collectAsState()
    val allProviders by viewModel.allProviders.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val allServices by viewModel.allActiveServices.collectAsState()
    val locations by viewModel.locations.collectAsState()
    val tickets by viewModel.supportTickets.collectAsState()
    val settings by viewModel.platformSettings.collectAsState()

    when (adminTab) {
        AdminTab.DASHBOARD -> AdminOverviewSection(viewModel)
        AdminTab.PROVIDERS -> AdminProvidersVerificationSection(allProviders, viewModel)
        AdminTab.SERVICES -> AdminServicesManagementSection(allServices, viewModel)
        AdminTab.BOOKINGS -> AdminBookingsSupervisionSection(allBookings, onViewBooking)
        AdminTab.LOCATIONS -> AdminLocationsSection(locations, viewModel)
        AdminTab.SETTINGS -> AdminSettingsSection(settings, viewModel)
    }
}

@Composable
fun AdminOverviewSection(viewModel: GharFixViewModel) {
    val allProviders by viewModel.allProviders.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val settings by viewModel.platformSettings.collectAsState()

    val completedBookings = allBookings.filter { it.status == "COMPLETED" }
    val totalGross = completedBookings.sumOf { it.totalPrice }
    val commissionPercent = settings?.commissionPercentage ?: 15.0
    val platformRevenue = totalGross * (commissionPercent / 100.0)

    val pendingKyc = allProviders.filter { it.kycStatus == "PENDING" }
    val approvedProviders = allProviders.filter { it.kycStatus == "APPROVED" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("admin_overview_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GharFixNavy)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                            color = GharFixBlueContainer
                        ) {
                            Text(
                                text = "HQ Command",
                                color = GharFixBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Chandrapur Hub Operations & Service Telemetry",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Supervise operations, KYC verifications, service catalogue & platform take-rate.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // Key Metrics
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        title = "Platform Revenue",
                        value = "₹${platformRevenue.toInt()}",
                        subtitle = "From $commissionPercent% commission",
                        icon = Icons.Default.Percent,
                        color = GharFixEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Gross GMV",
                        value = "₹${totalGross.toInt()}",
                        subtitle = "${completedBookings.size} completed jobs",
                        icon = Icons.Default.Assignment,
                        color = GharFixTeal,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminStatCard(
                        title = "Verified Pros",
                        value = "${approvedProviders.size}",
                        subtitle = "Active in Chandrapur",
                        icon = Icons.Default.Verified,
                        color = GharFixBlue,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "KYC Pending",
                        value = "${pendingKyc.size}",
                        subtitle = "Requires review",
                        icon = Icons.Default.Security,
                        color = if (pendingKyc.isNotEmpty()) GharFixRed else GharFixTextMuted,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick KYC alert if pending
        if (pendingKyc.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GharFixAmberContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Action Required: ${pendingKyc.size} Pro(s) awaiting verification",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                            Text(
                                text = "Review documents and approve background check to enable them.",
                                fontSize = 11.sp,
                                color = Color(0xFF92400E)
                            )
                        }
                        Button(
                            onClick = { viewModel.setAdminTab(AdminTab.PROVIDERS) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GharFixAmberDark),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Review", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Recent Bookings Snapshot
        item {
            Text(
                text = "Recent Platform Bookings (${allBookings.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )
        }

        items(allBookings.take(5)) { booking ->
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
                        Text(text = booking.serviceName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "${booking.customerName} → ${booking.providerName} • ${booking.area}", fontSize = 11.sp, color = GharFixTextSecondary)
                    }
                    StatusBadge(status = booking.status)
                }
            }
        }
    }
}

@Composable
fun AdminProvidersVerificationSection(
    providers: List<ProviderEntity>,
    viewModel: GharFixViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("admin_providers_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Service Provider Verification & KYC",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )
            Text(
                text = "All professionals must be verified before receiving customer requests in Chandrapur",
                fontSize = 12.sp,
                color = GharFixTextSecondary
            )
        }

        items(providers) { pro ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GharFixTealContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Engineering, contentDescription = null, tint = GharFixTealDark)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = pro.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "${pro.primaryCategory} • ${pro.experienceYears} Years Exp", fontSize = 11.sp, color = GharFixTextSecondary)
                            }
                        }
                        StatusBadge(status = pro.kycStatus)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Aadhaar: ${pro.aadhaarNumber} • Coverage: ${pro.serviceAreas}",
                        fontSize = 12.sp,
                        color = GharFixTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (pro.kycStatus != "APPROVED") {
                            Button(
                                onClick = { viewModel.updateProviderKyc(pro.id, "APPROVED") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GharFixEmerald),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve Pro", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (pro.kycStatus != "REJECTED") {
                            OutlinedButton(
                                onClick = { viewModel.updateProviderKyc(pro.id, "REJECTED") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GharFixRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixRed),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reject / Suspend", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminServicesManagementSection(
    services: List<ServiceEntity>,
    viewModel: GharFixViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newServiceName by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Electrician") }
    var newPrice by remember { mutableStateOf("299") }
    var newDuration by remember { mutableStateOf("45 mins") }
    var isFixedType by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("admin_services_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Service Catalogue Manager", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GharFixNavy)
                    Text(text = "Configure pricing, inclusions and models", fontSize = 12.sp, color = GharFixTextSecondary)
                }
                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Service", fontSize = 12.sp)
                }
            }
        }

        items(services) { s ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GharFixTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            CategoryIcon(key = s.iconKey, modifier = Modifier.size(20.dp), tint = GharFixTeal)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = s.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = if (s.bookingType == "FIXED") "Fixed: ₹${s.basePrice.toInt()} (${s.unit})" else "Get Quote Model",
                                fontSize = 11.sp,
                                color = GharFixTealDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Switch(
                        checked = s.isActive,
                        onCheckedChange = { viewModel.toggleServiceActive(s.id, it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = GharFixTeal, checkedTrackColor = GharFixTealContainer)
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Service", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newServiceName,
                        onValueChange = { newServiceName = it },
                        label = { Text("Service Name (e.g. Inverter Wiring)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Category (Electrician, Solar, AC, etc.)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPrice,
                        onValueChange = { newPrice = it },
                        label = { Text("Base Price (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDuration,
                        onValueChange = { newDuration = it },
                        label = { Text("Est. Duration (e.g. 1 hour)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Model: ${if (isFixedType) "Fixed Price" else "Get Quote"}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Switch(checked = isFixedType, onCheckedChange = { isFixedType = it })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newServiceName.isNotBlank()) {
                            viewModel.addNewService(
                                name = newServiceName,
                                categoryId = newCategory.lowercase(),
                                basePrice = newPrice.toDoubleOrNull() ?: 299.0,
                                duration = newDuration,
                                isQuote = !isFixedType
                            )
                        }
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal)
                ) {
                    Text("Save Service")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun AdminBookingsSupervisionSection(
    bookings: List<com.example.data.local.BookingEntity>,
    onViewBooking: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("admin_bookings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(text = "All Platform Bookings (${bookings.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GharFixNavy)
        }

        items(bookings) { b ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewBooking(b.id) }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = b.bookingNumber, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GharFixTealDark)
                        StatusBadge(status = b.status)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = b.serviceName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Customer: ${b.customerName} • Pro: ${b.providerName}", fontSize = 11.sp, color = GharFixTextSecondary)
                    Text(text = "Area: ${b.area}, Chandrapur • ₹${b.totalPrice.toInt()}", fontSize = 11.sp, color = GharFixTextPrimary)
                }
            }
        }
    }
}

@Composable
fun AdminLocationsSection(
    locations: List<LocationEntity>,
    viewModel: GharFixViewModel
) {
    var showAddLocationDialog by remember { mutableStateOf(false) }
    var newCity by remember { mutableStateOf("Chandrapur") }
    var newArea by remember { mutableStateOf("") }
    var newPincode by remember { mutableStateOf("442401") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("admin_locations_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Service Areas & Hubs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GharFixNavy)
                    Text(text = "Coverage: India → Maharashtra → Chandrapur", fontSize = 12.sp, color = GharFixTextSecondary)
                }
                Button(
                    onClick = { showAddLocationDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Area", fontSize = 12.sp)
                }
            }
        }

        items(locations) { loc ->
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = loc.area, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            if (loc.isLaunchCity) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = GharFixEmeraldContainer) {
                                    Text("Active Launch Hub", color = GharFixEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                        }
                        Text(text = "${loc.city}, ${loc.state}, ${loc.country} • PIN: ${loc.pincode}", fontSize = 11.sp, color = GharFixTextSecondary)
                    }

                    Switch(
                        checked = loc.isActive,
                        onCheckedChange = { viewModel.toggleLocationActive(loc.id, it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = GharFixTeal, checkedTrackColor = GharFixTealContainer)
                    )
                }
            }
        }
    }

    if (showAddLocationDialog) {
        AlertDialog(
            onDismissRequest = { showAddLocationDialog = false },
            title = { Text("Add Service Area", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCity,
                        onValueChange = { newCity = it },
                        label = { Text("City (e.g. Chandrapur, Nagpur)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newArea,
                        onValueChange = { newArea = it },
                        label = { Text("Area / Colony Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPincode,
                        onValueChange = { newPincode = it },
                        label = { Text("Pincode") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newArea.isNotBlank()) {
                            viewModel.addNewLocation(
                                city = newCity.ifBlank { "Chandrapur" },
                                area = newArea,
                                pincode = newPincode.ifBlank { "442401" }
                            )
                        }
                        showAddLocationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal)
                ) {
                    Text("Add Area")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddLocationDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun AdminSettingsSection(
    settings: com.example.data.local.PlatformSettingsInfo?,
    viewModel: GharFixViewModel
) {
    var commissionInput by remember { mutableDoubleStateOf(settings?.commissionPercentage ?: 15.0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("admin_settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "Platform Settings & Commission", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GharFixNavy)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Platform Take-Rate (Commission)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Current: ${commissionInput.toInt()}% deducted from service provider payout", fontSize = 12.sp, color = GharFixTextSecondary)

                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = commissionInput.toFloat(),
                        onValueChange = { commissionInput = it.toDouble() },
                        valueRange = 5f..30f,
                        steps = 24,
                        colors = SliderDefaults.colors(thumbColor = GharFixTeal, activeTrackColor = GharFixTeal),
                        modifier = Modifier.testTag("slider_commission")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GharFixTealContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Split on ₹1,000 Booking:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Pro gets ₹${(1000 * (1.0 - commissionInput / 100.0)).toInt()} • Platform: ₹${(1000 * (commissionInput / 100.0)).toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixTealDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.updateCommissionRate(commissionInput)
                            viewModel.showMessage("Platform commission set to ${commissionInput.toInt()}%")
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GharFixNavy),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Commission Rate")
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "GharFix Hub Contacts", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Helpline: ${settings?.helplineNumber ?: "+91 7172 250000"}", fontSize = 12.sp, color = GharFixTextSecondary)
                    Text(text = "Support Email: ${settings?.supportEmail ?: "support@gharfix.in"}", fontSize = 12.sp, color = GharFixTextSecondary)
                    Text(text = "Headquarters: Civil Lines, Chandrapur 442401", fontSize = 12.sp, color = GharFixTextSecondary)
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
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
