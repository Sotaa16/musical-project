package com.example.mucialproject

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.hypot

/**
 * Daftar bagian drum + posisi relatif di layar (xFrac, yFrac = 0f..1f dari lebar/tinggi area drum).
 * Nanti tinggal hubungkan [DrumPiece] ke file suara (SoundPool / Oboe).
 */
enum class DrumPiece(
    val label: String,
    val xFrac: Float,
    val yFrac: Float,
    val size: Dp,
    val colorLight: Color,
    val colorDark: Color,
) {
    CRASH_L("Crash", 0.09f, 0.24f, 125.dp, Color(0xFFFFE082), Color(0xFFB8860B)),
    SPLASH("Splash", 0.22f, 0.13f, 80.dp, Color(0xFFFFF59D), Color(0xFFF9A825)),
    TOM_1("Tom 1", 0.34f, 0.33f, 100.dp, Color(0xFFF48FB1), Color(0xFFAD1457)),
    TOM_2("Tom 2", 0.47f, 0.25f, 100.dp, Color(0xFFF48FB1), Color(0xFFAD1457)),
    TOM_3("Tom 3", 0.61f, 0.33f, 100.dp, Color(0xFFF48FB1), Color(0xFFAD1457)),
    CRASH_R("Crash 2", 0.76f, 0.17f, 105.dp, Color(0xFFFFE082), Color(0xFFB8860B)),
    RIDE("Ride", 0.90f, 0.30f, 120.dp, Color(0xFFFFCC80), Color(0xFFE65100)),

    RIDE_BELL("Bell", 0.90f, 0.30f, 0.dp, Color(0xFFFFCC80), Color(0xFFE65100)),
    HH_CLOSE("HH Close", 0.10f, 0.63f, 105.dp, Color(0xFFFFF59D), Color(0xFF9E8F1F)),
    HH_OPEN("HH Open", 0.22f, 0.84f, 85.dp, Color(0xFFFFF59D), Color(0xFF9E8F1F)),
    SNARE("Snare", 0.47f, 0.60f, 100.dp, Color(0xFFFFFFFF), Color(0xFF9E9E9E)),
    KICK_L("Kick", 0.38f, 0.82f, 120.dp, Color(0xFFEF9A9A), Color(0xFFB71C1C)),
    KICK_R("Kick", 0.56f, 0.82f, 120.dp, Color(0xFFEF9A9A), Color(0xFFB71C1C)),
    FLOOR_TOM("Floor", 0.82f, 0.68f, 125.dp, Color(0xFFA5D6A7), Color(0xFF2E7D32)),
}

@Composable
fun DrumScreen(
    onHit: (DrumPiece) -> Unit = {},
) {
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var kitName by remember { mutableStateOf("Acoustic Kit") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1B1B1F), Color(0xFF0D0D10))))
    ) {
        TopControls(
            kitName = kitName,
            isRecording = isRecording,
            isPlaying = isPlaying,
            onToggleRecord = { isRecording = !isRecording },
            onTogglePlay = { isPlaying = !isPlaying },
            onKitClick = { /* TODO: buka dialog pilih kit */ },
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val areaWidth = maxWidth
            val areaHeight = maxHeight
            DrumPiece.entries.filter { it != DrumPiece.RIDE_BELL }.forEach { piece ->
                DrumPad(
                    piece = piece,
                    onHit = onHit,
                    modifier = Modifier.offset(
                        x = areaWidth * piece.xFrac - piece.size / 2,
                        y = areaHeight * piece.yFrac - piece.size / 2,
                    ),
                )
            }
        }
    }
}

@Composable
private fun TopControls(
    kitName: String,
    isRecording: Boolean,
    isPlaying: Boolean,
    onToggleRecord: () -> Unit,
    onTogglePlay: () -> Unit,
    onKitClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF26262B))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        IconButton(onClick = { /* TODO: menu / settings */ }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        }

        // Pilih kit
        Text(
            text = kitName,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF3A3A42))
                .clickable(onClick = onKitClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )

        Spacer(Modifier.weight(1f))

        // Rekam
        IconButton(onClick = onToggleRecord) {
            Icon(
                Icons.Default.FiberManualRecord,
                contentDescription = "Rekam",
                tint = if (isRecording) Color.Red else Color.Gray,
            )
        }

        // Play / Stop
        IconButton(onClick = onTogglePlay) {
            Icon(
                if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Stop" else "Play",
                tint = Color.White,
            )
        }
    }
}

/** Satu pad drum. Tiap pad punya pointerInput sendiri, jadi multi-touch jalan. */
@Composable
fun DrumPad(
    piece: DrumPiece,
    onHit: (DrumPiece) -> Unit,
    modifier: Modifier = Modifier,
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        label = "padScale",
    )
    val hasBell = piece == DrumPiece.RIDE

    Box(
        modifier = modifier
            .size(piece.size)
            .scale(scale)
            .clip(CircleShape)
            .background(if (pressed) piece.colorLight.copy(alpha = 0.85f) else Color(0xFF1E1E24))
            .border(
                5.dp,
                Brush.sweepGradient(listOf(piece.colorLight, piece.colorDark, piece.colorLight)),
                CircleShape,
            )
            .pointerInput(piece) {
                detectTapGestures(
                    onPress = { offset ->
                        pressed = true
                        val jarak = hypot(offset.x - size.width / 2f, offset.y - size.height / 2f)
                        val kenaBell = hasBell && jarak < size.width * 0.18f
                        onHit(if (kenaBell) DrumPiece.RIDE_BELL else piece)
                        tryAwaitRelease()
                        pressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        if (hasBell) {
            Box(
                modifier = Modifier
                    .size(piece.size * 0.36f)
                    .clip(CircleShape)
                    .background(piece.colorLight.copy(alpha = 0.6f))
                    .border(2.dp, piece.colorDark, CircleShape)
            )
        }
        Text(
            text = piece.label,
            color = if (pressed) Color.Black else Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = if (hasBell) Modifier.offset(y = piece.size * 0.3f) else Modifier,
        )
    }
}

@Preview(widthDp = 900, heightDp = 420, showBackground = true)
@Composable
private fun DrumScreenPreview() {
    MaterialTheme { DrumScreen() }
}