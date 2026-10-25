package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AgentEntity
import com.example.data.model.AuditLogEntity
import com.example.data.model.CpaConversionEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PayoutEntity
import com.example.data.model.ProductEntity
import com.example.ui.components.AdminBalanceAdjustmentDialog
import com.example.ui.components.AdminCpaReviewDialog
import com.example.ui.components.OrderStatusBadge
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminConsoleScreen(
  products: List<ProductEntity>,
  orders: List<OrderEntity>,
  payouts: List<PayoutEntity>,
  agents: List<AgentEntity>,
  auditLogs: List<AuditLogEntity>,
  cpaConversions: List<CpaConversionEntity>,
  onAddNewProduct: () -> Unit,
  onUpdateOrderStatus: (OrderEntity) -> Unit,
  onApprovePayout: (PayoutEntity) -> Unit,
  onBroadcastPushNotification: () -> Unit,
  onOpenInvoice: (OrderEntity) -> Unit,
  onAdjustAgentBalance: (agentId: Long, amount: Double, actionType: String, reason: String) -> Unit,
  onUpdateAgentStatus: (agentId: Long, newStatus: String, reason: String) -> Unit,
  onUpdateCpaStatus: (conversion: CpaConversionEntity, newStatus: String, notes: String) -> Unit,
  onCloseAdminConsole: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf(
    "ড্যাশবোর্ড ও রিপোর্ট",
    "CPS অর্ডার (${orders.size})",
    "CPA লিড (${cpaConversions.size})",
    "ইউজার ও এজেন্ট (${agents.size})",
    "প্রোডাক্ট ক্যাটালগ",
    "পেআউট ও অডিট লগ"
  )

  // Report Date Filter State
  var reportPeriod by remember { mutableStateOf("চলতি মাস") }
  val periodOptions = listOf("আজকে", "চলতি সপ্তাহ", "চলতি মাস", "সর্বমোট")

  // Agent Management State
  var agentSearchQuery by remember { mutableStateOf("") }
  var agentStatusFilter by remember { mutableStateOf("সব") }
  var selectedAgentForBalance by remember { mutableStateOf<AgentEntity?>(null) }

  // CPA Review State
  var selectedConversionForReview by remember { mutableStateOf<CpaConversionEntity?>(null) }

  val webAdminUrl = "https://admin.standardbusiness.com/portal/login?auth=owner"

  // Financial aggregates
  val totalDeliveredRevenue = orders.filter { it.status == "ডেলিভারি সম্পন্ন" }.sumOf { it.codAmount }
  val totalAgentCommissions = orders.filter { it.status == "ডেলিভারি সম্পন্ন" }.sumOf { it.agentProfit }
  val totalCpaPayouts = cpaConversions.filter { it.status == "APPROVED" }.sumOf { it.payoutAmount }
  val pendingPayouts = payouts.filter { it.status == "পেন্ডিং" }.sumOf { it.amount }
  val pendingOrdersCount = orders.count { it.status != "ডেলিভারি সম্পন্ন" && it.status != "বাতিল" }
  val pendingCpaCount = cpaConversions.count { it.status == "PENDING" }

  // Filtered Agents
  val filteredAgents = agents.filter { ag ->
    val matchesStatus = when (agentStatusFilter) {
      "সক্রিয়" -> ag.status == "ACTIVE"
      "স্থগিত" -> ag.status == "SUSPENDED"
      else -> true
    }
    val q = agentSearchQuery.trim().lowercase(Locale.ROOT)
    val matchesQuery = q.isEmpty() ||
        ag.name.lowercase(Locale.ROOT).contains(q) ||
        ag.phone.contains(q) ||
        ag.businessName.lowercase(Locale.ROOT).contains(q) ||
        ag.referralCode.lowercase(Locale.ROOT).contains(q) ||
        ag.id.toString() == q

    matchesStatus && matchesQuery
  }

  // Active Dialogs
  selectedAgentForBalance?.let { ag ->
    AdminBalanceAdjustmentDialog(
      agent = ag,
      onDismiss = { selectedAgentForBalance = null },
      onConfirmAdjustment = { id, amount, type, reason ->
        onAdjustAgentBalance(id, amount, type, reason)
      }
    )
  }

  selectedConversionForReview?.let { conv ->
    AdminCpaReviewDialog(
      conversion = conv,
      onDismiss = { selectedConversionForReview = null },
      onUpdateStatus = { status, notes ->
        onUpdateCpaStatus(conv, status, notes)
      }
    )
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 90.dp)
  ) {
    // 1. Admin Master Header & Brand Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
          .testTag("admin_console_header_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, BrandPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                  .background(Color.White),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_standard_business_logo),
                  contentDescription = "Standard Business Logo",
                  modifier = Modifier.size(42.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Standard Business",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrandPrimary
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Surface(color = BrandSecondary, shape = RoundedCornerShape(4.dp)) {
                    Text(
                      text = "Master Admin",
                      color = Color.White,
                      fontSize = 9.5.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                    )
                  }
                }
                Text(
                  text = "🔐 এনক্রিপ্টেড ডিরেক্টর সেশন • ID: 99",
                  fontSize = 11.sp,
                  color = SuccessGreen,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            // Secure Logout Button
            IconButton(
              onClick = onCloseAdminConsole,
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(WarningOrange.copy(alpha = 0.12f))
                .testTag("admin_logout_btn")
            ) {
              Icon(imageVector = Icons.Default.Logout, contentDescription = "লগআউট", tint = WarningOrange, modifier = Modifier.size(20.dp))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Web Admin Portal URL Quick Copy
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "https://admin.standardbusiness.com/portal",
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
              OutlinedButton(
                onClick = {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  clipboard.setPrimaryClip(ClipData.newPlainText("Admin URL", webAdminUrl))
                  Toast.makeText(context, "ওয়েব অ্যাডমিন লিংক কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "কপি", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // 2. Tab Navigation
    item {
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 0.dp,
        containerColor = Color.Transparent,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedTab == index) BrandPrimary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          )
        }
      }
    }

    // 3. Tab Contents
    when (selectedTab) {
      0 -> { // ড্যাশবোর্ড ও রিপোর্ট
        item {
          // Date Filter Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "রিপোর্ট সময়কাল:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(periodOptions) { opt ->
                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (reportPeriod == opt) BrandPrimary else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { reportPeriod = opt }
                ) {
                  Text(
                    text = opt,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (reportPeriod == opt) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        // Summary Metric Grid
        item {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricStatCard(
              title = "মোট সংগৃহীত COD",
              value = "৳${totalDeliveredRevenue.toInt()}",
              color = BrandPrimary,
              modifier = Modifier.weight(1f)
            )
            MetricStatCard(
              title = "এজেন্ট CPS কমিশন",
              value = "৳${totalAgentCommissions.toInt()}",
              color = SuccessGreen,
              modifier = Modifier.weight(1f)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricStatCard(
              title = "অনুমোদিত CPA কমিশন",
              value = "৳${totalCpaPayouts.toInt()}",
              color = BrandSecondary,
              modifier = Modifier.weight(1f)
            )
            MetricStatCard(
              title = "পেন্ডিং পেআউট",
              value = "৳${pendingPayouts.toInt()}",
              color = WarningOrange,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Export / Share Report Action
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(text = "📊 $reportPeriod ব্যবসার সারসংক্ষেপ রিপোর্ট", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "অর্ডার: ${orders.size}টি | CPA লিড: ${cpaConversions.size}টি | সক্রিয় এজেন্ট: ${agents.count { it.status == "ACTIVE" }}জন", fontSize = 11.sp, color = Color.Gray)
              }
              Button(
                onClick = {
                  val reportText = """
                    📌 Standard Business Reseller Pro রিপোর্ট ($reportPeriod)
                    ━━━━━━━━━━━━━━━━━━━━━━
                    • মোট COD সেল: ৳${totalDeliveredRevenue.toInt()}
                    • এজেন্ট CPS কমিশন: ৳${totalAgentCommissions.toInt()}
                    • CPA পেইড কমিশন: ৳${totalCpaPayouts.toInt()}
                    • পেন্ডিং পেআউট: ৳${pendingPayouts.toInt()}
                    • মোট অর্ডার: ${orders.size}টি
                    • পেন্ডিং কুরিয়ার পার্সেল: ${pendingOrdersCount}টি
                    • মোট নিবন্ধিত উদ্যোক্তা: ${agents.size} জন
                    ━━━━━━━━━━━━━━━━━━━━━━
                    জেনারেট করেছেন: Standard Business পরিচালক
                  """.trimIndent()
                  val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, reportText)
                    type = "text/plain"
                  }
                  context.startActivity(Intent.createChooser(sendIntent, "রিপোর্ট শেয়ার করুন"))
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "শেয়ার", fontSize = 11.5.sp)
              }
            }
          }
        }
      }

      1 -> { // CPS অর্ডার
        items(orders) { order ->
          AdminOrderRowCard(
            order = order,
            onUpdateStatus = { onUpdateOrderStatus(order) },
            onOpenInvoice = { onOpenInvoice(order) },
            onCall = {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.customerPhone}"))
              context.startActivity(intent)
            }
          )
        }
      }

      2 -> { // CPA লিড ও কনভার্সন
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 10.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSecondary.copy(alpha = 0.1f))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(imageVector = Icons.Default.Assessment, contentDescription = null, tint = BrandSecondary)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(text = "CPA লিড অডিট ও অ্যান্টি-ফ্রড সিস্টেম", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "ডুপ্লিকেট ফোন ও ইমেইল প্রতিরোধে স্বয়ংক্রিয় হ্যাশ ট্র্যাকিং কার্যকর রয়েছে।", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }

        items(cpaConversions) { conv ->
          AdminCpaConversionRowCard(
            conversion = conv,
            onReview = { selectedConversionForReview = conv }
          )
        }
      }

      3 -> { // ইউজার ও এজেন্ট ম্যানেজমেন্ট
        item {
          Column(modifier = Modifier.padding(bottom = 10.dp)) {
            // Search Input
            OutlinedTextField(
              value = agentSearchQuery,
              onValueChange = { agentSearchQuery = it },
              placeholder = { Text("নাম, ফোন, ইউজার ID বা রেফারেল কোড দিয়ে সার্চ...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_agent_search_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status Filter Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("সব", "সক্রিয়", "স্থগিত").forEach { st ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (agentStatusFilter == st) BrandPrimary else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { agentStatusFilter = st }
                ) {
                  Text(
                    text = st,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (agentStatusFilter == st) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        items(filteredAgents) { ag ->
          AdminAgentManagementCard(
            agent = ag,
            onOpenBalanceAdjustment = { selectedAgentForBalance = ag },
            onToggleStatus = {
              val newStatus = if (ag.status == "ACTIVE") "SUSPENDED" else "ACTIVE"
              val reason = if (newStatus == "SUSPENDED") "অ্যাডমিন কর্তৃক অ্যাকাউন্ট স্থগিত করা হয়েছে" else "অ্যাকাউন্ট পুনরায় সক্রিয় করা হয়েছে"
              onUpdateAgentStatus(ag.id, newStatus, reason)
            }
          )
        }
      }

      4 -> { // প্রোডাক্ট ক্যাটালগ
        item {
          Button(
            onClick = onAddNewProduct,
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 10.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("নতুন CPS প্রোডাক্ট যুক্ত করুন", fontWeight = FontWeight.Bold)
          }
        }

        items(products) { prod ->
          AdminProductRowCard(product = prod)
        }
      }

      5 -> { // পেআউট ও অডিট লগ
        item {
          Text(text = "পেন্ডিং পেআউট অনুমোদন", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
        }

        val pendingList = payouts.filter { it.status == "পেন্ডিং" }
        if (pendingList.isEmpty()) {
          item {
            Text(text = "বর্তমানে কোনো পেন্ডিং পেআউট রিকোয়েস্ট নেই।", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 14.dp))
          }
        } else {
          items(pendingList) { p ->
            AdminPayoutRowCard(payout = p, onApprove = { onApprovePayout(p) })
          }
        }

        item {
          Spacer(modifier = Modifier.height(14.dp))
          Text(text = "অপরিবর্তনীয় সার্ভার অডিট লগ (Audit Trail)", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
        }

        items(auditLogs) { log ->
          AdminAuditLogRowCard(log = log)
        }
      }
    }
  }
}

@Composable
fun MetricStatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
    }
  }
}

@Composable
fun AdminAgentManagementCard(
  agent: AgentEntity,
  onOpenBalanceAdjustment: () -> Unit,
  onToggleStatus: () -> Unit
) {
  val isActive = agent.status == "ACTIVE"
  val isDirector = agent.role == "ADMIN"

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(if (isDirector) BrandSecondary.copy(alpha = 0.15f) else BrandPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = agent.name.take(1), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (isDirector) BrandSecondary else BrandPrimary)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(text = agent.name, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isActive) SuccessGreen.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f)
              ) {
                Text(
                  text = if (isActive) "সক্রিয়" else "স্থগিত",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isActive) SuccessGreen else Color.Red,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            Text(
              text = "ID: ${agent.id} • Ref: ${agent.referralCode.ifEmpty { "REF-${agent.id}" }} • ${agent.district}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(text = "৳${agent.availableBalance.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = SuccessGreen)
          Text(text = "ব্যালেন্স", fontSize = 10.sp, color = Color.Gray)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "ফোন: ${agent.phone}", fontSize = 11.5.sp)
        Text(text = "অর্ডার: ${agent.totalOrders}টি | CPA: ${agent.cpaConversionsCount}টি", fontSize = 11.5.sp, color = BrandPrimary, fontWeight = FontWeight.SemiBold)
      }

      if (!isDirector) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(
            onClick = onOpenBalanceAdjustment,
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.weight(1.3f)
          ) {
            Icon(Icons.Default.AttachMoney, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "ব্যালেন্স ও বোনাস (+/−)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = onToggleStatus,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = if (isActive) Icons.Default.Block else Icons.Default.Check, contentDescription = null, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = if (isActive) "সাসপেন্ড" else "সক্রিয় করুন", fontSize = 11.sp)
          }
        }
      }
    }
  }
}

@Composable
fun AdminCpaConversionRowCard(
  conversion: CpaConversionEntity,
  onReview: () -> Unit
) {
  val isApproved = conversion.status == "APPROVED"
  val isPending = conversion.status == "PENDING"
  val statusColor = when (conversion.status) {
    "APPROVED" -> SuccessGreen
    "PENDING" -> WarningOrange
    "REVERSED" -> Color.Magenta
    else -> Color.Red
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = conversion.offerTitle, fontWeight = FontWeight.Bold, fontSize = 12.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
        Surface(shape = RoundedCornerShape(6.dp), color = statusColor.copy(alpha = 0.15f)) {
          Text(text = conversion.status, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 10.5.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
      }

      Spacer(modifier = Modifier.height(4.dp))
      Text(text = "কাস্টমার: ${conversion.customerName} (${conversion.customerPhone})", fontSize = 11.5.sp)
      Text(text = "রেফারার: ${conversion.agentName} | কমিশন: ৳${conversion.payoutAmount.toInt()}", fontSize = 11.sp, color = BrandPrimary)

      if (conversion.adminNotes.isNotBlank()) {
        Text(text = "নোট: ${conversion.adminNotes}", fontSize = 10.5.sp, color = Color.Gray)
      }

      Spacer(modifier = Modifier.height(6.dp))
      Button(
        onClick = onReview,
        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "যাচাই ও কমিশন অনুমোদন / রিজেক্ট", fontSize = 11.sp)
      }
    }
  }
}

@Composable
fun AdminAuditLogRowCard(log: AuditLogEntity) {
  val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(log.timestamp))

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = log.actionType, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BrandPrimary)
        Text(text = dateStr, fontSize = 10.sp, color = Color.Gray)
      }
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = "ইউজার: ${log.targetAgentName} (ID: ${log.targetAgentId}) | পরিবর্তন: ৳${log.amount.toInt()}", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
      Text(text = "কারণ: ${log.reason}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(text = "পূর্ববর্তী ব্যালেন্স: ৳${log.previousBalance.toInt()} ➔ বর্তমান: ৳${log.newBalance.toInt()} • অ্যাডমিন: ${log.adminName}", fontSize = 10.sp, color = Color.Gray)
    }
  }
}

@Composable
fun AdminOrderRowCard(
  order: OrderEntity,
  onUpdateStatus: () -> Unit,
  onOpenInvoice: () -> Unit,
  onCall: () -> Unit
) {
  val dateStr = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date(order.orderDate))

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "#${order.orderNumber} • $dateStr", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OrderStatusBadge(status = order.status)
      }

      Divider(modifier = Modifier.padding(vertical = 6.dp))

      Text(text = "${order.productTitle} (x${order.quantity})", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
      Text(text = "কাস্টমার: ${order.customerName} (${order.customerPhone})", fontSize = 11.5.sp)
      Text(text = "ঠিকানা: ${order.customerAddress}, ${order.district}", fontSize = 11.sp, color = Color.Gray)

      Spacer(modifier = Modifier.height(4.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "COD কালেকশন: ৳${order.codAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(text = "এজেন্ট কমিশন: ৳${order.agentProfit.toInt()}", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }

      if (order.trackingNumber.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "কুরিয়ার: ${order.courierName} | ট্র্যাকিং: ${order.trackingNumber}", fontSize = 11.sp, color = BrandPrimary)
      }

      Spacer(modifier = Modifier.height(8.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Button(
          onClick = onUpdateStatus,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier.weight(1.2f)
        ) {
          Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "স্ট্যাটাস পরিবর্তন", fontSize = 11.sp)
        }
        OutlinedButton(
          onClick = onOpenInvoice,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text(text = "ক্যাশ মেমো", fontSize = 11.sp)
        }
        OutlinedButton(
          onClick = onCall,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
          modifier = Modifier.weight(0.8f)
        ) {
          Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "কল", fontSize = 11.sp)
        }
      }
    }
  }
}

@Composable
fun AdminProductRowCard(product: ProductEntity) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(BrandPrimary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(22.dp))
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = "${product.category} • স্টক: ${product.stock}টি", fontSize = 11.sp, color = if (product.stock < 10) Color.Red else SuccessGreen)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = "পাইকারি: ৳${product.wholesalePrice.toInt()}", fontSize = 11.sp, color = Color.Gray)
          Text(text = "খুচরা: ৳${product.retailPrice.toInt()}", fontSize = 11.sp)
          Text(text = "কমিশন: ৳${product.baseCommission.toInt()}", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
      }
    }
  }
}

@Composable
fun AdminPayoutRowCard(
  payout: PayoutEntity,
  onApprove: () -> Unit
) {
  val isPaid = payout.status == "পেমেন্ট সম্পন্ন"
  val methodColor = if (payout.method == "bKash") BkashPink else NagadOrange

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(shape = RoundedCornerShape(6.dp), color = methodColor.copy(alpha = 0.15f)) {
            Text(text = payout.method, color = methodColor, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "${payout.agentName} (${payout.accountNumber})", fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp)
        }
        Text(text = "৳${payout.amount.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = if (isPaid) SuccessGreen else WarningOrange)
      }

      if (isPaid && payout.trxId.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "TrxID: ${payout.trxId} (অনুমোদিত)", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Medium)
      }

      if (!isPaid) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = onApprove,
          colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "উইথড্রয়াল অনুমোদন করুন (বিকাশ/নগদ পেমেন্ট সম্পন্ন)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
