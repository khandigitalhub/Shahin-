package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AgentEntity
import com.example.data.model.AuditLogEntity
import com.example.data.model.CpaConversionEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfferEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PayoutEntity
import com.example.data.model.ProductEntity
import com.example.data.model.TrainingModuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
  @Query("SELECT * FROM products ORDER BY id DESC")
  fun getAllProducts(): Flow<List<ProductEntity>>

  @Query("SELECT * FROM products WHERE category = :category ORDER BY id DESC")
  fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

  @Query("SELECT * FROM products WHERE isTrending = 1 ORDER BY id DESC")
  fun getTrendingProducts(): Flow<List<ProductEntity>>

  @Query("SELECT * FROM products WHERE id = :id")
  fun getProductById(id: Long): Flow<ProductEntity?>

  @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
  fun searchProducts(query: String): Flow<List<ProductEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProduct(product: ProductEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(products: List<ProductEntity>)

  @Update
  suspend fun updateProduct(product: ProductEntity)

  @Delete
  suspend fun deleteProduct(product: ProductEntity)

  @Query("SELECT COUNT(*) FROM products")
  suspend fun count(): Int
}

@Dao
interface AgentDao {
  @Query("SELECT * FROM agents WHERE id = :id")
  fun getAgentById(id: Long): Flow<AgentEntity?>

  @Query("SELECT * FROM agents WHERE id = :id")
  suspend fun getAgentByIdSync(id: Long): AgentEntity?

  @Query("SELECT * FROM agents WHERE phone = :phone LIMIT 1")
  suspend fun getAgentByPhone(phone: String): AgentEntity?

  @Query("SELECT * FROM agents ORDER BY totalEarnings DESC, totalOrders DESC")
  fun getLeaderboard(): Flow<List<AgentEntity>>

  @Query("SELECT * FROM agents ORDER BY id ASC")
  fun getAllAgents(): Flow<List<AgentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAgent(agent: AgentEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(agents: List<AgentEntity>)

  @Update
  suspend fun updateAgent(agent: AgentEntity)

  @Query("UPDATE agents SET status = :status WHERE id = :id")
  suspend fun updateAgentStatus(id: Long, status: String)

  @Query("SELECT COUNT(*) FROM agents")
  suspend fun count(): Int
}

@Dao
interface OrderDao {
  @Query("SELECT * FROM orders ORDER BY orderDate DESC")
  fun getAllOrders(): Flow<List<OrderEntity>>

  @Query("SELECT * FROM orders WHERE agentId = :agentId ORDER BY orderDate DESC")
  fun getOrdersByAgent(agentId: Long): Flow<List<OrderEntity>>

  @Query("SELECT * FROM orders WHERE id = :id")
  fun getOrderById(id: Long): Flow<OrderEntity?>

  @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderDate DESC")
  fun getOrdersByStatus(status: String): Flow<List<OrderEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(orders: List<OrderEntity>)

  @Update
  suspend fun updateOrder(order: OrderEntity)

  @Query("SELECT COUNT(*) FROM orders")
  suspend fun count(): Int
}

@Dao
interface PayoutDao {
  @Query("SELECT * FROM payouts ORDER BY requestedDate DESC")
  fun getAllPayouts(): Flow<List<PayoutEntity>>

  @Query("SELECT * FROM payouts WHERE agentId = :agentId ORDER BY requestedDate DESC")
  fun getPayoutsByAgent(agentId: Long): Flow<List<PayoutEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayout(payout: PayoutEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(payouts: List<PayoutEntity>)

  @Update
  suspend fun updatePayout(payout: PayoutEntity)
}

@Dao
interface NotificationDao {
  @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
  fun getAllNotifications(): Flow<List<NotificationEntity>>

  @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
  fun getUnreadCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotification(notification: NotificationEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(notifications: List<NotificationEntity>)

  @Query("UPDATE notifications SET isRead = 1")
  suspend fun markAllAsRead()
}

@Dao
interface OfferDao {
  @Query("SELECT * FROM offers WHERE isActive = 1 ORDER BY id ASC")
  fun getActiveOffers(): Flow<List<OfferEntity>>

  @Query("SELECT * FROM offers ORDER BY id ASC")
  fun getAllOffers(): Flow<List<OfferEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(offers: List<OfferEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOffer(offer: OfferEntity): Long

  @Query("SELECT COUNT(*) FROM offers")
  suspend fun count(): Int
}

@Dao
interface TrainingDao {
  @Query("SELECT * FROM training_modules ORDER BY moduleOrder ASC")
  fun getAllModules(): Flow<List<TrainingModuleEntity>>

  @Query("SELECT * FROM training_modules WHERE id = :id")
  fun getModuleById(id: Long): Flow<TrainingModuleEntity?>

  @Query("SELECT COUNT(*) FROM training_modules WHERE isCompleted = 1")
  fun getCompletedCount(): Flow<Int>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(modules: List<TrainingModuleEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertModule(module: TrainingModuleEntity): Long

  @Update
  suspend fun updateModule(module: TrainingModuleEntity)

  @Query("UPDATE training_modules SET isCompleted = :completed WHERE id = :id")
  suspend fun setCompleted(id: Long, completed: Boolean)

  @Query("SELECT COUNT(*) FROM training_modules")
  suspend fun count(): Int
}

@Dao
interface AuditLogDao {
  @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
  fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAuditLog(log: AuditLogEntity): Long
}

@Dao
interface CpaConversionDao {
  @Query("SELECT * FROM cpa_conversions ORDER BY submissionDate DESC")
  fun getAllConversions(): Flow<List<CpaConversionEntity>>

  @Query("SELECT * FROM cpa_conversions WHERE agentId = :agentId ORDER BY submissionDate DESC")
  fun getConversionsByAgent(agentId: Long): Flow<List<CpaConversionEntity>>

  @Query("SELECT COUNT(*) FROM cpa_conversions WHERE antiFraudHash = :hash AND antiFraudHash != ''")
  suspend fun checkDuplicate(hash: String): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertConversion(conversion: CpaConversionEntity): Long

  @Update
  suspend fun updateConversion(conversion: CpaConversionEntity)

  @Query("UPDATE cpa_conversions SET status = :status, processedDate = :processedDate, adminNotes = :notes WHERE id = :id")
  suspend fun updateStatus(id: Long, status: String, processedDate: Long, notes: String)
}
