# PlacesToStay

## Overview

This Android application allows users to manage and explore points of interest on a map. It uses modern Android development practices, including Jetpack Compose for UI, ViewModel and LiveData for state management, and implements user settings and navigation modes.

## Features

- Interactive map display
- Add and manage points of interest
- User settings configuration
- GPS integration
- Web upload functionality

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture Components**: ViewModel, LiveData
- **Database**: SQLite
- **Location Services**: Android Location API

## Project Structure

The project is organized into several key components:

- `MainActivity`: Entry point of the application
- `LatLonViewModel`: Manages location data and GPS status
- Composable functions for each screen:
  - Home Screen
  - Add POI Screen
  - Settings Screen

## Key Implementations

### Jetpack Compose UI

- Utilizes reusable Composable functions for each screen
- Implements Box layout for flexible UI positioning
- Uses Modifiers for customizing UI element appearance
- Handles user input with state management in Composables

### ViewModel and LiveData

- `LatLonViewModel` manages and updates location data
- LiveData observables for latitude, longitude, and GPS status
- Custom setters for updating LiveData values

### User Settings

- Implements a dedicated Settings screen
- Utilizes Switch components for toggling settings
- Manages GPS status with real-time updates

### Navigation

- Implements navigation between screens using Jetpack Navigation component
- Utilizes back stack for efficient navigation history management

## Setup and Installation

1. Clone the repository
   ``` git clone https://github.com/hannahannx/PointsOfInterest_ ```
2. Open the project in Android Studio
3. Build and run the application on an emulator or physical device

## Contributing

Contributions to the Points of Interest application are welcome. Please feel free to submit a Pull Request.

