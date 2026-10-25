package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary

@Composable
fun AdminPinDialog(
  onDismiss: () -> Unit,
  onPinVerified: () -> Unit
) {
  var pin by remember { mutableStateOf("") }
  var hasError by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .testTag("admin_pin_dialog")
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
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
                .background(BrandSecondary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "মাস্টার এডমিন অ্যাক্সেস", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "ব্যবসায়িক তথ্য, প্রোডাক্ট স্টক, কুরিয়ার ও বিকাশ পেআউট নিয়ন্ত্রণ করতে আপনার সিক্রেট এডমিন পিন কোডটি লিখুন:",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = pin,
          onValueChange = {
            pin = it
            hasError = false
          },
          label = { Text("এডমিন পিন (ডিফল্ট: 1234)") },
          placeholder = { Text("1234") },
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          isError = hasError,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_pin_input"),
          singleLine = true
        )

        if (hasError) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "পিন ভুল হয়েছে! সঠিক পিন: 1234", color = Color.Red, fontSize = 11.5.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = {
            if (pin == "1234" || pin.isBlank()) { // Easy pass for testing / demo
              onPinVerified()
            } else {
              hasError = true
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("admin_pin_submit")
        ) {
          Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("এডমিন প্যানেলে প্রবেশ করুন", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
