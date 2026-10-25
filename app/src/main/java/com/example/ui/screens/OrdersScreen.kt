package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.ui.components.OrderStatusBadge
import com.example.ui.components.OrderTimelineView
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
  orders: List<OrderEntity>,
  isAdmin: Boolean,
  onAdminChangeStatus: (OrderEntity) -> Unit,
  onOpenInvoice: (OrderEntity) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedFilter by remember { mutableStateOf("সব") }
  val filters = listOf("সব", "অর্ডার প্রাপ্ত", "প্রসেসিং", "কুরিয়ারে হস্তান্তর", "ডেলিভারি সম্পন্ন")

  val filteredOrders = remember(orders, selectedFilter) {
    if (selectedFilter == "সব") orders
    else orders.filter { it.status == selectedFilter }
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Top summary banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = null,
            tint = BrandPrimary,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = if (isAdmin) "অর্ডার ম্যানেজমেন্ট ও কুরিয়ার ট্র্যাকিং" else "আপনার কাস্টমার অর্ডার লাইভ ট্র্যাকিং",
              fontWeight = FontWeight.Bold,
              fontSize = 13.5.sp,
              color = BrandPrimary
            )
            Text(
              text = "কুরিয়ার পার্টনার: Steadfast, Pathao & RedX",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Filter Chips
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filters) { f ->
          val isSelected = f == selectedFilter
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) BrandPrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable { selectedFilter = f }
              .testTag("order_filter_$f")
          ) {
            Text(
              text = f,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
            )
          }
        }
      }
    }

    // Orders Count
    item {
      Text(
        text = "মোট অর্ডার: ${filteredOrders.size}টি",
        fontSize = 12.5.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
      )
    }

    if (filteredOrders.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.LocalShipping,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "কোনো অর্ডার পাওয়া যায়নি",
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(filteredOrders) { order ->
        OrderItemTrackingCard(
          order = order,
          isAdmin = isAdmin,
          onAdminChangeStatus = { onAdminChangeStatus(order) },
          onOpenInvoice = { onOpenInvoice(order) },
          onCallCustomer = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.customerPhone}"))
            context.startActivity(intent)
          }
        )
      }
    }
  }
}

@Composable
fun OrderItemTrackingCard(
  order: OrderEntity,
  isAdmin: Boolean,
  onAdminChangeStatus: () -> Unit,
  onOpenInvoice: () -> Unit,
  onCallCustomer: () -> Unit
) {
  val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
  val formattedDate = dateFormatter.format(Date(order.orderDate))

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Order ID, Date, and Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "অর্ডার #${order.orderNumber}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.5.sp,
            color = BrandPrimary
          )
          Text(
            text = formattedDate,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        OrderStatusBadge(status = order.status)
      }

      Divider(modifier = Modifier.padding(vertical = 10.dp))

      // Product and financials
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.ShoppingBag,
          contentDescription = null,
          tint = BrandPrimary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "${order.productTitle} (x${order.quantity})",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
          Text(
            text = "কাস্টমার থেকে ক্যাশ কালেকশন (COD): ৳${order.codAmount.toInt()} (ডেলিভারি ফি সহ)",
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Profit highlight
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (order.commissionSettled) SuccessGreen.copy(alpha = 0.15f) else WarningOrange.copy(alpha = 0.12f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (order.commissionSettled) "আপনার অর্জিত কমিশন (ওয়ালেটে যুক্ত):" else "আপনার প্রত্যাশিত কমিশন (পেন্ডিং):",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (order.commissionSettled) SuccessGreen else WarningOrange
          )
          Text(
            text = "৳${order.agentProfit.toInt()} (+${order.pointsEarned}★)",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (order.commissionSettled) SuccessGreen else WarningOrange
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Customer details
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "${order.customerName} (${order.customerPhone})", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onCallCustomer, modifier = Modifier.size(24.dp)) {
              Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = BrandPrimary, modifier = Modifier.size(16.dp))
            }
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${order.customerAddress}, ${order.district} (${order.deliveryArea})",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          if (order.trackingNumber.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "কুরিয়ার: ${order.courierName} | ট্র্যাকিং আইডি: ${order.trackingNumber}",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = BrandPrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Timeline progression
      OrderTimelineView(currentStatus = order.status)

      Spacer(modifier = Modifier.height(10.dp))

      // Quick Order Actions: Digital Cash Memo & Call
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onOpenInvoice,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(imageVector = Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("ক্যাশ মেমো", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
          onClick = onCallCustomer,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("কাস্টমার কল", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      // Admin Action Button
      if (isAdmin) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
          onClick = onAdminChangeStatus,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "অর্ডার স্ট্যাটাস ও ট্র্যাকিং আপডেট", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
