package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AgentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfferEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PayoutEntity
import com.example.data.model.ProductEntity
import com.example.data.model.TrainingModuleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class ResellerRepository(private val db: AppDatabase) {

  // Products
  val allProducts: Flow<List<ProductEntity>> = db.productDao().getAllProducts()
  val trendingProducts: Flow<List<ProductEntity>> = db.productDao().getTrendingProducts()

  fun getProductsByCategory(category: String): Flow<List<ProductEntity>> =
    db.productDao().getProductsByCategory(category)

  fun getProductById(id: Long): Flow<ProductEntity?> =
    db.productDao().getProductById(id)

  fun searchProducts(query: String): Flow<List<ProductEntity>> =
    db.productDao().searchProducts(query)

  suspend fun addProduct(product: ProductEntity): Long {
    val id = db.productDao().insertProduct(product)
    // Dispatch broadcast notification about new product
    db.notificationDao().insertNotification(
      NotificationEntity(
        title = "নতুন প্রোডাক্ট যুক্ত হয়েছে! 🛍️",
        body = "${product.title} এখন ক্যাটালগে যুক্ত হয়েছে। প্রতিটি সেলে নিশ্চিত ৳${product.baseCommission.toInt()} কমিশন ও পয়েন্ট!",
        type = "PRODUCT",
        timestamp = System.currentTimeMillis()
      )
    )
    return id
  }

  suspend fun updateProduct(product: ProductEntity) {
    db.productDao().updateProduct(product)
  }

  suspend fun deleteProduct(product: ProductEntity) {
    db.productDao().deleteProduct(product)
  }

  // Agents & Current User
  fun getAgentById(agentId: Long): Flow<AgentEntity?> =
    db.agentDao().getAgentById(agentId)

  fun getLeaderboard(): Flow<List<AgentEntity>> =
    db.agentDao().getLeaderboard()

  suspend fun updateAgent(agent: AgentEntity) {
    db.agentDao().updateAgent(agent)
  }

  suspend fun registerOrUpdateAgent(agent: AgentEntity): Long {
    return db.agentDao().insertAgent(agent)
  }

  // Orders
  val allOrders: Flow<List<OrderEntity>> = db.orderDao().getAllOrders()

  fun getOrdersForAgent(agentId: Long): Flow<List<OrderEntity>> =
    db.orderDao().getOrdersByAgent(agentId)

  fun getOrderById(id: Long): Flow<OrderEntity?> =
    db.orderDao().getOrderById(id)

  fun getOrdersByStatus(status: String): Flow<List<OrderEntity>> =
    db.orderDao().getOrdersByStatus(status)

  suspend fun placeOrder(
    agentId: Long,
    agentName: String,
    product: ProductEntity,
    quantity: Int,
    resellPrice: Double,
    customerName: String,
    customerPhone: String,
    customerAddress: String,
    district: String,
    deliveryArea: String
  ): Long {
    val deliveryCharge = if (deliveryArea.contains("ভেতরে") || deliveryArea.contains("ঢাকা")) {
      product.deliveryChargeDhaka
    } else {
      product.deliveryChargeOutside
    }

    val profitPerUnit = (resellPrice - product.wholesalePrice) + product.baseCommission
    val totalAgentProfit = profitPerUnit * quantity
    val pointsEarned = product.points * quantity
    val codAmount = (resellPrice * quantity) + deliveryCharge

    val randomSuffix = Random.nextInt(1000, 9999)
    val orderNumber = "AP-${product.sku.takeLast(2)}-$randomSuffix"

    val order = OrderEntity(
      orderNumber = orderNumber,
      agentId = agentId,
      agentName = agentName,
      productId = product.id,
      productTitle = product.title,
      quantity = quantity,
      wholesalePrice = product.wholesalePrice,
      resellPrice = resellPrice,
      baseCommission = product.baseCommission,
      agentProfit = totalAgentProfit,
      pointsEarned = pointsEarned,
      customerName = customerName,
      customerPhone = customerPhone,
      customerAddress = customerAddress,
      district = district,
      deliveryArea = deliveryArea,
      deliveryCharge = deliveryCharge,
      codAmount = codAmount,
      status = "অর্ডার প্রাপ্ত",
      courierName = "Steadfast Courier",
      trackingNumber = "STF-$randomSuffix",
      orderDate = System.currentTimeMillis(),
      commissionSettled = false
    )

    val orderId = db.orderDao().insertOrder(order)

    // Update agent's pending balance
    val currentAgent = db.agentDao().getAgentById(agentId).firstOrNull()
    if (currentAgent != null) {
      val updated = currentAgent.copy(
        pendingBalance = currentAgent.pendingBalance + totalAgentProfit,
        totalOrders = currentAgent.totalOrders + 1
      )
      db.agentDao().updateAgent(updated)
    }

    // Add notification
    db.notificationDao().insertNotification(
      NotificationEntity(
        title = "নতুন কাস্টমার অর্ডার সফল! 📦",
        body = "অর্ডার #$orderNumber গ্রহণ করা হয়েছে। ডেলিভারি সম্পন্ন হলে ৳${totalAgentProfit.toInt()} কমিশন সরাসরি ওয়ালেটে ক্রেডিট হবে।",
        type = "ORDER",
        timestamp = System.currentTimeMillis(),
        relatedOrderId = orderId
      )
    )

    return orderId
  }

  suspend fun updateOrderStatus(
    order: OrderEntity,
    newStatus: String,
    trackingNumber: String = order.trackingNumber,
    courierName: String = order.courierName
  ) {
    val isNowDelivered = newStatus == "ডেলিভারি সম্পন্ন"
    val shouldSettleCommission = isNowDelivered && !order.commissionSettled

    val updatedOrder = order.copy(
      status = newStatus,
      trackingNumber = trackingNumber,
      courierName = courierName,
      deliveredDate = if (isNowDelivered) System.currentTimeMillis() else order.deliveredDate,
      commissionSettled = if (shouldSettleCommission) true else order.commissionSettled
    )
    db.orderDao().updateOrder(updatedOrder)

    if (shouldSettleCommission) {
      val agent = db.agentDao().getAgentById(order.agentId).firstOrNull()
      if (agent != null) {
        val newAvailable = agent.availableBalance + order.agentProfit
        val newPending = (agent.pendingBalance - order.agentProfit).coerceAtLeast(0.0)
        val newTotalEarnings = agent.totalEarnings + order.agentProfit
        val newPoints = agent.points + order.pointsEarned

        val newBadge = when {
          agent.totalOrders >= 40 -> "টপ সুপারস্টার"
          agent.totalOrders >= 25 -> "গোল্ড রিসেলার"
          agent.totalOrders >= 10 -> "স্টার রিসেলার"
          else -> "নতুন উদ্যোক্তা"
        }

        db.agentDao().updateAgent(
          agent.copy(
            availableBalance = newAvailable,
            pendingBalance = newPending,
            totalEarnings = newTotalEarnings,
            points = newPoints,
            badgeLevel = newBadge
          )
        )

        db.notificationDao().insertNotification(
          NotificationEntity(
            title = "কমিশন ওয়ালেটে ক্রেডিট হয়েছে! 💸",
            body = "অর্ডার #${order.orderNumber} সফল ডেলিভারি হয়েছে। ৳${order.agentProfit.toInt()} কমিশন ও ${order.pointsEarned} পয়েন্ট যোগ হয়েছে!",
            type = "COMMISSION",
            timestamp = System.currentTimeMillis(),
            relatedOrderId = order.id
          )
        )
      }
    } else {
      // General status update notification
      db.notificationDao().insertNotification(
        NotificationEntity(
          title = "অর্ডার স্ট্যাটাস আপডেট: $newStatus",
          body = "অর্ডার #${order.orderNumber} এর বর্তমান অবস্থা: $newStatus (কুরিয়ার: $courierName, ট্র্যাকিং: $trackingNumber)",
          type = "ORDER",
          timestamp = System.currentTimeMillis(),
          relatedOrderId = order.id
        )
      )
    }
  }

  // Payouts / Wallet
  val allPayouts: Flow<List<PayoutEntity>> = db.payoutDao().getAllPayouts()

  fun getPayoutsForAgent(agentId: Long): Flow<List<PayoutEntity>> =
    db.payoutDao().getPayoutsByAgent(agentId)

  suspend fun requestPayout(
    agentId: Long,
    agentName: String,
    amount: Double,
    method: String,
    accountNumber: String
  ): Result<Long> {
    val agent = db.agentDao().getAgentById(agentId).firstOrNull()
      ?: return Result.failure(Exception("এজেন্ট পাওয়া যায়নি"))

    if (amount <= 0) {
      return Result.failure(Exception("উইথড্রয়াল এমাউন্ট সঠিক নয়"))
    }

    if (amount > agent.availableBalance) {
      return Result.failure(Exception("পর্যাপ্ত এভেইলেবল ব্যালেন্স নেই"))
    }

    if (amount < 100.0) {
      return Result.failure(Exception("সর্বনিম্ন উইথড্রয়াল ১০০ টাকা"))
    }

    // Deduct available balance
    val updatedAgent = agent.copy(
      availableBalance = agent.availableBalance - amount
    )
    db.agentDao().updateAgent(updatedAgent)

    val payout = PayoutEntity(
      agentId = agentId,
      agentName = agentName,
      amount = amount,
      method = method,
      accountNumber = accountNumber,
      status = "পেন্ডিং",
      requestedDate = System.currentTimeMillis()
    )
    val payoutId = db.payoutDao().insertPayout(payout)

    db.notificationDao().insertNotification(
      NotificationEntity(
        title = "উইথড্র রিকোয়েস্ট সফল 📲",
        body = "৳${amount.toInt()} উইথড্রয়াল রিকোয়েস্ট জমা পড়েছে ($method: $accountNumber)। দ্রুত অনুমোদিত হবে।",
        type = "PAYOUT",
        timestamp = System.currentTimeMillis()
      )
    )

    return Result.success(payoutId)
  }

  suspend fun approvePayout(payout: PayoutEntity) {
    val prefix = when (payout.method) {
      "bKash" -> "BK"
      "Nagad" -> "NG"
      "Rocket" -> "RK"
      else -> "TRX"
    }
    val randomTrx = prefix + Random.nextInt(10000000, 99999999).toString()
    val updated = payout.copy(
      status = "পেমেন্ট সম্পন্ন",
      trxId = randomTrx,
      processedDate = System.currentTimeMillis()
    )
    db.payoutDao().updatePayout(updated)

    db.notificationDao().insertNotification(
      NotificationEntity(
        title = "পেমেন্ট সফলভাবে ট্রান্সফার হয়েছে! 💰",
        body = "${payout.agentName} এর ৳${payout.amount.toInt()} টাকা ${payout.method} নম্বরে সফলভাবে পাঠানো হয়েছে। TrxID: $randomTrx",
        type = "PAYOUT",
        timestamp = System.currentTimeMillis()
      )
    )
  }

  // Notifications
  val allNotifications: Flow<List<NotificationEntity>> =
    db.notificationDao().getAllNotifications()

  val unreadNotificationCount: Flow<Int> =
    db.notificationDao().getUnreadCount()

  suspend fun markAllNotificationsAsRead() {
    db.notificationDao().markAllAsRead()
  }

  suspend fun sendBroadcastNotification(title: String, message: String) {
    db.notificationDao().insertNotification(
      NotificationEntity(
        title = title,
        body = message,
        type = "OFFER",
        timestamp = System.currentTimeMillis()
      )
    )
  }

  // Offers
  val activeOffers: Flow<List<OfferEntity>> = db.offerDao().getActiveOffers()

  suspend fun addOffer(offer: OfferEntity): Long {
    return db.offerDao().insertOffer(offer)
  }

  // Training Modules
  val allTrainingModules: Flow<List<TrainingModuleEntity>> =
    db.trainingDao().getAllModules()

  val completedTrainingCount: Flow<Int> =
    db.trainingDao().getCompletedCount()

  suspend fun setModuleCompleted(id: Long, completed: Boolean) {
    db.trainingDao().setCompleted(id, completed)
  }

  // All Agents for Admin Management
  val allAgents: Flow<List<AgentEntity>> = db.agentDao().getAllAgents()

  // Audit Logs
  val allAuditLogs: Flow<List<com.example.data.model.AuditLogEntity>> =
    db.auditLogDao().getAllAuditLogs()

  // CPA Conversions
  val allCpaConversions: Flow<List<com.example.data.model.CpaConversionEntity>> =
    db.cpaConversionDao().getAllConversions()

  fun getCpaConversionsForAgent(agentId: Long): Flow<List<com.example.data.model.CpaConversionEntity>> =
    db.cpaConversionDao().getConversionsByAgent(agentId)

  suspend fun adjustAgentBalance(
    adminId: Long,
    adminName: String,
    agentId: Long,
    amount: Double,
    actionType: String, // "BALANCE_ADD", "BALANCE_SUBTRACT", "BONUS_ADD", "COMMISSION_ADJUST"
    reason: String
  ): Boolean {
    val agent = db.agentDao().getAgentByIdSync(agentId) ?: return false
    val prevBalance = agent.availableBalance
    val newBalance = when (actionType) {
      "BALANCE_SUBTRACT" -> (prevBalance - amount).coerceAtLeast(0.0)
      else -> prevBalance + amount
    }
    val newTotalEarnings = if (actionType == "BALANCE_SUBTRACT") {
      (agent.totalEarnings - amount).coerceAtLeast(0.0)
    } else {
      agent.totalEarnings + amount
    }
    val newBonusEarnings = if (actionType == "BONUS_ADD") agent.bonusEarnings + amount else agent.bonusEarnings

    val updatedAgent = agent.copy(
      availableBalance = newBalance,
      totalEarnings = newTotalEarnings,
      bonusEarnings = newBonusEarnings
    )
    db.agentDao().updateAgent(updatedAgent)

    // Save Audit Trail
    db.auditLogDao().insertAuditLog(
      com.example.data.model.AuditLogEntity(
        adminId = adminId,
        adminName = adminName,
        targetAgentId = agentId,
        targetAgentName = agent.name,
        actionType = actionType,
        amount = amount,
        previousBalance = prevBalance,
        newBalance = newBalance,
        reason = reason,
        timestamp = System.currentTimeMillis()
      )
    )

    // Send Agent Notification
    val actionText = when (actionType) {
      "BALANCE_ADD" -> "৳${amount.toInt()} ওয়ালেট ব্যালেন্স যোগ হয়েছে"
      "BALANCE_SUBTRACT" -> "৳${amount.toInt()} ব্যালেন্স সমন্বয় করা হয়েছে"
      "BONUS_ADD" -> "৳${amount.toInt()} স্পেশাল পারফরম্যান্স বোনাস যোগ হয়েছে! 🎁"
      else -> "৳${amount.toInt()} কমিশন সমন্বয় হয়েছে"
    }

    db.notificationDao().insertNotification(
      NotificationEntity(
        title = "ব্যালেন্স ও কমিশন আপডেট",
        body = "$actionText। কারণ: $reason (পূর্বের ব্যালেন্স: ৳${prevBalance.toInt()}, বর্তমান: ৳${newBalance.toInt()})",
        type = "COMMISSION",
        timestamp = System.currentTimeMillis()
      )
    )
    return true
  }

  suspend fun updateAgentStatus(
    agentId: Long,
    newStatus: String,
    adminId: Long,
    adminName: String,
    reason: String
  ): Boolean {
    val agent = db.agentDao().getAgentByIdSync(agentId) ?: return false
    db.agentDao().updateAgentStatus(agentId, newStatus)

    db.auditLogDao().insertAuditLog(
      com.example.data.model.AuditLogEntity(
        adminId = adminId,
        adminName = adminName,
        targetAgentId = agentId,
        targetAgentName = agent.name,
        actionType = "STATUS_CHANGE",
        amount = 0.0,
        previousBalance = agent.availableBalance,
        newBalance = agent.availableBalance,
        reason = "স্ট্যাটাস পরিবর্তন: $newStatus। বিবরণ: $reason",
        timestamp = System.currentTimeMillis()
      )
    )
    return true
  }

  suspend fun submitCpaLead(
    conversion: com.example.data.model.CpaConversionEntity
  ): Pair<Boolean, String> {
    val hash = "${conversion.offerId}_${conversion.customerPhone.trim()}"
    val isDuplicate = db.cpaConversionDao().checkDuplicate(hash) > 0
    if (isDuplicate) {
      return Pair(false, "এই ফোন নম্বর দিয়ে এই অফারে আগেই লিড রেজিস্টার করা হয়েছে! ডুপ্লিকেট লিড গ্রহণ করা হবে না।")
    }

    val entityToInsert = conversion.copy(antiFraudHash = hash)
    db.cpaConversionDao().insertConversion(entityToInsert)

    db.notificationDao().insertNotification(
      NotificationEntity(
        title = "CPA লিড সাবমিট হয়েছে 🚀",
        body = "${conversion.offerTitle} অফারে ${conversion.customerName} এর লিড পেন্ডিং আছে। ভেরিফিকেশনের পর ৳${conversion.payoutAmount.toInt()} ক্রেডিট হবে।",
        type = "OFFER",
        timestamp = System.currentTimeMillis()
      )
    )
    return Pair(true, "CPA লিড সফলভাবে গৃহীত হয়েছে! অ্যাডমিন ভেরিফিকেশনের পর কমিশন আনলক হবে।")
  }

  suspend fun updateCpaConversionStatus(
    conversion: com.example.data.model.CpaConversionEntity,
    newStatus: String,
    notes: String,
    adminId: Long,
    adminName: String
  ) {
    db.cpaConversionDao().updateStatus(
      id = conversion.id,
      status = newStatus,
      processedDate = System.currentTimeMillis(),
      notes = notes
    )

    // If approved, credit agent balance
    if (newStatus == "APPROVED" && conversion.status != "APPROVED") {
      val agent = db.agentDao().getAgentByIdSync(conversion.agentId)
      if (agent != null) {
        val updatedAgent = agent.copy(
          availableBalance = agent.availableBalance + conversion.payoutAmount,
          totalEarnings = agent.totalEarnings + conversion.payoutAmount,
          cpaEarnings = agent.cpaEarnings + conversion.payoutAmount,
          cpaConversionsCount = agent.cpaConversionsCount + 1
        )
        db.agentDao().updateAgent(updatedAgent)

        db.auditLogDao().insertAuditLog(
          com.example.data.model.AuditLogEntity(
            adminId = adminId,
            adminName = adminName,
            targetAgentId = conversion.agentId,
            targetAgentName = conversion.agentName,
            actionType = "CPA_APPROVAL",
            amount = conversion.payoutAmount,
            previousBalance = agent.availableBalance,
            newBalance = agent.availableBalance + conversion.payoutAmount,
            reason = "CPA লিড #${conversion.id} অনুমোদন (${conversion.offerTitle}): $notes",
            timestamp = System.currentTimeMillis()
          )
        )

        db.notificationDao().insertNotification(
          NotificationEntity(
            title = "CPA কমিশন অনুমোদিত! 💵",
            body = "আপনার ${conversion.offerTitle} অফারের লিডটি অনুমোদিত হয়েছে এবং ৳${conversion.payoutAmount.toInt()} ওয়ালেটে যোগ হয়েছে।",
            type = "COMMISSION",
            timestamp = System.currentTimeMillis()
          )
        )
      }
    } else if (newStatus == "REVERSED" && conversion.status == "APPROVED") {
      val agent = db.agentDao().getAgentByIdSync(conversion.agentId)
      if (agent != null) {
        val updatedAgent = agent.copy(
          availableBalance = (agent.availableBalance - conversion.payoutAmount).coerceAtLeast(0.0),
          totalEarnings = (agent.totalEarnings - conversion.payoutAmount).coerceAtLeast(0.0),
          cpaEarnings = (agent.cpaEarnings - conversion.payoutAmount).coerceAtLeast(0.0),
          cpaConversionsCount = (agent.cpaConversionsCount - 1).coerceAtLeast(0)
        )
        db.agentDao().updateAgent(updatedAgent)

        db.auditLogDao().insertAuditLog(
          com.example.data.model.AuditLogEntity(
            adminId = adminId,
            adminName = adminName,
            targetAgentId = conversion.agentId,
            targetAgentName = conversion.agentName,
            actionType = "CPA_REVERSAL",
            amount = conversion.payoutAmount,
            previousBalance = agent.availableBalance,
            newBalance = updatedAgent.availableBalance,
            reason = "CPA লিড #${conversion.id} রিভার্সাল: $notes",
            timestamp = System.currentTimeMillis()
          )
        )
      }
    }
  }
}

