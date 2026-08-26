package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GharFixAmber
import com.example.ui.theme.GharFixAmberContainer
import com.example.ui.theme.GharFixAmberDark
import com.example.ui.theme.GharFixBackground
import com.example.ui.theme.GharFixBlue
import com.example.ui.theme.GharFixBlueContainer
import com.example.ui.theme.GharFixCardStroke
import com.example.ui.theme.GharFixEmerald
import com.example.ui.theme.GharFixEmeraldContainer
import com.example.ui.theme.GharFixNavy
import com.example.ui.theme.GharFixRed
import com.example.ui.theme.GharFixTeal
import com.example.ui.theme.GharFixTealContainer
import com.example.ui.theme.GharFixTealDark
import com.example.ui.theme.GharFixTealLight
import com.example.ui.theme.GharFixTextMuted
import com.example.ui.theme.GharFixTextPrimary
import com.example.ui.theme.GharFixTextSecondary

/**
 * Visual Layout styles for the GharFix Brand Logo
 */
enum class BrandLogoLayout {
    HORIZONTAL,
    VERTICAL,
    COMPACT
}

/**
 * Theme contexts for GharFix Brand Logo
 */
enum class BrandTheme {
    LIGHT,       // On light/white surface
    DARK,        // On navy/dark teal surface
    MONOCHROME   // White on solid color
}

/**
 * Size scale for the GharFix Logo
 */
enum class LogoSize(val markSize: Dp, val titleSp: Int, val taglineSp: Int) {
    COMPACT(24.dp, 16, 9),
    SMALL(30.dp, 19, 10),
    MEDIUM(38.dp, 22, 11),
    LARGE(56.dp, 28, 13),
    HERO(84.dp, 36, 15)
}

/**
 * Geometric, Precision-Engineered GharFix Logo Icon.
 * Features:
 * 1. Modern Architectural House Roof & Wall
 * 2. Integrated Precision Fix / Wrench Keystone in Center Doorway
 * 3. Radiant Warm Amber Energy Spark in the top-right apex
 */
@Composable
fun GharFixLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    theme: BrandTheme = BrandTheme.LIGHT,
    showContainer: Boolean = true,
    containerShape: RoundedCornerShape = RoundedCornerShape(22)
) {
    val containerBg = when {
        !showContainer -> Color.Transparent
        theme == BrandTheme.DARK -> Color.White.copy(alpha = 0.12f)
        else -> GharFixTealContainer
    }

    val containerBorder = when {
        !showContainer -> null
        theme == BrandTheme.DARK -> Color.White.copy(alpha = 0.25f)
        else -> GharFixTealLight.copy(alpha = 0.4f)
    }

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showContainer) {
                    Modifier
                        .clip(containerShape)
                        .background(containerBg)
                        .then(
                            if (containerBorder != null) {
                                Modifier.border(1.dp, containerBorder, containerShape)
                            } else Modifier
                        )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (showContainer) (size.value * 0.14f).dp else 0.dp)
        ) {
            drawGharFixEmblem(
                w = this.size.width,
                h = this.size.height,
                theme = theme
            )
        }
    }
}

/**
 * Custom Canvas drawing engine for the GharFix geometric mark
 */
private fun DrawScope.drawGharFixEmblem(w: Float, h: Float, theme: BrandTheme) {
    val roofNavy = if (theme == BrandTheme.DARK) Color(0xFFE2E8F0) else Color(0xFF0A4D68)
    val wallTeal = if (theme == BrandTheme.DARK) Color(0xFF83C5BE) else Color(0xFF006D77)
    val amberSpark = Color(0xFFF59E0B)
    val amberHighlight = Color(0xFFFFD166)
    val innerDark = if (theme == BrandTheme.DARK) Color(0xFF0F172A) else Color(0xFF00353F)

    val midX = w * 0.5f
    val roofPeakY = h * 0.16f
    val roofLeftX = w * 0.10f
    val roofRightX = w * 0.90f
    val eaveY = h * 0.48f

    val wallLeftX = w * 0.20f
    val wallRightX = w * 0.80f
    val wallBottomY = h * 0.88f

    // 1. Left Roof & Wall Wing (Deep Navy / Structural)
    val leftPath = Path().apply {
        moveTo(midX, roofPeakY)
        lineTo(roofLeftX, eaveY)
        lineTo(wallLeftX + w * 0.04f, eaveY)
        lineTo(wallLeftX, wallBottomY)
        lineTo(midX - w * 0.04f, wallBottomY)
        lineTo(midX - w * 0.04f, h * 0.58f)
        lineTo(midX, h * 0.58f)
        close()
    }
    drawPath(path = leftPath, color = roofNavy, style = Fill)

    // 2. Right Roof & Wall Wing (Vibrant Teal / Service)
    val rightPath = Path().apply {
        moveTo(midX, roofPeakY)
        lineTo(roofRightX, eaveY)
        lineTo(wallRightX - w * 0.04f, eaveY)
        lineTo(wallRightX, wallBottomY)
        lineTo(midX + w * 0.04f, wallBottomY)
        lineTo(midX + w * 0.04f, h * 0.58f)
        lineTo(midX, h * 0.58f)
        close()
    }
    drawPath(path = rightPath, color = wallTeal, style = Fill)

    // 3. Center Precision Keystone / Doorway Tool Element
    val toolHeadPath = Path().apply {
        val toolMidX = midX
        val toolTopY = h * 0.46f
        val toolBottomY = h * 0.60f
        val toolRadius = w * 0.11f

        moveTo(toolMidX - toolRadius, toolTopY + (toolBottomY - toolTopY) * 0.5f)
        lineTo(toolMidX - toolRadius * 0.5f, toolTopY)
        lineTo(toolMidX + toolRadius * 0.5f, toolTopY)
        lineTo(toolMidX + toolRadius, toolTopY + (toolBottomY - toolTopY) * 0.5f)
        lineTo(toolMidX + toolRadius * 0.5f, toolBottomY)
        lineTo(toolMidX - toolRadius * 0.5f, toolBottomY)
        close()
    }
    drawPath(path = toolHeadPath, color = amberSpark, style = Fill)

    // Inner hex punch hole
    drawCircle(
        color = innerDark,
        radius = w * 0.045f,
        center = Offset(midX, h * 0.53f)
    )

    // Tool Handle Stem
    drawRoundRect(
        color = amberSpark,
        topLeft = Offset(midX - w * 0.035f, h * 0.58f),
        size = Size(w * 0.07f, h * 0.28f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.02f, w * 0.02f)
    )

    // 4. Radiant Energy Spark / Solar Burst (Top Right Roof Peak)
    val sparkCenterX = w * 0.82f
    val sparkCenterY = h * 0.20f
    val sparkRadius = w * 0.13f

    val sparkPath = Path().apply {
        moveTo(sparkCenterX, sparkCenterY - sparkRadius)
        lineTo(sparkCenterX + sparkRadius * 0.35f, sparkCenterY - sparkRadius * 0.35f)
        lineTo(sparkCenterX + sparkRadius, sparkCenterY)
        lineTo(sparkCenterX + sparkRadius * 0.35f, sparkCenterY + sparkRadius * 0.35f)
        lineTo(sparkCenterX, sparkCenterY + sparkRadius)
        lineTo(sparkCenterX - sparkRadius * 0.35f, sparkCenterY + sparkRadius * 0.35f)
        lineTo(sparkCenterX - sparkRadius, sparkCenterY)
        lineTo(sparkCenterX - sparkRadius * 0.35f, sparkCenterY - sparkRadius * 0.35f)
        close()
    }
    drawPath(path = sparkPath, color = amberHighlight, style = Fill)

    // Inner Core Spark
    drawCircle(
        color = amberSpark,
        radius = sparkRadius * 0.42f,
        center = Offset(sparkCenterX, sparkCenterY)
    )
}

/**
 * Unified GharFix Brand Logo Lockup.
 * Renders the brand mark with high-precision typography and "Har Ghar. Har Fix." tagline.
 */
@Composable
fun GharFixLogo(
    modifier: Modifier = Modifier,
    size: LogoSize = LogoSize.MEDIUM,
    layout: BrandLogoLayout = BrandLogoLayout.HORIZONTAL,
    theme: BrandTheme = BrandTheme.LIGHT,
    showTagline: Boolean = true,
    taglineOverride: String? = null
) {
    val primaryTextColor = when (theme) {
        BrandTheme.DARK -> Color.White
        BrandTheme.LIGHT -> GharFixNavy
        BrandTheme.MONOCHROME -> Color.White
    }

    val accentTextColor = when (theme) {
        BrandTheme.DARK -> GharFixTealLight
        BrandTheme.LIGHT -> GharFixTeal
        BrandTheme.MONOCHROME -> Color.White
    }

    val taglineColor = when (theme) {
        BrandTheme.DARK -> GharFixAmber
        BrandTheme.LIGHT -> GharFixAmberDark
        BrandTheme.MONOCHROME -> Color.White.copy(alpha = 0.9f)
    }

    when (layout) {
        BrandLogoLayout.HORIZONTAL -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GharFixLogoMark(
                    size = size.markSize,
                    theme = theme
                )
                Spacer(modifier = Modifier.width(if (size == LogoSize.COMPACT) 6.dp else 10.dp))
                Column {
                    // GharFix Dual-Color Wordmark
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = primaryTextColor, fontWeight = FontWeight.Black)) {
                                append("Ghar")
                            }
                            withStyle(style = SpanStyle(color = accentTextColor, fontWeight = FontWeight.Black)) {
                                append("Fix")
                            }
                        },
                        fontSize = size.titleSp.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.3).sp,
                        lineHeight = (size.titleSp + 2).sp
                    )

                    if (showTagline) {
                        Text(
                            text = taglineOverride ?: "Har Ghar. Har Fix.",
                            fontSize = size.taglineSp.sp,
                            fontWeight = FontWeight.Bold,
                            color = taglineColor,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }
        }

        BrandLogoLayout.VERTICAL -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GharFixLogoMark(
                    size = size.markSize,
                    theme = theme,
                    containerShape = RoundedCornerShape(26)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = primaryTextColor, fontWeight = FontWeight.Black)) {
                            append("Ghar")
                        }
                        withStyle(style = SpanStyle(color = accentTextColor, fontWeight = FontWeight.Black)) {
                            append("Fix")
                        }
                    },
                    fontSize = size.titleSp.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    textAlign = TextAlign.Center
                )

                if (showTagline) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (theme == BrandTheme.DARK) Color(0xFF0F2C3A) else GharFixAmberContainer,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (theme == BrandTheme.DARK) GharFixAmber.copy(alpha = 0.4f) else Color(0xFFFDE68A)
                        )
                    ) {
                        Text(
                            text = taglineOverride ?: "“Har Ghar. Har Fix.”",
                            fontSize = size.taglineSp.sp,
                            fontWeight = FontWeight.Bold,
                            color = taglineColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        BrandLogoLayout.COMPACT -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GharFixLogoMark(
                    size = size.markSize,
                    theme = theme,
                    showContainer = true
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = primaryTextColor, fontWeight = FontWeight.Black)) {
                            append("Ghar")
                        }
                        withStyle(style = SpanStyle(color = accentTextColor, fontWeight = FontWeight.Black)) {
                            append("Fix")
                        }
                    },
                    fontSize = size.titleSp.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

/**
 * Trust & Quality Seal Badge for Chandrapur
 */
@Composable
fun GharFixTrustBadge(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = GharFixTealContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixTealLight.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = GharFixTeal,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GharFixTealDark
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 9.sp,
                        color = GharFixTextSecondary
                    )
                }
            }
        }
    }
}

/**
 * Full Startup Brand Presentation & About Card
 */
@Composable
fun GharFixAboutCard(
    modifier: Modifier = Modifier,
    cityName: String = "Chandrapur"
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, GharFixCardStroke)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header with Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GharFixLogo(
                    size = LogoSize.SMALL,
                    layout = BrandLogoLayout.HORIZONTAL,
                    theme = BrandTheme.LIGHT
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GharFixEmeraldContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GharFixEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Verified Hub",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharFixEmerald
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "GharFix is Chandrapur's premier on-demand home service and repair network. Our mission is delivering honest, transparent, and prompt doorstep assistance for every household in Vidarbha.",
                fontSize = 12.sp,
                color = GharFixTextSecondary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = GharFixCardStroke)
            Spacer(modifier = Modifier.height(12.dp))

            // 3 Pillar Pillars of GharFix Promise
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BrandPillarItem(
                    icon = Icons.Default.Security,
                    title = "100% Verified",
                    desc = "Aadhaar & Police Checked",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BrandPillarItem(
                    icon = Icons.Default.Shield,
                    title = "30-Day Guarantee",
                    desc = "Free Re-inspection",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                BrandPillarItem(
                    icon = Icons.Default.CheckCircle,
                    title = "Zero Hidden Fees",
                    desc = "Upfront Fair Billing",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BrandPillarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = GharFixBackground,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GharFixTeal,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GharFixNavy,
                textAlign = TextAlign.Center
            )
            Text(
                text = desc,
                fontSize = 9.sp,
                color = GharFixTextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Branded Mobile OTP Login & Onboarding Modal
 */
@Composable
fun GharFixAuthModal(
    onDismiss: () -> Unit,
    onSuccessLogin: (String) -> Unit
) {
    var mobileInput by remember { mutableStateOf("9822012345") }
    var otpInput by remember { mutableStateOf("1234") }
    var isOtpSent by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GharFixLogo(
                    size = LogoSize.LARGE,
                    layout = BrandLogoLayout.VERTICAL,
                    theme = BrandTheme.LIGHT
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (!isOtpSent)
                        "Enter your mobile number to sign in or register with GharFix in Chandrapur:"
                    else
                        "Enter 4-digit code sent to +91 $mobileInput (Default demo: 1234):",
                    fontSize = 12.sp,
                    color = GharFixTextSecondary,
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = mobileInput,
                    onValueChange = { mobileInput = it },
                    label = { Text("Mobile Number") },
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        ) {
                            Text("🇮🇳 +91", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                if (isOtpSent) {
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { otpInput = it },
                        label = { Text("Enter OTP Code (1234)") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = GharFixTeal)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GharFixEmeraldContainer.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = GharFixEmerald, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "100% Aadhaar Verified Local Professionals",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF065F46)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!isOtpSent) {
                        isOtpSent = true
                    } else {
                        onSuccessLogin(mobileInput)
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GharFixTeal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (!isOtpSent) "Send OTP Verification" else "Verify & Continue",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = GharFixTextSecondary)
            }
        }
    )
}
