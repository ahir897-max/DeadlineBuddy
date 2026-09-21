package com.example.deadlinebuddy

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File


// =====================================================
// PROFILE DATA
// =====================================================

data class UserProfile(
    val name: String,
    val university: String,
    val department: String,
    val year: String,
    val semester: String,
    val imagePath: String
)


// =====================================================
// MAIN ACTIVITY
// =====================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DeadlineBuddyApp()
        }
    }
}


// =====================================================
// MAIN APP
// =====================================================

@Composable
fun DeadlineBuddyApp() {

    val context =
        androidx.compose.ui.platform.LocalContext.current

    val prefs = remember {

        context.getSharedPreferences(
            "deadline_buddy_data",
            Context.MODE_PRIVATE
        )
    }

    /*
     * IMPORTANT:
     *
     * App open হলে ALWAYS welcome screen আসবে।
     * তাই screen = "welcome"
     */

    var screen by remember {
        mutableStateOf("welcome")
    }

    var profile by remember {
        mutableStateOf(
            loadProfile(context)
        )
    }


    when (screen) {

        // =================================================
        // WELCOME
        // =================================================

        "welcome" -> {

            WelcomeScreen(

                onLoginClick = {

                    // Profile আগে তৈরি করা আছে?
                    if (
                        prefs.getBoolean(
                            "profile_created",
                            false
                        )
                    ) {

                        profile =
                            loadProfile(context)

                        screen = "dashboard"

                    } else {

                        // প্রথমবার login করলে
                        // profile create করতে যাবে
                        screen = "profile"
                    }
                },

                onSignUpClick = {

                    // Sign Up চাপলে profile create screen
                    screen = "profile"
                }
            )
        }


        // =================================================
        // PROFILE
        // =================================================

        "profile" -> {

            ProfileScreen(

                onSaveProfile = { newProfile ->

                    saveProfile(
                        context = context,
                        profile = newProfile
                    )

                    profile = newProfile

                    screen = "dashboard"
                }
            )
        }


        // =================================================
        // DASHBOARD
        // =================================================

        "dashboard" -> {

            DashboardScreen(

                profile = profile,

                onEditProfile = {

                    screen = "profile"
                }
            )
        }
    }
}


// =====================================================
// SAVE PROFILE
// =====================================================

fun saveProfile(
    context: Context,
    profile: UserProfile
) {

    val prefs =
        context.getSharedPreferences(
            "deadline_buddy_data",
            Context.MODE_PRIVATE
        )

    prefs.edit()

        .putBoolean(
            "profile_created",
            true
        )

        .putString(
            "name",
            profile.name
        )

        .putString(
            "university",
            profile.university
        )

        .putString(
            "department",
            profile.department
        )

        .putString(
            "year",
            profile.year
        )

        .putString(
            "semester",
            profile.semester
        )

        .putString(
            "image_path",
            profile.imagePath
        )

        .apply()
}


// =====================================================
// LOAD PROFILE
// =====================================================

fun loadProfile(
    context: Context
): UserProfile {

    val prefs =
        context.getSharedPreferences(
            "deadline_buddy_data",
            Context.MODE_PRIVATE
        )

    return UserProfile(

        name =
            prefs.getString(
                "name",
                ""
            ) ?: "",

        university =
            prefs.getString(
                "university",
                ""
            ) ?: "",

        department =
            prefs.getString(
                "department",
                ""
            ) ?: "",

        year =
            prefs.getString(
                "year",
                ""
            ) ?: "",

        semester =
            prefs.getString(
                "semester",
                ""
            ) ?: "",

        imagePath =
            prefs.getString(
                "image_path",
                ""
            ) ?: ""
    )
}


// =====================================================
// WELCOME SCREEN
// =====================================================

@Composable
fun WelcomeScreen(

    onLoginClick: () -> Unit,

    onSignUpClick: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFFFF8F7)
                )
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================================
        // APP NAME
        // =================================================

        Text(

            text =
                "Deadline Buddy",

            fontSize =
                36.sp,

            fontWeight =
                FontWeight.Bold,

            fontFamily =
                FontFamily.Cursive,

            color =
                Color(0xFF5B9BD5),

            textAlign =
                TextAlign.Center
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =================================================
        // HELLO KITTY
        // =================================================

        Box(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(
                        RoundedCornerShape(35.dp)
                    )
                    .background(
                        Color(0xFFFFE8EF)
                    ),

            contentAlignment =
                Alignment.Center
        ) {

            Image(

                painter =
                    painterResource(
                        id =
                            R.drawable.hello_kitty
                    ),

                contentDescription =
                    "Hello Kitty",

                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(15.dp),

                contentScale =
                    ContentScale.Fit
            )
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        Text(

            text =
                "Hello, Student! ♡",

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold,

            fontFamily =
                FontFamily.Cursive,

            color =
                Color(0xFF553B43)
        )


        Spacer(
            modifier =
                Modifier.height(6.dp)
        )


        Text(

            text =
                "Your little study buddy",

            fontSize =
                18.sp,

            fontFamily =
                FontFamily.Cursive,

            color =
                Color(0xFF9B536B)
        )


        Text(

            text =
                "for every deadline.",

            fontSize =
                18.sp,

            fontFamily =
                FontFamily.Cursive,

            color =
                Color(0xFF9B536B)
        )


        Spacer(
            modifier =
                Modifier.height(22.dp)
        )


        // =================================================
        // LOG IN
        // =================================================

        Button(

            onClick =
                onLoginClick,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp),

            shape =
                RoundedCornerShape(28.dp),

            colors =
                ButtonDefaults.buttonColors(

                    containerColor =
                        Color(0xFFFF9FBA)
                )
        ) {

            Text(

                text =
                    "Log In  →",

                fontSize =
                    20.sp,

                fontFamily =
                    FontFamily.Cursive,

                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        // =================================================
        // SIGN UP
        // =================================================

        OutlinedButton(

            onClick =
                onSignUpClick,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp),

            shape =
                RoundedCornerShape(28.dp),

            colors =
                ButtonDefaults.outlinedButtonColors(

                    contentColor =
                        Color(0xFFE85D8E)
                )
        ) {

            Text(

                text =
                    "Sign Up  ♡",

                fontSize =
                    19.sp,

                fontFamily =
                    FontFamily.Cursive,

                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Text(

            text =
                "Small steps every day lead to big dreams ♡",

            fontSize =
                14.sp,

            fontFamily =
                FontFamily.Cursive,

            color =
                Color(0xFF9B536B),

            textAlign =
                TextAlign.Center
        )
    }
}


// =====================================================
// PROFILE SCREEN
// =====================================================

@Composable
fun ProfileScreen(

    onSaveProfile:
        (UserProfile) -> Unit
) {

    val context =
        androidx.compose.ui.platform.LocalContext.current


    var name by remember {
        mutableStateOf("")
    }

    var university by remember {
        mutableStateOf("")
    }

    var department by remember {
        mutableStateOf("")
    }

    var year by remember {
        mutableStateOf("")
    }

    var semester by remember {
        mutableStateOf("")
    }

    var imagePath by remember {
        mutableStateOf("")
    }


    // =================================================
    // LOAD SAVED PROFILE
    // =================================================

    LaunchedEffect(Unit) {

        val oldProfile =
            loadProfile(context)

        name =
            oldProfile.name

        university =
            oldProfile.university

        department =
            oldProfile.department

        year =
            oldProfile.year

        semester =
            oldProfile.semester

        imagePath =
            oldProfile.imagePath
    }


    // =================================================
    // IMAGE PICKER
    // =================================================

    val imagePicker =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts.GetContent()

        ) { uri: Uri? ->

            if (uri != null) {

                imagePath =
                    saveImageToInternalStorage(
                        context,
                        uri
                    )
            }
        }


    val pink =
        Color(0xFFFFF0F6)

    val darkPink =
        Color(0xFFE85D8E)


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(pink)
                .padding(24.dp)
    ) {

        Text(

            text =
                "🌸 Create Your Profile",

            fontSize =
                28.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                darkPink
        )


        Text(

            text =
                "Let's get to know you 💕",

            fontSize =
                16.sp,

            modifier =
                Modifier.padding(
                    top = 5.dp,
                    bottom = 10.dp
                )
        )


        // =================================================
        // PROFILE IMAGE
        // =================================================

        Box(

            modifier =
                Modifier.fillMaxWidth(),

            contentAlignment =
                Alignment.Center
        ) {

            if (
                imagePath.isNotEmpty() &&
                File(imagePath).exists()
            ) {

                val bitmap =
                    remember(imagePath) {

                        BitmapFactory
                            .decodeFile(
                                imagePath
                            )
                            ?.asImageBitmap()
                    }


                if (bitmap != null) {

                    Image(

                        bitmap =
                            bitmap,

                        contentDescription =
                            "Profile Picture",

                        modifier =
                            Modifier
                                .size(105.dp)
                                .clip(
                                    CircleShape
                                )
                                .clickable {

                                    imagePicker
                                        .launch(
                                            "image/*"
                                        )
                                },

                        contentScale =
                            ContentScale.Crop
                    )
                }

            } else {

                Box(

                    modifier =
                        Modifier
                            .size(105.dp)
                            .clip(
                                CircleShape
                            )
                            .background(
                                Color(0xFFFFB6C9)
                            )
                            .clickable {

                                imagePicker
                                    .launch(
                                        "image/*"
                                    )
                            },

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "📷",
                        fontSize = 35.sp
                    )
                }
            }
        }


        Text(

            text =
                "Tap to add profile picture",

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 5.dp,
                        bottom = 10.dp
                    ),

            textAlign =
                TextAlign.Center,

            fontSize =
                14.sp,

            color =
                darkPink
        )


        // =================================================
        // INPUTS
        // =================================================

        ProfileInput(
            value = name,
            onValueChange = {
                name = it
            },
            label = "Name",
            placeholder = "Your name"
        )


        ProfileInput(
            value = university,
            onValueChange = {
                university = it
            },
            label = "University Name",
            placeholder = "Your university"
        )


        ProfileInput(
            value = department,
            onValueChange = {
                department = it
            },
            label = "Department",
            placeholder = "e.g. CSE"
        )


        ProfileInput(
            value = year,
            onValueChange = {
                year = it
            },
            label = "Year",
            placeholder = "e.g. 3rd Year"
        )


        ProfileInput(
            value = semester,
            onValueChange = {
                semester = it
            },
            label = "Semester",
            placeholder = "e.g. 1st Semester"
        )


        Spacer(
            modifier =
                Modifier.weight(1f)
        )


        // =================================================
        // SAVE PROFILE
        // =================================================

        Button(

            onClick = {

                val newProfile =
                    UserProfile(

                        name =
                            name,

                        university =
                            university,

                        department =
                            department,

                        year =
                            year,

                        semester =
                            semester,

                        imagePath =
                            imagePath
                    )

                onSaveProfile(
                    newProfile
                )
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),

            shape =
                RoundedCornerShape(28.dp),

            colors =
                ButtonDefaults.buttonColors(

                    containerColor =
                        darkPink
                )
        ) {

            Text(

                text =
                    "Save Profile  →",

                fontSize =
                    17.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =====================================================
// PROFILE INPUT
// =====================================================

@Composable
fun ProfileInput(

    value: String,

    onValueChange:
        (String) -> Unit,

    label: String,

    placeholder: String
) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        label = {
            Text(label)
        },

        placeholder = {
            Text(placeholder)
        },

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 7.dp
                ),

        shape =
            RoundedCornerShape(16.dp),

        singleLine =
            true
    )
}


// =====================================================
// DASHBOARD
// =====================================================

@Composable
fun DashboardScreen(

    profile: UserProfile,

    onEditProfile: () -> Unit
) {

    val pink =
        Color(0xFFFFF0F6)

    val darkPink =
        Color(0xFFE85D8E)


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(pink)
                .padding(20.dp)
    ) {


        // =================================================
        // DASHBOARD TITLE
        // =================================================

        Text(

            text =
                "Deadline Buddy 💙",

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color(0xFF5B9BD5)
        )


        Text(

            text =
                "Let's make your deadlines stress-free 🌸",

            fontSize =
                16.sp,

            modifier =
                Modifier.padding(
                    top = 5.dp,
                    bottom = 15.dp
                )
        )


        // =================================================
        // PROFILE CARD
        // =================================================

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(25.dp),

            colors =
                CardDefaults.cardColors(

                    containerColor =
                        Color.White
                )
        ) {

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                // =================================================
                // PROFILE IMAGE
                // =================================================

                if (
                    profile.imagePath.isNotEmpty() &&
                    File(
                        profile.imagePath
                    ).exists()
                ) {

                    val bitmap =
                        remember(
                            profile.imagePath
                        ) {

                            BitmapFactory
                                .decodeFile(
                                    profile.imagePath
                                )
                                ?.asImageBitmap()
                        }


                    if (bitmap != null) {

                        Image(

                            bitmap =
                                bitmap,

                            contentDescription =
                                "Profile Picture",

                            modifier =
                                Modifier
                                    .size(85.dp)
                                    .clip(
                                        CircleShape
                                    ),

                            contentScale =
                                ContentScale.Crop
                        )
                    }

                } else {

                    Box(

                        modifier =
                            Modifier
                                .size(85.dp)
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    Color(0xFFFFB6C9)
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = "👤",
                            fontSize = 32.sp
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.width(14.dp)
                )


                // =================================================
                // PROFILE DETAILS
                // =================================================

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(

                        text =
                            profile.name.ifEmpty {
                                "Student"
                            },

                        fontSize =
                            22.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            darkPink
                    )


                    Text(

                        text =
                            profile.university,

                        fontSize =
                            14.sp
                    )


                    Text(

                        text =
                            "${profile.department} • ${profile.year}",

                        fontSize =
                            14.sp
                    )


                    Text(

                        text =
                            profile.semester,

                        fontSize =
                            14.sp
                    )
                }
            }


            // =================================================
            // EDIT PROFILE
            // =================================================

            TextButton(

                onClick =
                    onEditProfile,

                modifier =
                    Modifier
                        .align(
                            Alignment.End
                        )
                        .padding(
                            end = 10.dp,
                            bottom = 5.dp
                        )
            ) {

                Text(

                    text =
                        "Edit Profile ✏️",

                    color =
                        darkPink
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        // =================================================
        // DASHBOARD BUTTONS
        // =================================================

        DashboardButton(
            emoji = "📝",
            title = "Add Task"
        )

        DashboardButton(
            emoji = "📚",
            title = "Add Assignment"
        )

        DashboardButton(
            emoji = "🗓️",
            title = "Calendar"
        )

        DashboardButton(
            emoji = "⏰",
            title = "Due Assignments"
        )

        DashboardButton(
            emoji = "🔔",
            title = "Reminder"
        )
    }
}


// =====================================================
// DASHBOARD BUTTON
// =====================================================

@Composable
fun DashboardButton(

    emoji: String,

    title: String
) {

    Button(

        onClick = {
            // Later functionality
        },

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                )
                .height(60.dp),

        shape =
            RoundedCornerShape(20.dp)
    ) {

        Text(

            text =
                "$emoji   $title",

            fontSize =
                18.sp
        )
    }
}


// =====================================================
// SAVE PROFILE IMAGE
// =====================================================

fun saveImageToInternalStorage(

    context: Context,

    uri: Uri
): String {

    val file =
        File(
            context.filesDir,
            "profile_picture.jpg"
        )


    context.contentResolver
        .openInputStream(uri)
        ?.use { input ->

            file.outputStream()
                .use { output ->

                    input.copyTo(output)
                }
        }


    return file.absolutePath
}