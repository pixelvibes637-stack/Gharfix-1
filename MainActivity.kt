package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.GharFixViewModel
import com.example.ui.components.AdminBottomNavBar
import com.example.ui.components.CustomerBottomNavBar
import com.example.ui.components.GharFixTopHeader
import com.example.ui.components.LocationSelectorDialog
import com.example.ui.components.ProviderBottomNavBar
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.model.AdminTab
import com.example.ui.model.AppRole
import com.example.ui.model.AppScreen
import com.example.ui.model.CustomerTab
import com.example.ui.model.ProviderTab
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AllServicesScreen
import com.example.ui.screens.BookingDetailTrackingScreen
import com.example.ui.screens.BookingFlowScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CustomerBookingsScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.screens.CustomerProfileAndSupportScreen
import com.example.ui.screens.CustomerQuotesScreen
import com.example.ui.screens.ProviderDashboardScreen
import com.example.ui.screens.ProviderEarningsScreen
import com.example.ui.screens.ServiceDetailScreen
import com.example.ui.theme.GharFixTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GharFixViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GharFixTheme {
                GharFixMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GharFixMainApp(viewModel: GharFixViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val customerTab by viewModel.currentCustomerTab.collectAsState()
    val providerTab by viewModel.currentProviderTab.collectAsState()
    val adminTab by viewModel.currentAdminTab.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val locations by viewModel.locations.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val allProviders by viewModel.allProviders.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showRoleDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }

    val activeCustomerBookingsCount = allBookings.count {
        it.customerId == "cust_1" && it.status !in listOf("COMPLETED", "CANCELLED")
    }
    val pendingJobsCount = allBookings.count { it.status == "PENDING" }
    val pendingKycCount = allProviders.count { it.kycStatus == "PENDING" }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    // Handle Android back button
    BackHandler(enabled = currentScreen != AppScreen.CustomerHome && currentScreen != AppScreen.ProviderDashboard && currentScreen != AppScreen.AdminHome) {
        when (currentRole) {
            AppRole.CUSTOMER -> viewModel.navigateTo(AppScreen.CustomerHome)
            AppRole.PROVIDER -> viewModel.navigateTo(AppScreen.ProviderDashboard)
            AppRole.ADMIN -> viewModel.navigateTo(AppScreen.AdminHome)
        }
    }

    val isTopLevelCustomerScreen = currentScreen in listOf(
        AppScreen.CustomerHome,
        AppScreen.CustomerBookings,
        AppScreen.CustomerQuotes,
        AppScreen.CustomerProfile
    )
    val isTopLevelProviderScreen = currentScreen in listOf(
        AppScreen.ProviderDashboard,
        AppScreen.ProviderJobs,
        AppScreen.ProviderQuotes,
        AppScreen.ProviderEarnings,
        AppScreen.ProviderProfile
    )
    val isTopLevelAdminScreen = currentScreen == AppScreen.AdminHome

    val showTopBar = true
    val showBottomBar = (currentRole == AppRole.CUSTOMER && isTopLevelCustomerScreen) ||
            (currentRole == AppRole.PROVIDER && isTopLevelProviderScreen) ||
            (currentRole == AppRole.ADMIN && isTopLevelAdminScreen)

    val isSubScreen = !isTopLevelCustomerScreen && !isTopLevelProviderScreen && !isTopLevelAdminScreen

    BackHandler(enabled = isSubScreen) {
        when (currentRole) {
            AppRole.CUSTOMER -> viewModel.navigateTo(AppScreen.CustomerHome)
            AppRole.PROVIDER -> viewModel.navigateTo(AppScreen.ProviderDashboard)
            AppRole.ADMIN -> viewModel.navigateTo(AppScreen.AdminHome)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (showTopBar) {
                val (title, subtitle) = when (currentScreen) {
                    is AppScreen.AllServices -> Pair("Services in Chandrapur", "Fixed price & custom quotes")
                    is AppScreen.ServiceDetail -> Pair("Service Details", "Verified technician specifications")
                    is AppScreen.BookingFlow -> Pair("Book Service", "Chandrapur Doorstep")
                    is AppScreen.BookingDetail -> Pair("Live Service Tracking", "Track status & OTP")
                    is AppScreen.Chat -> Pair("Chat with Technician", "Real-time updates")
                    else -> Pair(null, null)
                }

                val hasBack = !isTopLevelCustomerScreen && !isTopLevelProviderScreen && !isTopLevelAdminScreen

                GharFixTopHeader(
                    role = currentRole,
                    location = selectedLocation,
                    onLocationClick = { showLocationDialog = true },
                    onRoleSwitchClick = { showRoleDialog = true },
                    onBackClick = if (hasBack) {
                        {
                            when (currentRole) {
                                AppRole.CUSTOMER -> viewModel.navigateTo(AppScreen.CustomerHome)
                                AppRole.PROVIDER -> viewModel.navigateTo(AppScreen.ProviderDashboard)
                                AppRole.ADMIN -> viewModel.navigateTo(AppScreen.AdminHome)
                            }
                        }
                    } else null,
                    title = title,
                    subtitle = subtitle
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                when (currentRole) {
                    AppRole.CUSTOMER -> {
                        CustomerBottomNavBar(
                            selectedTab = customerTab,
                            onTabSelected = { tab -> viewModel.setCustomerTab(tab) },
                            activeBookingsCount = activeCustomerBookingsCount
                        )
                    }
                    AppRole.PROVIDER -> {
                        ProviderBottomNavBar(
                            selectedTab = providerTab,
                            onTabSelected = { tab -> viewModel.setProviderTab(tab) },
                            pendingJobsCount = pendingJobsCount
                        )
                    }
                    AppRole.ADMIN -> {
                        AdminBottomNavBar(
                            selectedTab = adminTab,
                            onTabSelected = { tab -> viewModel.setAdminTab(tab) },
                            pendingVerificationCount = pendingKycCount
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.CustomerHome -> {
                    CustomerHomeScreen(
                        viewModel = viewModel,
                        onNavigateToService = { serviceId -> viewModel.navigateTo(AppScreen.ServiceDetail(serviceId)) },
                        onNavigateToBookingFlow = { serviceId, isQuote -> viewModel.navigateTo(AppScreen.BookingFlow(serviceId, isQuote)) },
                        onViewAllServices = { catId -> viewModel.navigateTo(AppScreen.AllServices(catId)) },
                        onJoinAsProfessional = {
                            viewModel.switchRole(AppRole.PROVIDER)
                            viewModel.showMessage("Switched to Service Provider Mode!")
                        },
                        onLocationClick = { showLocationDialog = true }
                    )
                }
                is AppScreen.AllServices -> {
                    AllServicesScreen(
                        viewModel = viewModel,
                        initialCategoryId = screen.categoryId,
                        onServiceClick = { serviceId -> viewModel.navigateTo(AppScreen.ServiceDetail(serviceId)) },
                        onBack = { viewModel.navigateTo(AppScreen.CustomerHome) }
                    )
                }
                is AppScreen.ServiceDetail -> {
                    ServiceDetailScreen(
                        serviceId = screen.serviceId,
                        viewModel = viewModel,
                        onBookClick = { isQuote -> viewModel.navigateTo(AppScreen.BookingFlow(screen.serviceId, isQuote)) },
                        onBack = { viewModel.navigateTo(AppScreen.CustomerHome) }
                    )
                }
                is AppScreen.BookingFlow -> {
                    BookingFlowScreen(
                        serviceId = screen.serviceId,
                        isQuoteRequest = screen.isQuoteRequest,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.ServiceDetail(screen.serviceId)) }
                    )
                }
                is AppScreen.CustomerBookings -> {
                    CustomerBookingsScreen(
                        viewModel = viewModel,
                        onBookingClick = { bookingId -> viewModel.navigateTo(AppScreen.BookingDetail(bookingId)) },
                        onChatClick = { bookingId -> viewModel.navigateTo(AppScreen.Chat(bookingId)) }
                    )
                }
                is AppScreen.BookingDetail -> {
                    BookingDetailTrackingScreen(
                        bookingId = screen.bookingId,
                        viewModel = viewModel,
                        onChatClick = { bookingId -> viewModel.navigateTo(AppScreen.Chat(bookingId)) },
                        onBack = { viewModel.navigateTo(AppScreen.CustomerBookings) }
                    )
                }
                is AppScreen.CustomerQuotes -> {
                    CustomerQuotesScreen(
                        viewModel = viewModel,
                        onViewBooking = { bookingId -> viewModel.navigateTo(AppScreen.BookingDetail(bookingId)) },
                        onChatClick = { bookingId -> viewModel.navigateTo(AppScreen.Chat(bookingId)) }
                    )
                }
                is AppScreen.Chat -> {
                    ChatScreen(
                        bookingId = screen.bookingId,
                        viewModel = viewModel,
                        onBack = {
                            if (currentRole == AppRole.PROVIDER) {
                                viewModel.navigateTo(AppScreen.ProviderDashboard)
                            } else {
                                viewModel.navigateTo(AppScreen.CustomerBookings)
                            }
                        }
                    )
                }
                is AppScreen.CustomerProfile -> {
                    CustomerProfileAndSupportScreen(
                        viewModel = viewModel,
                        onRoleSwitchClick = { showRoleDialog = true }
                    )
                }
                is AppScreen.ProviderDashboard, is AppScreen.ProviderJobs, is AppScreen.ProviderQuotes -> {
                    ProviderDashboardScreen(
                        viewModel = viewModel,
                        onJobClick = { bookingId -> viewModel.navigateTo(AppScreen.BookingDetail(bookingId)) },
                        onChatClick = { bookingId -> viewModel.navigateTo(AppScreen.Chat(bookingId)) }
                    )
                }
                is AppScreen.ProviderEarnings -> {
                    ProviderEarningsScreen(viewModel = viewModel)
                }
                is AppScreen.ProviderProfile -> {
                    CustomerProfileAndSupportScreen(
                        viewModel = viewModel,
                        onRoleSwitchClick = { showRoleDialog = true }
                    )
                }
                is AppScreen.AdminHome -> {
                    AdminPanelScreen(
                        viewModel = viewModel,
                        onViewBooking = { bookingId -> viewModel.navigateTo(AppScreen.BookingDetail(bookingId)) }
                    )
                }
            }
        }
    }

    // Role Switcher Dialog
    if (showRoleDialog) {
        RoleSwitcherDialog(
            currentRole = currentRole,
            onSelectRole = { newRole ->
                viewModel.switchRole(newRole)
                viewModel.showMessage("Switched to ${newRole.displayName} Mode")
            },
            onDismiss = { showRoleDialog = false }
        )
    }

    // Location Selector Dialog
    if (showLocationDialog) {
        LocationSelectorDialog(
            locations = locations,
            currentLocation = selectedLocation,
            onSelectLocation = { city, area, pincode ->
                viewModel.updateLocation(city, area, pincode)
                viewModel.showMessage("Location updated to $area, $city")
            },
            onDismiss = { showLocationDialog = false }
        )
    }
}
