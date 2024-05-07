@file:Suppress("DEPRECATION")

package com.example.pointsofinterest_hannahann

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import com.example.pointsofinterest_hannahann.ui.theme.PointsOfInterest_hannahannTheme
import org.osmdroid.views.overlay.Marker
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() , LocationListener{
    private val latLonViewModel : LatLonViewModel by viewModels()
    lateinit var db: POIDatabase

    @SuppressLint("MissingPermission")
    fun startGPS(gpsRunning:Boolean) {
        val mgr = getSystemService(LOCATION_SERVICE) as LocationManager
        if (gpsRunning) {
            //CHECKS WHETHER ACCESS FINE LOCATION PERMISSION HAS BEEN GRANTED AT RUNTIME
            if (checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                return
            }
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        } else{
            mgr.removeUpdates(this)
        }
    }

    //Runs when a new location is received from the provider
    override fun onLocationChanged(location: Location) {
        latLonViewModel.latLon = LatLonViewModel.LatLon(location.latitude, location.longitude)
    }

    //When you physically disable or enable the GPS from device
    //run a specific behaviour to occur when the user switches the GPS ON and OFF
    //write this information here
    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this, "GPS enabled", Toast.LENGTH_LONG).show()
        latLonViewModel.gpsStatus = LatLonViewModel.GpsStatus(true)

    }
    //When you physically disable or enable the GPS from device
    //run a specific behaviour to occur when the user switches the GPS ON and OFF
    //write this information here
    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this, "GPS disabled", Toast.LENGTH_LONG).show()
        latLonViewModel.gpsStatus = LatLonViewModel.GpsStatus(false)
    }

    override fun onStatusChanged(provider: String,status:Int, extras: Bundle){
    }


    //NEED TO PUT THE COMPOSABLE FUNCTIONS INSIDE THE ACTIVITY
    @Composable
    fun HomeScreenComposable(navController: NavController,latLonViewModel: LatLonViewModel){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(BorderStroke(10.dp, Color.Black))
                .padding(20.dp)
        ) {
            Column {
                Surface(
                    modifier = Modifier
                        .zIndex(2.0f)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GpsPosition(latLonViewModel, this@MainActivity)
                        GpsStatusComposable(latLonViewModel, this@MainActivity)
                        //This button navigates to the ADDPOI SCREEN
                        Row {
                            Button(onClick = {
                                navController.navigate("poiScreen")
                            }) {
                                Text("Add POI Screen")
                            }
                            Button(onClick = {
                                navController.navigate("settingsScreen")
                            }) {
                                Text("Settings Screen")
                            }
                        }
                    }
                }
                MapComposable(
                    mod = Modifier.fillMaxWidth(),
                    latLonViewModel,
                    owner = this@MainActivity,markerToMap = true)
            }
        }
    }

    @Composable
    fun GpsPosition(latLonViewModel: LatLonViewModel, owner: LifecycleOwner) {
        //this will be the starting position for the map latitude and longitude when the GPS starts
        var latLon by remember { mutableStateOf(LatLonViewModel.LatLon())}
        latLonViewModel.liveDataLatLon.observe(owner) {
            var latLon = it
        }
    }

    @Composable
    fun GpsStatusComposable(latLonViewModel: LatLonViewModel, owner: LifecycleOwner){
        var status by remember { mutableStateOf(LatLonViewModel.GpsStatus())}
        latLonViewModel.liveGpsStatus.observe(owner){
            var status = it
        }
    }

    @Composable
    fun MapComposable(mod: Modifier, latLonViewModel: LatLonViewModel,owner: LifecycleOwner,markerToMap:Boolean){
        AndroidView(
            modifier = mod,
            factory = { ctx ->
                // This line sets the user agent, a requirement to download OSM maps
                org.osmdroid.config.Configuration.getInstance()
                    .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

                val map1 = MapView(ctx).apply {
                    isClickable = true
                    setMultiTouchControls(true)
                    setTileSource(TileSourceFactory.MAPNIK)
                    var opentopomap = true
                    setTileSource( if (opentopomap) TileSourceFactory.OpenTopo else TileSourceFactory.MAPNIK )
                }
                map1
            }
        ) { view ->
            if (markerToMap){
                val marker = Marker(view)
                marker.apply {
                    position = GeoPoint(latLonViewModel.latLon.lat,latLonViewModel.latLon.lon)
                    title = ""
                }
                view.overlays.add(marker)
            }
            view.controller.setZoom(14.0)
            view.controller.setCenter(GeoPoint(latLonViewModel.latLon.lat,latLonViewModel.latLon.lon))
        }
        GpsPosition(latLonViewModel, owner= this)
    }

    //This Screen displays the information to add the information which the user writes and apply it tot the SQLite database
    @Composable
    fun AddPOIScreenComposable(navController: NavController){
        var name by remember { mutableStateOf ("") }
        var type by remember { mutableStateOf ("") }
        var description by remember { mutableStateOf ("") }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(BorderStroke(10.dp, Color.Black))
                .padding(20.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Add new POI" , fontSize = 40.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = {Text("Name")})
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = type, onValueChange = { type = it }, label = {Text("Type")})
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = {Text("Description")})
                Spacer(Modifier.height(10.dp))
                OutlinedButton(onClick = {
                    //button to click and connect to the database to add the POI to the database
                    //uses lifecycle
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO){
                            val newPOI = POI(0,7, name, type, description,latLonViewModel.latLon.lat,latLonViewModel.latLon.lon)
                            db.poiDao().add(newPOI)
                        }
                    }
                }
                ) {
                    Text("Add to Map")
                }
                Row {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ){
                        Text(text = "Home Screen")
                    }
                    Button(
                        onClick = {
                            navController.navigate("settingsScreen")
                        }) {
                        Text(text = "Settings Screen")
                    }
                }
            }
        }
    }

    @Composable
    fun SettingsScreenComposable(navController: NavController){
        var uploaded by remember { mutableStateOf(false)}
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(BorderStroke(10.dp, Color.Black))
                .padding(20.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                GpsStatusComposable(latLonViewModel, owner= this@MainActivity )
                if (latLonViewModel.gpsStatus.status) {
                    Toast.makeText(applicationContext, "GPS enabled", Toast.LENGTH_SHORT).show()
                    startGPS(true)
                } else {
                    startGPS(false)
                    Toast.makeText(applicationContext, "GPS disabled", Toast.LENGTH_SHORT).show()
                }
                if (uploaded){
                    Toast.makeText(applicationContext, "Uploading to web enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Uploading to web disabled", Toast.LENGTH_SHORT).show()
                }


                Text("Settings" , fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                //UPLOADED TO WEB
                Row {
                    Text("Uploaded to Web " , fontSize = 30.sp, fontWeight = FontWeight.Thin)
                    Switch(checked = uploaded,
                        onCheckedChange = {
                            uploaded = it
                        }
                    )
                }
                Spacer(Modifier.height(10.dp))
                //GPS
                Row{
                    Text("GPS " , fontSize = 30.sp, fontWeight = FontWeight.Thin)
                    Switch(checked = latLonViewModel.gpsStatus.status,
                        onCheckedChange = {
                            latLonViewModel.gpsStatus.status = it
                        }
                    )
                }
                Row {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ){
                        Text(text = "Add POI Screen")
                    }
                    Button(
                        onClick = {
                            navController.navigate("homeScreen") {popUpTo("homeScreen")}
                        }) {
                        Text(text = "Home Screen")
                    }
                }
            }
        }
    }


    //ON CREATE FUNCTION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = POIDatabase.getDatabase(application)
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
                            /*When the user clicks the button in the UI, this lambda is called with the new value of geoPoint  from the UI.
                            Inside this lambda, the geoPoint variable is updated to the new value received from the UI*/
                            HomeScreenComposable(navController, latLonViewModel)
                        }
                        composable("poiScreen") {
                            AddPOIScreenComposable(navController)
                        }
                        composable("settingsScreen"){
                            SettingsScreenComposable(navController)
                        }
                    }
                    //FUNCTIONS TO BE CALLED WOULD BE INSIDE HERE
                }
            }
        }
        checkPermissions()
    }

    //this is a function to check if it has the appropriate permission granted for the mapping function
    private fun checkPermissions() {
        val requiredPermission = Manifest.permission.ACCESS_FINE_LOCATION

        if(checkSelfPermission(requiredPermission) == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Request the permission
            val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if(isGranted) {
                    startGPS(true) // A function to start the GPS - see below
                } else {
                    // Permission not granted
                    Toast.makeText(this, "GPS permission not granted", Toast.LENGTH_LONG).show()
                }
            }
            permissionLauncher.launch(requiredPermission)
        }
    }
}

