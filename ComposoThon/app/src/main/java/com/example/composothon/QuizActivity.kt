package com.example.composothon

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.compose.*
import com.example.composothon.ui.theme.ComposoThonTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class QuizActivity : ComponentActivity() {
    private val questionStateModel: QuestionStateModel by viewModels();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var objOfSquad = listOf(
                mapOf(
                    "squad" to "Sigma",
                    "name" to "Vaibhav",
                    "image" to painterResource(id = R.drawable.vaibhav) ,
                    "options" to listOf("Momentum", "Photon", "Sigma","Nucleus"),
                    "logos" to listOf(
                        painterResource(id = R.drawable.momentum) ,
                        painterResource(id = R.drawable.photon),
                        painterResource(id = R.drawable.sigma),
                        painterResource(id = R.drawable.nucleus))
                ),
                mapOf(
                    "squad" to "Photon",
                    "name" to "Manish",
                    "image" to painterResource(id = R.drawable.manish) ,
                    "options" to listOf("Photon", "Momentum", "Sigma","Nucleus"),
                    "logos" to listOf(
                        painterResource(id = R.drawable.photon) ,
                        painterResource(id = R.drawable.momentum),
                        painterResource(id = R.drawable.sigma),
                        painterResource(id = R.drawable.nucleus)
                    )
                ),
                mapOf(
                    "squad" to "Photon",
                    "name" to "Shivam",
                    "image" to painterResource(id =  R.drawable.shivam),
                    "options" to listOf("Sigma", "Photon", "Momentum","Nucleus"),
                    "logos" to listOf(
                        painterResource(id = R.drawable.sigma) ,
                        painterResource(id = R.drawable.photon),
                        painterResource(id = R.drawable.momentum),
                        painterResource(id = R.drawable.nucleus)
                    )
                ),
                mapOf(
                    "squad" to "Momentum",
                    "name" to "Utkarsh",
                    "image" to painterResource(id =  R.drawable.utkarsh),
                    "options" to listOf("Nucleus", "Photon", "Momentum","Sigma"),
                    "logos" to listOf(
                        painterResource(id = R.drawable.nucleus) ,
                        painterResource(id = R.drawable.photon),
                        painterResource(id = R.drawable.momentum),
                        painterResource(id = R.drawable.sigma)
                    )
                ),
                mapOf(
                    "squad" to "Qubit",
                    "name" to "Mayur",
                    "image" to painterResource(id =  R.drawable.mayur),
                    "options" to listOf("Nucleus", "Qubit", "Momentum","Sigma"),
                    "logos" to listOf(
                        painterResource(id = R.drawable.nucleus) ,
                        painterResource(id = R.drawable.qubit),
                        painterResource(id = R.drawable.momentum),
                        painterResource(id = R.drawable.sigma)
                    )
                ),
                mapOf(
                    "squad" to "None",
                    "name" to "Abhilash",
                    "image" to painterResource(id =  R.drawable.abhilash),
                    "options" to listOf("Nucleus", "Photon", "Momentum","None"),
                    "logos" to listOf(
                        painterResource(id = R.drawable.nucleus) ,
                        painterResource(id = R.drawable.photon),
                        painterResource(id = R.drawable.momentum),
                        painterResource(id = R.drawable.ic_launcher_background)
                    )
                )
            ).toMutableList()
            val shuffAlp = objOfSquad.shuffled()

            ComposoThonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var quesState = questionStateModel.state.collectAsState()
                    var scoreState = ViewModelProvider(this).get(ScoreViewModel::class.java)
                    if(quesState.value < shuffAlp.count()){
                        QuestionPoint(
                            Question(
                                type = "names",
                                answer= shuffAlp[quesState.value]["squad"].toString(),
                                options = shuffAlp[quesState.value]["options"] as List<String>,
                                squadLogos = shuffAlp[quesState.value]["logos"] as List<Painter>,
                                logoImage = shuffAlp[quesState.value]["image"]  as Painter,
                                name = shuffAlp[quesState.value]["name"] as String
                                ), questionStateModel, scoreState)
                    } else {
                        val context = LocalContext.current
                            context.startActivity(Intent(context, ResultsActivity::class.java).apply {
                                putExtra("score","${scoreState.value}")
                                putExtra("total","${shuffAlp.count()*10}")
                            })
                    }
                }
            }
        }
    }
}

// Model
class QuestionStateModel : ViewModel() {
    var state = MutableStateFlow(0);
}

// component of Logo image which shows the employee image and user has to identify the squad name of the person
@Composable
fun LogoDesign(painter:Painter){
    Card(
        shape = RoundedCornerShape(100.dp),
        elevation = 5.dp,
        modifier = Modifier.padding(100.dp)
    ){
        Image(painter = painter, contentDescription = "xyc" ,modifier = Modifier
            .height(200.dp)
            .width(200.dp)
        )
    }
}

//component to show list of options . User has to select the correct options
// Correct answer will be labeled as green and incorrect with red
@Composable
fun QuestionPoint(question: Question, questionState: QuestionStateModel, scoreState: ScoreViewModel){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        Scaffold(
            scaffoldState = scaffoldState
        ) {
        Column(modifier = Modifier.background(Color.Black)) {
            LogoDesign(painter =  question.logoImage)
            CardPoint(question, scoreState) {
                if(it){
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Correct Answer ",)
                    }
                } else{
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Wrong Answer",)
                    }
                }
                Handler().postDelayed({
                    questionState.state.value += 1
                },3000)
            }
        }
    }
    }
}

// component of card contains optionns
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardPoint(question: Question, scoreState: ScoreViewModel, callBack: (Boolean) -> Unit){
    val flag = remember(key1 = question.name) {
        mutableStateOf(value = false)
    }
    LazyColumn{
        items(count = question.options.count()){ item->
            CardHOC(question.name, question.options[item], flag = flag.value, question.answer, image = question.squadLogos[item], scoreState){
                callBack(it)
                flag.value = true;
            }
        }
    }
}

//component of subcard contains image of squad and squad name
@Composable
fun CardHOC(name: String, text: String, flag: Boolean, answer: String, image: Painter, scoreState: ScoreViewModel, callBack:(Boolean)->Unit) {
    val checkAnsState = remember(key1 = name, key2 = text) {
        mutableStateOf(value = Color.White)
    }
    Card(modifier = Modifier
        .background(Color.Black)
        .padding(10.dp)
        .fillMaxWidth()
        .clickable(enabled = !flag) {
            if (!flag) {
                if (text == answer) {
                    callBack(true)
                    checkAnsState.value = Color.Green;
                    scoreState.value +=10
                } else {
                    callBack(false)
                    checkAnsState.value = Color.Red;
                }
            }
        },
        backgroundColor = checkAnsState.value,
        shape = RoundedCornerShape(20.dp)
    ){
        Row(horizontalArrangement = Arrangement.Start,verticalAlignment = Alignment.CenterVertically) {
            Card(
                elevation = 5.dp,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(60.dp)
                    .height(60.dp)
            )
            {
                Image(
                    painter = image,
                    contentDescription = "hello",
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(vertical = 24.dp,horizontal = 10.dp)
            )
        }
    }
}
