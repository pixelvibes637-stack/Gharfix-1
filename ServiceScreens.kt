package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CouponEntity
import com.example.data.local.ProviderEntity
import com.example.data.local.ServiceEntity
import com.example.ui.GharFixViewModel
import com.example.ui.components.CategoryIcon
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllServicesScreen(
    viewModel: GharFixViewModel,
    initialCategoryId: String?,
    onServiceClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val allServices by viewModel.allActiveServices.collectAsState()

    var selectedCatId by remember { mutableStateOf(initialCategoryId) }
    var selectedTypeFilter by remember { mutableStateOf("ALL") } // ALL, FIXED, GET_QUOTE
    var searchQuery by remember { mutableStateOf(viewModel.searchQuery.value) }

    val filteredServices = allServices.filter { service ->
        (selectedCatId == null || service.categoryId == selectedCatId) &&
                (selectedTypeFilter == "ALL" || service.bookingType == selectedTypeFilter) &&
                (searchQuery.isBlank() || service.name.contains(searchQuery, ignoreCase = true) || service.description.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_all_services"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Search & Header
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search services in Chandrapur...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GharFixTeal) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GharFixTeal,
                        unfocusedBorderColor = GharFixCardStroke,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedCatId == null) GharFixTeal else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedCatId = null }
                        ) {
                            Text(
                                text = "All (${allServices.size})",
                                color = if (selectedCatId == null) Color.White else GharFixTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                    items(categories) { cat ->
                        val isSelected = selectedCatId == cat.id
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) GharFixTeal else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedCatId = cat.id }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CategoryIcon(
                                    key = cat.iconKey,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isSelected) Color.White else GharFixTeal
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = cat.name,
                                    color = if (isSelected) Color.White else GharFixTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Type Filter Chips: Fixed vs Get Quote
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf(
                        Pair("ALL", "All Types"),
                        Pair("FIXED", "⚡ Fixed Price"),
                        Pair("GET_QUOTE", "📋 Get Free Quote")
                    )
                    filters.forEach { (type, label) ->
                        val isSelected = selectedTypeFilter == type
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GharFixNavy else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) GharFixNavy else GharFixCardStroke),
                            modifier = Modifier.clickable { selectedTypeFilter = type }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else GharFixTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        // Service List
        items(filteredServices) { service ->
            ServiceFullItemCard(
                service = service,
                onClick = { onServiceClick(service.id) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (filteredServices.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GharFixTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No services matching your search",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceFullItemCard(
    service: ServiceEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
            .testTag("service_item_${service.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (service.bookingType == "FIXED") GharFixTealContainer else GharFixAmberContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                CategoryIcon(
                    key = service.iconKey,
                    modifier = Modifier.size(26.dp),
                    tint = if (service.bookingType == "FIXED") GharFixTealDark else GharFixAmberDark
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = service.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = service.description,
                    fontSize = 11.sp,
                    color = GharFixTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (service.bookingType == "FIXED") GharFixTealContainer else GharFixAmberContainer
                    ) {
                        Text(
                            text = if (service.bookingType == "FIXED") "Fixed: ₹${service.basePrice.toInt()}" else "Get Quote",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (service.bookingType == "FIXED") GharFixTealDark else GharFixAmberDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "• ${service.estimatedDuration}",
                        fontSize = 11.sp,
                        color = GharFixTextMuted
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GharFixAmber,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${service.rating} (${service.reviewCount})",
                            fontSize = 11.sp,
                            color = GharFixTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceDetailScreen(
    serviceId: String,
    viewModel: GharFixViewModel,
    onBookClick: (isQuote: Boolean) -> Unit,
    onBack: () -> Unit
) {
    val allServices by viewModel.allActiveServices.collectAsState()
    val service = allServices.find { it.id == serviceId }

    if (service == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GharFixTeal)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_service_detail"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (service.bookingType == "FIXED") GharFixNavy else Color(0xFF78350F)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryIcon(
                            key = service.iconKey,
                            modifier = Modifier.size(30.dp),
                            tint = GharFixTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = service.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${service.rating} (${service.reviewCount} reviews in Chandrapur)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = service.description,
                        fontSize = 13.sp,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Pricing Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (service.bookingType == "FIXED") "Service Price" else "Booking Model",
                            fontSize = 12.sp,
                            color = GharFixTextSecondary
                        )
                        Text(
                            text = if (service.bookingType == "FIXED") "₹${service.basePrice.toInt()}" else "Free Quotation Bidding",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = GharFixTeal
                        )
                        Text(
                            text = if (service.bookingType == "FIXED") service.unit else "Receive and compare local pro bids",
                            fontSize = 11.sp,
                            color = GharFixTextMuted
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GharFixEmeraldContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = GharFixEmerald,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "30-Day Warranty",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixEmerald
                            )
                        }
                    }
                }
            }
        }

        // What's Included & Excluded
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "What's Included",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    service.includedItems.split(",").forEach { item ->
                        if (item.isNotBlank()) {
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = GharFixEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.trim(),
                                    fontSize = 12.sp,
                                    color = GharFixTextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "What's Not Included",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    service.excludedItems.split(",").forEach { item ->
                        if (item.isNotBlank()) {
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = GharFixRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.trim(),
                                    fontSize = 12.sp,
                                    color = GharFixTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Bottom Sticky Bar CTA
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (service.bookingType == "FIXED") "₹${service.basePrice.toInt()}" else "Request Quotes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = GharFixNavy
                    )
                    Text(
                        text = if (service.bookingType == "FIXED") "Includes taxes" else "Free broadcast",
                        fontSize = 11.sp,
                        color = GharFixTextMuted
                    )
                }

                Button(
                    onClick = { onBookClick(service.bookingType == "GET_QUOTE") },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (service.bookingType == "FIXED") GharFixTeal else GharFixAmber
                    ),
                    modifier = Modifier.testTag("btn_service_detail_book")
                ) {
                    Text(
                        text = if (service.bookingType == "FIXED") "Proceed to Book" else "Get Quotes from Pros",
                        color = if (service.bookingType == "FIXED") Color.White else Color(0xFF451A03),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BookingFlowScreen(
    serviceId: String,
    isQuoteRequest: Boolean,
    viewModel: GharFixViewModel,
    onBack: () -> Unit
) {
    val allServices by viewModel.allActiveServices.collectAsState()
    val service = allServices.find { it.id == serviceId }
    val location by viewModel.selectedLocation.collectAsState()
    val approvedProviders by viewModel.approvedProviders.collectAsState()
    val coupons by viewModel.coupons.collectAsState()

    var selectedDate by remember { mutableStateOf("Today") }
    var selectedSlot by remember { mutableStateOf("11:00 AM - 01:00 PM") }
    var addressDetail by remember { mutableStateOf("Plot 42, Anand Nagar, Near Datta Mandir") }
    var problemDescription by remember { mutableStateOf("") }
    var couponCodeInput by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<CouponEntity?>(null) }
    var discountAmount by remember { mutableStateOf(0.0) }
    var selectedPaymentMethod by remember { mutableStateOf("UPI (Pay After Service)") }
    var selectedProvider by remember { mutableStateOf<ProviderEntity?>(null) }
    var uploadedPhotosCount by remember { mutableStateOf(if (isQuoteRequest) 2 else 0) }

    if (service == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GharFixTeal)
        }
        return
    }

    val basePrice = service.basePrice
    val finalPayable = (basePrice - discountAmount).coerceAtLeast(0.0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_booking_flow"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Summary Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = GharFixNavy)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryIcon(key = service.iconKey, modifier = Modifier.size(24.dp), tint = GharFixTeal)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = service.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isQuoteRequest) "Get Quote Request • Chandrapur" else "Fixed Price Booking • ₹${service.basePrice.toInt()}",
                            fontSize = 12.sp,
                            color = GharFixAmber
                        )
                    }
                }
            }
        }

        // 1. Date & Time Slot
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "1. Select Date & Time Slot", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Today", "Tomorrow", "Weekend").forEach { date ->
                            val isSelected = selectedDate == date
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) GharFixTeal else GharFixBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) GharFixTeal else GharFixCardStroke),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDate = date }
                            ) {
                                Text(
                                    text = date,
                                    color = if (isSelected) Color.White else GharFixTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val timeSlots = listOf(
                        "09:00 AM - 11:00 AM",
                        "11:00 AM - 01:00 PM",
                        "02:00 PM - 04:00 PM",
                        "04:00 PM - 06:00 PM",
                        "06:00 PM - 08:00 PM"
                    )

                    timeSlots.forEach { slot ->
                        val isSelected = selectedSlot == slot
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GharFixTealContainer else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) GharFixTeal else GharFixCardStroke.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSlot = slot }
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = slot,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) GharFixTealDark else GharFixTextPrimary
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Service Address in Chandrapur
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "2. Service Address in Chandrapur", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Area: ${location.area}, ${location.city} (${location.pincode})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GharFixTealDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = addressDetail,
                        onValueChange = { addressDetail = it },
                        label = { Text("House / Flat No., Landmark") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_address_detail"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }
        }

        // 3. Problem Notes & Photo Attachment Simulator
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isQuoteRequest) "3. Project Requirements & Photos" else "3. Additional Notes (Optional)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = problemDescription,
                        onValueChange = { problemDescription = it },
                        placeholder = {
                            Text(
                                if (isQuoteRequest)
                                    "Describe project scope (e.g. 5kW solar plant for 3-floor bungalow, roof area, brand preference)"
                                else
                                    "e.g. Fan is making humming sound, ceiling height is approx 10 ft"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .testTag("input_problem_desc"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Photo attachments row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = GharFixTextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$uploadedPhotosCount photo(s) attached",
                                fontSize = 12.sp,
                                color = GharFixTextSecondary
                            )
                        }

                        OutlinedButton(
                            onClick = { uploadedPhotosCount += 1 },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Photo", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 4. Provider Assignment (Fixed Price only)
        if (!isQuoteRequest) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Engineering, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "4. Assigned Verified Pro", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Option 1: Auto Assign
                        val isAuto = selectedProvider == null
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isAuto) GharFixTealContainer else GharFixBackground,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isAuto) GharFixTeal else GharFixCardStroke),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedProvider = null }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "⚡ GharFix Auto-Match (Recommended)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Matches the highest-rated pro closest to ${location.area}", fontSize = 11.sp, color = GharFixTextSecondary)
                                }
                                if (isAuto) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(16.dp))
                            }
                        }

                        // Specific Pros
                        approvedProviders.take(2).forEach { pro ->
                            val isChosen = selectedProvider?.id == pro.id
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChosen) GharFixTealContainer else GharFixBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isChosen) GharFixTeal else GharFixCardStroke),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedProvider = pro }
                                    .padding(top = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "${pro.name} (${pro.rating} ★)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = "${pro.experienceYears} yrs exp • ${pro.primaryCategory}", fontSize = 11.sp, color = GharFixTextSecondary)
                                    }
                                    if (isChosen) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Coupons & Promo code (Fixed Price)
        if (!isQuoteRequest) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Discount, contentDescription = null, tint = GharFixAmberDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "5. Offers & Coupons", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = couponCodeInput,
                                onValueChange = { couponCodeInput = it.uppercase() },
                                placeholder = { Text("Enter coupon (e.g. FIRSTFIX, GHARFIX50)", fontSize = 12.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_coupon_code"),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val matched = coupons.find { it.code.equals(couponCodeInput.trim(), true) }
                                    if (matched != null) {
                                        val disc = ((basePrice * matched.discountPercent) / 100.0).coerceAtMost(matched.maxDiscount)
                                        appliedCoupon = matched
                                        discountAmount = disc
                                        viewModel.showMessage("Coupon ${matched.code} applied! Saved ₹${disc.toInt()}")
                                    } else {
                                        viewModel.showMessage("Invalid coupon code")
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GharFixNavy)
                            ) {
                                Text("Apply")
                            }
                        }

                        if (appliedCoupon != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GharFixEmeraldContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = GharFixEmerald, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${appliedCoupon?.code}: ₹${discountAmount.toInt()} discount applied!",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GharFixEmerald
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Transparent Price Breakdown (Fixed) or Broadcast Details (Quote)
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isQuoteRequest) "Quote Request Summary" else "Bill Details (Transparent)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isQuoteRequest) {
                        Text(
                            text = "• Request will be sent to all active ${service.name} professionals in Chandrapur.\n• You will receive customized quotes (Labour + Material).\n• Compare bids and accept the best quote with zero advance obligation.",
                            fontSize = 12.sp,
                            color = GharFixTextSecondary,
                            lineHeight = 18.sp
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Service Base Labour", fontSize = 13.sp, color = GharFixTextSecondary)
                            Text("₹${basePrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        if (discountAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Coupon Discount", fontSize = 13.sp, color = GharFixEmerald)
                                Text("- ₹${discountAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GharFixEmerald)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Safety & Platform Fee", fontSize = 13.sp, color = GharFixTextSecondary)
                            Text("FREE (Launch offer)", fontSize = 12.sp, color = GharFixEmerald)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GharFixCardStroke)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GharFixTextPrimary)
                            Text("₹${finalPayable.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = GharFixTeal)
                        }
                    }
                }
            }
        }

        // 7. Payment Mode
        if (!isQuoteRequest) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = GharFixTeal, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Payment Method", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        listOf(
                            "UPI (GPay / PhonePe / Paytm) - Pay After Service",
                            "Cash on Delivery - Pay to Technician",
                            "Net Banking / Cards"
                        ).forEach { method ->
                            val isSelected = selectedPaymentMethod.startsWith(method.take(3))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPaymentMethod = method }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPaymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = GharFixTeal)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = method, fontSize = 12.sp, color = GharFixTextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }

    // Fixed Bottom Action Button
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isQuoteRequest) "Free Quote Broadcast" else "₹${finalPayable.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = GharFixNavy
                    )
                    Text(
                        text = "$selectedDate • $selectedSlot",
                        fontSize = 11.sp,
                        color = GharFixTextSecondary
                    )
                }

                Button(
                    onClick = {
                        if (isQuoteRequest) {
                            viewModel.requestQuote(
                                service = service,
                                categoryName = service.categoryId,
                                date = selectedDate,
                                timeSlot = selectedSlot,
                                address = addressDetail,
                                problemDescription = problemDescription.ifBlank { "Project quotation requested for ${service.name}" }
                            )
                        } else {
                            viewModel.bookFixedService(
                                service = service,
                                categoryName = service.categoryId,
                                date = selectedDate,
                                timeSlot = selectedSlot,
                                address = addressDetail,
                                notes = problemDescription,
                                couponCode = appliedCoupon?.code ?: "",
                                discountAmount = discountAmount,
                                paymentMethod = selectedPaymentMethod,
                                provider = selectedProvider
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isQuoteRequest) GharFixAmber else GharFixTeal
                    ),
                    modifier = Modifier.testTag("btn_confirm_booking")
                ) {
                    Text(
                        text = if (isQuoteRequest) "Send Quote Request" else "Confirm Booking",
                        color = if (isQuoteRequest) Color(0xFF451A03) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
