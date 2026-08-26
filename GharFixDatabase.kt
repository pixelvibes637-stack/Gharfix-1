package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        ServiceCategoryEntity::class,
        ServiceEntity::class,
        ProviderEntity::class,
        LocationEntity::class,
        BookingEntity::class,
        QuoteEntity::class,
        MessageEntity::class,
        ReviewEntity::class,
        CouponEntity::class,
        SupportTicketEntity::class,
        PlatformSettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GharFixDatabase : RoomDatabase() {

    abstract fun gharFixDao(): GharFixDao

    companion object {
        @Volatile
        private var INSTANCE: GharFixDatabase? = null

        fun getDatabase(context: Context): GharFixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GharFixDatabase::class.java,
                    "gharfix_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
