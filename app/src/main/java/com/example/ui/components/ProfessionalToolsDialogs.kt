package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.AgentEntity
import com.example.data.model.OrderEntity
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DigitalInvoiceDialog(
  order: OrderEntity,
  agent: AgentEntity?,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
  val dateStr = dateFormatter.format(Date(order.orderDate))

  val invoiceText = """
========================================
       STANDARD BUSINESS CASH MEMO      
========================================
মেমো নং: #${order.orderNumber}
তারিখ: $dateStr

[বিক্রেতা / রিসেলার তথ্য]
শপ: ${agent?.businessName ?: "Standard Business Partner"}
এজেন্ট: ${order.agentName}
মোবাইল: ${agent?.phone ?: "017XXXXXXXX"}

[কাস্টমার ডেলিভারি তথ্য]
নাম: ${order.customerName}
ফোন: ${order.customerPhone}
ঠিকানা: ${order.customerAddress}, ${order.district}
এরিয়া: ${order.deliveryArea}

----------------------------------------
আইটেম বিবরণ:
${order.productTitle} x ${order.quantity}
পণ্যের মোট মূল্য: ৳${(order.resellPrice * order.quantity).toInt()}
ডেলিভারি চার্জ: ৳${order.deliveryCharge.toInt()}
----------------------------------------
সর্বমোট প্রদেয় (COD): ৳${order.codAmount.toInt()}
পেমেন্ট মেথড: ক্যাশ অন ডেলিভারি (Cash on Delivery)
কুরিয়ার ট্র্যাকিং কোড: ${order.trackingNumber.ifBlank { "প্রসেসিং হচ্ছে" }}
----------------------------------------
Standard Business দ্বারা গুণগত মান পরীক্ষিত ও অনুমোদিত।
পণ্য হাতে পেয়ে মূল্য পরিশোধের জন্য ধন্যবাদ!
========================================
  """.trimIndent()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 14.dp)
        .testTag("digital_invoice_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Receipt,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "ডিজিটাল ক্যাশ মেমো ও ইনভয়েস",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "Standard Business Authorized Receipt",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Printable / Shareable Invoice Card with Border
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFFFAFAFA),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Watermark brand
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "STANDARD BUSINESS",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = BrandPrimary
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = SuccessGreen.copy(alpha = 0.15f)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = "অফিসিয়াল ভেরিফাইড", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                }
              }
            }

            Text(text = "ইনভয়েস নং: #${order.orderNumber} | $dateStr", fontSize = 10.5.sp, color = Color.Gray)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Parties
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Column(modifier = Modifier.weight(1f)) {
                Text(text = "বিক্রেতা:", fontSize = 10.sp, color = Color.Gray)
                Text(text = agent?.businessName ?: "অনলাইন শপ", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                Text(text = "এজেন্ট: ${order.agentName}", fontSize = 10.5.sp)
              }
              Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(text = "ক্রেতা:", fontSize = 10.sp, color = Color.Gray)
                Text(text = order.customerName, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                Text(text = order.customerPhone, fontSize = 10.5.sp)
                Text(text = "${order.customerAddress}, ${order.district}", fontSize = 10.sp, color = Color.Gray)
              }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Line items
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "${order.productTitle} (x${order.quantity})", fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
              Text(text = "৳${(order.resellPrice * order.quantity).toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "ডেলিভারি ফি (${order.deliveryArea})", fontSize = 11.sp, color = Color.Gray)
              Text(text = "৳${order.deliveryCharge.toInt()}", fontSize = 11.sp)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(text = "সর্বমোট প্রদেয় (ক্যাশ অন ডেলিভারি):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "পণ্য বুঝে পেয়ে মূল্য পরিশোধ করুন", fontSize = 9.5.sp, color = Color.Gray)
              }
              Text(
                text = "৳${order.codAmount.toInt()}",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SuccessGreen
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions: Copy & WhatsApp Share
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Standard Business Invoice", invoiceText)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "ইনভয়েস কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("মেমো কপি", fontSize = 11.5.sp)
          }

          Button(
            onClick = {
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, invoiceText)
                type = "text/plain"
              }
              val shareIntent = Intent.createChooser(sendIntent, "কাস্টমারকে ইনভয়েস পাঠান")
              context.startActivity(shareIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("হোয়াটসঅ্যাপে পাঠান", fontSize = 11.5.sp)
          }
        }
      }
    }
  }
}

@Composable
fun ProfitTargetCalculatorDialog(
  onDismiss: () -> Unit
) {
  var targetIncome by remember { mutableFloatStateOf(20000f) }
  val avgProfitPerOrder = 500f
  val requiredOrders = (targetIncome / avgProfitPerOrder).toInt().coerceAtLeast(1)
  val dailyOrders = String.format(Locale.US, "%.1f", requiredOrders / 30f)

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 14.dp)
        .testTag("target_calculator_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.TrackChanges,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "মাসিক ইনকাম ও সেলস গোল",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "আপনি মাসে কত টাকা উপার্জন করতে চান নির্বাচন করুন:",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "৳${targetIncome.toInt()} / মাস",
          fontSize = 24.sp,
          fontWeight = FontWeight.ExtraBold,
          color = BrandPrimary
        )

        Slider(
          value = targetIncome,
          onValueChange = { targetIncome = it },
          valueRange = 5000f..50000f,
          steps = 8,
          colors = SliderDefaults.colors(
            thumbColor = BrandPrimary,
            activeTrackColor = BrandPrimary
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Calculations & Road-map
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "🎯 আপনার মাসিক সেলস লক্ষ্যমাত্রা:",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = BrandPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "মাসে মোট সফল অর্ডার প্রয়োজন:", fontSize = 12.sp)
              Text(text = "$requiredOrders টি অর্ডার", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "প্রতিদিন গড়ে অর্ডার লক্ষ্যমাত্র:", fontSize = 12.sp)
              Text(text = "মাত্র $dailyOrders টি অর্ডার", fontWeight = FontWeight.ExtraBold, color = SuccessGreen, fontSize = 13.sp)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "অর্ডার প্রতি গড় ইনকাম:", fontSize = 12.sp)
              Text(text = "৳${avgProfitPerOrder.toInt()}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "কীভাবে দ্রুত লক্ষ্য অর্জন করবেন?",
          fontWeight = FontWeight.Bold,
          fontSize = 12.5.sp
        )
        Text(
          text = "১. প্রতিদিন ফেসবুকে ৩টি বেস্টসেলার জামদানি ও থ্রি-পিস পোস্ট করুন।\n২. 'ট্রেনিং একাডেমি' থেকে ভিডিওগুলো দেখে সেলস ক্লোজিং শিখুন।\n৩. কাস্টমারদের ক্যাশ অন ডেলিভারি ও ফ্রি রিটার্ন সুবিধার কথা বলুন।",
          fontSize = 11.5.sp,
          lineHeight = 16.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("টার্গেট সেট করেছি, সেল শুরু করি!")
        }
      }
    }
  }
}

@Composable
fun SupportHubDialog(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 14.dp)
        .testTag("support_hub_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Headphones,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Standard Business সাপোর্ট হাব",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "২৪/৭ রিসেলার হেল্পলাইন ও কমিউনিটি",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // WhatsApp Direct Help
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = SuccessGreen.copy(alpha = 0.12f),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/8801700000000?text=Hello%20Standard%20Business%20Support"))
              context.startActivity(intent)
            }
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SuccessGreen),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = "হোয়াটসঅ্যাপ সাপোর্ট হেল্পলাইন", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
              Text(text = "অর্ডার, কুরিয়ার বা পেমেন্ট সংক্রান্ত যেকোনো সমস্যায় ইনস্ট্যান্ট চ্যাট করুন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Direct Helpline Call
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = BrandPrimary.copy(alpha = 0.1f),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01700000000"))
              context.startActivity(intent)
            }
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = "সরাসরি ফোন কল করুন", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandPrimary)
              Text(text = "সকাল ৯টা থেকে রাত ১০টা পর্যন্ত কাস্টমার ম্যানেজার সবসময় প্রস্তুত", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Official Community Facebook Group
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = BrandSecondary.copy(alpha = 0.12f),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable {
              val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/groups/standardbusiness"))
              context.startActivity(intent)
            }
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandSecondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Groups, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = "অফিসিয়াল রিসেলার ফেসবুক গ্রুপ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandSecondary)
              Text(text = "অন্যান্য সফল নারী উদ্যোক্তাদের সাথে আলোচনা ও টিপস শেয়ার করুন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
            }
          }
        }
      }
    }
  }
}
