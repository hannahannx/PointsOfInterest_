package com.example.pointsofinterest_hannahann

import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.pointsofinterest_hannahann.ui.theme.PointsOfInterest_hannahannTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() , LocationListener{
    fun startGPS() {
        val mgr = getSystemService(LOCATION_SERVICE) as LocationManager
        if (checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointsOfInterest_hannahannTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //sets up the controller and remember the rotates
                    val navController = rememberNavController()
                    //These initialise the application starting point
                    NavHost(navController=navController, startDestination="homeScreen") {
                        composable("homeScreen") {
                            HomeScreenComposable()(
                        }
                        composable("poiScreen") {
                            AddPOIScreen()
                        }
                    }
                    //Just tesing if the application is running
                    Greeting("Test to see if the application is running ")
                }
            }
        }
    }
}
//this is a function to check if it has the appropriate permission granted for the mapping fucntion
fun checkPermissions() {
    val requiredPermission = Manifest.permission.ACCESS_FINE_LOCATION

    if(checkSelfPermission(requiredPermission) == PackageManager.PERMISSION_GRANTED) {
        startGPS() // a function to start the GPS - see below
    } else {
        // Request the permission
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) {
                startGPS() // A function to start the GPS - see below
            } else {
                // Permission not granted
                Toast.makeText(this, "GPS permission not granted", Toast.LENGTH_LONG).show()
            }
        }
        permissionLauncher.launch(requiredPermission)
    }
}

//This is the top level Parent Composable which helps with the navigation between each of the screens
@Composable
fun ParentComposable(){
    val navController = rememberNavController()
    NavHost(navController=navController) {
        composable("homeScreen") {
            HomeScreenComposable(AddPOIScreen = {
                navController.navigate("poiScreen")
            })
        }
        composable("poiScreen") {
            AddPOIScreen()
        }
    }

//This is the main page which when clicked will nanigate to enter the appication.
//Mainly here for testing purposes
@Composable
//AddPOIScreen IS a call back function
fun HomeScreenComposable(AddPOIScreen:() -> Unit){
    Button(onClick = {AddPOIScreen()}){
        Text("Click to Enter Application")
    }
}

//This Screen displays the infomation to add the infomation which the user writes and apply it tot the SQLite database
@Composable
fun AddPOIScreen(){
    var name by remember { mutableStateOf (" ") }
    var type by remember { mutableStateOf (" ") }
    var description by remember { mutableStateOf (" ") }
    Box{
        Column{
            TextField(value = name, onValueChange = {
                name = it } )
            TextField(value = type, onValueChange = {
                type = it })
            TextField(value = description, onValueChange = {
                description = it })
        }
    }
}

//Just tesing if the application is running
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello!",
        modifier = modifier
    )
}