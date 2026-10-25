package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgentEntity
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen

@Composable
fun LeaderboardScreen(
  currentAgent: AgentEntity?,
  agents: List<AgentEntity>,
  modifier: Modifier = Modifier
) {
  // Filter out admin from rankings
  val resellerAgents = agents.filter { it.role != "ADMIN" }
  val topThree = resellerAgents.take(3)
  val restAgents = resellerAgents.drop(3)

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Top Title & Motivation
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(26.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "শীর্ষ নারী উদ্যোক্তা লিডারবোর্ড",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Text(
          text = "চলতি মাসের সেরা পারফর্মিং রিসেলার আপারা",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Top 3 Podium
    if (topThree.isNotEmpty()) {
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
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "🏆 মাসিক চ্যাম্পিয়নস পোডিয়াম",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = BrandPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly,
              verticalAlignment = Alignment.Bottom
            ) {
              // #2 Silver
              if (topThree.size > 1) {
                PodiumColumn(
                  rank = 2,
                  agent = topThree[1],
                  medalColor = Color(0xFFC0C0C0),
                  height = 90.dp
                )
              }
              // #1 Gold
              if (topThree.isNotEmpty()) {
                PodiumColumn(
                  rank = 1,
                  agent = topThree[0],
                  medalColor = Color(0xFFFFD700),
                  height = 115.dp
                )
              }
              // #3 Bronze
              if (topThree.size > 2) {
                PodiumColumn(
                  rank = 3,
                  agent = topThree[2],
                  medalColor = Color(0xFFCD7F32),
                  height = 75.dp
                )
              }
            }
          }
        }
      }
    }

    // Monthly Bonus Incentive Card
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = BrandSecondary.copy(alpha = 0.12f),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CardGiftcard,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(26.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "মাসিক সেরা ৩ জনের জন্য বিশেষ পুরস্কার!",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = BrandSecondary
            )
            Text(
              text = "১ম স্থান: ৳৫,০০০ নগদ বোনাস | ২য় স্থান: ৳৩,০০০ | ৩য় স্থান: ৳২,০০০",
              fontSize = 11.5.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // Current User Standing Card
    if (currentAgent != null && currentAgent.role != "ADMIN") {
      item {
        val userRank = resellerAgents.indexOfFirst { it.id == currentAgent.id }.let { if (it >= 0) it + 1 else "-" }
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = BrandPrimary.copy(alpha = 0.08f)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(BrandPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "#$userRank",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color.White
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "আপনার বর্তমান অবস্থান: ${currentAgent.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
              )
              Text(
                text = "মোট সম্পন্ন অর্ডার: ${currentAgent.totalOrders}টি | উপার্জন: ৳${currentAgent.totalEarnings.toInt()}",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = SuccessGreen
            ) {
              Text(
                text = currentAgent.badgeLevel,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }

    // Rankings List Header
    item {
      Text(
        text = "সকল রিসেলারদের তালিকা",
        fontWeight = FontWeight.Bold,
        fontSize = 13.5.sp,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
      )
    }

    itemsIndexed(resellerAgents) { index, agent ->
      val isCurrentUser = agent.id == currentAgent?.id
      LeaderboardItemRow(
        rank = index + 1,
        agent = agent,
        isCurrentUser = isCurrentUser
      )
    }
  }
}

@Composable
fun PodiumColumn(
  rank: Int,
  agent: AgentEntity,
  medalColor: Color,
  height: androidx.compose.ui.unit.Dp
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.width(96.dp)
  ) {
    Box(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape)
        .background(medalColor.copy(alpha = 0.2f))
        .border(2.dp, medalColor, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (rank == 1) Icons.Default.EmojiEvents else Icons.Default.WorkspacePremium,
        contentDescription = null,
        tint = medalColor,
        modifier = Modifier.size(24.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = agent.name.split(" ").firstOrNull() ?: agent.name,
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp,
      maxLines = 1
    )
    Text(
      text = "৳${agent.totalEarnings.toInt()}",
      fontSize = 11.sp,
      fontWeight = FontWeight.ExtraBold,
      color = BrandPrimary
    )
    Text(
      text = "${agent.totalOrders}টি অর্ডার",
      fontSize = 9.5.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(6.dp))

    // Step pillar
    Box(
      modifier = Modifier
        .width(80.dp)
        .height(height)
        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        .background(
          Brush.verticalGradient(
            colors = listOf(medalColor.copy(alpha = 0.45f), medalColor.copy(alpha = 0.15f))
          )
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "#$rank",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        color = medalColor
      )
    }
  }
}

@Composable
fun LeaderboardItemRow(
  rank: Int,
  agent: AgentEntity,
  isCurrentUser: Boolean
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = if (isCurrentUser) BrandPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "#$rank",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        color = if (rank <= 3) BrandSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.width(32.dp)
      )

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = agent.name,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
          if (isCurrentUser) {
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = BrandPrimary
            ) {
              Text(
                text = "আপনি",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
        }
        Text(
          text = "${agent.businessName} • ${agent.district}",
          fontSize = 10.5.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "৳${agent.totalEarnings.toInt()}",
          fontWeight = FontWeight.Bold,
          fontSize = 13.5.sp,
          color = SuccessGreen
        )
        Text(
          text = "${agent.totalOrders}টি অর্ডার (${agent.points}★)",
          fontSize = 10.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
