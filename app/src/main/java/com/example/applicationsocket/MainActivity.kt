package com.example.applicationsocket

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.applicationsocket.ViewModel.Home.Home
import com.example.applicationsocket.ViewModel.ProfileUser.mainScreenProfile
import com.example.applicationsocket.ViewModel.Screen.CreatedEmail
import com.example.applicationsocket.ViewModel.Screen.CreatedName
import com.example.applicationsocket.ViewModel.Screen.CreatedPass
import com.example.applicationsocket.ViewModel.Screen.LoginScreen
import com.example.applicationsocket.ViewModel.Screen.ScreenLogin
import com.example.applicationsocket.ViewModel.Screen.checkIfUserExists
import com.example.applicationsocket.ViewModel.Screen.completeCreate
import com.example.applicationsocket.ViewModel.Screen.createOTP
import com.example.applicationsocket.ViewModel.Screen.saveBasicUserData
import com.example.applicationsocket.data.modelNameUser
//import com.example.applicationsocket.ViewModel.Screen.ScreenLoginEmail
//import com.example.applicationsocket.ViewModel.Screen.ScreenLoginModel
import com.example.applicationsocket.ui.theme.ApplicationSocketTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Khởi tạo Firebase
        setContent {
            ApplicationSocketTheme {
                MainNaviga()
            }
        }
    }
}

@Composable
fun MainNaviga(){
    val userViewModel: UserSessionViewModel = viewModel()
    val navController = rememberNavController()
    ApplicationSocketTheme {
        NavHost(navController = navController, startDestination = "homeLogin") {
            // Navigate to Login screen
            composable("homeLogin") {
                ScreenLogin(
                    openCreateEmail = {
                        navController.navigate("createEmail")
                    },
                    openLoginEmail = {
                        navController.navigate("loginEmail")
                    }
                )
            }
            // Navigate to CreateEmail screen
            composable("createEmail") {
                CreatedEmail(
                    openloginOTP = { email, otp ->
                        navController.navigate("createOTP/$email/$otp")
                    },
                    comback = {
                        navController.navigate("homeLogin")
                    }
                )
            }
            composable(
                "createOTP/{email}/{otp}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("otp") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                requireNotNull(email)// Provide a default value if null
                val otp = backStackEntry.arguments?.getString("otp")
                requireNotNull(otp)// Provide a default value for otp as well
                createOTP(email = email, otp = otp,
                    comback = {
                        navController.navigate("createEmail")
                    },
                    checkMail = {email ->
                        navController.navigate("createPass/$email")
                    },
                )
            }

            composable(
                "createPass/{email}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                requireNotNull(email)// Provide a default value if null
                CreatedPass(
                    email = email,
                    comback = {
                        navController.navigate("createEmail")
                    },
                    getPassEmail = {email, pass, context ->
                        val auth = FirebaseAuth.getInstance()
                        auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    user?.let {
                                        val userId = it.uid
                                        saveBasicUserData(userId, email, pass)
//                                        Toast.makeText(context, "Thành công", Toast.LENGTH_LONG).show()
                                        navController.navigate("CreateName/$userId")
                                    }
                                } else {
                                    checkIfUserExists(email, pass, context)
                                }
                            }
//                        navController.navigate("HomeLogin")
                    }
                )
            }

            composable("CreateName/{userid}",
            arguments = listOf(
                navArgument("userid") { type = NavType.StringType }
            )
            ){backStackEntry ->
                val userid = backStackEntry.arguments?.getString("userid")
                requireNotNull(userid)// Provide a default value if null
                CreatedName(
                    userid = userid,
                    comback = {
                        navController.navigate("HomeLogin")
                    },
                    tologin = {userid , ho, ten , context ->
                        val database = FirebaseDatabase.getInstance()
                        val userRef = database.getReference("users").child(userid).child("information")

                        val information = modelNameUser(ho, ten)

                        userRef.setValue(information)
                            .addOnSuccessListener {
//                                Toast.makeText(context, "Đăng Ký thành Công, Hãy đăng nhập !", Toast.LENGTH_LONG).show()
                                navController.navigate("completeCreateUser")
                            }
                            .addOnFailureListener { exception ->
                                // Failed to save user data
                                Toast.makeText(context, "Lỗi: ${exception.message}", Toast.LENGTH_LONG).show()
                            }

                    })
            }

            composable("completeCreateUser"){
                completeCreate(
                    comback = {
                        navController.navigate("HomeLogin")
                    }
                )

            }   // sau khi đăng nhập truy vấn realrime firebase để lấy first và last name để hiển thị làm key cho user
            composable("loginEmail"){
                LoginScreen(
                    comback = {
                        navController.navigate("homeLogin")
                    },
                    getPassEmail = { email: String, pass: String, context: android.content.Context ->
                        login(email, pass, context, userViewModel , toHome = {userId ->
                            getUserName(userID = userId, context = context,userViewModel ,togoHome = {
                                navController.navigate("Home")
                            })
                        })
                    }
                )
            }
            composable("Home"){
                Home(userViewModel = userViewModel,
                    toProfile = {
                        navController.navigate("profile")
                    }

                    )
            }
            composable("profile"){
                mainScreenProfile(userViewModel,
                    comback = {
                        navController.navigate("Home")
                    },
                    tologout = {
                        navController.navigate("homeLogin")
                    },
                    )
            }

        }
    }
}
fun login(email: String, pass: String, context: Context, userViewModel: UserSessionViewModel, toHome: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show()
            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
            getUserName(userId, context, userViewModel, togoHome = {
                toHome(userId) // This callback is now responsible for navigation after user data retrieval
            })
        } else {
            val errorMessage = task.exception?.let {
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> "Thông tin đăng nhập không chính xác"
                    is FirebaseAuthInvalidUserException -> "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"
                    else -> "Đăng nhập thất bại: ${it.message}"
                }
            } ?: "Lỗi không xác định"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}

fun getUserName(userID: String, context: Context, userViewModel: UserSessionViewModel, togoHome: () -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val userRef = database.getReference("users").child(userID).child("information")

    userRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val userInformation = task.result?.getValue(modelNameUser::class.java)
            if (userInformation != null) {
                userViewModel.setUserInformation(userInformation)
                togoHome()
            } else {
                Toast.makeText(context, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show()
            }
        } else {
            val errorMessage = task.exception?.message ?: "Lỗi không xác định"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

// đây làm duy trì đăng nhập giống session(PHP) chư abieest đặt tên English nh nào để đây
class UserSessionViewModel : ViewModel() {
    private val _userInformation = MutableLiveData<modelNameUser?>()
    val userInformation: LiveData<modelNameUser?> = _userInformation

    fun setUserInformation(userInformation: modelNameUser) {
        _userInformation.value = userInformation
    }

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
        _userInformation.value = null
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewScreenEmail() {
    ApplicationSocketTheme {
        MainNaviga()
    }
}