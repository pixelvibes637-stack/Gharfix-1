package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val role: String, // "CUSTOMER", "PROVIDER", "ADMIN"
    val avatarUrl: String = "",
    val address: String = "",
    val city: String = "Chandrapur",
    val state: String = "Maharashtra",
    val area: String = "Ramnagar",
    val pincode: String = "442401"
)

@Entity(tableName = "service_categories")
data class ServiceCategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconKey: String, // "electrician", "solar", "ac", "plumbing", "cctv", "ro", "paint", "cleaning"
    val description: String,
    val displayOrder: Int = 0,
    val isPopular: Boolean = true
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val bookingType: String, // "FIXED", "GET_QUOTE"
    val basePrice: Double = 0.0,
    val unit: String = "per service", // "per unit", "per visit", "estimated"
    val estimatedDuration: String = "1-2 hrs",
    val iconKey: String = "",
    val isPopular: Boolean = false,
    val rating: Double = 4.8,
    val reviewCount: Int = 42,
    val isActive: Boolean = true,
    val includedItems: String = "", // comma-separated
    val excludedItems: String = "" // comma-separated
)

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val phone: String,
    val email: String,
    val avatarUrl: String = "",
    val rating: Double = 4.9,
    val reviewCount: Int = 58,
    val experienceYears: Int = 6,
    val primaryCategory: String = "Electrician",
    val servicesOffered: String = "Fan Installation, Switchboard Repair, House Wiring",
    val serviceAreas: String = "Tukum, Ramnagar, Civil Lines, Babupeth, Tadoba Road",
    val languages: String = "Hindi, Marathi, English",
    val isVerified: Boolean = true,
    val verificationStatus: String = "APPROVED", // "PENDING", "APPROVED", "REJECTED", "SUSPENDED"
    val aadhaarNumber: String = "XXXX-XXXX-4921",
    val isOnline: Boolean = true,
    val completedJobs: Int = 142,
    val totalEarnings: Double = 54200.0,
    val bankAccount: String = "HDFC Bank - A/C **8912 (UPI: pro@okhdfc)",
    val joinedDate: String = "Jan 2025"
) {
    val kycStatus: String get() = verificationStatus
}

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: String,
    val country: String = "India",
    val state: String = "Maharashtra",
    val city: String,
    val area: String,
    val pincode: String,
    val isLaunchCity: Boolean = true,
    val isActive: Boolean = true
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val bookingNumber: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val providerId: String? = null,
    val providerName: String = "Awaiting Assignment",
    val serviceId: String,
    val serviceName: String,
    val categoryName: String,
    val bookingType: String, // "FIXED", "GET_QUOTE"
    val status: String, // "PENDING", "ACCEPTED", "ON_THE_WAY", "STARTED", "COMPLETED", "CANCELLED", "DISPUTED"
    val scheduledDate: String,
    val scheduledTimeSlot: String,
    val address: String,
    val city: String = "Chandrapur",
    val area: String = "Ramnagar",
    val problemDescription: String = "",
    val basePrice: Double = 0.0,
    val labourPrice: Double = 0.0,
    val materialPrice: Double = 0.0,
    val additionalWorkNotes: String = "",
    val additionalWorkAmount: Double = 0.0,
    val couponCode: String = "",
    val discountAmount: Double = 0.0,
    val platformCommissionRate: Double = 15.0, // 15%
    val platformCommissionAmount: Double = 0.0,
    val providerEarnings: Double = 0.0,
    val totalPrice: Double = 0.0,
    val paymentMethod: String = "UPI (Pay After Service)",
    val paymentStatus: String = "PENDING", // "PENDING", "PAID", "REFUNDED"
    val beforePhotoNotes: String = "",
    val completionPhotoNotes: String = "",
    val completionOtp: String = "5821",
    val createdAt: Long = System.currentTimeMillis(),
    val customerReviewRating: Double = 0.0,
    val customerReviewComment: String = ""
) {
    val customerAddress: String get() = address
}

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey val id: String,
    val bookingId: String,
    val providerId: String,
    val providerName: String,
    val providerRating: Double = 4.9,
    val providerPhone: String,
    val labourCost: Double,
    val materialCost: Double,
    val totalPrice: Double,
    val estimatedDuration: String,
    val notes: String,
    val status: String = "SUBMITTED", // "SUBMITTED", "ACCEPTED", "REJECTED"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val bookingId: String,
    val senderId: String,
    val senderName: String,
    val senderRole: String, // "CUSTOMER", "PROVIDER", "ADMIN"
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
) {
    val text: String get() = messageText
    val timestampText: String get() = "Just now"
}

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val bookingId: String,
    val customerName: String,
    val customerArea: String,
    val providerName: String,
    val serviceName: String,
    val rating: Double,
    val comment: String,
    val dateText: String
)

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey val code: String,
    val title: String,
    val discountPercent: Int,
    val maxDiscount: Double,
    val minBookingAmount: Double,
    val description: String,
    val isActive: Boolean = true
)

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userRole: String,
    val subject: String,
    val category: String,
    val description: String,
    val status: String = "OPEN", // "OPEN", "IN_PROGRESS", "RESOLVED"
    val priority: String = "MEDIUM", // "LOW", "MEDIUM", "HIGH"
    val createdAt: Long = System.currentTimeMillis(),
    val resolutionNotes: String = ""
)

@Entity(tableName = "platform_settings")
data class PlatformSettingEntity(
    @PrimaryKey val settingKey: String,
    val settingValue: String
)

data class PlatformSettingsInfo(
    val commissionPercentage: Double = 15.0,
    val helplineNumber: String = "+91 7172 250000",
    val supportEmail: String = "support@gharfix.in"
)
