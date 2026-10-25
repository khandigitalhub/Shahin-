package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.data.model.OfferEntity
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OffersScreen(
  offers: List<OfferEntity>,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp)
  ) {
    item {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.LocalOffer,
          contentDescription = null,
          tint = BrandPrimary,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "বাংলাদেশে স্পেশাল অফার ও বোনাস",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Text(
            text = "রিসেলার আপাদের জন্য উৎসব ও পারফরম্যান্স রিওয়ার্ডস",
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    items(offers) { offer ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandPrimary
            ) {
              Text(
                text = offer.categoryTag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Text(
              text = "মেয়াদ: ${offer.validUntil}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = offer.title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = BrandPrimary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = offer.subtitle,
            fontSize = 12.5.sp,
            lineHeight = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = offer.bonusDetails,
              fontSize = 11.5.sp,
              lineHeight = 15.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(10.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandSecondary.copy(alpha = 0.15f)
            ) {
              Text(
                text = "প্রোমো কোড: ${offer.promoCode}",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BrandSecondary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }

            OutlinedButton(
              onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Promo Code", offer.promoCode)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "${offer.promoCode} কোডটি কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("কোড কপি", fontSize = 11.5.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
fun NotificationsScreen(
  notifications: List<NotificationEntity>,
  isAdmin: Boolean,
  onMarkAllRead: () -> Unit,
  onOpenBroadcastDialog: () -> Unit,
  modifier: Modifier = Modifier
) {
  val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp)
  ) {
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "ইন-অ্যাপ পুশ নোটিফিকেশন",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Text(
            text = "অর্ডার, কমিশন ও ক্যাম্পেইন আপডেট",
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        OutlinedButton(
          onClick = onMarkAllRead,
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("সব পঠিত", fontSize = 11.sp)
        }
      }
    }

    if (isAdmin) {
      item {
        Button(
          onClick = onOpenBroadcastDialog,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        ) {
          Icon(imageVector = Icons.Default.Campaign, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("সকল এজেন্টকে নতুন নোটিফিকেশন পাঠান", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
        }
      }
    }

    if (notifications.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "কোনো নতুন নোটিফিকেশন নেই",
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      items(notifications) { notif ->
        val (icon, iconColor) = when (notif.type) {
          "COMMISSION" -> Icons.Default.MonetizationOn to SuccessGreen
          "ORDER" -> Icons.Default.ShoppingBag to BrandPrimary
          "PAYOUT" -> Icons.Default.CheckCircle to Color(0xFF00897B)
          "OFFER" -> Icons.Default.Campaign to BrandSecondary
          else -> Icons.Default.Notifications to BrandPrimary
        }

        Surface(
          shape = RoundedCornerShape(14.dp),
          color = if (notif.isRead) MaterialTheme.colorScheme.surface else BrandPrimary.copy(alpha = 0.08f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = notif.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = notif.body,
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = dateFormatter.format(Date(notif.timestamp)),
                fontSize = 9.5.sp,
                color = MaterialTheme.colorScheme.outline
              )
            }
          }
        }
      }
    }
  }
}
