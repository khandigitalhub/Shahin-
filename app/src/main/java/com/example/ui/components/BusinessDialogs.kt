package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AgentEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.SuccessGreen

@Composable
fun MarketingShareDialog(
  product: ProductEntity,
  agent: AgentEntity?,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var customPrice by remember { mutableDoubleStateOf(product.retailPrice) }
  var customPriceText by remember { mutableStateOf(product.retailPrice.toInt().toString()) }

  val baseCommission = product.baseCommission
  val extraMargin = (customPrice - product.wholesalePrice).coerceAtLeast(0.0)
  val totalProfit = extraMargin + baseCommission

  val marketingCopy = """
🌸 নতুন আকর্ষণীয় অফার কালেকশন! 🌸
🛍️ ${product.title}
✨ ফেব্রিক/কোয়ালিটি: ${product.fabricOrMaterial}
✨ বিবরণ: ${product.description}

💰 আকর্ষণীয় অফার মূল্য: মাত্র ৳${customPrice.toInt()}
🚚 সারাদেশে ক্যাশ অন ডেলিভারি (পণ্য হাতে পেয়ে মূল্য পরিশোধের সুবিধা)
📦 ডেলিভারি চার্জ: ঢাকা সিটিতে ৳৬০, ঢাকার বাইরে ৳১২০

অর্ডার করতে ইনবক্স করুন অথবা হোয়াটসঅ্যাপে মেসেজ দিন:
📱 ${agent?.phone ?: "017XXXXXXXX"}
🏡 শপ: ${agent?.businessName ?: "অনলাইন বুটিক গ্যালারি"}
  """.trimIndent()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("marketing_dialog")
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
              imageVector = Icons.Default.Campaign,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "মার্কেটিং ও শেয়ার টুলস",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = product.title,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Selling Price Input & Live Profit Calculator
        OutlinedTextField(
          value = customPriceText,
          onValueChange = { input ->
            customPriceText = input
            input.toDoubleOrNull()?.let { customPrice = it }
          },
          label = { Text("আপনার কাস্টমার বিক্রয় মূল্য (টাকা)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("resell_price_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Profit card
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = SuccessGreen.copy(alpha = 0.12f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "পাইকারি মূল্য:", fontSize = 12.sp)
              Text(text = "৳${product.wholesalePrice.toInt()}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "নিশ্চিত বেস কমিশন:", fontSize = 12.sp)
              Text(text = "+ ৳${baseCommission.toInt()}", color = BrandPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "আপনার অতিরিক্ত মার্জিন:", fontSize = 12.sp)
              Text(text = "+ ৳${(customPrice - product.wholesalePrice).toInt()}", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Divider(modifier = Modifier.padding(vertical = 6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "এই সেলে আপনার মোট লাভ:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(
                text = "৳${totalProfit.toInt()} (${product.points} পয়েন্ট)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = SuccessGreen
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "সোশ্যাল মিডিয়া ক্যাপশন প্রিভিউ:",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
        ) {
          Text(
            text = marketingCopy,
            fontSize = 11.5.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Actions: Copy & Share
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Marketing Post", marketingCopy)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "ক্যাপশন কপি করা হয়েছে! ফেসবুকে পেস্ট করুন।", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .weight(1f)
              .testTag("copy_caption_button")
          ) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("কপি ক্যাপশন", fontSize = 12.sp)
          }

          Button(
            onClick = {
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, marketingCopy)
                type = "text/plain"
              }
              val shareIntent = Intent.createChooser(sendIntent, "শেয়ার করুন (WhatsApp/Facebook)")
              context.startActivity(shareIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            modifier = Modifier
              .weight(1f)
              .testTag("share_post_button")
          ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("শেয়ার করুন", fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
fun PlaceOrderDialog(
  product: ProductEntity,
  agent: AgentEntity?,
  onDismiss: () -> Unit,
  onSubmit: (quantity: Int, resellPrice: Double, name: String, phone: String, address: String, district: String, deliveryArea: String) -> Unit
) {
  var customerName by remember { mutableStateOf("") }
  var customerPhone by remember { mutableStateOf("") }
  var customerAddress by remember { mutableStateOf("") }
  var district by remember { mutableStateOf(agent?.district ?: "ঢাকা") }
  var deliveryArea by remember { mutableStateOf("ঢাকা সিটির ভেতরে") }
  var quantity by remember { mutableIntStateOf(1) }
  var resellPriceText by remember { mutableStateOf(product.retailPrice.toInt().toString()) }

  val resellPrice = resellPriceText.toDoubleOrNull() ?: product.retailPrice
  val deliveryCharge = if (deliveryArea == "ঢাকা সিটির ভেতরে") product.deliveryChargeDhaka else product.deliveryChargeOutside
  val profitPerUnit = (resellPrice - product.wholesalePrice) + product.baseCommission
  val totalProfit = profitPerUnit * quantity
  val codTotal = (resellPrice * quantity) + deliveryCharge

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("place_order_dialog")
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
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "কাস্টমার অর্ডার প্লেস করুন",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = product.title,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = customerName,
          onValueChange = { customerName = it },
          label = { Text("কাস্টমারের পুরো নাম") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("customer_name_input"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = customerPhone,
          onValueChange = { customerPhone = it },
          label = { Text("মোবাইল নম্বর (যেমন 017XXXXXXXX)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("customer_phone_input"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = customerAddress,
          onValueChange = { customerAddress = it },
          label = { Text("সম্পূর্ণ ডেলিভারি ঠিকানা (বাড়ি/রোড/এলাকা)") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("customer_address_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = district,
            onValueChange = { district = it },
            label = { Text("জেলা/শহর") },
            modifier = Modifier.weight(1f)
          )

          // Quantity selector
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "পরিমাণ: ", fontSize = 13.sp)
            IconButton(
              onClick = { if (quantity > 1) quantity-- },
              modifier = Modifier.size(36.dp)
            ) {
              Text(text = "−", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = "$quantity", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            IconButton(
              onClick = { quantity++ },
              modifier = Modifier.size(36.dp)
            ) {
              Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Delivery Area Selection
        Text(text = "ডেলিভারি লোকেশন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically) {
          RadioButton(
            selected = deliveryArea == "ঢাকা সিটির ভেতরে",
            onClick = { deliveryArea = "ঢাকা সিটির ভেতরে" }
          )
          Text(text = "ঢাকা (৳৬০)", fontSize = 12.sp)
          Spacer(modifier = Modifier.width(16.dp))
          RadioButton(
            selected = deliveryArea == "ঢাকা সিটির বাইরে",
            onClick = { deliveryArea = "ঢাকা সিটির বাইরে" }
          )
          Text(text = "ঢাকার বাইরে (৳১২০)", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = resellPriceText,
          onValueChange = { resellPriceText = it },
          label = { Text("কাস্টমার প্রতি পিস বিক্রয় মূল্য (টাকা)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("order_resell_price_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Order Summary Card
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "কাস্টমার থেকে ক্যাশ কালেকশন (COD):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              Text(text = "৳${codTotal.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "ডেলিভারি সম্পন্ন হলে আপনার ইনকাম:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
              Text(
                text = "৳${totalProfit.toInt()}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = SuccessGreen
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (customerName.isNotBlank() && customerPhone.isNotBlank() && customerAddress.isNotBlank()) {
              onSubmit(quantity, resellPrice, customerName, customerPhone, customerAddress, district, deliveryArea)
            }
          },
          enabled = customerName.isNotBlank() && customerPhone.isNotBlank() && customerAddress.isNotBlank(),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("confirm_order_button")
        ) {
          Icon(imageVector = Icons.Default.Check, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("অর্ডার কনফার্ম করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun WithdrawDialog(
  availableBalance: Double,
  agent: AgentEntity?,
  onDismiss: () -> Unit,
  onSubmit: (amount: Double, method: String, account: String) -> Unit
) {
  var method by remember { mutableStateOf("bKash") }
  var account by remember { mutableStateOf(agent?.bkashNumber.takeIf { !it.isNullOrBlank() } ?: "") }
  var amountText by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("withdraw_dialog")
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
              imageVector = Icons.Default.AccountBalanceWallet,
              contentDescription = null,
              tint = BrandPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "কমিশন টাকা উইথড্রয়াল",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Balance badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = SuccessGreen.copy(alpha = 0.12f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "উইথড্র যোগ্য ব্যালেন্স:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(
              text = "৳${availableBalance.toInt()}",
              fontSize = 17.sp,
              fontWeight = FontWeight.ExtraBold,
              color = SuccessGreen
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "পেমেন্ট মাধ্যম বেছে নিন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("bKash" to BkashPink, "Nagad" to NagadOrange, "Rocket" to Color(0xFF8C3297)).forEach { (m, color) ->
            val isSelected = method == m
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) color else Color.Transparent,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                  method = m
                  if (m == "bKash") account = agent?.bkashNumber ?: ""
                  if (m == "Nagad") account = agent?.nagadNumber ?: ""
                }
            ) {
              Text(
                text = m,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 10.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = account,
          onValueChange = { account = it },
          label = { Text("$method একাউন্ট নম্বর") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("withdraw_account_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("উইথড্রয়াল পরিমাণ (টাকা)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("withdraw_amount_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quick amount pills
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(500, 1000, 2000).forEach { amt ->
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .clickable { amountText = amt.toString() }
            ) {
              Text(text = "৳$amt", fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
          }
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = BrandPrimary.copy(alpha = 0.1f),
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .clickable { amountText = availableBalance.toInt().toString() }
          ) {
            Text(text = "সব টাকা", fontSize = 11.sp, color = BrandPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val amount = amountText.toDoubleOrNull() ?: 0.0
        val isValid = amount >= 100.0 && amount <= availableBalance && account.isNotBlank()

        Button(
          onClick = {
            if (isValid) {
              onSubmit(amount, method, account)
            }
          },
          enabled = isValid,
          colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("confirm_withdraw_button")
        ) {
          Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("উইথড্র রিকোয়েস্ট পাঠান", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun AddProductDialog(
  onDismiss: () -> Unit,
  onSubmit: (title: String, category: String, wholesale: Double, retail: Double, commission: Double, points: Int, stock: Int, desc: String, fabric: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("শাড়ি") }
  var wholesaleText by remember { mutableStateOf("") }
  var retailText by remember { mutableStateOf("") }
  var commissionText by remember { mutableStateOf("") }
  var pointsText by remember { mutableStateOf("50") }
  var stockText by remember { mutableStateOf("30") }
  var desc by remember { mutableStateOf("") }
  var fabric by remember { mutableStateOf("") }

  val categories = listOf("ফ্যাশন ও পোশাক", "ফুড ও গ্রোসারি", "ইলেকট্রনিক্স ও গ্যাজেট", "কসমেটিকস ও বিউটি", "জুয়েলারি", "হ্যান্ডব্যাগ", "হোম ও লাইফস্টাইল")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("add_product_dialog")
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
          Text(
            text = "নতুন প্রোডাক্ট যুক্ত করুন",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("প্রোডাক্টের নাম (টাইটেল)") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "ক্যাটাগরি:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        androidx.compose.foundation.lazy.LazyRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(categories.size) { index ->
            val cat = categories[index]
            val isSelected = category == cat
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) BrandPrimary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { category = cat }
            ) {
              Text(
                text = cat,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = wholesaleText,
            onValueChange = { wholesaleText = it },
            label = { Text("পাইকারি (৳)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = retailText,
            onValueChange = { retailText = it },
            label = { Text("খুচরা (৳)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = commissionText,
            onValueChange = { commissionText = it },
            label = { Text("কমিশন (৳)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = stockText,
            onValueChange = { stockText = it },
            label = { Text("স্টক সংখ্যা") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = fabric,
          onValueChange = { fabric = it },
          label = { Text("ফেব্রিক বা মেটেরিয়াল স্পেক্স") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("বিস্তারিত বিবরণ") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        val wholesale = wholesaleText.toDoubleOrNull() ?: 0.0
        val retail = retailText.toDoubleOrNull() ?: 0.0
        val commission = commissionText.toDoubleOrNull() ?: 0.0
        val stock = stockText.toIntOrNull() ?: 10
        val points = pointsText.toIntOrNull() ?: 40

        Button(
          onClick = {
            if (title.isNotBlank() && wholesale > 0 && retail > 0) {
              onSubmit(title, category, wholesale, retail, commission, points, stock, desc, fabric)
            }
          },
          enabled = title.isNotBlank() && wholesale > 0 && retail > 0,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Text("প্রোডাক্ট সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun UpdateOrderStatusDialog(
  order: OrderEntity,
  onDismiss: () -> Unit,
  onSubmit: (newStatus: String, trackingNumber: String, courierName: String) -> Unit
) {
  var status by remember { mutableStateOf(order.status) }
  var tracking by remember { mutableStateOf(order.trackingNumber) }
  var courier by remember { mutableStateOf(order.courierName) }

  val statuses = listOf(
    "অর্ডার প্রাপ্ত",
    "প্রসেসিং",
    "কুরিয়ারে হস্তান্তর",
    "ডেলিভারি সম্পন্ন",
    "বাতিল"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "অর্ডার স্ট্যাটাস আপডেট (#${order.orderNumber})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "এজেন্ট: ${order.agentName} | কমিশন: ৳${order.agentProfit.toInt()}",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "নতুন স্ট্যাটাস নির্ধারণ করুন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        statuses.forEach { s ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { status = s }
          ) {
            RadioButton(selected = status == s, onClick = { status = s })
            Text(text = s, fontSize = 13.sp)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = courier,
          onValueChange = { courier = it },
          label = { Text("কুরিয়ার কোম্পানির নাম") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = tracking,
          onValueChange = { tracking = it },
          label = { Text("কুরিয়ার ট্র্যাকিং কোড") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = { onSubmit(status, tracking, courier) },
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Text("স্ট্যাটাস আপডেট করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun BroadcastNotificationDialog(
  onDismiss: () -> Unit,
  onSubmit: (title: String, body: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var body by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "সকল এজেন্টকে পুশ নোটিফিকেশন পাঠান",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("নোটিফিকেশন শিরোনাম") },
          placeholder = { Text("যেমন: বৈশাখী মেগা বোনাস চালু!") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = body,
          onValueChange = { body = it },
          label = { Text("নোটিফিকেশন মেসেজ") },
          placeholder = { Text("যেমন: আজ রাত ১২টা পর্যন্ত সকল জামদানিতে ২০০৳ অতিরিক্ত ক্যাশব্যাক...") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (title.isNotBlank() && body.isNotBlank()) {
              onSubmit(title, body)
            }
          },
          enabled = title.isNotBlank() && body.isNotBlank(),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Icon(imageVector = Icons.Default.Campaign, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("ব্রডকাস্ট নোটিফিকেশন সেন্ড করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun RoleSwitcherDialog(
  currentAgentId: Long,
  agents: List<AgentEntity>,
  onDismiss: () -> Unit,
  onSelectAgent: (Long) -> Unit,
  onOpenSignUp: () -> Unit,
  onOpenAdminLogin: () -> Unit
) {
  val nonAdminAgents = agents.filter { it.role != "ADMIN" }
  val adminAgent = agents.find { it.role == "ADMIN" }
  val isCurrentAdmin = agents.find { it.id == currentAgentId }?.role == "ADMIN"

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("role_switcher_dialog")
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
          Text(
            text = "রিসেলার অ্যাকাউন্ট সুইচ",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "আপনার অনুমোদিত মেম্বার অ্যাকাউন্ট নির্বাচন করুন:",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        nonAdminAgents.forEach { ag ->
          val isSelected = ag.id == currentAgentId

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) BrandPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                onSelectAgent(ag.id)
                onDismiss()
              }
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = isSelected,
                onClick = {
                  onSelectAgent(ag.id)
                  onDismiss()
                }
              )
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = ag.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
                Text(
                  text = "${ag.businessName} • ${ag.district} • ব্যালেন্স: ৳${ag.availableBalance.toInt()}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
          onClick = {
            onDismiss()
            onOpenSignUp()
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("নতুন রিসেলার মেম্বার সাইন আপ", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Secure Master Admin Authentication Entry
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isCurrentAdmin) BrandSecondary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
          border = androidx.compose.foundation.BorderStroke(1.dp, BrandSecondary.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
              onDismiss()
              onOpenAdminLogin()
            }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = androidx.compose.material.icons.Icons.Default.AdminPanelSettings,
              contentDescription = null,
              tint = BrandSecondary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = if (isCurrentAdmin) "🛡️ মাস্টার অ্যাডমিন (লগড ইন)" else "🔐 অনুমোদিত অ্যাডমিন লগইন",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BrandSecondary
              )
              Text(
                text = if (isCurrentAdmin) "সেশন সক্রিয় রয়েছে (ID: 99)" else "মালিক/পরিচালকদের জন্য নিরাপদ পিন অথেনটিকেশন",
                fontSize = 10.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun SignUpDialog(
  onDismiss: () -> Unit,
  onSubmit: (name: String, phone: String, district: String, businessName: String, bkash: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var district by remember { mutableStateOf("ঢাকা") }
  var businessName by remember { mutableStateOf("") }
  var bkash by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("signup_dialog")
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
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .border(1.dp, BrandPrimary.copy(alpha = 0.2f), CircleShape)
                .background(Color.White),
              contentAlignment = Alignment.Center
            ) {
              androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_standard_business_logo),
                contentDescription = "Standard Business Logo",
                modifier = Modifier.size(34.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Standard Business",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = BrandPrimary
              )
              Text(
                text = "Reseller Pro মেম্বার সাইন আপ",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "মেম্বার হিসেবে জয়েন করে ঘরে বসেই CPS ও CPA প্রোডাক্ট মার্কেটিং শুরু করুন! ৳৫০ জয়েনিং বোনাস ইনস্ট্যান্ট পাবেন।",
          fontSize = 11.5.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("আপনার পূর্ণ নাম") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("মোবাইল নম্বর") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = businessName,
          onValueChange = { businessName = it },
          label = { Text("আপনার পেজ বা বিজনেসের নাম") },
          placeholder = { Text("যেমন: মায়াবী বুটিকস") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = district,
          onValueChange = { district = it },
          label = { Text("জেলা/শহর") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = bkash,
          onValueChange = { bkash = it },
          label = { Text("বিকাশ বা নগদ নম্বর (কমিশন প্রাপ্তির জন্য)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (name.isNotBlank() && phone.isNotBlank()) {
              onSubmit(name, phone, district, if (businessName.isBlank()) "$name's Collection" else businessName, if (bkash.isBlank()) phone else bkash)
            }
          },
          enabled = name.isNotBlank() && phone.isNotBlank(),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Text("সাইন আপ ও জয়েন করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
