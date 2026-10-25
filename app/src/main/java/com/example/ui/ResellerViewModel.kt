package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DatabaseInitializer
import com.example.data.model.AgentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfferEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PayoutEntity
import com.example.data.model.ProductEntity
import com.example.data.model.TrainingModuleEntity
import com.example.data.repository.ResellerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab(val title: String) {
  HOME("হোম"),
  CATALOG("প্রোডাক্ট"),
  ORDERS("অর্ডার"),
  TRAINING("ট্রেনিং একাডেমি"),
  WALLET("ওয়ালেট"),
  LEADERBOARD("লিডারবোর্ড")
}

class ResellerViewModel(application: Application) : AndroidViewModel(application) {

  private val database = AppDatabase.getDatabase(application)
  val repository = ResellerRepository(database)

  private val _currentAgentId = MutableStateFlow(1L)
  val currentAgentId: StateFlow<Long> = _currentAgentId.asStateFlow()

  private val _activeAgent = MutableStateFlow<AgentEntity?>(null)
  val activeAgent: StateFlow<AgentEntity?> = _activeAgent.asStateFlow()
  val currentAgent: StateFlow<AgentEntity?> = _activeAgent.asStateFlow()

  private val _currentTab = MutableStateFlow(MainTab.HOME)
  val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

  private val _selectedCategory = MutableStateFlow("সব")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val trendingProducts: StateFlow<List<ProductEntity>> = repository.trendingProducts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val filteredProducts: StateFlow<List<ProductEntity>> = combine(
    allProducts,
    _selectedCategory,
    _searchQuery
  ) { products, category, query ->
    products.filter { product ->
      val matchesCategory = (category == "সব" || product.category == category)
      val matchesQuery = query.isBlank() ||
          product.title.contains(query, ignoreCase = true) ||
          product.category.contains(query, ignoreCase = true) ||
          product.description.contains(query, ignoreCase = true)
      matchesCategory && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val leaderboard: StateFlow<List<AgentEntity>> = repository.getLeaderboard()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val activeOffers: StateFlow<List<OfferEntity>> = repository.activeOffers
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val unreadNotificationsCount: StateFlow<Int> = repository.unreadNotificationCount
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  // Orders
  val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Payouts
  val allPayouts: StateFlow<List<PayoutEntity>> = repository.allPayouts
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // UI state for Dialogs & Modals
  private val _selectedProductForDetail = MutableStateFlow<ProductEntity?>(null)
  val selectedProductForDetail: StateFlow<ProductEntity?> = _selectedProductForDetail.asStateFlow()

  private val _selectedProductForOrder = MutableStateFlow<ProductEntity?>(null)
  val selectedProductForOrder: StateFlow<ProductEntity?> = _selectedProductForOrder.asStateFlow()

  private val _selectedProductForMarketing = MutableStateFlow<ProductEntity?>(null)
  val selectedProductForMarketing: StateFlow<ProductEntity?> = _selectedProductForMarketing.asStateFlow()

  private val _isWithdrawDialogVisible = MutableStateFlow(false)
  val isWithdrawDialogVisible: StateFlow<Boolean> = _isWithdrawDialogVisible.asStateFlow()

  private val _isAddProductDialogVisible = MutableStateFlow(false)
  val isAddProductDialogVisible: StateFlow<Boolean> = _isAddProductDialogVisible.asStateFlow()

  private val _isBroadcastDialogVisible = MutableStateFlow(false)
  val isBroadcastDialogVisible: StateFlow<Boolean> = _isBroadcastDialogVisible.asStateFlow()

  private val _isRoleSwitchDialogVisible = MutableStateFlow(false)
  val isRoleSwitchDialogVisible: StateFlow<Boolean> = _isRoleSwitchDialogVisible.asStateFlow()

  private val _isSignUpDialogVisible = MutableStateFlow(false)
  val isSignUpDialogVisible: StateFlow<Boolean> = _isSignUpDialogVisible.asStateFlow()

  private val _selectedOrderForAdminStatus = MutableStateFlow<OrderEntity?>(null)
  val selectedOrderForAdminStatus: StateFlow<OrderEntity?> = _selectedOrderForAdminStatus.asStateFlow()

  // Training Academy
  val allTrainingModules: StateFlow<List<TrainingModuleEntity>> = repository.allTrainingModules
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val completedTrainingCount: StateFlow<Int> = repository.completedTrainingCount
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  private val _selectedTrainingModule = MutableStateFlow<TrainingModuleEntity?>(null)
  val selectedTrainingModule: StateFlow<TrainingModuleEntity?> = _selectedTrainingModule.asStateFlow()

  // Free Professional Tools: Digital Invoice, Target Calculator, Support Hub
  private val _selectedOrderForInvoice = MutableStateFlow<OrderEntity?>(null)
  val selectedOrderForInvoice: StateFlow<OrderEntity?> = _selectedOrderForInvoice.asStateFlow()

  private val _isTargetCalculatorVisible = MutableStateFlow(false)
  val isTargetCalculatorVisible: StateFlow<Boolean> = _isTargetCalculatorVisible.asStateFlow()

  private val _isSupportHubVisible = MutableStateFlow(false)
  val isSupportHubVisible: StateFlow<Boolean> = _isSupportHubVisible.asStateFlow()

  // Audit Logs & CPA
  val allAuditLogs: StateFlow<List<com.example.data.model.AuditLogEntity>> = repository.allAuditLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allCpaConversions: StateFlow<List<com.example.data.model.CpaConversionEntity>> = repository.allCpaConversions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allAgents: StateFlow<List<AgentEntity>> = repository.allAgents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Multi-Currency Mode (BDT / USD)
  private val _currencyMode = MutableStateFlow("BDT")
  val currencyMode: StateFlow<String> = _currencyMode.asStateFlow()

  private val _userFeedbackMessage = MutableStateFlow<String?>(null)
  val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

  init {
    viewModelScope.launch {
      DatabaseInitializer.populateInitialDataIfEmpty(database)
      refreshActiveAgent(_currentAgentId.value)
    }
  }

  fun refreshActiveAgent(agentId: Long) {
    viewModelScope.launch {
      database.agentDao().getAgentById(agentId).collect { agent ->
        _activeAgent.value = agent
      }
    }
  }

  fun switchAgent(agentId: Long) {
    _currentAgentId.value = agentId
    refreshActiveAgent(agentId)
    _userFeedbackMessage.value = if (agentId == 99L) "অ্যাডমিন (বিজনেস ওনার) মোডে সুইচ করা হয়েছে" else "এজেন্ট অ্যাকাউন্টে সুইচ করা হয়েছে"
  }

  fun setTab(tab: MainTab) {
    _currentTab.value = tab
  }

  fun setCategory(category: String) {
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun showProductDetail(product: ProductEntity?) {
    _selectedProductForDetail.value = product
  }

  fun openOrderPlacement(product: ProductEntity?) {
    _selectedProductForOrder.value = product
  }

  fun openMarketingDialog(product: ProductEntity?) {
    _selectedProductForMarketing.value = product
  }

  fun setWithdrawDialogVisible(visible: Boolean) {
    _isWithdrawDialogVisible.value = visible
  }

  fun setAddProductDialogVisible(visible: Boolean) {
    _isAddProductDialogVisible.value = visible
  }

  fun setBroadcastDialogVisible(visible: Boolean) {
    _isBroadcastDialogVisible.value = visible
  }

  fun setRoleSwitchDialogVisible(visible: Boolean) {
    _isRoleSwitchDialogVisible.value = visible
  }

  fun setSignUpDialogVisible(visible: Boolean) {
    _isSignUpDialogVisible.value = visible
  }

  fun setSelectedOrderForAdminStatus(order: OrderEntity?) {
    _selectedOrderForAdminStatus.value = order
  }

  fun clearFeedbackMessage() {
    _userFeedbackMessage.value = null
  }

  fun submitOrder(
    product: ProductEntity,
    quantity: Int,
    resellPrice: Double,
    customerName: String,
    customerPhone: String,
    customerAddress: String,
    district: String,
    deliveryArea: String
  ) {
    val agent = _activeAgent.value ?: return
    viewModelScope.launch {
      val orderId = repository.placeOrder(
        agentId = agent.id,
        agentName = agent.name,
        product = product,
        quantity = quantity,
        resellPrice = resellPrice,
        customerName = customerName,
        customerPhone = customerPhone,
        customerAddress = customerAddress,
        district = district,
        deliveryArea = deliveryArea
      )
      _selectedProductForOrder.value = null
      _userFeedbackMessage.value = "অর্ডার সফলভাবে প্লেস করা হয়েছে! অর্ডার ট্র্যাকিং সেকশনে চেক করুন।"
      _currentTab.value = MainTab.ORDERS
    }
  }

  fun requestWithdrawal(amount: Double, method: String, account: String) {
    val agent = _activeAgent.value ?: return
    viewModelScope.launch {
      val result = repository.requestPayout(
        agentId = agent.id,
        agentName = agent.name,
        amount = amount,
        method = method,
        accountNumber = account
      )
      if (result.isSuccess) {
        _isWithdrawDialogVisible.value = false
        _userFeedbackMessage.value = "৳${amount.toInt()} উইথড্রয়াল রিকোয়েস্ট সফল হয়েছে!"
      } else {
        _userFeedbackMessage.value = result.exceptionOrNull()?.message ?: "উইথড্রয়াল ব্যর্থ হয়েছে"
      }
    }
  }

  fun adminUpdateOrderStatus(order: OrderEntity, newStatus: String, tracking: String, courier: String) {
    viewModelScope.launch {
      repository.updateOrderStatus(order, newStatus, tracking, courier)
      _selectedOrderForAdminStatus.value = null
      _userFeedbackMessage.value = "অর্ডার #${order.orderNumber} এর স্ট্যাটাস আপডেট: $newStatus"
    }
  }

  fun adminApprovePayout(payout: PayoutEntity) {
    viewModelScope.launch {
      repository.approvePayout(payout)
      _userFeedbackMessage.value = "৳${payout.amount.toInt()} পেআউট সফলভাবে অ্যাপ্রুভ হয়েছে!"
    }
  }

  fun adminAddNewProduct(
    title: String,
    category: String,
    wholesalePrice: Double,
    retailPrice: Double,
    commission: Double,
    points: Int,
    stock: Int,
    desc: String,
    fabric: String
  ) {
    viewModelScope.launch {
      val sku = "AP-${category.take(2)}-${(10..99).random()}"
      val newProduct = ProductEntity(
        title = title,
        category = category,
        wholesalePrice = wholesalePrice,
        retailPrice = retailPrice,
        baseCommission = commission,
        points = points,
        stock = stock,
        description = desc,
        fabricOrMaterial = fabric,
        sku = sku,
        isTrending = true
      )
      repository.addProduct(newProduct)
      _isAddProductDialogVisible.value = false
      _userFeedbackMessage.value = "নতুন প্রোডাক্ট সফলভাবে যুক্ত হয়েছে এবং এজেন্টদের নোটিফিকেশন পাঠানো হয়েছে!"
    }
  }

  fun adminSendBroadcastNotification(title: String, body: String) {
    viewModelScope.launch {
      repository.sendBroadcastNotification(title, body)
      _isBroadcastDialogVisible.value = false
      _userFeedbackMessage.value = "সকল এজেন্টকে পুশ নোটিফিকেশন পাঠানো হয়েছে!"
    }
  }

  fun registerNewAgent(
    name: String,
    phone: String,
    district: String,
    businessName: String,
    bkash: String
  ) {
    viewModelScope.launch {
      val newAgent = AgentEntity(
        name = name,
        phone = phone,
        email = "$phone@standardbusiness.com",
        district = district,
        businessName = businessName,
        bkashNumber = bkash,
        nagadNumber = bkash,
        availableBalance = 50.0, // Joining bonus
        totalEarnings = 50.0,
        badgeLevel = "নতুন উদ্যোক্তা",
        role = "AGENT"
      )
      val newId = repository.registerOrUpdateAgent(newAgent)
      _currentAgentId.value = newId
      refreshActiveAgent(newId)
      _isSignUpDialogVisible.value = false
      _userFeedbackMessage.value = "অভিনন্দন $name! আপনার Standard Business রিসেলার অ্যাকাউন্ট তৈরি হয়েছে (৳৫০ জয়েনিং বোনাস যোগ হয়েছে)!"
    }
  }

  fun markAllNotificationsRead() {
    viewModelScope.launch {
      repository.markAllNotificationsAsRead()
    }
  }

  fun selectTrainingModule(module: TrainingModuleEntity?) {
    _selectedTrainingModule.value = module
  }

  fun toggleModuleCompleted(id: Long, completed: Boolean) {
    viewModelScope.launch {
      repository.setModuleCompleted(id, completed)
      _userFeedbackMessage.value = if (completed) "মডিউল সম্পন্ন হিসেবে চিহ্নিত হয়েছে! 🎉" else "মডিউল আন-চেক করা হয়েছে"
    }
  }

  fun openInvoice(order: OrderEntity?) {
    _selectedOrderForInvoice.value = order
  }

  fun setTargetCalculatorVisible(visible: Boolean) {
    _isTargetCalculatorVisible.value = visible
  }

  fun setSupportHubVisible(visible: Boolean) {
    _isSupportHubVisible.value = visible
  }

  fun setCurrencyMode(mode: String) {
    _currencyMode.value = mode
  }

  fun loginAsAdmin() {
    _currentAgentId.value = 99L
    refreshActiveAgent(99L)
    _userFeedbackMessage.value = "🔐 মাস্টার অ্যাডমিন সেশন ভেরিফাইড ও সক্রিয় হয়েছে!"
  }

  fun logoutAdmin() {
    _currentAgentId.value = 1L
    refreshActiveAgent(1L)
    _userFeedbackMessage.value = "অ্যাডমিন সেশন সফলভাবে ক্লোজ হয়েছে। সাধারণ রিসেলার ভিউতে ফিরে গেছেন।"
  }

  fun adminAdjustAgentBalance(
    agentId: Long,
    amount: Double,
    actionType: String,
    reason: String
  ) {
    viewModelScope.launch {
      val admin = _activeAgent.value
      val adminId = admin?.id ?: 99L
      val adminName = admin?.name ?: "Standard Business পরিচালক"
      val success = repository.adjustAgentBalance(
        adminId = adminId,
        adminName = adminName,
        agentId = agentId,
        amount = amount,
        actionType = actionType,
        reason = reason
      )
      if (success) {
        _userFeedbackMessage.value = "ব্যালেন্স সমন্বয় সফল হয়েছে এবং অডিট লগ সংরক্ষিত হয়েছে!"
      } else {
        _userFeedbackMessage.value = "ব্যালেন্স সমন্বয় ব্যর্থ হয়েছে!"
      }
    }
  }

  fun adminUpdateAgentStatus(
    agentId: Long,
    newStatus: String,
    reason: String
  ) {
    viewModelScope.launch {
      val admin = _activeAgent.value
      val adminId = admin?.id ?: 99L
      val adminName = admin?.name ?: "Standard Business পরিচালক"
      val success = repository.updateAgentStatus(
        agentId = agentId,
        newStatus = newStatus,
        adminId = adminId,
        adminName = adminName,
        reason = reason
      )
      if (success) {
        _userFeedbackMessage.value = "এজেন্ট স্ট্যাটাস ($newStatus) সফলভাবে আপডেট হয়েছে!"
      }
    }
  }

  fun submitCpaLead(
    offer: OfferEntity,
    customerName: String,
    customerPhone: String,
    customerEmail: String
  ) {
    viewModelScope.launch {
      val agent = _activeAgent.value
      val agentId = agent?.id ?: 1L
      val agentName = agent?.name ?: "রিসেলার"
      val conversion = com.example.data.model.CpaConversionEntity(
        offerId = offer.id,
        offerTitle = offer.title,
        agentId = agentId,
        agentName = agentName,
        customerName = customerName,
        customerPhone = customerPhone,
        customerEmail = customerEmail,
        targetCountry = offer.targetCountry,
        payoutAmount = offer.bonusAmount,
        currency = offer.currency
      )
      val result = repository.submitCpaLead(conversion)
      _userFeedbackMessage.value = result.second
    }
  }

  fun adminUpdateCpaStatus(
    conversion: com.example.data.model.CpaConversionEntity,
    newStatus: String,
    notes: String
  ) {
    viewModelScope.launch {
      val admin = _activeAgent.value
      val adminId = admin?.id ?: 99L
      val adminName = admin?.name ?: "Standard Business পরিচালক"
      repository.updateCpaConversionStatus(
        conversion = conversion,
        newStatus = newStatus,
        notes = notes,
        adminId = adminId,
        adminName = adminName
      )
      _userFeedbackMessage.value = "CPA লিড স্ট্যাটাস ($newStatus) আপডেট সম্পন্ন!"
    }
  }
}
