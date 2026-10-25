package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AgentEntity
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
  agent: AgentEntity?,
  unreadCount: Int,
  onNotificationClick: () -> Unit,
  onSwitchRoleClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isAdmin = agent?.role == "ADMIN"

  TopAppBar(
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, BrandPrimary.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .background(Color.White),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_standard_business_logo),
            contentDescription = "Standard Business Logo",
            modifier = Modifier.size(36.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Standard Business",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BrandPrimary
              )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = BrandSecondary.copy(alpha = 0.9f)
            ) {
              Text(
                text = "Reseller Pro",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
          Text(
            text = if (isAdmin) "🛡️ মাস্টার অ্যাডমিন কন্ট্রোল" else (agent?.businessName ?: "উদ্যোক্তা প্ল্যাটফর্ম"),
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 11.sp,
              color = if (isAdmin) BrandSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = if (isAdmin) FontWeight.SemiBold else FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    },
    actions = {
      // Role & Profile badge chip
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isAdmin) BrandSecondary.copy(alpha = 0.15f) else BrandPrimary.copy(alpha = 0.1f),
        modifier = Modifier
          .padding(end = 4.dp)
          .clip(RoundedCornerShape(20.dp))
          .clickable { onSwitchRoleClick() }
          .testTag("role_switcher_chip")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
            contentDescription = "Role",
            tint = if (isAdmin) BrandSecondary else BrandPrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isAdmin) "অ্যাডমিন" else (agent?.name?.split(" ")?.firstOrNull() ?: "এজেন্ট"),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isAdmin) BrandSecondary else BrandPrimary
          )
        }
      }

      // Notification Bell with Badge
      IconButton(
        onClick = onNotificationClick,
        modifier = Modifier.testTag("notification_button")
      ) {
        BadgedBox(
          badge = {
            if (unreadCount > 0) {
              Badge(
                containerColor = BkashPink,
                contentColor = Color.White
              ) {
                Text(text = if (unreadCount > 9) "9+" else unreadCount.toString())
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  )
}

@Composable
fun OrderStatusBadge(status: String) {
  val (bgColor, textColor, icon) = when (status) {
    "ডেলিভারি সম্পন্ন" -> Triple(SuccessGreen.copy(alpha = 0.15f), SuccessGreen, Icons.Default.CheckCircle)
    "কুরিয়ারে হস্তান্তর" -> Triple(WarningOrange.copy(alpha = 0.15f), WarningOrange, Icons.Default.LocalShipping)
    "প্রসেসিং", "প্যাকিং সম্পন্ন" -> Triple(Color(0xFF0288D1).copy(alpha = 0.15f), Color(0xFF0288D1), Icons.Default.PendingActions)
    "অর্ডার প্রাপ্ত" -> Triple(BrandPrimary.copy(alpha = 0.12f), BrandPrimary, Icons.Default.ShoppingBag)
    else -> Triple(Color.Gray.copy(alpha = 0.15f), Color.DarkGray, Icons.Default.PendingActions)
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = status,
        tint = textColor,
        modifier = Modifier.size(13.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = status,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun OrderTimelineView(currentStatus: String, modifier: Modifier = Modifier) {
  val steps = listOf(
    "অর্ডার প্রাপ্ত",
    "প্রসেসিং",
    "কুরিয়ারে হস্তান্তর",
    "ডেলিভারি সম্পন্ন"
  )

  val currentIndex = when (currentStatus) {
    "অর্ডার প্রাপ্ত" -> 0
    "প্রসেসিং", "প্যাকিং সম্পন্ন" -> 1
    "কুরিয়ারে হস্তান্তর" -> 2
    "ডেলিভারি সম্পন্ন" -> 3
    else -> 0
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Text(
        text = "অর্ডার লাইভ ট্র্যাকিং অগ্রগতি",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        steps.forEachIndexed { index, title ->
          val isPassed = index <= currentIndex
          val isCurrent = index == currentIndex

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                  if (isPassed) BrandPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ),
              contentAlignment = Alignment.Center
            ) {
              if (isPassed) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
              } else {
                Text(
                  text = "${index + 1}",
                  fontSize = 11.sp,
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = title,
              fontSize = 9.5.sp,
              fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
              color = if (isCurrent) BrandPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    }
  }
}
