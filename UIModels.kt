package com.example.ui.model

enum class AppRole(val displayName: String, val badgeText: String) {
    CUSTOMER("Customer", "Customer App"),
    PROVIDER("Service Provider", "Partner Pro"),
    ADMIN("Admin", "Platform Admin")
}

enum class CustomerTab(val label: String) {
    HOME("Home"),
    BOOKINGS("Bookings"),
    MESSAGES("Messages"),
    PROFILE("Profile")
}

enum class ProviderTab(val label: String) {
    DASHBOARD("Overview"),
    JOBS("Bookings"),
    QUOTES("Quotes Inbox"),
    EARNINGS("Earnings"),
    PROFILE("Profile & KYC")
}

enum class AdminTab(val label: String) {
    DASHBOARD("Overview"),
    PROVIDERS("Verification"),
    SERVICES("Services"),
    BOOKINGS("Bookings"),
    LOCATIONS("Locations"),
    SETTINGS("Commission & Rules")
}

sealed class AppScreen {
    // Customer Screens
    object CustomerHome : AppScreen()
    data class AllServices(val categoryId: String? = null) : AppScreen()
    data class ServiceDetail(val serviceId: String) : AppScreen()
    data class BookingFlow(val serviceId: String, val isQuoteRequest: Boolean = false) : AppScreen()
    object CustomerBookings : AppScreen()
    data class BookingDetail(val bookingId: String) : AppScreen()
    object CustomerQuotes : AppScreen()
    data class Chat(val bookingId: String) : AppScreen()
    object CustomerProfile : AppScreen()

    // Provider Screens
    object ProviderDashboard : AppScreen()
    object ProviderJobs : AppScreen()
    object ProviderQuotes : AppScreen()
    object ProviderEarnings : AppScreen()
    object ProviderProfile : AppScreen()

    // Admin Screens
    object AdminHome : AppScreen()
}

data class SelectedLocation(
    val country: String = "India",
    val state: String = "Maharashtra",
    val city: String = "Chandrapur",
    val area: String = "Ramnagar",
    val pincode: String = "442401"
)
