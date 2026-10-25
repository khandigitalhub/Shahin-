package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AgentEntity
import com.example.data.model.CpaConversionEntity
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange

@Composable
fun AdminBalanceAdjustmentDialog(
  agent: AgentEntity,
  onDismiss: () -> Unit,
  onConfirmAdjustment: (agentId: Long, amount: Double, actionType: String, reason: String) -> Unit
) {
  var actionType by remember { mutableStateOf("BALANCE_ADD") }
  var amountText by remember { mutableStateOf("") }
  var reasonText by remember { mutableStateOf("") }
  var showConfirmAlert by remember { mutableStateOf(false) }

  val amount = amountText.toDoubleOrNull() ?: 0.0
  val isValid = amount > 0.0 && reasonText.isNotBlank()

  if (showConfirmAlert) {
    val actionLabel = when (actionType) {
      "BALANCE_ADD" -> "যোগ (+)"
      "BALANCE_SUBTRACT" -> "বিয়োগ (-)"
      "BONUS_ADD" -> "স্পেশাল বোনাস যোগ"
      else -> "কমিশন সমন্বয়"
    }

    AlertDialog(
      onDismissRequest = { showConfirmAlert = false },
      title = { Text("ব্যালেন্স পরিবর্তনের নিশ্চিতকরণ", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text("আপনি কি নিশ্চিত যে নিচের আর্থিক সমন্বয়টি সম্পন্ন করতে চান?")
          Spacer(modifier = Modifier.height(8.dp))
          Text("• ইউজার: ${agent.name} (ID: ${agent.id})", fontWeight = FontWeight.SemiBold)
          Text("• অ্যাকশন: $actionLabel", color = BrandPrimary, fontWeight = FontWeight.Bold)
          Text("• পরিমাণ: ৳${amount.toInt()}", fontWeight = FontWeight.Bold)
          Text("• কারণ: $reasonText")
          Spacer(modifier = Modifier.height(4.dp))
          Text("⚠️ এই লেনদেনটি সার্ভার অডিট লগে স্থায়ীভাবে রেকর্ড করা হবে।", fontSize = 11.sp, color = WarningOrange)
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showConfirmAlert = false
            onConfirmAdjustment(agent.id, amount, actionType, reasonText)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
          Text("হ্যাঁ, সম্পন্ন করুন")
        }
      },
      dismissButton = {
        TextButton(onClick = { showConfirmAlert = false }) {
          Text("বাতিল")
        }
      }
    )
  }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("admin_balance_adjustment_dialog")
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
            Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null, tint = BrandPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ব্যালেন্স ও বোনাস সমন্বয়", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        // Agent Summary Pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "টার্গেট এজেন্ট: ${agent.name} (${agent.phone})", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
            Text(text = "বর্তমান ব্যালেন্স: ৳${agent.availableBalance.toInt()} | মোট আয়: ৳${agent.totalEarnings.toInt()}", fontSize = 11.5.sp, color = SuccessGreen)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(text = "সমন্বয়ের ধরন নির্বাচন করুন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

        val options = listOf(
          Triple("BALANCE_ADD", "ব্যালেন্স যোগ (+)", SuccessGreen),
          Triple("BALANCE_SUBTRACT", "ব্যালেন্স বিয়োগ (−)", WarningOrange),
          Triple("BONUS_ADD", "স্পেশাল বোনাস / গিফট (+)", BrandSecondary)
        )

        options.forEach { (type, label, color) ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { actionType = type }
              .padding(vertical = 2.dp)
          ) {
            RadioButton(selected = actionType == type, onClick = { actionType = type })
            Text(text = label, fontWeight = if (actionType == type) FontWeight.Bold else FontWeight.Normal, color = if (actionType == type) color else MaterialTheme.colorScheme.onSurface, fontSize = 12.5.sp)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it },
          label = { Text("টাকার পরিমাণ (৳)") },
          placeholder = { Text("যেমন: 500") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = reasonText,
          onValueChange = { reasonText = it },
          label = { Text("লেনদেনের কারণ ও অডিট বিবরণ (বাধ্যতামূলক)") },
          placeholder = { Text("যেমন: বৈশাখী মেগা ক্যাম্পেইন টপ সেলার বোনাস") },
          minLines = 2,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = { showConfirmAlert = true },
          enabled = isValid,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Icon(imageVector = Icons.Default.Check, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("যাচাই ও কনফার্ম করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun AdminCpaReviewDialog(
  conversion: CpaConversionEntity,
  onDismiss: () -> Unit,
  onUpdateStatus: (newStatus: String, notes: String) -> Unit
) {
  var selectedStatus by remember { mutableStateOf(conversion.status) }
  var adminNotes by remember { mutableStateOf(conversion.adminNotes) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("admin_cpa_review_dialog")
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
          Text("CPA লিড অডিট ও ভেরিফিকেশন", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(text = conversion.offerTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "কাস্টমার: ${conversion.customerName}", fontSize = 12.sp)
            Text(text = "ফোন: ${conversion.customerPhone}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            if (conversion.customerEmail.isNotBlank()) {
              Text(text = "ইমেইল: ${conversion.customerEmail}", fontSize = 11.sp, color = Color.Gray)
            }
            Text(text = "টার্গেট মার্কেট: ${conversion.targetCountry} | পেআউট: ৳${conversion.payoutAmount.toInt()}", fontSize = 11.5.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
            Text(text = "রেফারার এজেন্ট: ${conversion.agentName} (ID: ${conversion.agentId})", fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(text = "স্ট্যাটাস নির্ধারণ করুন:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

        val statuses = listOf(
          Pair("PENDING", "পেন্ডিং (Pending Verification)"),
          Pair("APPROVED", "অনুমোদিত (Approved - ব্যালেন্সে ক্রেডিট হবে)"),
          Pair("REJECTED", "প্রত্যাখ্যাত (Rejected - ইনভ্যালিড/ভুয়ো লিড)"),
          Pair("REVERSED", "রিভার্সড (Reversed - কমিশন ফেরত)")
        )

        statuses.forEach { (st, label) ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedStatus = st }
              .padding(vertical = 2.dp)
          ) {
            RadioButton(selected = selectedStatus == st, onClick = { selectedStatus = st })
            Text(text = label, fontSize = 12.sp, fontWeight = if (selectedStatus == st) FontWeight.Bold else FontWeight.Normal)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = adminNotes,
          onValueChange = { adminNotes = it },
          label = { Text("অ্যাডমিন অডিট নোট ও রেফারেন্স") },
          placeholder = { Text("যেমন: ওটিপি ভেরিফায়েড এবং প্রথম ডিপোজিট সম্পন্ন") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            onUpdateStatus(selectedStatus, adminNotes)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Text("স্ট্যাটাস আপডেট ও কমিশন সেটেল করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
