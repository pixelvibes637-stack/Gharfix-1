package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ProviderEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.ServiceCategoryEntity
import com.example.data.local.ServiceEntity
import com.example.ui.GharFixViewModel
import com.example.ui.components.BrandLogoLayout
import com.example.ui.components.BrandTheme
import com.example.ui.components.CategoryIcon
import com.example.ui.components.GharFixAboutCard
import com.example.ui.components.GharFixLogo
import com.example.ui.components.LogoSize
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
import com.example.ui.theme.GharFixTeal
import com.example.ui.theme.GharFixTealContainer
import com.example.ui.theme.GharFixTealDark
import com.example.ui.theme.GharFixTextMuted
import com.example.ui.theme.GharFixTextPrimary
import com.example.ui.theme.GharFixTextSecondary

@Composable
fun CustomerHomeScreen(
    viewModel: GharFixViewModel,
    onNavigateToService: (String) -> Unit,
    onNavigateToBookingFlow: (String, Boolean) -> Unit,
    onViewAllServices: (String?) -> Unit,
    onJoinAsProfessional: () -> Unit,
    onLocationClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()
    val activeServices by viewModel.allActiveServices.collectAsState()
    val providers by viewModel.approvedProviders.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val location by viewModel.selectedLocation.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val popularFixedServices = activeServices.filter { it.bookingType == "FIXED" && it.isPopular }
    val quoteServices = activeServices.filter { it.bookingType == "GET_QUOTE" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GharFixBackground)
            .testTag("screen_customer_home"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Hero Brand Banner with GharFix Logo & Tagline
        item {
            CustomerHeroBanner(
                cityName = location.city,
                areaName = location.area,
                onBookServiceClick = { onViewAllServices(null) },
                onGetQuoteClick = {
                    val solarQuote = quoteServices.firstOrNull { it.categoryId == "cat_solar" }
                        ?: activeServices.firstOrNull { it.bookingType == "GET_QUOTE" }
                    if (solarQuote != null) {
                        onNavigateToBookingFlow(solarQuote.id, true)
                    } else {
                        onViewAllServices(null)
                    }
                },
                onJoinProClick = onJoinAsProfessional
            )
        }

        // 2. Location Selector Bar (Chandrapur Default)
        item {
            CustomerLocationSelectorBar(
                area = location.area,
                city = location.city,
                pincode = location.pincode,
                onClick = onLocationClick
            )
        }

        // 3. Search Bar: "Aapko kaunsi service chahiye?" + Quick Action Chips
        item {
            CustomerSearchBarSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchSubmit = { query ->
                    viewModel.setSearchQuery(query)
                    onViewAllServices(null)
                },
                onChipSelected = { categoryKey ->
                    val matchingCategory = categories.firstOrNull {
                        it.iconKey.equals(categoryKey, ignoreCase = true) ||
                                it.name.contains(categoryKey, ignoreCase = true)
                    }
                    if (matchingCategory != null) {
                        onViewAllServices(matchingCategory.id)
                    } else {
                        viewModel.setSearchQuery(categoryKey)
                        onViewAllServices(null)
                    }
                }
            )
        }

        // 4. Six Core Service Cards with Icons: Solar, Electrician, AC Service, Plumbing, CCTV, RO
        item {
            CoreServicesCardsSection(
                categories = categories,
                onCategoryClick = { catId -> onViewAllServices(catId) },
                onBookNowClick = { catId ->
                    val service = activeServices.firstOrNull { it.categoryId == catId && it.bookingType == "FIXED" }
                    if (service != null) {
                        onNavigateToBookingFlow(service.id, false)
                    } else {
                        onViewAllServices(catId)
                    }
                },
                onGetQuoteClick = { catId ->
                    val quoteService = activeServices.firstOrNull { it.categoryId == catId && it.bookingType == "GET_QUOTE" }
                    if (quoteService != null) {
                        onNavigateToBookingFlow(quoteService.id, true)
                    } else {
                        onViewAllServices(catId)
                    }
                },
                onViewAllClick = { onViewAllServices(null) }
            )
        }

        // 5. Popular Fixed-Price Services (with Direct "Book Now" Buttons)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Popular Doorstep Services",
                subtitle = "Upfront pricing • 30-Day GharFix warranty",
                actionLabel = "View All (${popularFixedServices.size})",
                onActionClick = { onViewAllServices(null) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(popularFixedServices) { service ->
                    PopularServiceCard(
                        service = service,
                        onClick = { onNavigateToService(service.id) },
                        onBookNowClick = { onNavigateToBookingFlow(service.id, false) }
                    )
                }
            }
        }

        // 6. "Get a Quote" Spotlight for Big Projects (Solar, Full Rewiring, CCTV Setup, Plumbing Overhaul)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            QuoteProjectsSpotlightSection(
                quoteServices = quoteServices,
                onServiceClick = { serviceId -> onNavigateToService(serviceId) },
                onGetQuoteClick = { serviceId -> onNavigateToBookingFlow(serviceId, true) },
                onViewAllQuotes = { onViewAllServices(null) }
            )
        }

        // 7. How GharFix Works Section (3 Steps)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            HowGharFixWorksCard()
        }

        // 8. Verified Professionals in Chandrapur
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "Verified Pros in ${location.city}",
                subtitle = "100% Aadhaar Verified • Police Checked • Background Verified",
                actionLabel = null,
                onActionClick = {}
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                providers.take(4).forEach { pro ->
                    VerifiedProCard(
                        provider = pro,
                        onBookPro = {
                            val proCategory = categories.firstOrNull { cat ->
                                pro.primaryCategory.contains(cat.name, ignoreCase = true)
                            }
                            onViewAllServices(proCategory?.id)
                        }
                    )
                }
            }
        }

        // 9. Customer Reviews in Chandrapur
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "Chandrapur Customer Reviews",
                subtitle = "Real feedback from Ramnagar, Tukum & Civil Lines locals",
                actionLabel = null,
                onActionClick = {}
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews) { review ->
                    CustomerReviewCard(review = review)
                }
            }
        }

        // 10. Service Guarantee, Helpline & Trust Footer
        item {
            Spacer(modifier = Modifier.height(20.dp))
            GharFixAboutCard(
                cityName = location.city,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
            TrustAndHelplineFooter(
                onCallHelpline = {
                    try {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917172250000"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        viewModel.showMessage("Helpline: +91 7172 250000")
                    }
                }
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = GharFixTextSecondary
                )
            }
        }
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixTeal,
                modifier = Modifier
                    .clickable { onActionClick() }
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun CustomerHeroBanner(
    cityName: String,
    areaName: String,
    onBookServiceClick: () -> Unit,
    onGetQuoteClick: () -> Unit,
    onJoinProClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GharFixNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GharFixNavy, Color(0xFF003844), Color(0xFF00232B))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                // Top Brand Row: Logo + App Name + Tagline Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GharFixLogo(
                        size = LogoSize.MEDIUM,
                        layout = BrandLogoLayout.HORIZONTAL,
                        theme = BrandTheme.DARK,
                        showTagline = true
                    )

                    // Chandrapur Launch Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GharFixEmeraldContainer.copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GharFixEmerald,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$cityName Launch",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixEmerald
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Ghar ke har kaam ke liye trusted professional.",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 25.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Solar, Electrician, AC Service, Plumbing, CCTV, RO aur bahut kuch — $cityName ke verified pros book karein.",
                    fontSize = 13.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 3 Action Buttons in Hero
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onBookServiceClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GharFixAmber),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("btn_hero_book_service")
                    ) {
                        Text(
                            text = "Book Now",
                            color = Color(0xFF451A03),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onGetQuoteClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("btn_hero_get_quote")
                    ) {
                        Text(
                            text = "Get a Quote",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onJoinProClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFF94A3B8)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(0.9f)
                            .testTag("btn_hero_join_pro")
                    ) {
                        Text(
                            text = "Join Pro",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerLocationSelectorBar(
    area: String,
    city: String,
    pincode: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, GharFixCardStroke),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
            .testTag("chip_location_selector")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(GharFixTealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = GharFixTealDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$area, $city",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharFixTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = GharFixEmeraldContainer
                        ) {
                            Text(
                                text = "PIN: $pincode",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixEmerald,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Doorstep coverage available in Chandrapur",
                        fontSize = 11.sp,
                        color = GharFixTextSecondary
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = GharFixBackground,
                border = BorderStroke(1.dp, GharFixCardStroke)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Change", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GharFixTeal)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "Change Location",
                        tint = GharFixTeal,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerSearchBarSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit,
    onChipSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = "Aapko kaunsi service chahiye?",
                    fontSize = 13.sp,
                    color = GharFixTextMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = GharFixTeal
                )
            },
            trailingIcon = {
                Button(
                    onClick = { onSearchSubmit(searchQuery.ifBlank { "All" }) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text("Search", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_home_search"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GharFixTeal,
                unfocusedBorderColor = GharFixCardStroke,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Category Suggestions Chips
        val quickChips = listOf(
            Pair("☀️ Solar", "solar"),
            Pair("⚡ Electrician", "electrician"),
            Pair("❄️ AC Service", "ac"),
            Pair("🚰 Plumbing", "plumbing"),
            Pair("📹 CCTV", "cctv"),
            Pair("💧 RO Purifier", "ro")
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickChips) { (label, key) ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, GharFixCardStroke),
                    modifier = Modifier
                        .clickable { onChipSelected(key) }
                        .testTag("chip_search_$key")
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GharFixTextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CoreServicesCardsSection(
    categories: List<ServiceCategoryEntity>,
    onCategoryClick: (String) -> Unit,
    onBookNowClick: (String) -> Unit,
    onGetQuoteClick: (String) -> Unit,
    onViewAllClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Services in Chandrapur",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixNavy
                    )
                    Text(
                        text = "Solar • Electrician • AC • Plumbing • CCTV • RO",
                        fontSize = 11.sp,
                        color = GharFixTextSecondary
                    )
                }
                Text(
                    text = "View All →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GharFixTeal,
                    modifier = Modifier
                        .clickable { onViewAllClick() }
                        .testTag("btn_view_all_services")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6 Core Service Cards arranged in a 2x3 Grid with icons and dual actions
            val coreList = listOf(
                CoreServiceItem("cat_solar", "Solar", "solar", "Rooftop & Cleaning", "Govt Subsidy", true),
                CoreServiceItem("cat_electrician", "Electrician", "electrician", "Wiring, Fans & MCB", "From ₹149", false),
                CoreServiceItem("cat_ac", "AC Service", "ac", "Jet Wash & Gas Refill", "From ₹499", false),
                CoreServiceItem("cat_plumbing", "Plumbing", "plumbing", "Taps, Pipes & Motor", "From ₹199", false),
                CoreServiceItem("cat_cctv", "CCTV", "cctv", "HD Camera & DVR Setup", "5MP HD", true),
                CoreServiceItem("cat_ro", "RO Purifier", "ro", "Filter & Membrane Fix", "TDS Tuning", false)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (rowIndex in coreList.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (colIndex in 0..1) {
                            val itemIndex = rowIndex + colIndex
                            if (itemIndex < coreList.size) {
                                val item = coreList[itemIndex]
                                CoreServiceGridCard(
                                    item = item,
                                    onCardClick = { onCategoryClick(item.categoryId) },
                                    onBookNow = { onBookNowClick(item.categoryId) },
                                    onGetQuote = { onGetQuoteClick(item.categoryId) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CoreServiceItem(
    val categoryId: String,
    val name: String,
    val iconKey: String,
    val description: String,
    val badge: String,
    val isQuoteOriented: Boolean
)

@Composable
fun CoreServiceGridCard(
    item: CoreServiceItem,
    onCardClick: () -> Unit,
    onBookNow: () -> Unit,
    onGetQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = GharFixBackground,
        border = BorderStroke(1.dp, GharFixCardStroke),
        modifier = modifier
            .clickable { onCardClick() }
            .testTag("core_card_${item.iconKey}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when (item.iconKey) {
                                "solar" -> GharFixAmberContainer
                                "ac" -> GharFixBlueContainer
                                "cctv" -> Color(0xFFEDE9FE)
                                else -> GharFixTealContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryIcon(
                        key = item.iconKey,
                        modifier = Modifier.size(24.dp),
                        tint = when (item.iconKey) {
                            "solar" -> GharFixAmberDark
                            "ac" -> GharFixBlue
                            "cctv" -> Color(0xFF6D28D9)
                            else -> GharFixTealDark
                        }
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (item.isQuoteOriented) GharFixAmberContainer else GharFixEmeraldContainer
                ) {
                    Text(
                        text = item.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isQuoteOriented) GharFixAmberDark else GharFixEmerald,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.description,
                fontSize = 11.sp,
                color = GharFixTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button: "Book Now" or "Get a Quote"
            if (item.isQuoteOriented) {
                Button(
                    onClick = onGetQuote,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixAmberDark),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .testTag("btn_quote_${item.iconKey}")
                ) {
                    Text(
                        text = "Get a Quote",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = onBookNow,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .testTag("btn_book_${item.iconKey}")
                ) {
                    Text(
                        text = "Book Now",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PopularServiceCard(
    service: ServiceEntity,
    onClick: () -> Unit,
    onBookNowClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(230.dp)
            .clickable { onClick() }
            .testTag("popular_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GharFixCardStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GharFixTealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryIcon(
                        key = service.iconKey,
                        modifier = Modifier.size(20.dp),
                        tint = GharFixTealDark
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GharFixEmeraldContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${service.rating}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
                modifier = Modifier.height(36.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = GharFixTextMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = service.estimatedDuration,
                    fontSize = 11.sp,
                    color = GharFixTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GharFixCardStroke)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${service.basePrice.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = GharFixTealDark
                    )
                    Text(
                        text = service.unit,
                        fontSize = 10.sp,
                        color = GharFixTextMuted
                    )
                }

                Button(
                    onClick = onBookNowClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_book_now_${service.id}")
                ) {
                    Text(
                        text = "Book Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun QuoteProjectsSpotlightSection(
    quoteServices: List<ServiceEntity>,
    onServiceClick: (String) -> Unit,
    onGetQuoteClick: (String) -> Unit,
    onViewAllQuotes: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GharFixAmberContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RequestQuote,
                            contentDescription = null,
                            tint = GharFixAmberDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Big Projects? Get a Free Quote",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                        Text(
                            text = "Itemized labour & material quotes from Chandrapur pros",
                            fontSize = 11.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                quoteServices.take(4).forEach { service ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onServiceClick(service.id) }
                            .testTag("quote_project_${service.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GharFixAmberContainer.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CategoryIcon(
                                    key = service.iconKey,
                                    modifier = Modifier.size(18.dp),
                                    tint = GharFixAmberDark
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = service.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GharFixTextPrimary
                                )
                                Text(
                                    text = service.description,
                                    fontSize = 11.sp,
                                    color = GharFixTextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onGetQuoteClick(service.id) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GharFixAmberDark),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(
                                    text = "Get Quote",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HowGharFixWorksCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "How GharFix Works",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy
            )
            Text(
                text = "3 simple steps to book verified doorstep services in Chandrapur",
                fontSize = 12.sp,
                color = GharFixTextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            val steps = listOf(
                Triple("1", "Select Service or Post Project", "Choose fixed-price repairs (AC jet wash, fan repair) or post custom quote requirements for Solar & CCTV."),
                Triple("2", "Verified Local Pro Assigned", "Background checked, Aadhaar verified Chandrapur technician is assigned with upfront pricing."),
                Triple("3", "Doorstep Service & Secure OTP", "Technician completes work at your home. Share 4-digit OTP and pay securely after 100% satisfaction.")
            )

            steps.forEachIndexed { index, (num, title, desc) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(GharFixTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = num,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharFixTextPrimary
                        )
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = GharFixTextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 14.dp, top = 4.dp, bottom = 4.dp)
                            .width(2.dp)
                            .height(18.dp)
                            .background(GharFixCardStroke)
                    )
                }
            }
        }
    }
}

@Composable
fun VerifiedProCard(
    provider: ProviderEntity,
    onBookPro: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, GharFixCardStroke),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GharFixTealContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Engineering,
                    contentDescription = null,
                    tint = GharFixTealDark,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = provider.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = GharFixEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "${provider.primaryCategory} • ${provider.experienceYears} yrs exp",
                    fontSize = 12.sp,
                    color = GharFixTextSecondary
                )
                Text(
                    text = "Covers: ${provider.serviceAreas}",
                    fontSize = 11.sp,
                    color = GharFixTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GharFixEmeraldContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${provider.rating}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onBookPro,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTealContainer),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(26.dp)
                ) {
                    Text(
                        text = "Book",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTealDark
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerReviewCard(review: ReviewEntity) {
    Card(
        modifier = Modifier.width(250.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    repeat(review.rating.toInt()) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GharFixAmber,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = review.dateText,
                    fontSize = 10.sp,
                    color = GharFixTextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${review.comment}\"",
                fontSize = 12.sp,
                color = GharFixTextPrimary,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = review.customerName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixTealDark
            )
            Text(
                text = "${review.serviceName} • ${review.customerArea}",
                fontSize = 10.sp,
                color = GharFixTextSecondary
            )
        }
    }
}

@Composable
fun TrustAndHelplineFooter(
    onCallHelpline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = GharFixTealContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = GharFixTealDark,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GharFix 100% Service Protection",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharFixTealDark
                    )
                    Text(
                        text = "30-day rework warranty • Transparent pricing • Helpline: +91 7172 250000",
                        fontSize = 11.sp,
                        color = GharFixTextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onCallHelpline,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_call_helpline")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "GharFix India • Launch City: Chandrapur, Maharashtra",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = GharFixTextMuted
        )
    }
}
