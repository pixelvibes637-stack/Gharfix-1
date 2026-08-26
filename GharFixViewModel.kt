package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookingEntity
import com.example.data.local.CouponEntity
import com.example.data.local.GharFixDatabase
import com.example.data.local.LocationEntity
import com.example.data.local.MessageEntity
import com.example.data.local.PlatformSettingsInfo
import com.example.data.local.ProviderEntity
import com.example.data.local.QuoteEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.ServiceCategoryEntity
import com.example.data.local.ServiceEntity
import com.example.data.local.SupportTicketEntity
import com.example.data.repository.GharFixRepository
import com.example.ui.model.AdminTab
import com.example.ui.model.AppRole
import com.example.ui.model.AppScreen
import com.example.ui.model.CustomerTab
import com.example.ui.model.ProviderTab
import com.example.ui.model.SelectedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class GharFixViewModel(application: Application) : AndroidViewModel(application) {

    private val database = GharFixDatabase.getDatabase(application)
    private val repository = GharFixRepository(database.gharFixDao())

    // Current Role & User
    private val _currentRole = MutableStateFlow(AppRole.CUSTOMER)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    private val _currentCustomerId = MutableStateFlow("user_cust_1")
    val currentCustomerId: StateFlow<String> = _currentCustomerId.asStateFlow()

    private val _currentProviderId = MutableStateFlow("pro_1")
    val currentProviderId: StateFlow<String> = _currentProviderId.asStateFlow()

    // Navigation
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.CustomerHome)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentCustomerTab = MutableStateFlow(CustomerTab.HOME)
    val currentCustomerTab: StateFlow<CustomerTab> = _currentCustomerTab.asStateFlow()

    private val _currentProviderTab = MutableStateFlow(ProviderTab.DASHBOARD)
    val currentProviderTab: StateFlow<ProviderTab> = _currentProviderTab.asStateFlow()

    private val _currentAdminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val currentAdminTab: StateFlow<AdminTab> = _currentAdminTab.asStateFlow()

    private val screenBackStack = mutableListOf<AppScreen>()

    // Location
    private val _selectedLocation = MutableStateFlow(SelectedLocation())
    val selectedLocation: StateFlow<SelectedLocation> = _selectedLocation.asStateFlow()

    // UI Feedback (Snackbar/Dialog)
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Search & Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    // Data streams
    val categories: StateFlow<List<ServiceCategoryEntity>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allActiveServices: StateFlow<List<ServiceEntity>> = repository.allActiveServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allServices: StateFlow<List<ServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProviders: StateFlow<List<ProviderEntity>> = repository.providers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val approvedProviders: StateFlow<List<ProviderEntity>> = repository.approvedProviders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val locations: StateFlow<List<LocationEntity>> = repository.activeLocations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeLocations: StateFlow<List<LocationEntity>> = repository.activeLocations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviews: StateFlow<List<ReviewEntity>> = repository.reviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coupons: StateFlow<List<CouponEntity>> = repository.activeCoupons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val supportTickets: StateFlow<List<SupportTicketEntity>> = repository.supportTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _platformSettings = MutableStateFlow(PlatformSettingsInfo())
    val platformSettings: StateFlow<PlatformSettingsInfo> = _platformSettings.asStateFlow()

    init {
        viewModelScope.launch {
            val rate = repository.getCommissionRate()
            _platformSettings.value = PlatformSettingsInfo(commissionPercentage = rate)
        }
    }

    // Role Switching
    fun switchRole(newRole: AppRole) {
        _currentRole.value = newRole
        screenBackStack.clear()
        _currentScreen.value = when (newRole) {
            AppRole.CUSTOMER -> AppScreen.CustomerHome
            AppRole.PROVIDER -> AppScreen.ProviderDashboard
            AppRole.ADMIN -> AppScreen.AdminHome
        }
        showMessage("Switched to ${newRole.displayName} view")
    }

    // Navigation helpers
    fun navigateTo(screen: AppScreen) {
        screenBackStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            return true
        }
        return false
    }

    fun setCustomerTab(tab: CustomerTab) {
        _currentCustomerTab.value = tab
        _currentScreen.value = when (tab) {
            CustomerTab.HOME -> AppScreen.CustomerHome
            CustomerTab.BOOKINGS -> AppScreen.CustomerBookings
            CustomerTab.MESSAGES -> AppScreen.Chat("book_101")
            CustomerTab.PROFILE -> AppScreen.CustomerProfile
        }
    }

    fun setProviderTab(tab: ProviderTab) {
        _currentProviderTab.value = tab
        _currentScreen.value = when (tab) {
            ProviderTab.DASHBOARD -> AppScreen.ProviderDashboard
            ProviderTab.JOBS -> AppScreen.ProviderJobs
            ProviderTab.QUOTES -> AppScreen.ProviderQuotes
            ProviderTab.EARNINGS -> AppScreen.ProviderEarnings
            ProviderTab.PROFILE -> AppScreen.ProviderProfile
        }
    }

    fun setAdminTab(tab: AdminTab) {
        _currentAdminTab.value = tab
        _currentScreen.value = AppScreen.AdminHome
    }

    // Location selection
    fun updateLocation(city: String, area: String, pincode: String = "442401") {
        _selectedLocation.value = _selectedLocation.value.copy(
            city = city,
            area = area,
            pincode = pincode
        )
        showMessage("Location updated to $area, $city")
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategoryFilter(catId: String?) {
        _selectedCategoryFilter.value = catId
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    // Customer Booking Actions
    fun bookFixedService(
        service: ServiceEntity,
        categoryName: String,
        date: String,
        timeSlot: String,
        address: String,
        notes: String,
        couponCode: String,
        discountAmount: Double,
        paymentMethod: String,
        provider: ProviderEntity?
    ) {
        viewModelScope.launch {
            val bookingId = repository.createFixedBooking(
                customerId = _currentCustomerId.value,
                customerName = "Sunil Sharma",
                customerPhone = "+91 98220 12345",
                service = service,
                categoryName = categoryName,
                scheduledDate = date,
                scheduledTimeSlot = timeSlot,
                address = address.ifBlank { "${_selectedLocation.value.area}, ${_selectedLocation.value.city}" },
                city = _selectedLocation.value.city,
                area = _selectedLocation.value.area,
                problemDescription = notes,
                couponCode = couponCode,
                discountAmount = discountAmount,
                paymentMethod = paymentMethod,
                assignedProvider = provider
            )
            showMessage("Booking Confirmed! Track live updates below.")
            navigateTo(AppScreen.BookingDetail(bookingId))
        }
    }

    fun requestQuote(
        service: ServiceEntity,
        categoryName: String,
        date: String,
        timeSlot: String,
        address: String,
        problemDescription: String
    ) {
        viewModelScope.launch {
            val bookingId = repository.createQuoteRequestBooking(
                customerId = _currentCustomerId.value,
                customerName = "Sunil Sharma",
                customerPhone = "+91 98220 12345",
                service = service,
                categoryName = categoryName,
                scheduledDate = date,
                scheduledTimeSlot = timeSlot,
                address = address.ifBlank { "${_selectedLocation.value.area}, ${_selectedLocation.value.city}" },
                city = _selectedLocation.value.city,
                area = _selectedLocation.value.area,
                problemDescription = problemDescription
            )
            showMessage("Quote Request broadcasted to verified Chandrapur professionals!")
            navigateTo(AppScreen.BookingDetail(bookingId))
        }
    }

    // Quotes
    fun getQuotesForBooking(bookingId: String) = repository.getQuotesForBooking(bookingId)

    fun acceptQuote(bookingId: String, quote: QuoteEntity) {
        viewModelScope.launch {
            repository.acceptQuote(bookingId, quote)
            showMessage("Accepted quote from ${quote.providerName}!")
        }
    }

    fun sendQuote(
        bookingId: String,
        labourCost: Double,
        materialCost: Double,
        duration: String,
        notes: String
    ) {
        viewModelScope.launch {
            val currentPro = allProviders.value.find { it.id == _currentProviderId.value }
                ?: allProviders.value.firstOrNull()
            if (currentPro != null) {
                repository.submitQuote(
                    bookingId = bookingId,
                    providerId = currentPro.id,
                    providerName = "${currentPro.name} (${currentPro.primaryCategory})",
                    providerPhone = currentPro.phone,
                    providerRating = currentPro.rating,
                    labourCost = labourCost,
                    materialCost = materialCost,
                    estimatedDuration = duration,
                    notes = notes
                )
                showMessage("Quotation submitted successfully to customer!")
            }
        }
    }

    // Provider Job Workflow Actions
    fun startJob(bookingId: String, beforeNotes: String = "") {
        updateBookingStatus(bookingId, "STARTED", beforeNotes = beforeNotes)
    }

    fun completeJob(bookingId: String, otp: String, completionNotes: String = "") {
        val booking = allBookings.value.find { it.id == bookingId }
        if (booking != null && booking.completionOtp == otp.trim()) {
            updateBookingStatus(bookingId, "COMPLETED", completionNotes = completionNotes)
            showMessage("Job verified with OTP & completed! ₹${booking.providerEarnings.toInt()} credited to earnings.")
        } else {
            showMessage("Incorrect 4-digit OTP. Please verify with customer.")
        }
    }

    fun updateBookingStatus(
        bookingId: String,
        status: String,
        beforeNotes: String? = null,
        completionNotes: String? = null
    ) {
        viewModelScope.launch {
            val currentPro = allProviders.value.find { it.id == _currentProviderId.value }
            repository.updateBookingStatus(
                bookingId = bookingId,
                newStatus = status,
                providerId = currentPro?.id,
                providerName = currentPro?.name,
                beforeNotes = beforeNotes,
                completionNotes = completionNotes
            )
            showMessage("Job status updated to: $status")
        }
    }

    fun addAdditionalWork(bookingId: String, extraAmount: Double, extraNotes: String) {
        viewModelScope.launch {
            repository.addAdditionalWork(bookingId, extraNotes, extraAmount)
            showMessage("Added additional work: ₹$extraAmount")
        }
    }

    fun toggleProviderOnline(isOnline: Boolean) {
        viewModelScope.launch {
            repository.toggleProviderOnline(_currentProviderId.value, isOnline)
            showMessage(if (isOnline) "You are now ONLINE for bookings in Chandrapur" else "You are OFFLINE")
        }
    }

    // Chat
    fun getMessagesForBooking(bookingId: String) = repository.getMessagesForBooking(bookingId)

    fun sendMessage(bookingId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val (senderId, senderName, senderRole) = when (_currentRole.value) {
                AppRole.CUSTOMER -> Triple(_currentCustomerId.value, "Sunil Sharma", "CUSTOMER")
                AppRole.PROVIDER -> {
                    val pro = allProviders.value.find { it.id == _currentProviderId.value }
                    Triple(_currentProviderId.value, pro?.name ?: "Provider", "PROVIDER")
                }
                AppRole.ADMIN -> Triple("admin_1", "GharFix Support", "ADMIN")
            }
            repository.sendMessage(bookingId, senderId, senderName, senderRole, text.trim())
        }
    }

    // Customer Review
    fun submitReview(
        bookingId: String,
        providerName: String,
        serviceName: String,
        rating: Double,
        comment: String
    ) {
        viewModelScope.launch {
            repository.submitReview(
                bookingId = bookingId,
                customerName = "Sunil Sharma",
                customerArea = "${_selectedLocation.value.area}, ${_selectedLocation.value.city}",
                providerName = providerName,
                serviceName = serviceName,
                rating = rating,
                comment = comment
            )
            showMessage("Thank you! Your review has been published.")
        }
    }

    // Admin Actions
    fun updateProviderKyc(providerId: String, status: String) {
        viewModelScope.launch {
            repository.updateProviderStatus(providerId, status)
            showMessage("Provider verification updated to $status")
        }
    }

    fun updateCommissionRate(newRate: Double) {
        viewModelScope.launch {
            repository.updateCommissionRate(newRate)
            _platformSettings.value = _platformSettings.value.copy(commissionPercentage = newRate)
            showMessage("Platform commission set to $newRate%")
        }
    }

    fun toggleServiceActive(serviceId: String, isActive: Boolean) {
        viewModelScope.launch {
            val s = allServices.value.find { it.id == serviceId }
            if (s != null) {
                repository.addService(s.copy(isActive = isActive))
                showMessage("Service status updated")
            }
        }
    }

    fun addNewService(
        name: String,
        categoryId: String,
        basePrice: Double,
        duration: String,
        isQuote: Boolean
    ) {
        viewModelScope.launch {
            repository.addService(
                ServiceEntity(
                    id = "srv_${UUID.randomUUID().toString().take(6)}",
                    categoryId = categoryId.lowercase().trim(),
                    name = name.trim(),
                    description = "Professional doorstep service in Chandrapur with warranty.",
                    bookingType = if (isQuote) "GET_QUOTE" else "FIXED",
                    basePrice = basePrice,
                    estimatedDuration = duration,
                    iconKey = categoryId.lowercase().take(4),
                    isActive = true
                )
            )
            showMessage("Service '$name' added to catalogue!")
        }
    }

    fun toggleLocationActive(locationId: String, isActive: Boolean) {
        viewModelScope.launch {
            val loc = locations.value.find { it.id == locationId }
            if (loc != null) {
                repository.addLocation(loc.copy(isActive = isActive))
                showMessage("Location status updated")
            }
        }
    }

    fun addNewLocation(city: String, area: String, pincode: String) {
        viewModelScope.launch {
            repository.addLocation(
                LocationEntity(
                    id = "loc_${UUID.randomUUID().toString().take(6)}",
                    city = city.trim(),
                    area = area.trim(),
                    pincode = pincode.trim(),
                    isLaunchCity = (city.equals("Chandrapur", ignoreCase = true)),
                    isActive = true
                )
            )
            showMessage("Added new service area: $area, $city")
        }
    }

    fun raiseSupportTicket(subject: String, message: String) {
        viewModelScope.launch {
            repository.submitSupportTicket(
                userId = _currentCustomerId.value,
                userName = "Sunil Sharma",
                userRole = _currentRole.value.name,
                subject = subject,
                category = "General",
                description = message
            )
            showMessage("Support ticket submitted. Ticket ID generated.")
        }
    }
}
