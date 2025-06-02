# Donde Va

Donde Va is an Android application designed to classify waste materials and provide users with guidance on proper disposal. The app leverages modern technologies and good development practices to deliver a robust and user-friendly experience.

## Purpose of the App

The app's primary goal is to help users identify the type of waste (e.g., cardboard, glass, metal, paper, plastic, trash) and provide recommendations for proper disposal. It uses a camera-based interface to capture images of waste items and classify them using machine learning.

---

## Technologies Used and Their Purpose

### 1. **Jetpack Compose**
   - **Purpose**: Used for building the app's UI declaratively.
   - **Features**: Simplifies UI development with a modern, reactive approach.

### 2. **CameraX**
   - **Purpose**: Provides camera functionality for capturing images.
   - **Features**: Simplifies camera integration with lifecycle-aware components.

### 3. **TensorFlow Lite**
   - **Purpose**: Enables on-device machine learning for waste classification.
   - **Features**: Efficient and lightweight ML model inference.

### 4. **Firebase**
   - **Purpose**: Provides backend services such as authentication and Firestore for data storage.
   - **Features**: Simplifies user authentication and real-time database management.

### 5. **Navigation Component**
   - **Purpose**: Manages navigation between screens.
   - **Features**: Ensures a structured and predictable navigation flow.

### 6. **Kotlin Serialization**
   - **Purpose**: Handles JSON serialization and deserialization.
   - **Features**: Simplifies working with JSON data.

---

## Good Practices Followed

- **Modular Code Structure**: The project is divided into logical modules such as `ui`, `presentation`, and `domain` for better maintainability.
- **Dependency Injection**: Uses Gradle's dependency management for clean and reusable code.
- **Material Design 3**: Ensures a modern and consistent UI/UX.
- **Lifecycle Awareness**: Components like CameraX and Compose are lifecycle-aware, reducing memory leaks.
- **Test Coverage**: Includes unit and instrumented tests for reliability.

---

## Dependencies

### Core Dependencies
- **Jetpack Compose**: `androidx.compose.*`
- **Material Design 3**: `androidx.compose.material3`
- **Navigation Component**: `androidx.navigation:navigation-compose`

### Camera
- **CameraX**: `androidx.camera:camera-*`

### Machine Learning
- **TensorFlow Lite**: `org.tensorflow:tensorflow-lite`
- **TensorFlow Lite GPU**: `org.tensorflow:tensorflow-lite-gpu`

### Firebase
- **Firebase Authentication**: `com.google.firebase:firebase-auth`
- **Firestore**: `com.google.firebase:firebase-firestore`

---

## Additional Information
- **ML Model**: The app uses a TensorFlow Lite model (garbage_classifier.tflite) located in the assets folder. The model classifies waste into six categories: Cardboard, Glass, Metal, Paper, Plastic, and Trash.
- **Labels**: The classification labels are stored in labels.txt in the assets folder.
- **Permissions**: The app requests camera permissions at runtime.

---
