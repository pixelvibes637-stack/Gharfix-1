package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LocationEntity
import com.example.ui.model.AdminTab
import com.example.ui.model.AppRole
import com.example.ui.model.CustomerTab
import com.example.ui.model.ProviderTab
import com.example.ui.model.SelectedLocation
import com.example.ui.theme.GharFixAmber
import com.example.ui.theme.GharFixAmberContainer
import com.example.ui.theme.GharFixAmberDark
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
fun GharFixTopHeader(
    role: AppRole,
    location: SelectedLocation,
    onLocationClick: () -> Unit,
    onRoleSwitchClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    title: String? = null,
    subtitle: String? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Row: Brand / Title + Role Switcher Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("btn_back")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go Back",
                                tint = GharFixTealDark
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Column {
                        if (title != null) {
                            Text(
                                text = title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharFixTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (subtitle != null) {
                                Text(
                                    text = subtitle,
                                    fontSize = 12.sp,
                                    color = GharFixTextSecondary
                                )
                            }
                        } else {
                            GharFixLogo(
                                size = LogoSize.SMALL,
                                layout = BrandLogoLayout.HORIZONTAL,
                                theme = BrandTheme.LIGHT,
                                showTagline = true
                            )
                        }
                    }
                }

                // Role Switcher Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (role) {
                        AppRole.CUSTOMER -> GharFixTealContainer
                        AppRole.PROVIDER -> GharFixAmberContainer
                        AppRole.ADMIN -> GharFixBlueContainer
                    },
                    modifier = Modifier
                        .clickable { onRoleSwitchClick() }
                        .testTag("btn_role_switcher")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = when (role) {
                                AppRole.CUSTOMER -> Icons.Default.Person
                                AppRole.PROVIDER -> Icons.Default.Engineering
                                AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                            },
                            contentDescription = "Role",
                            tint = when (role) {
                                AppRole.CUSTOMER -> GharFixTealDark
                                AppRole.PROVIDER -> GharFixAmber
                                AppRole.ADMIN -> GharFixBlue
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = role.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (role) {
                                AppRole.CUSTOMER -> GharFixTealDark
                                AppRole.PROVIDER -> Color(0xFFB45309)
                                AppRole.ADMIN -> Color(0xFF1D4ED8)
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch",
                            tint = GharFixTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Location Bar (for Customer and Provider)
            if (role != AppRole.ADMIN && title == null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLocationClick() }
                        .testTag("chip_location_selector")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = GharFixTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${location.area}, ${location.city}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GharFixTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = GharFixEmeraldContainer
                                    ) {
                                        Text(
                                            text = "Launch City",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GharFixEmerald,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${location.state}, ${location.country} - ${location.pincode}",
                                    fontSize = 11.sp,
                                    color = GharFixTextSecondary
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Change Location",
                            tint = GharFixTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bg, textColor, label) = when (status) {
        "PENDING" -> Triple(GharFixAmberContainer, Color(0xFFB45309), "Pending")
        "ACCEPTED" -> Triple(GharFixBlueContainer, GharFixBlue, "Accepted")
        "ON_THE_WAY" -> Triple(Color(0xFFE0E7FF), Color(0xFF4338CA), "On The Way")
        "STARTED" -> Triple(Color(0xFFFEF08A), Color(0xFF854D0E), "Work In Progress")
        "COMPLETED" -> Triple(GharFixEmeraldContainer, GharFixEmerald, "Completed")
        "CANCELLED" -> Triple(GharFixRedContainer, GharFixRed, "Cancelled")
        "DISPUTED" -> Triple(GharFixRedContainer, Color(0xFF991B1B), "Disputed")
        "APPROVED" -> Triple(GharFixEmeraldContainer, GharFixEmerald, "Verified ✓")
        "REJECTED" -> Triple(GharFixRedContainer, GharFixRed, "Rejected")
        "SUSPENDED" -> Triple(GharFixRedContainer, GharFixRed, "Suspended")
        else -> Triple(Color(0xFFF1F5F9), GharFixTextSecondary, status)
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg,
        modifier = Modifier.testTag("badge_status_$status")
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun CategoryIcon(key: String, modifier: Modifier = Modifier, tint: Color = GharFixTeal) {
    val icon = when (key.lowercase()) {
        "electrician", "electrical" -> Icons.Default.ElectricBolt
        "solar" -> Icons.Default.WbSunny
        "ac", "ac service" -> Icons.Default.Air
        "plumbing", "plumber" -> Icons.Default.Plumbing
        "cctv", "security" -> Icons.Default.Videocam
        "ro", "water" -> Icons.Default.WaterDrop
        "paint", "painting" -> Icons.Default.FormatPaint
        "cleaning" -> Icons.Default.CleaningServices
        else -> Icons.Default.Build
    }
    Icon(imageVector = icon, contentDescription = key, modifier = modifier, tint = tint)
}

@Composable
fun CustomerBottomNavBar(
    selectedTab: CustomerTab,
    onTabSelected: (CustomerTab) -> Unit,
    activeBookingsCount: Int = 0
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        CustomerTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    val icon = when (tab) {
                        CustomerTab.HOME -> Icons.Default.Home
                        CustomerTab.BOOKINGS -> Icons.Default.Assignment
                        CustomerTab.MESSAGES -> Icons.AutoMirrored.Filled.Chat
                        CustomerTab.PROFILE -> Icons.Default.Person
                    }
                    if (tab == CustomerTab.BOOKINGS && activeBookingsCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = GharFixAmber) {
                                    Text(text = activeBookingsCount.toString(), color = Color.White)
                                }
                            }
                        ) {
                            Icon(imageVector = icon, contentDescription = tab.label)
                        }
                    } else {
                        Icon(imageVector = icon, contentDescription = tab.label)
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GharFixTeal,
                    selectedTextColor = GharFixTeal,
                    indicatorColor = GharFixTealContainer,
                    unselectedIconColor = GharFixTextMuted,
                    unselectedTextColor = GharFixTextMuted
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

@Composable
fun ProviderBottomNavBar(
    selectedTab: ProviderTab,
    onTabSelected: (ProviderTab) -> Unit,
    pendingJobsCount: Int = 0
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        ProviderTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    val icon = when (tab) {
                        ProviderTab.DASHBOARD -> Icons.Default.Dashboard
                        ProviderTab.JOBS -> Icons.Default.Assignment
                        ProviderTab.QUOTES -> Icons.Default.RequestQuote
                        ProviderTab.EARNINGS -> Icons.Default.AccountBalanceWallet
                        ProviderTab.PROFILE -> Icons.Default.Person
                    }
                    if (tab == ProviderTab.JOBS && pendingJobsCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = GharFixRed) {
                                    Text(text = pendingJobsCount.toString(), color = Color.White)
                                }
                            }
                        ) {
                            Icon(imageVector = icon, contentDescription = tab.label)
                        }
                    } else {
                        Icon(imageVector = icon, contentDescription = tab.label)
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GharFixAmber,
                    selectedTextColor = GharFixAmberDark,
                    indicatorColor = GharFixAmberContainer,
                    unselectedIconColor = GharFixTextMuted,
                    unselectedTextColor = GharFixTextMuted
                ),
                modifier = Modifier.testTag("provider_nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

@Composable
fun AdminBottomNavBar(
    selectedTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit,
    pendingVerificationCount: Int = 0
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        AdminTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    val icon = when (tab) {
                        AdminTab.DASHBOARD -> Icons.Default.Dashboard
                        AdminTab.PROVIDERS -> Icons.Default.Security
                        AdminTab.SERVICES -> Icons.Default.Build
                        AdminTab.BOOKINGS -> Icons.Default.Assignment
                        AdminTab.LOCATIONS -> Icons.Default.LocationOn
                        AdminTab.SETTINGS -> Icons.Default.Settings
                    }
                    if (tab == AdminTab.PROVIDERS && pendingVerificationCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = GharFixRed) {
                                    Text(text = pendingVerificationCount.toString(), color = Color.White)
                                }
                            }
                        ) {
                            Icon(imageVector = icon, contentDescription = tab.label)
                        }
                    } else {
                        Icon(imageVector = icon, contentDescription = tab.label)
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GharFixBlue,
                    selectedTextColor = GharFixBlue,
                    indicatorColor = GharFixBlueContainer,
                    unselectedIconColor = GharFixTextMuted,
                    unselectedTextColor = GharFixTextMuted
                ),
                modifier = Modifier.testTag("admin_nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

@Composable
fun RoleSwitcherDialog(
    currentRole: AppRole,
    onSelectRole: (AppRole) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GharFixLogo(
                        size = LogoSize.SMALL,
                        layout = BrandLogoLayout.HORIZONTAL,
                        theme = BrandTheme.LIGHT,
                        showTagline = true
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GharFixTealContainer
                    ) {
                        Text(
                            text = "3-in-1 Hub",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharFixTealDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select Experience Mode",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GharFixTextPrimary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "GharFix provides 3 integrated role dashboards. Select a role to test its specific features:",
                    fontSize = 12.sp,
                    color = GharFixTextSecondary
                )

                AppRole.values().forEach { role ->
                    val isSelected = currentRole == role
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) {
                            when (role) {
                                AppRole.CUSTOMER -> GharFixTealContainer
                                AppRole.PROVIDER -> GharFixAmberContainer
                                AppRole.ADMIN -> GharFixBlueContainer
                            }
                        } else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            when (role) {
                                AppRole.CUSTOMER -> GharFixTeal
                                AppRole.PROVIDER -> GharFixAmber
                                AppRole.ADMIN -> GharFixBlue
                            }
                        ) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectRole(role)
                                onDismiss()
                            }
                            .testTag("role_select_${role.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    AppRole.CUSTOMER -> Icons.Default.Person
                                    AppRole.PROVIDER -> Icons.Default.Engineering
                                    AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                                },
                                contentDescription = role.displayName,
                                tint = when (role) {
                                    AppRole.CUSTOMER -> GharFixTealDark
                                    AppRole.PROVIDER -> GharFixAmberDark
                                    AppRole.ADMIN -> GharFixBlue
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = role.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = GharFixTextPrimary
                                )
                                Text(
                                    text = when (role) {
                                        AppRole.CUSTOMER -> "Book services, get quotes, track live status, chat & review"
                                        AppRole.PROVIDER -> "Accept bookings, send quote proposals, manage active jobs, view earnings"
                                        AppRole.ADMIN -> "Supervise bookings, verify providers, manage services & commission"
                                    },
                                    fontSize = 11.sp,
                                    color = GharFixTextSecondary
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = when (role) {
                                        AppRole.CUSTOMER -> GharFixTeal
                                        AppRole.PROVIDER -> GharFixAmber
                                        AppRole.ADMIN -> GharFixBlue
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun LocationSelectorDialog(
    locations: List<LocationEntity>,
    currentLocation: SelectedLocation,
    onSelectLocation: (city: String, area: String, pincode: String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCityTab by remember { mutableStateOf("Chandrapur") }

    val cities = listOf("Chandrapur", "Nagpur", "Pune", "Mumbai", "Wardha")
    val filteredLocations = locations.filter {
        it.city.equals(selectedCityTab, ignoreCase = true) &&
                (searchQuery.isBlank() || it.area.contains(searchQuery, ignoreCase = true) || it.pincode.contains(searchQuery))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GharFixTeal
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Select Service Area", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Text(
                    text = "GharFix is actively live in Chandrapur, Maharashtra",
                    fontSize = 12.sp,
                    color = GharFixTextSecondary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // City Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    cities.take(3).forEach { city ->
                        val isSelected = selectedCityTab.equals(city, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) GharFixTeal else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedCityTab = city }
                        ) {
                            Text(
                                text = if (city == "Chandrapur") "Chandrapur ★" else city,
                                color = if (isSelected) Color.White else GharFixTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search area in $selectedCityTab (e.g. Tukum, Ramnagar)", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    items(filteredLocations) { loc ->
                        val isCurrent = loc.city.equals(currentLocation.city, true) && loc.area.equals(currentLocation.area, true)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isCurrent) GharFixTealContainer else Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectLocation(loc.city, loc.area, loc.pincode)
                                    onDismiss()
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = loc.area,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp,
                                        color = if (isCurrent) GharFixTealDark else GharFixTextPrimary
                                    )
                                    Text(
                                        text = "${loc.city}, ${loc.state} • ${loc.pincode}",
                                        fontSize = 11.sp,
                                        color = GharFixTextSecondary
                                    )
                                }
                                if (loc.isLaunchCity) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = GharFixEmeraldContainer
                                    ) {
                                        Text(
                                            text = "Active Hub",
                                            color = GharFixEmerald,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(color = GharFixCardStroke.copy(alpha = 0.5f))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
