package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // শাড়ি, থ্রি-পিস, জুয়েলারি, কসমেটিকস, হ্যান্ডব্যাগ, ফ্যাশন, ফুড ও গ্রোসারি, গ্যাজেটস
  val wholesalePrice: Double, // পাইকারি মূল্য
  val retailPrice: Double, // সম্ভাব্য খুচরা মূল্য
  val baseCommission: Double, // নিশ্চিত বেস কমিশন
  val points: Int,
  val stock: Int,
  val description: String,
  val fabricOrMaterial: String,
  val sku: String,
  val isTrending: Boolean = false,
  val imageResName: String = "reseller_hero_banner_1792806449181",
  val deliveryChargeDhaka: Double = 60.0,
  val deliveryChargeOutside: Double = 120.0,
  val country: String = "বাংলাদেশ",
  val currency: String = "BDT"
)

@Entity(tableName = "agents")
data class AgentEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val phone: String,
  val email: String,
  val district: String,
  val businessName: String,
  val bkashNumber: String,
  val nagadNumber: String,
  val totalEarnings: Double = 0.0,
  val availableBalance: Double = 0.0,
  val pendingBalance: Double = 0.0,
  val totalOrders: Int = 0,
  val points: Int = 0,
  val badgeLevel: String = "নতুন উদ্যোক্তা",
  val role: String = "AGENT", // "AGENT" or "ADMIN"
  val status: String = "ACTIVE", // "ACTIVE", "SUSPENDED"
  val country: String = "বাংলাদেশ",
  val referralCode: String = "",
  val cpaConversionsCount: Int = 0,
  val cpaEarnings: Double = 0.0,
  val cpsEarnings: Double = 0.0,
  val bonusEarnings: Double = 0.0
)

@Entity(tableName = "orders")
data class OrderEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val orderNumber: String,
  val agentId: Long,
  val agentName: String,
  val productId: Long,
  val productTitle: String,
  val quantity: Int = 1,
  val wholesalePrice: Double,
  val resellPrice: Double,
  val baseCommission: Double,
  val agentProfit: Double,
  val pointsEarned: Int,
  val customerName: String,
  val customerPhone: String,
  val customerAddress: String,
  val district: String,
  val deliveryArea: String, // "ঢাকা সিটির ভেতরে", "ঢাকা সিটির বাইরে"
  val deliveryCharge: Double,
  val codAmount: Double,
  val status: String, // "অর্ডার প্রাপ্ত", "প্রসেসিং", "কুরিয়ারে হস্তান্তর", "ডেলিভারি সম্পন্ন", "বাতিল"
  val courierName: String = "Steadfast Courier",
  val trackingNumber: String = "",
  val orderDate: Long = System.currentTimeMillis(),
  val deliveredDate: Long? = null,
  val commissionSettled: Boolean = false
)

@Entity(tableName = "payouts")
data class PayoutEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val agentId: Long,
  val agentName: String,
  val amount: Double,
  val method: String, // "bKash", "Nagad", "Rocket", "Bank", "USDT"
  val accountNumber: String,
  val status: String, // "পেন্ডিং", "অনুমোদিত", "পেমেন্ট সম্পন্ন", "হোল্ড"
  val trxId: String = "",
  val requestedDate: Long = System.currentTimeMillis(),
  val processedDate: Long? = null
)

@Entity(tableName = "notifications")
data class NotificationEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val body: String,
  val type: String, // "ORDER", "COMMISSION", "PAYOUT", "OFFER", "SYSTEM", "AUDIT"
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = false,
  val relatedOrderId: Long? = null
)

@Entity(tableName = "offers")
data class OfferEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val subtitle: String,
  val bonusDetails: String,
  val promoCode: String,
  val validUntil: String,
  val minOrdersRequired: Int = 1,
  val bonusAmount: Double = 0.0,
  val isActive: Boolean = true,
  val categoryTag: String = "বাংলাদেশ স্পেশাল",
  val offerType: String = "CPA_LEAD", // "PROMO_BONUS", "APP_INSTALL", "SIGNUP", "CPA_LEAD"
  val targetCountry: String = "বাংলাদেশ",
  val currency: String = "BDT",
  val payoutRate: Double = 50.0,
  val trackingUrl: String = "",
  val rules: String = "সঠিক কাস্টমার তথ্য যাচাই ও ফোন কল ভেরিফিকেশনের পর কমিশন অনুমোদিত হবে।"
)

@Entity(tableName = "training_modules")
data class TrainingModuleEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val subtitle: String,
  val category: String, // "প্রোডাক্ট নলেজ", "মার্কেটিং কৌশল", "অ্যাপ ও পেমেন্ট গাইড"
  val durationText: String,
  val summary: String,
  val detailedContent: String,
  val keyTakeaways: String,
  val practicalTips: String,
  val videoTitle: String,
  val isCompleted: Boolean = false,
  val isBookmarked: Boolean = false,
  val moduleOrder: Int = 1
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val adminId: Long,
  val adminName: String,
  val targetAgentId: Long,
  val targetAgentName: String,
  val actionType: String, // "BALANCE_ADD", "BALANCE_SUBTRACT", "BONUS_ADD", "COMMISSION_ADJUST", "STATUS_CHANGE", "CPA_APPROVAL"
  val amount: Double = 0.0,
  val previousBalance: Double = 0.0,
  val newBalance: Double = 0.0,
  val reason: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cpa_conversions")
data class CpaConversionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val offerId: Long,
  val offerTitle: String,
  val agentId: Long,
  val agentName: String,
  val customerName: String,
  val customerPhone: String,
  val customerEmail: String = "",
  val targetCountry: String = "বাংলাদেশ",
  val payoutAmount: Double,
  val currency: String = "BDT",
  val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED", "REVERSED"
  val antiFraudHash: String = "",
  val submissionDate: Long = System.currentTimeMillis(),
  val processedDate: Long? = null,
  val adminNotes: String = ""
)
