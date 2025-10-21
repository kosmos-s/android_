package com.example.w6

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w6.ui.theme.StudyGTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyGTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BubbleGameScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BubbleGameScreen() {
    val gameState = remember { GameState() }
    var showDialog by remember { mutableStateOf(false) }

    // 타이머
    LaunchedEffect(gameState.isGameOver) {
        if (!gameState.isGameOver) {
            while (true) {
                delay(1000L)
                gameState.timeLeft--
                if (gameState.timeLeft <= 0) {
                    gameState.isGameOver = true
                    showDialog = true
                    break
                }
                val now = System.currentTimeMillis()
                gameState.bubbles = gameState.bubbles.filter { now - it.creationTime < 4000 }
            }
        }
    }

    // 배경 그라데이션 애니메이션
    val color1 by animateColorAsState(
        targetValue = Color(
            Random.nextInt(100, 255),
            Random.nextInt(100, 255),
            Random.nextInt(200, 255)
        ),
        animationSpec = tween(3000, easing = LinearEasing)
    )

    val color2 by animateColorAsState(
        targetValue = Color(
            Random.nextInt(100, 255),
            Random.nextInt(100, 255),
            Random.nextInt(200, 255)
        ),
        animationSpec = tween(3500, easing = LinearEasing)
    )

    // 메인 UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(color1, color2)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GameStatusRow(score = gameState.score, timeLeft = gameState.timeLeft)

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val density = LocalDensity.current
                val canvasWidthPx = with(density) { maxWidth.toPx() }
                val canvasHeightPx = with(density) { maxHeight.toPx() }

                // 버블 이동
                LaunchedEffect(gameState.isGameOver) {
                    if (!gameState.isGameOver) {
                        while (true) {
                            delay(16)
                            if (gameState.bubbles.isEmpty()) {
                                gameState.bubbles = List(3) { makeNewBubble(maxWidth, maxHeight) }
                            }
                            if (Random.nextFloat() < 0.05f && gameState.bubbles.size < 15) {
                                gameState.bubbles += makeNewBubble(maxWidth, maxHeight)
                            }
                            gameState.bubbles = updateBubblePositions(
                                gameState.bubbles,
                                canvasWidthPx,
                                canvasHeightPx,
                                density
                            )
                        }
                    }
                }

                // 버블 그리기
                gameState.bubbles.forEach { bubble ->
                    BubbleComposable(bubble = bubble) {
                        gameState.score++
                        gameState.bubbles = gameState.bubbles.filterNot { it.id == bubble.id }
                    }
                }
            }
        }

        // 게임 오버 다이얼로그
        if (showDialog) {
            GameOverDialog(
                score = gameState.score,
                onRestart = {
                    restartGame(gameState)
                    showDialog = false
                },
                onExit = { showDialog = false }
            )
        }
    }
}

@Composable
fun GameStatusRow(score: Int, timeLeft: Int) {
    val scaleAnim = remember { Animatable(1f) }

    // 점수 바뀔 때 애니메이션
    LaunchedEffect(score) {
        scaleAnim.animateTo(1.3f, tween(150))
        scaleAnim.animateTo(1f, tween(150))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Score: $score",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier.scale(scaleAnim.value)
        )
        Text(
            text = "Time: ${timeLeft}s",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ---------- 버블 관련 ----------

data class Bubble(
    val id: Int,
    val position: Offset,
    val radius: Float,
    val color: Color,
    val creationTime: Long = System.currentTimeMillis(),
    val velocityX: Float = 0f,
    val velocityY: Float = 0f
)

class GameState(
    initialBubbles: List<Bubble> = emptyList()
) {
    var bubbles by mutableStateOf(initialBubbles)
    var score by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)
    var timeLeft by mutableStateOf(60)
}

@Composable
fun BubbleComposable(bubble: Bubble, onClick: () -> Unit) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(visible = visible) {
        val scale by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(200, easing = FastOutSlowInEasing)
        )
        Canvas(
            modifier = Modifier
                .size((bubble.radius * 2).dp)
                .offset(bubble.position.x.dp, bubble.position.y.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    visible = false
                    onClick()
                }
        ) {
            scale(scale) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            bubble.color.copy(alpha = 0.9f),
                            bubble.color.copy(alpha = 0.4f)
                        ),
                        center = center,
                        radius = size.width / 2
                    ),
                    radius = size.width / 2
                )
            }
        }
    }
}

fun makeNewBubble(maxWidth: Dp, maxHeight: Dp): Bubble {
    return Bubble(
        id = Random.nextInt(),
        position = Offset(
            Random.nextFloat() * maxWidth.value,
            Random.nextFloat() * maxHeight.value
        ),
        radius = Random.nextFloat() * 40 + 40,
        velocityX = Random.nextFloat() * 3f,
        velocityY = Random.nextFloat() * 3f,
        color = Color(
            red = Random.nextInt(150, 255),
            green = Random.nextInt(150, 255),
            blue = Random.nextInt(255),
            alpha = 220
        )
    )
}

fun updateBubblePositions(
    bubbles: List<Bubble>,
    canvasWidthPx: Float,
    canvasHeightPx: Float,
    density: androidx.compose.ui.unit.Density
): List<Bubble> {
    return bubbles.map { bubble ->
        with(density) {
            val radiusPx = bubble.radius.dp.toPx()
            var xPx = bubble.position.x.dp.toPx()
            var yPx = bubble.position.y.dp.toPx()
            var newVx = bubble.velocityX
            var newVy = bubble.velocityY

            xPx += newVx * 1.5f
            yPx += newVy * 1.5f

            if (xPx < radiusPx || xPx > canvasWidthPx - radiusPx) newVx *= -1
            if (yPx < radiusPx || yPx > canvasHeightPx - radiusPx) newVy *= -1

            xPx = xPx.coerceIn(radiusPx, canvasWidthPx - radiusPx)
            yPx = yPx.coerceIn(radiusPx, canvasHeightPx - radiusPx)

            bubble.copy(
                position = Offset(xPx.toDp().value, yPx.toDp().value),
                velocityX = newVx,
                velocityY = newVy
            )
        }
    }
}

// ---------- 다이얼로그 ----------

@Composable
fun GameOverDialog(score: Int, onRestart: () -> Unit, onExit: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("🎮 Game Over!", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
        text = { Text("당신의 점수는 $score 점입니다!", fontSize = 18.sp) },
        confirmButton = {
            TextButton(onClick = onRestart) {
                Text("다시 시작", fontSize = 18.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("종료", fontSize = 18.sp)
            }
        }
    )
}

fun restartGame(gameState: GameState) {
    gameState.score = 0
    gameState.timeLeft = 60
    gameState.isGameOver = false
    gameState.bubbles = emptyList()
}
