package com.example.pointsofinterest_hannahann

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pointsofinterest_hannahann.ui.theme.PointsOfInterest_hannahannTheme

public class MainActivity : ComponentActivity() , LocationListener{
    private fun startGPS() {
        val mgr = getSystemService(LOCATION_SERVICE) as LocationManager
        //CHECKS WHETHER ACCESS FINE LOCATION PERMISSION HAS BEEN GRANTED AT RUNTIME
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

    //Runs when a new location is received from the provider
    override fun onLocationChanged(location: Location) {
        Toast.makeText(this, "Latitude: ${location.latitude}, Longitude: ${location.longitude}", Toast.LENGTH_LONG).show()    }


    //When you physically disable or enable the GPS from device
    //run a specific behaviour to occur when the user switches the GPS ON and OFF
    //write this information here
    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this, "GPS enabled", Toast.LENGTH_LONG).show()

    }
    //When you physically disable or enable the GPS from device
    //run a specific behaviour to occur when the user switches the GPS ON and OFF
    //write this information here
    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this, "GPS disabled", Toast.LENGTH_LONG).show()
    }

    override fun onStatusChanged(provider: String,status:Int, extras: Bundle){
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
                    //This is the top level Parent Composable which helps with the navigation between each of the screens
                    //sets up the controller and remember the rotates
                    val navController = rememberNavController()
                    //These initialise the application starting point
                    NavHost(navController= navController, startDestination="homeScreen") {
                        composable("homeScreen") {
                            HomeScreenComposable(navController)
                        }
                        composable("poiScreen") {
                            AddPOIScreenComposable()
                        }
                    }
                    //FUNCTIONS TO BE CALLED WOULD BE INSIDE HERE

                }
            }
        }
    }

    //this is a function to check if it has the appropriate permission granted for the mapping function
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
}

//This is the main page which when clicked will navigate to enter the application.
//Mainly here for testing purposes
@Composable
//AddPOIScreen IS a call back function
fun HomeScreenComposable(navController: NavController){
    Box (
        modifier = Modifier
            .fillMaxSize()
            .border(BorderStroke(10.dp, Color.Black))
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                navController.navigate("poiScreen")
            }) {
                Text("Click to go to ADD POI")
            }
        }
    }
}

//This Screen displays the information to add the information which the user writes and apply it tot the SQLite database
@Composable
fun AddPOIScreenComposable(){
    var name by remember { mutableStateOf (" ") }
    var type by remember { mutableStateOf (" ") }
    var description by remember { mutableStateOf (" ") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(BorderStroke(10.dp, Color.Black))
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Add new POI" , fontSize = 40.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(100.dp))
            OutlinedTextField(value = name, onValueChange = { name = it })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = type, onValueChange = { type = it })
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = description, onValueChange = { description = it })
            Spacer(Modifier.height(10.dp))
            //this button should have a label TEXT but currently not working
            OutlinedButton(onClick = {
                //button to click and connect to the database to add the POI to the database
                //uses lifecycle
            }) {
                Text("Add")
            }
        }
    }
}