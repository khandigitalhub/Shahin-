package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.AgentEntity
import com.example.data.model.ProductEntity
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen

@Composable
fun CpaLeadMarketingKitDialog(
  product: ProductEntity,
  agent: AgentEntity?,
  onDismiss: () -> Unit,
  onSubmitCustomerLead: (qty: Int, resellPrice: Double, name: String, phone: String, address: String, district: String, area: String) -> Unit
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("মিডিয়া ও লিংক", "লিড সাবমিশন", "চ্যাট স্ক্রিপ্ট")

  val agentPhone = agent?.phone ?: "017XXXXXXXX"
  val agentCode = agent?.name?.replace(" ", "")?.take(6)?.uppercase() ?: "AGENT"
  val referralLink = "https://standardbusiness.com/item/${product.sku}?ref=$agentCode"

  var customSellPriceText by remember { mutableStateOf(product.retailPrice.toInt().toString()) }
  val customSellPrice = customSellPriceText.toDoubleOrNull() ?: product.retailPrice
  val profit = (customSellPrice - product.wholesalePrice) + product.baseCommission

  // Social media copy formatted for Facebook / WhatsApp
  val facebookPostCopy = """
🔥 আকর্ষণীয় কালেকশন: ${product.title} 🔥
✨ কোয়ালিটি: ${product.fabricOrMaterial}
✨ বিবরণ: ${product.description}

💰 আকর্ষণীয় অফার প্রাইস: মাত্র ৳${customSellPrice.toInt()}
🚚 ক্যাশ অন ডেলিভারি (পণ্য হাতে পেয়ে মূল্য পরিশোধ)
📦 ডেলিভারি চার্জ: ঢাকা সিটিতে ৳৬০, ঢাকার বাইরে ৳১২০

👉 সরাসরি অর্ডার করতে ইনবক্স করুন অথবা লিংকে যান:
$referralLink
📱 হোয়াটসঅ্যাপে অর্ডার বুকিং: $agentPhone
— ${agent?.businessName ?: "Standard Business Authorized Reseller"}
  """.trimIndent()

  val whatsappMessageCopy = """
আসসালামু আলাইকুম আপু/ভাইয়া! 😊
আমাদের Standard Business-এর নতুন কালেকশন এসেছে:
🛍️ *${product.title}*
✨ ফেব্রিক: ${product.fabricOrMaterial}
💰 বিশেষ অফার রেট: মাত্র *৳${customSellPrice.toInt()}*

🚚 সারা বাংলাদেশে হোম ডেলিভারি ও পার্সেল খুলে দেখে পেমেন্ট করার সুবিধা আছে।
আপনার নাম, মোবাইল ও ডেলিভারি ঠিকানা দিলে আজই বুকিং করে দেব!
  """.trimIndent()

  // Lead submission form states
  var leadCustName by remember { mutableStateOf("") }
  var leadCustPhone by remember { mutableStateOf("") }
  var leadCustAddress by remember { mutableStateOf("") }
  var leadDistrict by remember { mutableStateOf(agent?.district ?: "ঢাকা") }
  var leadDeliveryArea by remember { mutableStateOf("ঢাকা সিটির ভেতরে") }
  var leadQuantity by remember { mutableIntStateOf(1) }
  var leadSource by remember { mutableStateOf("ফেসবুক পেজ / ইনবক্স") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
        .testTag("cpa_marketing_kit_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(BrandPrimary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(text = "CPA মার্কেটিং ও লিড জেনারেশন হাব", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text(text = "প্রোডাক্ট লিংক, মিডিয়া ও কাস্টমার লিড এন্ট্রি", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Product Summary Banner
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = R.drawable.reseller_hero_banner_1792806449181),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 12.5.sp, maxLines = 1)
              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = "হোলসেল: ৳${product.wholesalePrice.toInt()}", fontSize = 11.sp, color = Color.Gray)
                Text(text = "কমিশন: +৳${product.baseCommission.toInt()}", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Navigation
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.Transparent,
          contentColor = BrandPrimary
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTab == index,
              onClick = { selectedTab = index },
              text = { Text(title, fontSize = 11.5.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
          // Tab 0: Affiliate Link & Marketing Copy
          0 -> {
            Column {
              // 1. Unique Affiliate CPA Referral Link
              Text(text = "🔗 আপনার ইউনিক এফিলিয়েট / লিড লিংক:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandPrimary)
              Spacer(modifier = Modifier.height(4.dp))
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = BrandPrimary.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = referralLink,
                    fontSize = 11.sp,
                    color = BrandPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                  )
                  IconButton(
                    onClick = {
                      val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                      val clip = ClipData.newPlainText("Affiliate Link", referralLink)
                      clipboard.setPrimaryClip(clip)
                      Toast.makeText(context, "রেফারেল লিংক কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Link", tint = BrandPrimary, modifier = Modifier.size(16.dp))
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Pricing Margin
              OutlinedTextField(
                value = customSellPriceText,
                onValueChange = { customSellPriceText = it },
                label = { Text("কাস্টমার প্রস্তাবিত বিক্রয় মূল্য (টাকা)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = SuccessGreen.copy(alpha = 0.12f),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = "এই সেলে আপনার কমিশন প্রফিট:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                  Text(text = "৳${profit.toInt()} (+${product.points}★)", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Facebook Marketing Copy
              Text(text = "📢 রেডি ফেসবুক ক্যাপশন (এক ক্লিকে কপি করুন):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 6.dp)
              ) {
                Text(text = facebookPostCopy, fontSize = 11.sp, lineHeight = 15.sp, modifier = Modifier.padding(10.dp))
              }

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                  onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Facebook Copy", facebookPostCopy)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "ফেসবুক পোস্ট টেক্সট কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                  },
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("কপি পোস্ট", fontSize = 11.sp)
                }
                Button(
                  onClick = {
                    val sendIntent = Intent().apply {
                      action = Intent.ACTION_SEND
                      putExtra(Intent.EXTRA_TEXT, facebookPostCopy)
                      type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "শেয়ার করুন (Facebook/WhatsApp)"))
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("শেয়ার করুন", fontSize = 11.sp)
                }
              }
            }
          }

          // Tab 1: Direct Customer Lead Submission (Reseller submits lead, Admin delivers!)
          1 -> {
            Column {
              Text(
                text = "💡 আপনি শুধু কাস্টমার লিড এন্ট্রি করুন, ডেলিভারি ও ক্যাশ কালেকশন করবে Standard Business টিম!",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = BrandPrimary,
                lineHeight = 16.sp
              )
              Spacer(modifier = Modifier.height(10.dp))

              OutlinedTextField(
                value = leadCustName,
                onValueChange = { leadCustName = it },
                label = { Text("কাস্টমারের পুরো নাম") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )
              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = leadCustPhone,
                onValueChange = { leadCustPhone = it },
                label = { Text("কাস্টমার মোবাইল নম্বর") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )
              Spacer(modifier = Modifier.height(8.dp))

              OutlinedTextField(
                value = leadCustAddress,
                onValueChange = { leadCustAddress = it },
                label = { Text("ডেলিভারি ঠিকানা (বাসা/রোড/এলাকা)") },
                modifier = Modifier.fillMaxWidth()
              )
              Spacer(modifier = Modifier.height(8.dp))

              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                  value = leadDistrict,
                  onValueChange = { leadDistrict = it },
                  label = { Text("জেলা/শহর") },
                  modifier = Modifier.weight(1f),
                  singleLine = true
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                  Text(text = "পরিমাণ: ", fontSize = 12.sp)
                  IconButton(onClick = { if (leadQuantity > 1) leadQuantity-- }, modifier = Modifier.size(32.dp)) {
                    Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                  }
                  Text(text = "$leadQuantity", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                  IconButton(onClick = { leadQuantity++ }, modifier = Modifier.size(32.dp)) {
                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(text = "ডেলিভারি এরিয়া:", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
              Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = leadDeliveryArea == "ঢাকা সিটির ভেতরে", onClick = { leadDeliveryArea = "ঢাকা সিটির ভেতরে" })
                Text(text = "ঢাকা (৳৬০)", fontSize = 11.5.sp)
                Spacer(modifier = Modifier.width(12.dp))
                RadioButton(selected = leadDeliveryArea == "ঢাকা সিটির বাইরে", onClick = { leadDeliveryArea = "ঢাকা সিটির বাইরে" })
                Text(text = "ঢাকার বাইরে (৳১২০)", fontSize = 11.5.sp)
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Total COD calculation
              val delFee = if (leadDeliveryArea == "ঢাকা সিটির ভেতরে") 60.0 else 120.0
              val totalCod = (customSellPrice * leadQuantity) + delFee
              val totalProfit = profit * leadQuantity

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = SuccessGreen.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "কাস্টমার থেকে ক্যাশ কালেকশন:", fontSize = 11.5.sp)
                    Text(text = "৳${totalCod.toInt()}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "ডেলিভারি হলে আপনার কমিশন প্রফিট:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    Text(text = "৳${totalProfit.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = SuccessGreen)
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Button(
                onClick = {
                  if (leadCustName.isNotBlank() && leadCustPhone.isNotBlank() && leadCustAddress.isNotBlank()) {
                    onSubmitCustomerLead(
                      leadQuantity,
                      customSellPrice,
                      leadCustName,
                      leadCustPhone,
                      leadCustAddress,
                      leadDistrict,
                      leadDeliveryArea
                    )
                    Toast.makeText(context, "লিড সফলভাবে সাবমিট হয়েছে! অ্যাডমিন কুরিয়ার প্রসেস করবে।", Toast.LENGTH_LONG).show()
                    onDismiss()
                  }
                },
                enabled = leadCustName.isNotBlank() && leadCustPhone.isNotBlank() && leadCustAddress.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(48.dp)
                  .testTag("submit_lead_button")
              ) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("কাস্টমার লিড সাবমিট করুন (অ্যাডমিন ডেলিভারি করবে)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }

          // Tab 2: Messenger / WhatsApp Customer Chat Scripts
          2 -> {
            Column {
              Text(
                text = "💬 মেসেঞ্জারে কাস্টমারকে সন্তুষ্ট করে দ্রুত সেলস ক্লোজিং করার স্ক্রিপ্ট:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary
              )
              Spacer(modifier = Modifier.height(8.dp))

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(text = whatsappMessageCopy, fontSize = 11.5.sp, lineHeight = 16.sp, modifier = Modifier.padding(10.dp))
              }

              Spacer(modifier = Modifier.height(10.dp))

              Button(
                onClick = {
                  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                  val clip = ClipData.newPlainText("Chat Script", whatsappMessageCopy)
                  clipboard.setPrimaryClip(clip)
                  Toast.makeText(context, "চ্যাট মেসেজ কপি হয়েছে! ইনবক্সে পেস্ট করুন।", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                modifier = Modifier.fillMaxWidth()
              ) {
                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("স্ক্রিপ্ট কপি করুন")
              }
            }
          }
        }
      }
    }
  }
}
