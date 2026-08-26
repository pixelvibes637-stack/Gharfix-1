package com.example.data.local

object SeedData {

    fun getDefaultUsers(): List<UserEntity> = listOf(
        UserEntity(
            id = "user_cust_1",
            name = "Aniket Sharma",
            phone = "+91 98223 45678",
            email = "aniket.sharma@example.com",
            role = "CUSTOMER",
            avatarUrl = "",
            address = "Plot 42, Anand Nagar, Near Datta Mandir",
            city = "Chandrapur",
            state = "Maharashtra",
            area = "Ramnagar",
            pincode = "442401"
        ),
        UserEntity(
            id = "user_cust_2",
            name = "Pooja Patil",
            phone = "+91 97654 32100",
            email = "pooja.patil@example.com",
            role = "CUSTOMER",
            avatarUrl = "",
            address = "Flat 302, Sai Heights, Tukum",
            city = "Chandrapur",
            state = "Maharashtra",
            area = "Tukum",
            pincode = "442401"
        ),
        UserEntity(
            id = "user_pro_1",
            name = "Suresh Chauhan",
            phone = "+91 94221 88990",
            email = "suresh.electric@gharfix.pro",
            role = "PROVIDER",
            avatarUrl = "",
            address = "Shop 12, Gandhi Chowk",
            city = "Chandrapur",
            state = "Maharashtra",
            area = "Ramnagar",
            pincode = "442401"
        ),
        UserEntity(
            id = "user_admin_1",
            name = "GharFix Operations Admin",
            phone = "+91 7172 250000",
            email = "admin@gharfix.in",
            role = "ADMIN",
            avatarUrl = "",
            address = "GharFix Hub, Civil Lines",
            city = "Chandrapur",
            state = "Maharashtra",
            area = "Civil Lines",
            pincode = "442401"
        )
    )

    fun getDefaultCategories(): List<ServiceCategoryEntity> = listOf(
        ServiceCategoryEntity(
            id = "cat_electrician",
            name = "Electrician",
            iconKey = "electrician",
            description = "Wiring, Fan, Switchboard, Inverter & MCB fixes",
            displayOrder = 1,
            isPopular = true
        ),
        ServiceCategoryEntity(
            id = "cat_solar",
            name = "Solar",
            iconKey = "solar",
            description = "Rooftop solar, Panel cleaning & Inverter setup",
            displayOrder = 2,
            isPopular = true
        ),
        ServiceCategoryEntity(
            id = "cat_ac",
            name = "AC Service",
            iconKey = "ac",
            description = "Jet pump wash, Gas refill, Cooling repair & Installation",
            displayOrder = 3,
            isPopular = true
        ),
        ServiceCategoryEntity(
            id = "cat_plumbing",
            name = "Plumbing",
            iconKey = "plumbing",
            description = "Taps, Pipes, Flush tanks, Motor & Water tank automation",
            displayOrder = 4,
            isPopular = true
        ),
        ServiceCategoryEntity(
            id = "cat_cctv",
            name = "CCTV",
            iconKey = "cctv",
            description = "HD/IP camera install, DVR/NVR setup & Mobile remote view",
            displayOrder = 5,
            isPopular = true
        ),
        ServiceCategoryEntity(
            id = "cat_ro",
            name = "RO Purifier",
            iconKey = "ro",
            description = "Filter service, Membrane change, TDS tuning & Leak repair",
            displayOrder = 6,
            isPopular = true
        ),
        ServiceCategoryEntity(
            id = "cat_painting",
            name = "Painting",
            iconKey = "paint",
            description = "Full home painting, Waterproofing & Texture design",
            displayOrder = 7,
            isPopular = false
        ),
        ServiceCategoryEntity(
            id = "cat_cleaning",
            name = "Deep Cleaning",
            iconKey = "cleaning",
            description = "Full home, Kitchen & Bathroom sanitization",
            displayOrder = 8,
            isPopular = false
        )
    )

    fun getDefaultServices(): List<ServiceEntity> = listOf(
        // Electrician - Fixed
        ServiceEntity(
            id = "srv_fan_install",
            categoryId = "cat_electrician",
            name = "Ceiling Fan Installation & Repair",
            description = "Complete mounting, balancing, regulator connection and wiring check.",
            bookingType = "FIXED",
            basePrice = 199.0,
            unit = "per fan",
            estimatedDuration = "45 mins",
            iconKey = "electrician",
            isPopular = true,
            rating = 4.9,
            reviewCount = 124,
            includedItems = "Safety clamp inspection, blade balancing, regulator connection test",
            excludedItems = "New fan cost, new step regulator hardware"
        ),
        ServiceEntity(
            id = "srv_switchboard_fix",
            categoryId = "cat_electrician",
            name = "Switchboard & Socket Repair",
            description = "Fix burnt switches, sparking sockets, MCB trip issues and internal wire loose contacts.",
            bookingType = "FIXED",
            basePrice = 149.0,
            unit = "up to 2 points",
            estimatedDuration = "30 mins",
            iconKey = "electrician",
            isPopular = true,
            rating = 4.8,
            reviewCount = 89,
            includedItems = "Point testing, loose wire tightening, grounding check",
            excludedItems = "Modular switch/socket hardware replacement cost"
        ),
        ServiceEntity(
            id = "srv_inverter_check",
            categoryId = "cat_electrician",
            name = "Inverter & Battery Servicing",
            description = "Battery distilled water top-up, terminal cleaning, backup duration check and charging testing.",
            bookingType = "FIXED",
            basePrice = 299.0,
            unit = "per system",
            estimatedDuration = "1 hour",
            iconKey = "electrician",
            isPopular = false,
            rating = 4.9,
            reviewCount = 56,
            includedItems = "Acid gravity check, distilled water top-up, wiring terminal de-sulfation",
            excludedItems = "New battery or inverter PCB replacement"
        ),
        // Electrician - Get Quote
        ServiceEntity(
            id = "srv_house_wiring",
            categoryId = "cat_electrician",
            name = "Complete House Wiring & Piping",
            description = "Concealed wall chasing, PVC conduit laying, MCB distribution board installation and full house earthing.",
            bookingType = "GET_QUOTE",
            basePrice = 0.0,
            unit = "custom quotation",
            estimatedDuration = "3-7 days",
            iconKey = "electrician",
            isPopular = true,
            rating = 4.9,
            reviewCount = 37,
            includedItems = "Site visit, load calculation, quotation with labour and wire brand options",
            excludedItems = "Government electricity board meter fees"
        ),

        // Solar - Fixed
        ServiceEntity(
            id = "srv_solar_cleaning",
            categoryId = "cat_solar",
            name = "Solar Panel Cleaning & Maintenance",
            description = "High-pressure de-mineralized water wash, dust removal, inverter generation check and cable inspection.",
            bookingType = "FIXED",
            basePrice = 499.0,
            unit = "up to 10 panels",
            estimatedDuration = "1-2 hrs",
            iconKey = "solar",
            isPopular = true,
            rating = 4.9,
            reviewCount = 64,
            includedItems = "Chemical-free microfiber cleaning, generation output logging, loose connector check",
            excludedItems = "Inverter component replacements"
        ),
        // Solar - Get Quote
        ServiceEntity(
            id = "srv_solar_install",
            categoryId = "cat_solar",
            name = "Rooftop Solar Plant (On-Grid / Hybrid)",
            description = "Custom 3kW to 10kW+ solar installation with PM Surya Ghar Muft Bijli Yojana subsidy guidance, net metering and 25-yr warranty panels.",
            bookingType = "GET_QUOTE",
            basePrice = 0.0,
            unit = "custom quotation",
            estimatedDuration = "2-5 days",
            iconKey = "solar",
            isPopular = true,
            rating = 5.0,
            reviewCount = 48,
            includedItems = "Roof shadow analysis, structure fabrication, Tier-1 Mono PERC panels, net-metering liaison",
            excludedItems = "Roof structural waterproofing if required beforehand"
        ),

        // AC - Fixed
        ServiceEntity(
            id = "srv_ac_deep_clean",
            categoryId = "cat_ac",
            name = "Split / Window AC Jet Pump Service",
            description = "Intense foam jet wash of indoor cooling coil, blower wheel, filter cleaning and outdoor condenser flush.",
            bookingType = "FIXED",
            basePrice = 499.0,
            unit = "per AC",
            estimatedDuration = "1 hour",
            iconKey = "ac",
            isPopular = true,
            rating = 4.9,
            reviewCount = 210,
            includedItems = "Foam wash jacket protection, indoor/outdoor coil wash, drain pipe unclogging, amp check",
            excludedItems = "Gas charging and spare parts"
        ),
        ServiceEntity(
            id = "srv_ac_gas_refill",
            categoryId = "cat_ac",
            name = "AC Gas Refill & Leak Testing",
            description = "Nitrogen pressure leak detection, copper brazing, vacuumization and 100% pure R32/R410A refrigerant charge.",
            bookingType = "FIXED",
            basePrice = 1499.0,
            unit = "per AC",
            estimatedDuration = "2 hours",
            iconKey = "ac",
            isPopular = true,
            rating = 4.8,
            reviewCount = 95,
            includedItems = "Leak diagnosis, vacuum pump purging, full refrigerant weigh-in charge",
            excludedItems = "Compressor replacement"
        ),

        // Plumbing - Fixed
        ServiceEntity(
            id = "srv_tap_leak_fix",
            categoryId = "cat_plumbing",
            name = "Tap & Pipeline Leakage Repair",
            description = "Fix dripping taps, leaking angle valves, loose connectors and Teflon seal replacement.",
            bookingType = "FIXED",
            basePrice = 199.0,
            unit = "up to 2 taps",
            estimatedDuration = "40 mins",
            iconKey = "plumbing",
            isPopular = true,
            rating = 4.8,
            reviewCount = 143,
            includedItems = "Washer replacement, spindle repair, Teflon tape sealing, pressure testing",
            excludedItems = "New luxury brass bib cock / diverter unit cost"
        ),
        ServiceEntity(
            id = "srv_flush_tank_fix",
            categoryId = "cat_plumbing",
            name = "Flush Tank & Sanitary Fitting",
            description = "Repair continuous water overflow, ball-valve replacement, syphon fix or new cistern installation.",
            bookingType = "FIXED",
            basePrice = 349.0,
            unit = "per cistern",
            estimatedDuration = "1 hour",
            iconKey = "plumbing",
            isPopular = false,
            rating = 4.7,
            reviewCount = 68,
            includedItems = "Internal mechanism alignment, inlet float valve tune, overflow stoppage",
            excludedItems = "New ceramic cistern hardware"
        ),
        // Plumbing - Get Quote
        ServiceEntity(
            id = "srv_plumbing_overhaul",
            categoryId = "cat_plumbing",
            name = "Complete Bathroom & Kitchen Plumbing Pipeline",
            description = "Full CPVC/UPVC pipe overhaul, concealed diverter fitting, water tank overflow automation and sewage lines.",
            bookingType = "GET_QUOTE",
            basePrice = 0.0,
            unit = "custom quotation",
            estimatedDuration = "2-4 days",
            iconKey = "plumbing",
            isPopular = true,
            rating = 4.9,
            reviewCount = 31,
            includedItems = "Detailed site inspection, pressure gauge test, pipeline blueprint & itemized quote",
            excludedItems = "Tile breaking repair if structural"
        ),

        // CCTV - Fixed
        ServiceEntity(
            id = "srv_cctv_install_fixed",
            categoryId = "cat_cctv",
            name = "CCTV Camera Installation & Angle Tuning",
            description = "Mount dome/bullet camera, connect BNC/DC jack, cable routing to DVR and field-of-view calibration.",
            bookingType = "FIXED",
            basePrice = 349.0,
            unit = "per camera",
            estimatedDuration = "1 hour",
            iconKey = "cctv",
            isPopular = true,
            rating = 4.8,
            reviewCount = 74,
            includedItems = "Drilling, mounting, connector crimping, mobile view configuration on phone",
            excludedItems = "Camera, wire roll, SMPS power supply and hard drive hardware"
        ),
        // CCTV - Get Quote
        ServiceEntity(
            id = "srv_cctv_project",
            categoryId = "cat_cctv",
            name = "Complete Commercial / Home CCTV Surveillance Setup",
            description = "4 to 16 Channel 5MP ColorVu / IP Camera system with Night Vision, Cloud Storage, NVR and multi-monitor display.",
            bookingType = "GET_QUOTE",
            basePrice = 0.0,
            unit = "custom quotation",
            estimatedDuration = "1-2 days",
            iconKey = "cctv",
            isPopular = true,
            rating = 4.9,
            reviewCount = 52,
            includedItems = "Premises security survey, blind spot analysis, wire casing & quotation",
            excludedItems = "Broadband Wi-Fi connection monthly subscription"
        ),

        // RO - Fixed
        ServiceEntity(
            id = "srv_ro_service",
            categoryId = "cat_ro",
            name = "RO Filter Replacement & General Servicing",
            description = "Replacement of Pre-carbon, Sediment filter, Spun candle filter, booster pump testing and sanitization.",
            bookingType = "FIXED",
            basePrice = 399.0,
            unit = "service + inspection",
            estimatedDuration = "45 mins",
            iconKey = "ro",
            isPopular = true,
            rating = 4.9,
            reviewCount = 188,
            includedItems = "TDS input/output testing, pipe flushing, pressure pump test, leak arrest",
            excludedItems = "RO Membrane and UV Lamp if replacement needed"
        ),
        ServiceEntity(
            id = "srv_ro_membrane_change",
            categoryId = "cat_ro",
            name = "RO Membrane Replacement & TDS Tuning",
            description = "High-TDS 80 GPD Filmtec/Vontron membrane installation, flow restrictor replacement and pure sweet water calibration.",
            bookingType = "FIXED",
            basePrice = 1199.0,
            unit = "all inclusive",
            estimatedDuration = "1 hour",
            iconKey = "ro",
            isPopular = true,
            rating = 4.9,
            reviewCount = 112,
            includedItems = "Brand new 80 GPD membrane, FR450 flow restrictor, 6-month warranty",
            excludedItems = "External booster pump motor replacement"
        )
    )

    fun getDefaultProviders(): List<ProviderEntity> = listOf(
        ProviderEntity(
            id = "pro_1",
            userId = "user_pro_1",
            name = "Suresh Chauhan",
            phone = "+91 94221 88990",
            email = "suresh.electric@gharfix.pro",
            rating = 4.9,
            reviewCount = 84,
            experienceYears = 8,
            primaryCategory = "Electrician & Solar",
            servicesOffered = "Ceiling Fan, Switchboard, House Wiring, Solar Rooftop, Inverter",
            serviceAreas = "Ramnagar, Tukum, Civil Lines, Babupeth, Tadoba Road",
            languages = "Hindi, Marathi, English",
            isVerified = true,
            verificationStatus = "APPROVED",
            aadhaarNumber = "XXXX-XXXX-4921",
            isOnline = true,
            completedJobs = 196,
            totalEarnings = 78450.0,
            bankAccount = "State Bank of India - A/C **4512 (UPI: suresh@oksbi)"
        ),
        ProviderEntity(
            id = "pro_2",
            userId = "user_pro_2",
            name = "Rajesh Shinde",
            phone = "+91 98812 33445",
            email = "rajesh.ac@gharfix.pro",
            rating = 4.8,
            reviewCount = 67,
            experienceYears = 6,
            primaryCategory = "AC Service",
            servicesOffered = "AC Deep Jet Cleaning, Gas Refill, AC Installation",
            serviceAreas = "Civil Lines, Tukum, Ramnagar, Datala, Bengali Camp",
            languages = "Marathi, Hindi",
            isVerified = true,
            verificationStatus = "APPROVED",
            aadhaarNumber = "XXXX-XXXX-8833",
            isOnline = true,
            completedJobs = 148,
            totalEarnings = 62100.0,
            bankAccount = "HDFC Bank - A/C **8912 (UPI: rajesh.ac@okhdfc)"
        ),
        ProviderEntity(
            id = "pro_3",
            userId = "user_pro_3",
            name = "Amit Meshram",
            phone = "+91 93701 44556",
            email = "amit.plumb@gharfix.pro",
            rating = 4.9,
            reviewCount = 92,
            experienceYears = 9,
            primaryCategory = "Plumbing & RO",
            servicesOffered = "Tap Repair, Flush Tank, Pipeline Overhaul, RO Servicing",
            serviceAreas = "Babupeth, Rayatwari, Ramnagar, Ballarpur Road",
            languages = "Marathi, Hindi",
            isVerified = true,
            verificationStatus = "APPROVED",
            aadhaarNumber = "XXXX-XXXX-1904",
            isOnline = true,
            completedJobs = 215,
            totalEarnings = 89300.0,
            bankAccount = "Bank of Maharashtra - A/C **6621 (UPI: amit.m@mahb)"
        ),
        ProviderEntity(
            id = "pro_4",
            userId = "user_pro_4",
            name = "Vikram Jadhav",
            phone = "+91 91580 99887",
            email = "vikram.cctv@gharfix.pro",
            rating = 4.7,
            reviewCount = 29,
            experienceYears = 4,
            primaryCategory = "CCTV & Security",
            servicesOffered = "CCTV Camera Installation, DVR Setup, Network Cabling",
            serviceAreas = "Tadoba Road, Tukum, Ghugus Road, Mul Road",
            languages = "Hindi, Marathi, English",
            isVerified = false,
            verificationStatus = "PENDING",
            aadhaarNumber = "XXXX-XXXX-7712",
            isOnline = false,
            completedJobs = 34,
            totalEarnings = 14200.0,
            bankAccount = "Axis Bank - A/C **3309 (UPI: vikram@okaxis)"
        )
    )

    fun getDefaultLocations(): List<LocationEntity> = listOf(
        // Chandrapur Areas (Launch City)
        LocationEntity(id = "loc_ch_1", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Ramnagar", pincode = "442401", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_2", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Tukum", pincode = "442401", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_3", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Civil Lines", pincode = "442401", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_4", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Babupeth", pincode = "442403", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_5", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Tadoba Road", pincode = "442401", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_6", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Datala", pincode = "442402", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_7", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Bengali Camp", pincode = "442402", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_8", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Ballarpur Road", pincode = "442401", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_9", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Ghugus Road", pincode = "442401", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_10", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Mul Road", pincode = "442402", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_11", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Bajar Ward", pincode = "442402", isLaunchCity = true, isActive = true),
        LocationEntity(id = "loc_ch_12", country = "India", state = "Maharashtra", city = "Chandrapur", area = "Rayatwari", pincode = "442403", isLaunchCity = true, isActive = true),

        // Expansion Cities ready in architecture
        LocationEntity(id = "loc_ng_1", country = "India", state = "Maharashtra", city = "Nagpur", area = "Dharampeth", pincode = "440010", isLaunchCity = false, isActive = true),
        LocationEntity(id = "loc_ng_2", country = "India", state = "Maharashtra", city = "Nagpur", area = "Sitabuldi", pincode = "440012", isLaunchCity = false, isActive = true),
        LocationEntity(id = "loc_pn_1", country = "India", state = "Maharashtra", city = "Pune", area = "Kothrud", pincode = "411038", isLaunchCity = false, isActive = true),
        LocationEntity(id = "loc_pn_2", country = "India", state = "Maharashtra", city = "Pune", area = "Wakad", pincode = "411057", isLaunchCity = false, isActive = true),
        LocationEntity(id = "loc_mb_1", country = "India", state = "Maharashtra", city = "Mumbai", area = "Andheri West", pincode = "400058", isLaunchCity = false, isActive = true),
        LocationEntity(id = "loc_wr_1", country = "India", state = "Maharashtra", city = "Wardha", area = "Sevagram Road", pincode = "442001", isLaunchCity = false, isActive = true)
    )

    fun getDefaultCoupons(): List<CouponEntity> = listOf(
        CouponEntity(
            code = "FIRSTFIX",
            title = "First Booking Discount",
            discountPercent = 20,
            maxDiscount = 150.0,
            minBookingAmount = 299.0,
            description = "Get 20% off up to ₹150 on your first home service",
            isActive = true
        ),
        CouponEntity(
            code = "GHARFIX50",
            title = "Flat ₹50 Off",
            discountPercent = 15,
            maxDiscount = 50.0,
            minBookingAmount = 199.0,
            description = "Save ₹50 on any quick repair in Chandrapur",
            isActive = true
        ),
        CouponEntity(
            code = "SOLAR500",
            title = "Solar Special",
            discountPercent = 10,
            maxDiscount = 500.0,
            minBookingAmount = 999.0,
            description = "Save up to ₹500 on Solar panel cleaning & checkup",
            isActive = true
        )
    )

    fun getDefaultBookings(): List<BookingEntity> = listOf(
        // 1. Active Fixed Price Booking in progress
        BookingEntity(
            id = "book_101",
            bookingNumber = "GF-CH-2026-101",
            customerId = "user_cust_1",
            customerName = "Aniket Sharma",
            customerPhone = "+91 98223 45678",
            providerId = "pro_2",
            providerName = "Rajesh Shinde",
            serviceId = "srv_ac_deep_clean",
            serviceName = "Split / Window AC Jet Pump Service",
            categoryName = "AC Service",
            bookingType = "FIXED",
            status = "ON_THE_WAY",
            scheduledDate = "Today",
            scheduledTimeSlot = "11:00 AM - 01:00 PM",
            address = "Plot 42, Anand Nagar, Near Datta Mandir",
            city = "Chandrapur",
            area = "Ramnagar",
            problemDescription = "Split AC cooling is low and emitting bad odor. Needs full foam jet wash.",
            basePrice = 499.0,
            labourPrice = 499.0,
            materialPrice = 0.0,
            couponCode = "GHARFIX50",
            discountAmount = 50.0,
            platformCommissionRate = 15.0,
            platformCommissionAmount = 67.35,
            providerEarnings = 381.65,
            totalPrice = 449.0,
            paymentMethod = "UPI (Pay After Service)",
            paymentStatus = "PENDING",
            beforePhotoNotes = "Customer confirmed 1.5 Ton Voltas Split AC",
            completionOtp = "6294",
            createdAt = System.currentTimeMillis() - 7200000
        ),
        // 2. Active "Get Quote" Booking with submitted quotes
        BookingEntity(
            id = "book_102",
            bookingNumber = "GF-CH-2026-102",
            customerId = "user_cust_1",
            customerName = "Aniket Sharma",
            customerPhone = "+91 98223 45678",
            providerId = null,
            providerName = "Comparing Quotes",
            serviceId = "srv_solar_install",
            serviceName = "Rooftop Solar Plant (On-Grid / Hybrid)",
            categoryName = "Solar",
            bookingType = "GET_QUOTE",
            status = "PENDING",
            scheduledDate = "This Weekend",
            scheduledTimeSlot = "10:00 AM - 02:00 PM",
            address = "Plot 42, Anand Nagar, Near Datta Mandir",
            city = "Chandrapur",
            area = "Ramnagar",
            problemDescription = "Want 5kW On-Grid solar rooftop with subsidy assistance for RCC roof (approx 650 sq ft shadow-free).",
            basePrice = 0.0,
            labourPrice = 0.0,
            materialPrice = 0.0,
            platformCommissionRate = 15.0,
            totalPrice = 0.0,
            paymentMethod = "Milestone UPI / Bank Transfer",
            paymentStatus = "PENDING",
            completionOtp = "9183",
            createdAt = System.currentTimeMillis() - 18000000
        ),
        // 3. Completed Booking with review
        BookingEntity(
            id = "book_103",
            bookingNumber = "GF-CH-2026-103",
            customerId = "user_cust_1",
            customerName = "Aniket Sharma",
            customerPhone = "+91 98223 45678",
            providerId = "pro_1",
            providerName = "Suresh Chauhan",
            serviceId = "srv_fan_install",
            serviceName = "Ceiling Fan Installation & Repair",
            categoryName = "Electrician",
            bookingType = "FIXED",
            status = "COMPLETED",
            scheduledDate = "Yesterday",
            scheduledTimeSlot = "04:00 PM - 06:00 PM",
            address = "Plot 42, Anand Nagar, Near Datta Mandir",
            city = "Chandrapur",
            area = "Ramnagar",
            problemDescription = "New Orient BLDC fan assembly and ceiling hook hanging in living room.",
            basePrice = 199.0,
            labourPrice = 199.0,
            materialPrice = 0.0,
            couponCode = "",
            discountAmount = 0.0,
            platformCommissionRate = 15.0,
            platformCommissionAmount = 29.85,
            providerEarnings = 169.15,
            totalPrice = 199.0,
            paymentMethod = "Google Pay UPI",
            paymentStatus = "PAID",
            beforePhotoNotes = "Unboxed brand new fan",
            completionPhotoNotes = "Tested at all 5 speed levels with remote controller",
            completionOtp = "4412",
            createdAt = System.currentTimeMillis() - 86400000,
            customerReviewRating = 5.0,
            customerReviewComment = "Suresh ji arrived on time with proper ladder and tools. Clean job done in 25 mins!"
        )
    )

    fun getDefaultQuotes(): List<QuoteEntity> = listOf(
        QuoteEntity(
            id = "quote_201",
            bookingId = "book_102",
            providerId = "pro_1",
            providerName = "Suresh Chauhan (Suresh Solar Solutions)",
            providerRating = 4.9,
            providerPhone = "+91 94221 88990",
            labourCost = 35000.0,
            materialCost = 195000.0,
            totalPrice = 230000.0,
            estimatedDuration = "3 Days",
            notes = "Includes 5.2kW Tier-1 Bifacial Solar Panels, 5kW Deye On-Grid Inverter, GI Structure + Full MSEDCL net metering assistance and ₹78,000 PM Surya Ghar subsidy paper processing.",
            status = "SUBMITTED",
            createdAt = System.currentTimeMillis() - 14400000
        ),
        QuoteEntity(
            id = "quote_202",
            bookingId = "book_102",
            providerId = "pro_4",
            providerName = "Vikram Jadhav (SunPower Tech)",
            providerRating = 4.7,
            providerPhone = "+91 91580 99887",
            labourCost = 38000.0,
            materialCost = 205000.0,
            totalPrice = 243000.0,
            estimatedDuration = "4 Days",
            notes = "Includes 5kW Mono PERC Half-cut panels with 25-yr linear power warranty, Growatt Inverter, ACDB/DCDB protection boxes and complete earthing kit.",
            status = "SUBMITTED",
            createdAt = System.currentTimeMillis() - 7200000
        )
    )

    fun getDefaultMessages(): List<MessageEntity> = listOf(
        MessageEntity(
            id = "msg_1",
            bookingId = "book_101",
            senderId = "user_pro_2",
            senderName = "Rajesh Shinde",
            senderRole = "PROVIDER",
            messageText = "Namaste Aniket ji, I have accepted your AC service request. I have left Civil Lines and will reach Ramnagar in 15 minutes.",
            timestamp = System.currentTimeMillis() - 3600000
        ),
        MessageEntity(
            id = "msg_2",
            bookingId = "book_101",
            senderId = "user_cust_1",
            senderName = "Aniket Sharma",
            senderRole = "CUSTOMER",
            messageText = "Sure Rajesh ji. Gate is open, house is near the Hanuman Temple corner.",
            timestamp = System.currentTimeMillis() - 2400000
        ),
        MessageEntity(
            id = "msg_3",
            bookingId = "book_101",
            senderId = "user_pro_2",
            senderName = "Rajesh Shinde",
            senderRole = "PROVIDER",
            messageText = "Noted sir. I have brought the pressure jet pump and coil cleaner foam.",
            timestamp = System.currentTimeMillis() - 1200000
        )
    )

    fun getDefaultReviews(): List<ReviewEntity> = listOf(
        ReviewEntity(
            id = "rev_1",
            bookingId = "book_103",
            customerName = "Aniket Sharma",
            customerArea = "Ramnagar, Chandrapur",
            providerName = "Suresh Chauhan",
            serviceName = "Ceiling Fan Installation",
            rating = 5.0,
            comment = "Very polite and professional electrician in Chandrapur. Fixed the fan with zero wobbling.",
            dateText = "Yesterday"
        ),
        ReviewEntity(
            id = "rev_2",
            bookingId = "book_old_1",
            customerName = "Pooja Patil",
            customerArea = "Tukum, Chandrapur",
            providerName = "Amit Meshram",
            serviceName = "RO Filter Replacement",
            rating = 5.0,
            comment = "TDS was 750 in Tukum borewell water, Amit tuned it down to 90. Water tastes so fresh now!",
            dateText = "3 days ago"
        ),
        ReviewEntity(
            id = "rev_3",
            bookingId = "book_old_2",
            customerName = "Dr. Sameer Deshmukh",
            customerArea = "Civil Lines, Chandrapur",
            providerName = "Rajesh Shinde",
            serviceName = "Split AC Jet Service",
            rating = 4.8,
            comment = "Cleaned the outdoor condenser thoroughly without dirtying the balcony walls. Excellent work.",
            dateText = "Last week"
        )
    )

    fun getDefaultTickets(): List<SupportTicketEntity> = listOf(
        SupportTicketEntity(
            id = "tkt_1",
            userId = "user_cust_2",
            userName = "Pooja Patil",
            userRole = "CUSTOMER",
            subject = "Inquiry regarding Solar Subsidy paper timeline",
            category = "Solar Subsidy",
            description = "How many days does MSEDCL take for Chandrapur circle net meter approval?",
            status = "RESOLVED",
            priority = "MEDIUM",
            createdAt = System.currentTimeMillis() - 172800000,
            resolutionNotes = "Informed customer that MSEDCL Chandrapur circle typically completes inspection within 7-10 working days."
        )
    )

    fun getDefaultSettings(): List<PlatformSettingEntity> = listOf(
        PlatformSettingEntity(settingKey = "platform_commission_percent", settingValue = "15.0"),
        PlatformSettingEntity(settingKey = "helpline_phone", settingValue = "+91 7172 250000"),
        PlatformSettingEntity(settingKey = "support_email", settingValue = "support@gharfix.in"),
        PlatformSettingEntity(settingKey = "launch_city", settingValue = "Chandrapur"),
        PlatformSettingEntity(settingKey = "launch_state", settingValue = "Maharashtra")
    )
}
