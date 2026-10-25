package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.TrainingModuleEntity
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningOrange

@Composable
fun TrainingAcademyScreen(
  modules: List<TrainingModuleEntity>,
  completedCount: Int,
  onSelectModule: (TrainingModuleEntity) -> Unit,
  onToggleCompleted: (Long, Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("সব") }
  val categories = listOf("সব", "প্রোডাক্ট নলেজ", "মার্কেটিং কৌশল", "অ্যাপ ও পেমেন্ট গাইড")

  val filteredModules = remember(modules, selectedCategory) {
    if (selectedCategory == "সব") modules
    else modules.filter { it.category == selectedCategory }
  }

  val totalCount = modules.size.coerceAtLeast(1)
  val progress = (completedCount.toFloat() / totalCount).coerceIn(0f, 1f)

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 90.dp)
  ) {
    // 1. Academy Hero Progress Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.linearGradient(listOf(BrandPrimary, BrandSecondary))
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.School,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(26.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Standard Business একাডেমি",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = BrandPrimary
                )
                Text(
                  text = "এজেন্টদের সেলস ও স্কিল ট্রেইনিং",
                  fontSize = 11.5.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (progress == 1f) SuccessGreen.copy(alpha = 0.15f) else BrandSecondary.copy(alpha = 0.15f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.WorkspacePremium,
                  contentDescription = null,
                  tint = if (progress == 1f) SuccessGreen else BrandSecondary,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (progress == 1f) "সার্টিফাইড" else "লার্নার",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (progress == 1f) SuccessGreen else BrandSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "আপনার লার্নিং অগ্রগতি ($completedCount/$totalCount সম্পন্ন)",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "${(progress * 100).toInt()}%",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = BrandPrimary
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = BrandPrimary,
            trackColor = BrandPrimary.copy(alpha = 0.15f)
          )

          if (progress == 1f) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = SuccessGreen.copy(alpha = 0.12f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "🎉 অভিনন্দন! আপনি সফলভাবে সকল মডিউল সম্পন্ন করেছেন। এখন আপনি Standard Business-এর একজন অফিসিয়াল সার্টিফাইড এজেন্ট!",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }
      }
    }

    // 2. Category Filter Pills
    item {
      LazyRow(
        contentPadding = PaddingValues(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { cat ->
          val isSelected = cat == selectedCategory
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) BrandPrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable { selectedCategory = cat }
              .testTag("training_cat_$cat")
          ) {
            Text(
              text = cat,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
            )
          }
        }
      }
    }

    // 3. Module Cards
    items(filteredModules) { module ->
      TrainingModuleCard(
        module = module,
        onClick = { onSelectModule(module) },
        onToggleCompleted = { onToggleCompleted(module.id, !module.isCompleted) }
      )
    }
  }
}

@Composable
fun TrainingModuleCard(
  module: TrainingModuleEntity,
  onClick: () -> Unit,
  onToggleCompleted: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
      .clip(RoundedCornerShape(18.dp))
      .clickable { onClick() }
      .testTag("module_card_${module.id}"),
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
          color = BrandPrimary.copy(alpha = 0.12f)
        ) {
          Text(
            text = module.category,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (module.isCompleted) SuccessGreen.copy(alpha = 0.15f) else WarningOrange.copy(alpha = 0.12f),
          modifier = Modifier.clickable { onToggleCompleted() }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (module.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayCircleFilled,
              contentDescription = null,
              tint = if (module.isCompleted) SuccessGreen else WarningOrange,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (module.isCompleted) "সম্পন্ন" else "বাকি আছে",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (module.isCompleted) SuccessGreen else WarningOrange
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = module.title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.5.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = module.subtitle,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = module.summary,
        fontSize = 11.5.sp,
        lineHeight = 16.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Divider(modifier = Modifier.padding(vertical = 10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = BrandSecondary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = module.durationText,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
          )
        }

        Button(
          onClick = onClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(text = "ভিডিও ও গাইড দেখুন", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun TrainingModuleDetailDialog(
  module: TrainingModuleEntity,
  onDismiss: () -> Unit,
  onToggleCompleted: (Boolean) -> Unit
) {
  val context = LocalContext.current
  var isPlaying by remember { mutableStateOf(false) }
  var playbackProgress by remember { mutableFloatStateOf(0.35f) }
  var playbackSpeed by remember { mutableStateOf("1.0x") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
        .testTag("training_module_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandPrimary.copy(alpha = 0.12f)
          ) {
            Text(
              text = module.category,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BrandPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Text(
          text = module.title,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 1. Simulated Interactive Video Player with Controls
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.verticalGradient(
                listOf(Color(0xFF1F112A), Color(0xFF3B1540))
              )
            )
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            // Video Top Info
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.Black.copy(alpha = 0.6f)
              ) {
                Text(
                  text = "HD 1080p",
                  color = Color.White,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.clickable {
                  playbackSpeed = when (playbackSpeed) {
                    "1.0x" -> "1.25x"
                    "1.25x" -> "1.5x"
                    else -> "1.0x"
                  }
                }
              ) {
                Text(
                  text = playbackSpeed,
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            // Central Play/Pause Button
            Box(
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(54.dp)
                .clip(CircleShape)
                .background(BrandPrimary.copy(alpha = 0.85f))
                .clickable { isPlaying = !isPlaying },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
              )
            }

            // Bottom Timeline & Status
            Column {
              Text(
                text = module.videoTitle,
                color = Color.White,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Slider(
                value = playbackProgress,
                onValueChange = { playbackProgress = it },
                colors = SliderDefaults.colors(
                  thumbColor = BrandSecondary,
                  activeTrackColor = BrandPrimary,
                  inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(24.dp)
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (isPlaying) "চলছে... (স্পিড: $playbackSpeed)" else "ভিডিও প্লে করতে ট্যাপ করুন",
                  color = Color.White.copy(alpha = 0.8f),
                  fontSize = 10.sp
                )
                Text(
                  text = module.durationText.split("(").firstOrNull()?.trim() ?: "৫ মি.",
                  color = Color.White.copy(alpha = 0.8f),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Step-by-Step Training Guide
        Text(
          text = "মডিউল বিস্তারিত গাইডলাইন:",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
        ) {
          Text(
            text = module.detailedContent,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(12.dp)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Key Takeaways Checklist
        Text(
          text = "মনে রাখার মতো গুরুত্বপূর্ণ বিষয় (Checklist):",
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Bold,
          color = SuccessGreen
        )
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = SuccessGreen.copy(alpha = 0.1f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
        ) {
          Text(
            text = module.keyTakeaways,
            fontSize = 11.5.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(12.dp)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4. Practical Sales Tip with Copy Button
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = BrandSecondary.copy(alpha = 0.12f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = BrandSecondary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = "বাস্তব সেলস টিপস", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BrandSecondary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = module.practicalTips, fontSize = 11.5.sp, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
              onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Sales Tip", module.practicalTips)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "টিপস কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("টিপস কপি করুন", fontSize = 10.5.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Completion Toggle Button
        Button(
          onClick = {
            onToggleCompleted(!module.isCompleted)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (module.isCompleted) Color(0xFF616161) else SuccessGreen
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("toggle_module_complete_btn")
        ) {
          Icon(
            imageVector = if (module.isCompleted) Icons.Default.Close else Icons.Default.CheckCircle,
            contentDescription = null
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (module.isCompleted) "পুনরায় অসম্পূর্ণ হিসেবে চিহ্নিত করুন" else "মডিউল সম্পন্ন হিসেবে মার্ক করুন ✓",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }
  }
}
