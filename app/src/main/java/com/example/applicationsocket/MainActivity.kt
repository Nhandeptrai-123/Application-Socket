package com.example.applicationsocket

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
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
import com.example.applicationsocket.ViewModel.ProfileUser.mainChangePass
import com.example.applicationsocket.ViewModel.ProfileUser.mainScreenChanceName
import com.example.applicationsocket.ViewModel.ProfileUser.mainScreenFeedback
import com.example.applicationsocket.ViewModel.ProfileUser.mainScreenProfile
import com.example.applicationsocket.ViewModel.Screen.CreatedEmail
import com.example.applicationsocket.ViewModel.Screen.CreatedName
import com.example.applicationsocket.ViewModel.Screen.CreatedPass
import com.example.applicationsocket.ViewModel.Screen.LoginScreen
import com.example.applicationsocket.ViewModel.Screen.ScreenLogin
import com.example.applicationsocket.ViewModel.Screen.checkIfUserExists
import com.example.applicationsocket.ViewModel.Screen.completeCreate
import com.example.applicationsocket.ViewModel.Screen.createOTP
import com.example.applicationsocket.ViewModel.Screen.generateOTP
import com.example.applicationsocket.ViewModel.Screen.saveBasicUserData
import com.example.applicationsocket.data.UserIDModel
import com.example.applicationsocket.data.UserSessionViewModel
import com.example.applicationsocket.data.getUserEmail
import com.example.applicationsocket.data.getUserName
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
import com.sun.mail.iap.Argument
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
    val userIDModel: UserIDModel = viewModel()
    val navController = rememberNavController()
    val context = LocalContext.current
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
                                        navController.navigate("CreateName/$userId")
                                    }
                                } else {
                                    checkIfUserExists(email, pass, context)
                                }
                            }
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
                    getPassEmail = {email, pass, context ->
                        login(email, pass, context, userViewModel, userIDModel ) { userId ->
                            navController.navigate("Home/$userId")
                        }
                    },
                )
            }
            composable(
                "Home/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                requireNotNull(userId) // Ensure userId is not null
                Home(userID = userId,
                    userViewModel = userViewModel,
                    idmodelUser = userIDModel,
                    toProfile = {
                        navController.navigate("profile/$userId")
                    }
                )
            }

            composable("profile/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ){backStackEntry ->
                val userid = backStackEntry.arguments?.getString("userId")
                requireNotNull(userid)// Provide a default value if null
                mainScreenProfile(userid,userViewModel,userIDModel,
                    comback = {
                        navController.navigate("Home/$userid")
                    },
                    tologout = {
                        navController.navigate("homeLogin") {
                            // tác dụng không cho quay lại màn hình trước sau khi đăng xuất
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    toChangeName = {
                        navController.navigate("ChangeName/$userid")
                    },
                    toChangePass = {
                        navController.navigate("ChangePass/$userid")
                    },
                    toSendFeedBack = {
                        navController.navigate("sendFeedBack/$userid")
                    },
                    )
            }
            composable("ChangeName/{userid}",
                arguments = listOf(
                    navArgument("userid") { type = NavType.StringType } ,
                )
                ){
                val userid = it.arguments?.getString("userid")
                requireNotNull(userid)// Provide a default value if null
                mainScreenChanceName(userModel = userViewModel, userid,
                    comback = {
                        navController.navigate("profile/$userid")
                    }
                )

            }
            composable("ChangePass/{userid}",
                arguments = listOf(navArgument("userid") { type = NavType.StringType },)
                ){
                val userid = it.arguments?.getString("userid")
                requireNotNull(userid)// Provide a default value if null
                mainChangePass(userid,userIDModel,
                    comback = {
                        navController.navigate("profile/$userid")
                    })

            }
            composable("sendFeedBack/{userid}",
                arguments = listOf(navArgument("userid") { type = NavType.StringType })
                ){
                val userid = it.arguments?.getString("userid")
                requireNotNull(userid)// Provide a default value if null
                mainScreenFeedback( userid, userIDModel,
                    comback = {
                        navController.navigate("profile/$userid")
                    })


            }

        }
    }
}
fun login(email: String, pass: String, context: Context, userViewModel: UserSessionViewModel,userIDModel: UserIDModel, toHome: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show()
            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

            // nêm truyền ữ liệu sau khi đăng nhập để dễ goji và sữ đụng
            getUserName(userId, context, userViewModel, togoHome = {
                toHome(userId)
            })
        } else {
            val errorMessage = task.exception?.let {
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> "Mật khẩu không không chính xác"
                    is FirebaseAuthInvalidUserException -> "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"
                    else -> "Đăng nhập thất bại: ${it.message}"
                }
            } ?: "Lỗi không xác định"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
