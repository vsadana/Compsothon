package com.example.composothon

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.*
import com.example.composothon.ui.theme.ComposoThonTheme

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val people = intent.getSerializableExtra("score")
            val total = intent.getSerializableExtra("total")
            ComposoThonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val painter = painterResource(id = R.drawable.vaibhav)
                    val context = LocalContext.current
                    val mp: MediaPlayer = MediaPlayer.create(context,R.raw.quizend)
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ){

                        Column (horizontalAlignment = Alignment.CenterHorizontally){
                            ResultsGreeting(name = "10points")
                            Text(text = "Score:  $people / $total",
                                color = Color.White,

                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.paddingFromBaseline(top = 70.dp)
                            )
                            Card(modifier = Modifier
                                .background(Color.Black)
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    context.startActivity(Intent(context, MainActivity::class.java))
                                }
                                .padding(horizontal = 40.dp),
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = Color.Yellow,
                            ){
                                mp.start()
                                Text(
                                    text = "Start Quiz",
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center

                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// component to show result card
@Composable
fun ResultsGreeting(name: String) {
    PlayLottieAnim(spec = LottieCompositionSpec.RawRes(R.raw.gameover))
}

// component to play lottie animation
@Composable
fun PlayLottieAnim(spec: LottieCompositionSpec){
    val composition by rememberLottieComposition(spec)
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = progress,
    )
}