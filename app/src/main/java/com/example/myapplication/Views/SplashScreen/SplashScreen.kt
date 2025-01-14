import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.myapplication.R
import com.example.myapplication.Utilities.Constants
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(onSplashCompleted: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val scaleAndRotation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1500),
        label = "scale and rotation"
    )

    val montserrat = FontFamily(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onSplashCompleted()
    }

    Box(
        modifier = Modifier
            .background(Constants.hubWhite)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = "Uygulama Logosu",
                modifier = Modifier
                    .size(230.dp)
                    .scale(scaleAndRotation)
                    .rotate(scaleAndRotation * 362f)
            )
            
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF3C9BFF),
                            fontWeight = FontWeight.Bold,
                            fontFamily = montserrat
                        )
                    ) {
                        append("seven")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF3C9BFF),
                            fontWeight = FontWeight.Normal,
                            fontFamily = montserrat
                        )
                    ) {
                        append("hub")
                    }
                },
                fontSize = 48.sp,
                modifier = Modifier
                    .scale(scaleAndRotation)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        onSplashCompleted = {}
    )
}


