package com.example.data.repository

import com.example.data.local.BookingEntity
import com.example.data.local.CouponEntity
import com.example.data.local.GharFixDao
import com.example.data.local.LocationEntity
import com.example.data.local.MessageEntity
import com.example.data.local.PlatformSettingEntity
import com.example.data.local.ProviderEntity
import com.example.data.local.QuoteEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.SeedData
import com.example.data.local.ServiceCategoryEntity
import com.example.data.local.ServiceEntity
import com.example.data.local.SupportTicketEntity
import com.example.data.local.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

class GharFixRepository(
    private val dao: GharFixDao,
    private val appScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    init {
        // Initialize seed data if database is fresh
        appScope.launch(Dispatchers.IO) {
            initializeSeedDataIfNeeded()
        }
    }

    private suspend fun initializeSeedDataIfNeeded() {
        val existingCategories = dao.getAllCategories().firstOrNull()
        if (existingCategories.isNullOrEmpty()) {
            dao.insertUsers(SeedData.getDefaultUsers())
            dao.insertCategories(SeedData.getDefaultCategories())
            dao.insertServices(SeedData.getDefaultServices())
            dao.insertProviders(SeedData.getDefaultProviders())
            dao.insertLocations(SeedData.getDefaultLocations())
            dao.insertCoupons(SeedData.getDefaultCoupons())
            dao.insertBookings(SeedData.getDefaultBookings())
            dao.insertQuotes(SeedData.getDefaultQuotes())
            dao.insertMessages(SeedData.getDefaultMessages())
            dao.insertReviews(SeedData.getDefaultReviews())
            SeedData.getDefaultTickets().forEach { dao.insertSupportTicket(it) }
            SeedData.getDefaultSettings().forEach { dao.setSetting(it) }
        }
    }

    // Category & Services
    val categories: Flow<List<ServiceCategoryEntity>> = dao.getAllCategories()
    val allActiveServices: Flow<List<ServiceEntity>> = dao.getAllActiveServices()
    val allServices: Flow<List<ServiceEntity>> = dao.getAllServices()

    fun getServicesByCategory(categoryId: String): Flow<List<ServiceEntity>> =
        dao.getServicesByCategory(categoryId)

    suspend fun getServiceById(serviceId: String): ServiceEntity? =
        dao.getServiceById(serviceId)

    suspend fun addService(service: ServiceEntity) {
        dao.insertService(service)
    }

    suspend fun updateService(service: ServiceEntity) {
        dao.insertService(service)
    }

    suspend fun deleteService(serviceId: String) {
        dao.deleteServiceById(serviceId)
    }

    // Providers
    val providers: Flow<List<ProviderEntity>> = dao.getAllProviders()
    val approvedProviders: Flow<List<ProviderEntity>> = dao.getApprovedProviders()

    suspend fun getProviderById(providerId: String): ProviderEntity? =
        dao.getProviderById(providerId)

    suspend fun getProviderByUserId(userId: String): ProviderEntity? =
        dao.getProviderByUserId(userId)

    suspend fun updateProviderStatus(providerId: String, status: String) {
        val pro = dao.getProviderById(providerId) ?: return
        val isVerified = (status == "APPROVED")
        dao.updateProvider(pro.copy(verificationStatus = status, isVerified = isVerified))
    }

    suspend fun toggleProviderOnline(providerId: String, isOnline: Boolean) {
        val pro = dao.getProviderById(providerId) ?: return
        dao.updateProvider(pro.copy(isOnline = isOnline))
    }

    suspend fun registerProvider(provider: ProviderEntity) {
        dao.insertProvider(provider)
    }

    // Locations
    val activeLocations: Flow<List<LocationEntity>> = dao.getAllActiveLocations()
    val distinctCities: Flow<List<String>> = dao.getDistinctCities()

    fun getAreasForCity(city: String): Flow<List<String>> =
        dao.getAreasForCity(city)

    suspend fun addLocation(location: LocationEntity) {
        dao.insertLocation(location)
    }

    // Bookings
    val allBookings: Flow<List<BookingEntity>> = dao.getAllBookings()
    val quoteRequestBookings: Flow<List<BookingEntity>> = dao.getAllQuoteRequestBookings()

    fun getBookingsForCustomer(customerId: String): Flow<List<BookingEntity>> =
        dao.getBookingsForCustomer(customerId)

    fun getBookingsForProvider(providerId: String): Flow<List<BookingEntity>> =
        dao.getBookingsForProvider(providerId)

    suspend fun getBookingById(bookingId: String): BookingEntity? =
        dao.getBookingById(bookingId)

    fun getBookingFlowById(bookingId: String): Flow<BookingEntity?> =
        dao.getBookingFlowById(bookingId)

    suspend fun createFixedBooking(
        customerId: String,
        customerName: String,
        customerPhone: String,
        service: ServiceEntity,
        categoryName: String,
        scheduledDate: String,
        scheduledTimeSlot: String,
        address: String,
        city: String,
        area: String,
        problemDescription: String,
        couponCode: String,
        discountAmount: Double,
        paymentMethod: String,
        assignedProvider: ProviderEntity? = null
    ): String {
        val commissionRate = getCommissionRate()
        val basePrice = service.basePrice
        val finalPrice = (basePrice - discountAmount).coerceAtLeast(0.0)
        val commissionAmount = (finalPrice * commissionRate) / 100.0
        val providerEarnings = finalPrice - commissionAmount

        val bookingNumber = "GF-CH-${(1000..9999).random()}"
        val bookingId = "book_${UUID.randomUUID().toString().take(8)}"
        val otp = (1000..9999).random().toString()

        val booking = BookingEntity(
            id = bookingId,
            bookingNumber = bookingNumber,
            customerId = customerId,
            customerName = customerName,
            customerPhone = customerPhone,
            providerId = assignedProvider?.id,
            providerName = assignedProvider?.name ?: "Auto Matching Pro",
            serviceId = service.id,
            serviceName = service.name,
            categoryName = categoryName,
            bookingType = "FIXED",
            status = if (assignedProvider != null) "ACCEPTED" else "PENDING",
            scheduledDate = scheduledDate,
            scheduledTimeSlot = scheduledTimeSlot,
            address = address,
            city = city,
            area = area,
            problemDescription = problemDescription,
            basePrice = basePrice,
            labourPrice = basePrice,
            materialPrice = 0.0,
            couponCode = couponCode,
            discountAmount = discountAmount,
            platformCommissionRate = commissionRate,
            platformCommissionAmount = commissionAmount,
            providerEarnings = providerEarnings,
            totalPrice = finalPrice,
            paymentMethod = paymentMethod,
            paymentStatus = "PENDING",
            completionOtp = otp,
            createdAt = System.currentTimeMillis()
        )

        dao.insertBooking(booking)

        // Insert initial message
        dao.insertMessage(
            MessageEntity(
                id = "msg_${UUID.randomUUID().toString().take(8)}",
                bookingId = bookingId,
                senderId = "system",
                senderName = "GharFix Support",
                senderRole = "ADMIN",
                messageText = "Namaste $customerName! Your booking for ${service.name} has been received. Our verified professional will attend as per your scheduled slot ($scheduledDate, $scheduledTimeSlot).",
                timestamp = System.currentTimeMillis()
            )
        )

        return bookingId
    }

    suspend fun createQuoteRequestBooking(
        customerId: String,
        customerName: String,
        customerPhone: String,
        service: ServiceEntity,
        categoryName: String,
        scheduledDate: String,
        scheduledTimeSlot: String,
        address: String,
        city: String,
        area: String,
        problemDescription: String
    ): String {
        val bookingNumber = "GF-QR-${(1000..9999).random()}"
        val bookingId = "book_${UUID.randomUUID().toString().take(8)}"
        val otp = (1000..9999).random().toString()

        val booking = BookingEntity(
            id = bookingId,
            bookingNumber = bookingNumber,
            customerId = customerId,
            customerName = customerName,
            customerPhone = customerPhone,
            providerId = null,
            providerName = "Awaiting Quotations",
            serviceId = service.id,
            serviceName = service.name,
            categoryName = categoryName,
            bookingType = "GET_QUOTE",
            status = "PENDING",
            scheduledDate = scheduledDate,
            scheduledTimeSlot = scheduledTimeSlot,
            address = address,
            city = city,
            area = area,
            problemDescription = problemDescription,
            basePrice = 0.0,
            labourPrice = 0.0,
            materialPrice = 0.0,
            platformCommissionRate = getCommissionRate(),
            totalPrice = 0.0,
            paymentMethod = "Milestone UPI / Cash",
            paymentStatus = "PENDING",
            completionOtp = otp,
            createdAt = System.currentTimeMillis()
        )

        dao.insertBooking(booking)

        dao.insertMessage(
            MessageEntity(
                id = "msg_${UUID.randomUUID().toString().take(8)}",
                bookingId = bookingId,
                senderId = "system",
                senderName = "GharFix Quote Hub",
                senderRole = "ADMIN",
                messageText = "Quote request broadcasted to all verified ${service.name} professionals in $area, $city. You will receive customized quotes shortly.",
                timestamp = System.currentTimeMillis()
            )
        )

        return bookingId
    }

    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: String,
        providerId: String? = null,
        providerName: String? = null,
        beforeNotes: String? = null,
        completionNotes: String? = null
    ) {
        val current = dao.getBookingById(bookingId) ?: return
        var updated = current.copy(
            status = newStatus,
            providerId = providerId ?: current.providerId,
            providerName = providerName ?: current.providerName
        )
        if (beforeNotes != null) {
            updated = updated.copy(beforePhotoNotes = beforeNotes)
        }
        if (completionNotes != null) {
            updated = updated.copy(
                completionPhotoNotes = completionNotes,
                paymentStatus = "PAID"
            )
        }
        dao.updateBooking(updated)
    }

    suspend fun addAdditionalWork(bookingId: String, extraNotes: String, extraAmount: Double) {
        val current = dao.getBookingById(bookingId) ?: return
        val newTotal = current.totalPrice + extraAmount
        val commissionRate = current.platformCommissionRate
        val commissionAmount = (newTotal * commissionRate) / 100.0
        val providerEarnings = newTotal - commissionAmount

        val updated = current.copy(
            additionalWorkNotes = extraNotes,
            additionalWorkAmount = extraAmount,
            totalPrice = newTotal,
            platformCommissionAmount = commissionAmount,
            providerEarnings = providerEarnings
        )
        dao.updateBooking(updated)
    }

    // Quotes
    fun getQuotesForBooking(bookingId: String): Flow<List<QuoteEntity>> =
        dao.getQuotesForBooking(bookingId)

    fun getQuotesForProvider(providerId: String): Flow<List<QuoteEntity>> =
        dao.getQuotesForProvider(providerId)

    suspend fun submitQuote(
        bookingId: String,
        providerId: String,
        providerName: String,
        providerPhone: String,
        providerRating: Double,
        labourCost: Double,
        materialCost: Double,
        estimatedDuration: String,
        notes: String
    ) {
        val totalPrice = labourCost + materialCost
        val quote = QuoteEntity(
            id = "quote_${UUID.randomUUID().toString().take(8)}",
            bookingId = bookingId,
            providerId = providerId,
            providerName = providerName,
            providerPhone = providerPhone,
            providerRating = providerRating,
            labourCost = labourCost,
            materialCost = materialCost,
            totalPrice = totalPrice,
            estimatedDuration = estimatedDuration,
            notes = notes,
            status = "SUBMITTED",
            createdAt = System.currentTimeMillis()
        )
        dao.insertQuote(quote)
    }

    suspend fun acceptQuote(bookingId: String, quote: QuoteEntity) {
        val booking = dao.getBookingById(bookingId) ?: return
        val commissionRate = booking.platformCommissionRate
        val commissionAmount = (quote.totalPrice * commissionRate) / 100.0
        val providerEarnings = quote.totalPrice - commissionAmount

        val updatedBooking = booking.copy(
            providerId = quote.providerId,
            providerName = quote.providerName,
            labourPrice = quote.labourCost,
            materialPrice = quote.materialCost,
            totalPrice = quote.totalPrice,
            platformCommissionAmount = commissionAmount,
            providerEarnings = providerEarnings,
            status = "ACCEPTED"
        )
        dao.updateBooking(updatedBooking)
        dao.updateQuote(quote.copy(status = "ACCEPTED"))

        dao.insertMessage(
            MessageEntity(
                id = "msg_${UUID.randomUUID().toString().take(8)}",
                bookingId = bookingId,
                senderId = "system",
                senderName = "GharFix System",
                senderRole = "ADMIN",
                messageText = "Quotation of ₹${quote.totalPrice.toInt()} by ${quote.providerName} has been ACCEPTED by customer. Provider will contact you for site work initiation.",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    // Messages
    fun getMessagesForBooking(bookingId: String): Flow<List<MessageEntity>> =
        dao.getMessagesForBooking(bookingId)

    suspend fun sendMessage(bookingId: String, senderId: String, senderName: String, senderRole: String, text: String) {
        dao.insertMessage(
            MessageEntity(
                id = "msg_${UUID.randomUUID().toString().take(8)}",
                bookingId = bookingId,
                senderId = senderId,
                senderName = senderName,
                senderRole = senderRole,
                messageText = text,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    // Reviews
    val reviews: Flow<List<ReviewEntity>> = dao.getAllReviews()

    suspend fun submitReview(
        bookingId: String,
        customerName: String,
        customerArea: String,
        providerName: String,
        serviceName: String,
        rating: Double,
        comment: String
    ) {
        val review = ReviewEntity(
            id = "rev_${UUID.randomUUID().toString().take(8)}",
            bookingId = bookingId,
            customerName = customerName,
            customerArea = customerArea,
            providerName = providerName,
            serviceName = serviceName,
            rating = rating,
            comment = comment,
            dateText = "Just now"
        )
        dao.insertReview(review)

        val booking = dao.getBookingById(bookingId)
        if (booking != null) {
            dao.updateBooking(
                booking.copy(
                    customerReviewRating = rating,
                    customerReviewComment = comment
                )
            )
        }
    }

    // Coupons
    val activeCoupons: Flow<List<CouponEntity>> = dao.getActiveCoupons()

    suspend fun validateCoupon(code: String, amount: Double): Pair<Boolean, Double> {
        val coupon = dao.getCouponByCode(code.trim().uppercase()) ?: return Pair(false, 0.0)
        if (amount < coupon.minBookingAmount) {
            return Pair(false, 0.0)
        }
        val discount = ((amount * coupon.discountPercent) / 100.0).coerceAtMost(coupon.maxDiscount)
        return Pair(true, discount)
    }

    // Support Tickets
    val supportTickets: Flow<List<SupportTicketEntity>> = dao.getAllSupportTickets()

    fun getSupportTicketsForUser(userId: String): Flow<List<SupportTicketEntity>> =
        dao.getSupportTicketsForUser(userId)

    suspend fun submitSupportTicket(
        userId: String,
        userName: String,
        userRole: String,
        subject: String,
        category: String,
        description: String,
        priority: String = "MEDIUM"
    ) {
        val ticket = SupportTicketEntity(
            id = "tkt_${UUID.randomUUID().toString().take(8)}",
            userId = userId,
            userName = userName,
            userRole = userRole,
            subject = subject,
            category = category,
            description = description,
            status = "OPEN",
            priority = priority,
            createdAt = System.currentTimeMillis()
        )
        dao.insertSupportTicket(ticket)
    }

    suspend fun resolveSupportTicket(ticketId: String, notes: String) {
        val all = dao.getAllSupportTickets().firstOrNull() ?: return
        val target = all.find { it.id == ticketId } ?: return
        dao.updateSupportTicket(target.copy(status = "RESOLVED", resolutionNotes = notes))
    }

    // Commission & Settings
    suspend fun getCommissionRate(): Double {
        val value = dao.getSetting("platform_commission_percent")
        return value?.toDoubleOrNull() ?: 15.0
    }

    suspend fun updateCommissionRate(newRate: Double) {
        dao.setSetting(
            PlatformSettingEntity(
                settingKey = "platform_commission_percent",
                settingValue = newRate.toString()
            )
        )
    }
}
