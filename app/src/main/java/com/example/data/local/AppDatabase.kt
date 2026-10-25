package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AgentDao
import com.example.data.dao.AuditLogDao
import com.example.data.dao.CpaConversionDao
import com.example.data.dao.NotificationDao
import com.example.data.dao.OfferDao
import com.example.data.dao.OrderDao
import com.example.data.dao.PayoutDao
import com.example.data.dao.ProductDao
import com.example.data.dao.TrainingDao
import com.example.data.model.AgentEntity
import com.example.data.model.AuditLogEntity
import com.example.data.model.CpaConversionEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfferEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PayoutEntity
import com.example.data.model.ProductEntity
import com.example.data.model.TrainingModuleEntity

@Database(
  entities = [
    ProductEntity::class,
    AgentEntity::class,
    OrderEntity::class,
    PayoutEntity::class,
    NotificationEntity::class,
    OfferEntity::class,
    TrainingModuleEntity::class,
    AuditLogEntity::class,
    CpaConversionEntity::class
  ],
  version = 3,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun productDao(): ProductDao
  abstract fun agentDao(): AgentDao
  abstract fun orderDao(): OrderDao
  abstract fun payoutDao(): PayoutDao
  abstract fun notificationDao(): NotificationDao
  abstract fun offerDao(): OfferDao
  abstract fun trainingDao(): TrainingDao
  abstract fun auditLogDao(): AuditLogDao
  abstract fun cpaConversionDao(): CpaConversionDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "standard_business_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
