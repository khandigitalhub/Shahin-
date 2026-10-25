package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentEntity
import com.example.data.model.PayoutEntity
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
fun WalletScreen(
  agent: AgentEntity?,
  payouts: List<PayoutEntity>,
  isAdmin: Boolean,
  onOpenWithdraw: () -> Unit,
  onAdminApprovePayout: (PayoutEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // 1. Wallet Balance Hero Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "উইথড্র যোগ্য ব্যালেন্স",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "৳${agent?.availableBalance?.toInt() ?: 0}",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SuccessGreen
              )
            }
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(26.dp)
              )
            }
          }

          Divider(modifier = Modifier.padding(vertical = 12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "পেন্ডিং কমিশন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "৳${agent?.pendingBalance?.toInt() ?: 0}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WarningOrange)
            }
            Column {
              Text(text = "সর্বমোট কমিশন আর্নিং", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "৳${agent?.totalEarnings?.toInt() ?: 0}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
            }
            Column {
              Text(text = "রিওয়ার্ড পয়েন্ট", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "${agent?.points ?: 0}★", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandSecondary)
            }
          }

          if (!isAdmin) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onOpenWithdraw,
              colors = ButtonDefaults.buttonColors(containerColor = BkashPink),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("wallet_withdraw_button")
            ) {
              Icon(imageVector = Icons.Default.Payment, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("বিকাশ বা নগদে টাকা উইথড্র করুন", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
            }
          }
        }
      }
    }

    // 2. Info on Payment System
    item {
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = BrandPrimary.copy(alpha = 0.08f),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = BrandPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "কাস্টমার ডেলিভারি নেওয়ার পর ক্যাশ কালেক্ট হলে পেন্ডিং কমিশন স্বয়ংক্রিয়ভাবে উইথড্রযোগ্য ব্যালেন্সে যুক্ত হয়। সর্বনিম্ন উইথড্রয়াল ১০০ টাকা।",
            fontSize = 11.5.sp,
            lineHeight = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    // 3. Payout History List
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isAdmin) "সকল এজেন্ট পেআউট রিকোয়েস্ট (${payouts.size})" else "পেমেন্ট উইথড্রয়াল হিস্ট্রি (${payouts.size})",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    if (payouts.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "এখনো কোনো পেমেন্ট রেকর্ড নেই",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      items(payouts) { payout ->
        val isPaid = payout.status == "পেমেন্ট সম্পন্ন"
        val methodColor = when (payout.method) {
          "bKash" -> BkashPink
          "Nagad" -> NagadOrange
          else -> Color(0xFF8C3297)
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = methodColor.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = payout.method,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = methodColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = payout.accountNumber,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp
                )
              }

              Text(
                text = "৳${payout.amount.toInt()}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = if (isPaid) SuccessGreen else WarningOrange
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = dateFormatter.format(Date(payout.requestedDate)),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isPaid) SuccessGreen.copy(alpha = 0.15f) else WarningOrange.copy(alpha = 0.15f)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = if (isPaid) Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = if (isPaid) SuccessGreen else WarningOrange,
                    modifier = Modifier.size(12.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = payout.status,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaid) SuccessGreen else WarningOrange
                  )
                }
              }
            }

            if (payout.trxId.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "ট্রানজেকশন আইডি (TrxID): ${payout.trxId}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandPrimary
              )
            }

            // Admin approve button if pending
            if (isAdmin && !isPaid) {
              Spacer(modifier = Modifier.height(10.dp))
              Button(
                onClick = { onAdminApprovePayout(payout) },
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "পেআউট অনুমোদন করুন (বিকাশ/নগদ ট্রান্সফার)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}
