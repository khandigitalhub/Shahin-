package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainTab
import com.example.ui.ResellerViewModel
import com.example.ui.components.AddProductDialog
import com.example.ui.components.AppHeader
import com.example.ui.components.AdminPinDialog
import com.example.ui.components.BroadcastNotificationDialog
import com.example.ui.components.CpaLeadMarketingKitDialog
import com.example.ui.components.DigitalInvoiceDialog
import com.example.ui.components.MarketingShareDialog
import com.example.ui.components.PlaceOrderDialog
import com.example.ui.components.ProfitTargetCalculatorDialog
import com.example.ui.components.RoleSwitcherDialog
import com.example.ui.components.SignUpDialog
import com.example.ui.components.SupportHubDialog
import com.example.ui.components.UpdateOrderStatusDialog
import com.example.ui.components.WithdrawDialog
import com.example.ui.screens.AdminConsoleScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OffersScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.TrainingAcademyScreen
import com.example.ui.screens.TrainingModuleDetailDialog
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.MyApplicationTheme

enum class SecondaryScreen {
  NONE,
  NOTIFICATIONS,
  OFFERS,
  ADMIN_CONSOLE
}

class MainActivity : ComponentActivity() {

  private val viewModel: ResellerViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ResellerApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ResellerApp(viewModel: ResellerViewModel) {
  val activeAgent by viewModel.activeAgent.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()
  val allProducts by viewModel.allProducts.collectAsState()
  val trendingProducts by viewModel.trendingProducts.collectAsState()
  val filteredProducts by viewModel.filteredProducts.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val allOrders by viewModel.allOrders.collectAsState()
  val allPayouts by viewModel.allPayouts.collectAsState()
  val activeOffers by viewModel.activeOffers.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val unreadCount by viewModel.unreadNotificationsCount.collectAsState()
  val leaderboard by viewModel.leaderboard.collectAsState()
  val allAgents by viewModel.allAgents.collectAsState()
  val allAuditLogs by viewModel.allAuditLogs.collectAsState()
  val allCpaConversions by viewModel.allCpaConversions.collectAsState()
  val currencyMode by viewModel.currencyMode.collectAsState()

  // Training state
  val trainingModules by viewModel.allTrainingModules.collectAsState()
  val completedTrainingCount by viewModel.completedTrainingCount.collectAsState()
  val selectedTrainingModule by viewModel.selectedTrainingModule.collectAsState()

  // Professional Free Platform Tools State
  val selectedOrderForInvoice by viewModel.selectedOrderForInvoice.collectAsState()
  val isTargetCalcVisible by viewModel.isTargetCalculatorVisible.collectAsState()
  val isSupportHubVisible by viewModel.isSupportHubVisible.collectAsState()

  // Modal Dialog States
  val productForMarketing by viewModel.selectedProductForMarketing.collectAsState()
  val productForOrder by viewModel.selectedProductForOrder.collectAsState()
  val isWithdrawVisible by viewModel.isWithdrawDialogVisible.collectAsState()
  val isAddProductVisible by viewModel.isAddProductDialogVisible.collectAsState()
  val isBroadcastVisible by viewModel.isBroadcastDialogVisible.collectAsState()
  val isRoleSwitchVisible by viewModel.isRoleSwitchDialogVisible.collectAsState()
  val isSignUpVisible by viewModel.isSignUpDialogVisible.collectAsState()
  val orderForAdminStatus by viewModel.selectedOrderForAdminStatus.collectAsState()
  val feedbackMessage by viewModel.userFeedbackMessage.collectAsState()

  var secondaryScreen by remember { mutableStateOf(SecondaryScreen.NONE) }
  var isAdminPinDialogOpen by remember { mutableStateOf(false) }
  val snackbarHostState = remember { SnackbarHostState() }

  val isAdmin = activeAgent?.role == "ADMIN"

  // Filter orders and payouts for active agent unless in admin mode
  val visibleOrders = remember(allOrders, activeAgent, isAdmin) {
    if (isAdmin) allOrders
    else allOrders.filter { it.agentId == activeAgent?.id }
  }

  val visiblePayouts = remember(allPayouts, activeAgent, isAdmin) {
    if (isAdmin) allPayouts
    else allPayouts.filter { it.agentId == activeAgent?.id }
  }

  // Handle back press if secondary screen is active
  BackHandler(enabled = secondaryScreen != SecondaryScreen.NONE) {
    secondaryScreen = SecondaryScreen.NONE
  }

  // Display feedback snackbar
  LaunchedEffect(feedbackMessage) {
    feedbackMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.clearFeedbackMessage()
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      AppHeader(
        agent = activeAgent,
        unreadCount = unreadCount,
        onNotificationClick = {
          secondaryScreen = SecondaryScreen.NOTIFICATIONS
        },
        onSwitchRoleClick = {
          viewModel.setRoleSwitchDialogVisible(true)
        }
      )
    },
    bottomBar = {
      if (secondaryScreen == SecondaryScreen.NONE) {
        NavigationBar {
          NavigationBarItem(
            selected = currentTab == MainTab.HOME,
            onClick = { viewModel.setTab(MainTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(22.dp)) },
            label = { Text("হোম", fontSize = 10.sp, fontWeight = if (currentTab == MainTab.HOME) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = BrandPrimary,
              selectedTextColor = BrandPrimary,
              indicatorColor = BrandPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("tab_home")
          )
          NavigationBarItem(
            selected = currentTab == MainTab.CATALOG,
            onClick = { viewModel.setTab(MainTab.CATALOG) },
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Products", modifier = Modifier.size(22.dp)) },
            label = { Text("প্রোডাক্ট", fontSize = 10.sp, fontWeight = if (currentTab == MainTab.CATALOG) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = BrandPrimary,
              selectedTextColor = BrandPrimary,
              indicatorColor = BrandPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("tab_catalog")
          )
          NavigationBarItem(
            selected = currentTab == MainTab.ORDERS,
            onClick = { viewModel.setTab(MainTab.ORDERS) },
            icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Orders", modifier = Modifier.size(22.dp)) },
            label = { Text("অর্ডার", fontSize = 10.sp, fontWeight = if (currentTab == MainTab.ORDERS) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = BrandPrimary,
              selectedTextColor = BrandPrimary,
              indicatorColor = BrandPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("tab_orders")
          )
          NavigationBarItem(
            selected = currentTab == MainTab.TRAINING,
            onClick = { viewModel.setTab(MainTab.TRAINING) },
            icon = { Icon(Icons.Default.School, contentDescription = "Academy", modifier = Modifier.size(22.dp)) },
            label = { Text("একাডেমি", fontSize = 10.sp, fontWeight = if (currentTab == MainTab.TRAINING) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = BrandPrimary,
              selectedTextColor = BrandPrimary,
              indicatorColor = BrandPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("tab_training")
          )
          NavigationBarItem(
            selected = currentTab == MainTab.WALLET,
            onClick = { viewModel.setTab(MainTab.WALLET) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet", modifier = Modifier.size(22.dp)) },
            label = { Text("ওয়ালেট", fontSize = 10.sp, fontWeight = if (currentTab == MainTab.WALLET) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = BrandPrimary,
              selectedTextColor = BrandPrimary,
              indicatorColor = BrandPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("tab_wallet")
          )
          NavigationBarItem(
            selected = currentTab == MainTab.LEADERBOARD,
            onClick = { viewModel.setTab(MainTab.LEADERBOARD) },
            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard", modifier = Modifier.size(22.dp)) },
            label = { Text("র‍্যাংক", fontSize = 10.sp, fontWeight = if (currentTab == MainTab.LEADERBOARD) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = BrandSecondary,
              selectedTextColor = BrandSecondary,
              indicatorColor = BrandSecondary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("tab_leaderboard")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding)) {
      when (secondaryScreen) {
        SecondaryScreen.NOTIFICATIONS -> {
          NotificationsScreen(
            notifications = notifications,
            isAdmin = isAdmin,
            onMarkAllRead = { viewModel.markAllNotificationsRead() },
            onOpenBroadcastDialog = { viewModel.setBroadcastDialogVisible(true) }
          )
        }
        SecondaryScreen.OFFERS -> {
          OffersScreen(offers = activeOffers)
        }
        SecondaryScreen.ADMIN_CONSOLE -> {
          AdminConsoleScreen(
            products = allProducts,
            orders = allOrders,
            payouts = allPayouts,
            agents = if (allAgents.isNotEmpty()) allAgents else leaderboard,
            auditLogs = allAuditLogs,
            cpaConversions = allCpaConversions,
            onAddNewProduct = { viewModel.setAddProductDialogVisible(true) },
            onUpdateOrderStatus = { order -> viewModel.setSelectedOrderForAdminStatus(order) },
            onApprovePayout = { payout -> viewModel.adminApprovePayout(payout) },
            onBroadcastPushNotification = { viewModel.setBroadcastDialogVisible(true) },
            onOpenInvoice = { order -> viewModel.openInvoice(order) },
            onAdjustAgentBalance = { agentId, amount, type, reason ->
              viewModel.adminAdjustAgentBalance(agentId, amount, type, reason)
            },
            onUpdateAgentStatus = { agentId, status, reason ->
              viewModel.adminUpdateAgentStatus(agentId, status, reason)
            },
            onUpdateCpaStatus = { conversion, status, notes ->
              viewModel.adminUpdateCpaStatus(conversion, status, notes)
            },
            onCloseAdminConsole = {
              viewModel.logoutAdmin()
              secondaryScreen = SecondaryScreen.NONE
            }
          )
        }
        SecondaryScreen.NONE -> {
          when (currentTab) {
            MainTab.HOME -> {
              HomeScreen(
                agent = activeAgent,
                trendingProducts = trendingProducts,
                activeOffers = activeOffers,
                onNavigateTab = { tab -> viewModel.setTab(tab) },
                onOpenMarketing = { product -> viewModel.openMarketingDialog(product) },
                onOpenOrder = { product -> viewModel.openOrderPlacement(product) },
                onOpenWithdraw = { viewModel.setWithdrawDialogVisible(true) },
                onOpenOffers = { secondaryScreen = SecondaryScreen.OFFERS },
                onOpenTargetCalculator = { viewModel.setTargetCalculatorVisible(true) },
                onOpenSupportHub = { viewModel.setSupportHubVisible(true) },
                onOpenAdminPanel = {
                  if (isAdmin) {
                    secondaryScreen = SecondaryScreen.ADMIN_CONSOLE
                  } else {
                    isAdminPinDialogOpen = true
                  }
                }
              )
            }
            MainTab.CATALOG -> {
              CatalogScreen(
                products = filteredProducts,
                selectedCategory = selectedCategory,
                searchQuery = searchQuery,
                isAdmin = isAdmin,
                onCategorySelected = { cat -> viewModel.setCategory(cat) },
                onSearchQueryChanged = { q -> viewModel.setSearchQuery(q) },
                onOpenMarketing = { product -> viewModel.openMarketingDialog(product) },
                onOpenOrder = { product -> viewModel.openOrderPlacement(product) },
                onAddNewProduct = { viewModel.setAddProductDialogVisible(true) }
              )
            }
            MainTab.ORDERS -> {
              OrdersScreen(
                orders = visibleOrders,
                isAdmin = isAdmin,
                onAdminChangeStatus = { order -> viewModel.setSelectedOrderForAdminStatus(order) },
                onOpenInvoice = { order -> viewModel.openInvoice(order) }
              )
            }
            MainTab.TRAINING -> {
              TrainingAcademyScreen(
                modules = trainingModules,
                completedCount = completedTrainingCount,
                onSelectModule = { module -> viewModel.selectTrainingModule(module) },
                onToggleCompleted = { id, comp -> viewModel.toggleModuleCompleted(id, comp) }
              )
            }
            MainTab.WALLET -> {
              WalletScreen(
                agent = activeAgent,
                payouts = visiblePayouts,
                isAdmin = isAdmin,
                onOpenWithdraw = { viewModel.setWithdrawDialogVisible(true) },
                onAdminApprovePayout = { payout -> viewModel.adminApprovePayout(payout) }
              )
            }
            MainTab.LEADERBOARD -> {
              LeaderboardScreen(
                currentAgent = activeAgent,
                agents = leaderboard
              )
            }
          }
        }
      }
    }
  }

  // CPA Lead Marketing Kit & Social Affiliate Media Dialog
  productForMarketing?.let { prod ->
    CpaLeadMarketingKitDialog(
      product = prod,
      agent = activeAgent,
      onDismiss = { viewModel.openMarketingDialog(null) },
      onSubmitCustomerLead = { qty, resellPrice, name, phone, address, district, area ->
        viewModel.submitOrder(prod, qty, resellPrice, name, phone, address, district, area)
      }
    )
  }

  // Place Customer Order Dialog
  productForOrder?.let { prod ->
    PlaceOrderDialog(
      product = prod,
      agent = activeAgent,
      onDismiss = { viewModel.openOrderPlacement(null) },
      onSubmit = { qty, resellPrice, name, phone, address, district, area ->
        viewModel.submitOrder(prod, qty, resellPrice, name, phone, address, district, area)
      }
    )
  }

  // Training Module Detail / Interactive Video Dialog
  selectedTrainingModule?.let { module ->
    TrainingModuleDetailDialog(
      module = module,
      onDismiss = { viewModel.selectTrainingModule(null) },
      onToggleCompleted = { comp -> viewModel.toggleModuleCompleted(module.id, comp) }
    )
  }

  // Digital Invoice Dialog
  selectedOrderForInvoice?.let { order ->
    DigitalInvoiceDialog(
      order = order,
      agent = activeAgent,
      onDismiss = { viewModel.openInvoice(null) }
    )
  }

  // Profit Target & Sales Goal Calculator Dialog
  if (isTargetCalcVisible) {
    ProfitTargetCalculatorDialog(
      onDismiss = { viewModel.setTargetCalculatorVisible(false) }
    )
  }

  // Free Support Hub Dialog (WhatsApp, Call, Facebook Group)
  if (isSupportHubVisible) {
    SupportHubDialog(
      onDismiss = { viewModel.setSupportHubVisible(false) }
    )
  }

  // Withdrawal Dialog
  if (isWithdrawVisible) {
    WithdrawDialog(
      availableBalance = activeAgent?.availableBalance ?: 0.0,
      agent = activeAgent,
      onDismiss = { viewModel.setWithdrawDialogVisible(false) },
      onSubmit = { amount, method, account ->
        viewModel.requestWithdrawal(amount, method, account)
      }
    )
  }

  // Admin Add Product Dialog
  if (isAddProductVisible) {
    AddProductDialog(
      onDismiss = { viewModel.setAddProductDialogVisible(false) },
      onSubmit = { title, cat, wholesale, retail, comm, points, stock, desc, fabric ->
        viewModel.adminAddNewProduct(title, cat, wholesale, retail, comm, points, stock, desc, fabric)
      }
    )
  }

  // Admin Order Status Update Dialog
  orderForAdminStatus?.let { order ->
    UpdateOrderStatusDialog(
      order = order,
      onDismiss = { viewModel.setSelectedOrderForAdminStatus(null) },
      onSubmit = { status, tracking, courier ->
        viewModel.adminUpdateOrderStatus(order, status, tracking, courier)
      }
    )
  }

  // Admin Broadcast Notification Dialog
  if (isBroadcastVisible) {
    BroadcastNotificationDialog(
      onDismiss = { viewModel.setBroadcastDialogVisible(false) },
      onSubmit = { title, body ->
        viewModel.adminSendBroadcastNotification(title, body)
      }
    )
  }

  // Role Switcher Dialog
  if (isRoleSwitchVisible) {
    RoleSwitcherDialog(
      currentAgentId = activeAgent?.id ?: 1L,
      agents = if (allAgents.isNotEmpty()) allAgents else leaderboard,
      onDismiss = { viewModel.setRoleSwitchDialogVisible(false) },
      onSelectAgent = { id -> viewModel.switchAgent(id) },
      onOpenSignUp = { viewModel.setSignUpDialogVisible(true) },
      onOpenAdminLogin = { isAdminPinDialogOpen = true }
    )
  }

  // Agent Sign Up Dialog
  if (isSignUpVisible) {
    SignUpDialog(
      onDismiss = { viewModel.setSignUpDialogVisible(false) },
      onSubmit = { name, phone, district, biz, bkash ->
        viewModel.registerNewAgent(name, phone, district, biz, bkash)
      }
    )
  }

  // Admin PIN Protection Dialog
  if (isAdminPinDialogOpen) {
    AdminPinDialog(
      onDismiss = { isAdminPinDialogOpen = false },
      onPinVerified = {
        isAdminPinDialogOpen = false
        viewModel.loginAsAdmin()
        secondaryScreen = SecondaryScreen.ADMIN_CONSOLE
      }
    )
  }
}
