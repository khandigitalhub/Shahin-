package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductEntity
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen

@Composable
fun CatalogScreen(
  products: List<ProductEntity>,
  selectedCategory: String,
  searchQuery: String,
  isAdmin: Boolean,
  onCategorySelected: (String) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onOpenMarketing: (ProductEntity) -> Unit,
  onOpenOrder: (ProductEntity) -> Unit,
  onAddNewProduct: () -> Unit,
  modifier: Modifier = Modifier
) {
  val categories = listOf(
    "সব",
    "ফ্যাশন ও পোশাক",
    "ফুড ও গ্রোসারি",
    "ইলেকট্রনিক্স ও গ্যাজেট",
    "কসমেটিকস ও বিউটি",
    "জুয়েলারি",
    "হ্যান্ডব্যাগ",
    "হোম ও লাইফস্টাইল"
  )

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      // Search Bar
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChanged,
          placeholder = { Text("প্রোডাক্ট বা ক্যাটাগরি সার্চ করুন...", fontSize = 13.sp) },
          leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = BrandPrimary)
          },
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("catalog_search_input"),
          singleLine = true
        )
      }

      // Categories Row
      item {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(categories) { category ->
            val isSelected = category == selectedCategory
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) BrandPrimary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { onCategorySelected(category) }
                .testTag("category_chip_$category")
            ) {
              Text(
                text = category,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.5.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
              )
            }
          }
        }
      }

      // Header summary
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "উপলব্ধ প্রোডাক্ট (${products.size}টি)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "নিশ্চিত কমিশন ও পয়েন্ট",
            fontSize = 11.5.sp,
            color = SuccessGreen,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      // Products List
      if (products.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(50.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "কোনো প্রোডাক্ট পাওয়া যায়নি",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(products) { product ->
          ProductCatalogCard(
            product = product,
            onMarketingClick = { onOpenMarketing(product) },
            onOrderClick = { onOpenOrder(product) }
          )
        }
      }
    }

    // Admin FAB to add product
    if (isAdmin) {
      FloatingActionButton(
        onClick = onAddNewProduct,
        containerColor = BrandPrimary,
        contentColor = Color.White,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(bottom = 76.dp, end = 16.dp)
          .testTag("admin_add_product_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product")
      }
    }
  }
}

@Composable
fun ProductCatalogCard(
  product: ProductEntity,
  onMarketingClick: () -> Unit,
  onOrderClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        Box(
          modifier = Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrandPrimary.copy(alpha = 0.08f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = null,
            tint = BrandPrimary,
            modifier = Modifier.size(34.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = BrandPrimary.copy(alpha = 0.12f)
            ) {
              Text(
                text = product.category,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            Text(
              text = "স্টক: ${product.stock}টি",
              fontSize = 11.sp,
              color = if (product.stock < 10) Color.Red else SuccessGreen,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.5.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = product.fabricOrMaterial,
            fontSize = 11.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = product.description,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Divider(modifier = Modifier.padding(vertical = 10.dp))

      // Financials row: Wholesale, Suggested Retail, Base Commission, Points
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "পাইকারি রেট:",
            fontSize = 10.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "৳${product.wholesalePrice.toInt()}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Column {
          Text(
            text = "প্রস্তাবিত খুচরা:",
            fontSize = 10.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "৳${product.retailPrice.toInt()}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = SuccessGreen.copy(alpha = 0.15f)
        ) {
          Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "কমিশন: ৳${product.baseCommission.toInt()}",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              color = SuccessGreen
            )
            Text(
              text = "+${product.points} পয়েন্ট",
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              color = BrandSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Buttons: Marketing Share Tool & Direct Customer Order Placement
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onMarketingClick,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
        ) {
          Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("মার্কেটিং করো", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        Button(
          onClick = onOrderClick,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
        ) {
          Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("অর্ডার দিন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
