package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AgentEntity
import com.example.data.model.OfferEntity
import com.example.data.model.ProductEntity
import com.example.ui.MainTab
import com.example.ui.theme.BkashPink
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange

@Composable
fun HomeScreen(
  agent: AgentEntity?,
  trendingProducts: List<ProductEntity>,
  activeOffers: List<OfferEntity>,
  onNavigateTab: (MainTab) -> Unit,
  onOpenMarketing: (ProductEntity) -> Unit,
  onOpenOrder: (ProductEntity) -> Unit,
  onOpenWithdraw: () -> Unit,
  onOpenOffers: () -> Unit,
  onOpenTargetCalculator: () -> Unit = {},
  onOpenSupportHub: () -> Unit = {},
  onOpenAdminPanel: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val isAdmin = agent?.role == "ADMIN"

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    // 1. Hero Promotional Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Image(
          painter = painterResource(id = R.drawable.reseller_hero_banner_1792806449181),
          contentDescription = "Women Entrepreneur Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
        )

        // Gradient overlay with motivational Bengali badge
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                startY = 60f
              )
            )
            .padding(14.dp),
          contentAlignment = Alignment.BottomStart
        ) {
          Column {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = BrandSecondary.copy(alpha = 0.95f)
            ) {
              Text(
                text = "🌸 Standard Business উদ্যোক্তা ক্যাম্পেইন",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (isAdmin) "ব্যবসা ও রিসেলার নেটওয়ার্ক পরিচালনা" else "পণ্য মার্কেটিং করুন ও ঘরে বসেই ইনকাম করুন",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }

    // 2. Reseller Earning & Wallet Dashboard Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = if (isAdmin) "ব্যবসার সার্বিক তথ্য" else "আপনার রিসেলিং ইনকাম সামারি",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "৳${agent?.totalEarnings?.toInt() ?: 0}",
                  fontSize = 24.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = BrandPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = BrandSecondary.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = "${agent?.badgeLevel ?: "স্টার রিসেলার"} ⭐",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
              }
            }

            if (!isAdmin) {
              Button(
                onClick = onOpenWithdraw,
                colors = ButtonDefaults.buttonColors(containerColor = BkashPink),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("home_withdraw_button")
              ) {
                Icon(
                  imageVector = Icons.Default.AccountBalanceWallet,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "উইথড্র", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          Divider(modifier = Modifier.padding(vertical = 12.dp))

          // Sub-metrics: Available, Pending, Orders, Points
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            MetricPill(
              title = "উইথড্র যোগ্য",
              value = "৳${agent?.availableBalance?.toInt() ?: 0}",
              color = SuccessGreen
            )
            MetricPill(
              title = "পেন্ডিং কমিশন",
              value = "৳${agent?.pendingBalance?.toInt() ?: 0}",
              color = WarningOrange
            )
            MetricPill(
              title = "মোট অর্ডার",
              value = "${agent?.totalOrders ?: 0}টি",
              color = BrandPrimary
            )
            MetricPill(
              title = "রিওয়ার্ড পয়েন্ট",
              value = "${agent?.points ?: 0}★",
              color = BrandSecondary
            )
          }
        }
      }
    }

    // 3. Quick Action Hub
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionCard(
          icon = Icons.Default.ShoppingBag,
          title = "প্রোডাক্ট ক্যাটালগ",
          subtitle = "হোলসেল রেট",
          color = BrandPrimary,
          onClick = { onNavigateTab(MainTab.CATALOG) },
          modifier = Modifier.weight(1f)
        )
        QuickActionCard(
          icon = Icons.Default.LocalShipping,
          title = "অর্ডার ট্র্যাকিং",
          subtitle = "লাইভ আপডেট",
          color = SuccessGreen,
          onClick = { onNavigateTab(MainTab.ORDERS) },
          modifier = Modifier.weight(1f)
        )
        QuickActionCard(
          icon = Icons.Default.EmojiEvents,
          title = "লিডারবোর্ড",
          subtitle = "টপ আর্নার আপারা",
          color = BrandSecondary,
          onClick = { onNavigateTab(MainTab.LEADERBOARD) },
          modifier = Modifier.weight(1f)
        )
      }

      // Second Row of Professional Tools
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionCard(
          icon = Icons.Default.School,
          title = "ট্রেনিং একাডেমি",
          subtitle = "ভিডিও ও গাইড",
          color = BrandPrimary,
          onClick = { onNavigateTab(MainTab.TRAINING) },
          modifier = Modifier.weight(1f)
        )
        QuickActionCard(
          icon = Icons.Default.TrackChanges,
          title = "ইনকাম গোল",
          subtitle = "টার্গেট ক্যালকুলেটর",
          color = SuccessGreen,
          onClick = onOpenTargetCalculator,
          modifier = Modifier.weight(1f)
        )
        QuickActionCard(
          icon = Icons.Default.Headphones,
          title = "সাপোর্ট হাব",
          subtitle = "২৪/৭ হেল্পলাইন",
          color = Color(0xFF00897B),
          onClick = onOpenSupportHub,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Master Admin Control Hub Banner (STRICTLY visible ONLY to authenticated ADMIN)
    if (isAdmin) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onOpenAdminPanel() }
            .testTag("home_admin_panel_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = CircleShape,
              color = BrandPrimary,
              modifier = Modifier.size(46.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.AdminPanelSettings,
                  contentDescription = "Admin Panel",
                  tint = Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "👑 মাস্টার অ্যাডমিন প্যানেল",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = BrandSecondary.copy(alpha = 0.2f)
                ) {
                  Text(
                    text = "A to Z কন্ট্রোল",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandSecondary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = "ইউজার ম্যানেজমেন্ট, ব্যালেন্স (+/-), CPS ও CPA কন্ট্রোল",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Button(
              onClick = onOpenAdminPanel,
              colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
              shape = RoundedCornerShape(12.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
              modifier = Modifier.testTag("admin_panel_open_btn")
            ) {
              Text(text = "ম্যানেজ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 4. Special Offers for Bangladesh Business
    if (activeOffers.isNotEmpty()) {
      item {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Celebration,
                contentDescription = null,
                tint = BrandSecondary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "বাংলাদেশে স্পেশাল অফার ও বোনাস",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            Text(
              text = "সব অফার",
              color = BrandPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier
                .clickable { onOpenOffers() }
                .padding(4.dp)
            )
          }

          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(activeOffers) { offer ->
              OfferCard(offer = offer)
            }
          }
        }
      }
    }

    // 5. Trending Products Section
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = BrandPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "বেস্ট সেলিং হট কালেকশন 🔥",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
        Text(
          text = "সব দেখুন",
          color = BrandPrimary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .clickable { onNavigateTab(MainTab.CATALOG) }
            .padding(4.dp)
        )
      }
    }

    items(trendingProducts.take(4)) { product ->
      HomeProductItemCard(
        product = product,
        onMarketingClick = { onOpenMarketing(product) },
        onOrderClick = { onOpenOrder(product) }
      )
    }
  }
}

@Composable
fun MetricPill(title: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Text(
      text = title,
      fontSize = 10.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun QuickActionCard(
  icon: ImageVector,
  title: String,
  subtitle: String,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 11.5.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = subtitle,
        fontSize = 9.5.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun OfferCard(offer: OfferEntity) {
  Card(
    modifier = Modifier
      .width(260.dp)
      .clip(RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    )
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
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
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
        Text(
          text = offer.validUntil,
          fontSize = 10.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = offer.title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = BrandPrimary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = offer.subtitle,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}

@Composable
fun HomeProductItemCard(
  product: ProductEntity,
  onMarketingClick: () -> Unit,
  onOrderClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 5.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Product icon badge
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(BrandPrimary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.ShoppingBag,
          contentDescription = null,
          tint = BrandPrimary,
          modifier = Modifier.size(28.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = product.title,
          fontSize = 13.5.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = "${product.category} • স্টক: ${product.stock}টি",
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "পাইকারি: ৳${product.wholesalePrice.toInt()}",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold
          )
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = SuccessGreen.copy(alpha = 0.15f)
          ) {
            Text(
              text = "কমিশন: +৳${product.baseCommission.toInt()}",
              color = SuccessGreen,
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column(horizontalAlignment = Alignment.End) {
        OutlinedButton(
          onClick = onMarketingClick,
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
          modifier = Modifier.testTag("marketing_share_btn_${product.id}")
        ) {
          Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "মার্কেটিং", fontSize = 11.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Button(
          onClick = onOrderClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.testTag("order_place_btn_${product.id}")
        ) {
          Text(text = "অর্ডার", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
