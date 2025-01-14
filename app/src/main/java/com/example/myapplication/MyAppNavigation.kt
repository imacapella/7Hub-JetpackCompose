package com.example.myapplication

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.AccountView.AccountScreen
import com.example.myapplication.Views.ChatList.ChatListScreen
import com.example.myapplication.Views.ChatScreen.ChatScreen
import com.example.myapplication.Views.ClubsView.ClubDetailScreen
import com.example.myapplication.Views.ClubsView.ClubDetailViewModel
import com.example.myapplication.DataLayer.Models.ClubTab
import com.example.myapplication.Views.ClubsView.ClubsScreen
import com.example.myapplication.Views.ClubsView.ClubsViewModel
import com.example.myapplication.Views.CourseView.CourseDetailScreen
import com.example.myapplication.Views.CourseView.CourseDetailViewModel
import com.example.myapplication.Views.CourseView.CoursesScreen
import com.example.myapplication.Views.CourseView.CoursesViewModel
import com.example.myapplication.Views.GroupsView.GroupsScreen
import com.example.myapplication.Views.HelpView.HelpScreen
import com.example.myapplication.Views.HomeView
import com.example.myapplication.Views.LoginView.AuthState
import com.example.myapplication.Views.LoginView.LoginPage
import com.example.myapplication.Views.LoginView.LoginViewModel
import com.example.myapplication.Views.ResetPassword.ResetPasswordScreen
import com.example.myapplication.Views.ResetPassword.ResetPasswordViewModel
import com.example.myapplication.Views.ReviewScreen.CourseDetailsScreen
import com.example.myapplication.Views.ReviewScreen.ReviewCoursesScreen
import com.example.myapplication.Views.ReviewScreen.ReviewScreen
import com.example.myapplication.Views.ReviewScreen.TeacherDetailsScreen
import com.example.myapplication.Views.ReviewScreen.dummyCourse1
import com.example.myapplication.Views.ReviewScreen.dummyCourse2
import com.example.myapplication.Views.ReviewScreen.dummyTeacher1
import com.example.myapplication.Views.ReviewScreen.dummyTeacher2
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Reviews : BottomNavItem("reviews", Icons.Default.Star, "Reviews")
    object Account : BottomNavItem("account", Icons.Default.Person, "Account")
    object Chat : BottomNavItem("chatlist", Icons.Default.Chat, "Chat")
}

@Composable
fun AppBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Reviews,
        BottomNavItem.Account,
        BottomNavItem.Chat,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(100.dp)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) Constants.hubGreen else Color(0xFF4285F4),
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = if (currentRoute == item.route) Constants.hubGreen else Color(0xFF4285F4)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Constants.hubGreen,
                    unselectedIconColor = Color(0xFF4285F4),
                    indicatorColor = Color.White
                )
            )
        }
    }
}

@Composable
fun MainScreen(loginViewModel: LoginViewModel) {
    val navController = rememberNavController()
    val authState by loginViewModel.authState.observeAsState()
    val db = remember { Firebase.firestore }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
            is AuthState.Authenticated -> {
                if (navController.currentDestination?.route == "login") {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            else -> Unit
        }
    }

    Scaffold(
        bottomBar = {
            if (authState is AuthState.Authenticated) {
                AppBottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginPage(navController = navController, loginViewModel = loginViewModel)
            }

            composable("resetpw") {
                ResetPasswordScreen(
                    modifier = Modifier,
                    navController = navController,
                    viewModel = ResetPasswordViewModel()
                )
            }

            composable("home") {
                HomeView(navController = navController, loginViewModel = loginViewModel)
            }

            composable("courses") {
                CoursesScreen(
                    viewModel = CoursesViewModel(),
                    onNavigateBack = { navController.navigateUp() },
                    onCourseClick = { course ->
                        navController.navigate("course_detail/${course.Identifier}")
                    }
                )
            }

            composable(
                "course_detail/{courseCode}",
                arguments = listOf(navArgument("courseCode") { type = NavType.StringType })
            ) { backStackEntry ->
                val courseCode = backStackEntry.arguments?.getString("courseCode") ?: return@composable
                val viewModel = remember { CourseDetailViewModel() }
                
                CourseDetailScreen(
                    viewModel = viewModel,
                    courseCode = courseCode,
                    onBackClick = { 
                        navController.popBackStack()
                    },
                    onChatClick = { 
                        viewModel.joinOrCreateCourseChat(courseCode) { chatId ->
                            navController.navigate("chat/$chatId")
                        }
                    }
                )
            }

            composable("groups") {
                GroupsScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onGroupClick = { groupId ->
                        navController.navigate("chat/$groupId")
                    }
                )
            }

            composable("chatlist") {
                ChatListScreen(
                    onChatClick = { chatId ->
                        navController.navigate("chat/$chatId")
                    }
                )
            }

            composable(
                "chat/{chatId}",
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
                ChatScreen(
                    chatId = chatId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("reviews") {
                ReviewScreen(
                    navController = navController
                )
            }

            composable("account") {
                AccountScreen(
                    onNavigateBack = { navController.navigateUp() },
                    loginViewModel = loginViewModel,
                    onSignOut = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }
            composable("coursesReview") {
                ReviewCoursesScreen(
                    navController = navController,
                    onNavigateBack = { navController.navigateUp() }
                )
            }

            composable(
                "coursesReview/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt("courseId") ?: return@composable
                val course = when (courseId) {
                    1 -> dummyCourse1
                    2 -> dummyCourse2
                    else -> return@composable
                }
                
                CourseDetailsScreen(
                    course = course,
                    onNavigateBack = { navController.navigateUp() },
                    onRateCourseClick = { /* Rate işlemi */ }
                )
            }

            composable("clubs") {
                val viewModel: ClubsViewModel = viewModel()
                val selectedTab by viewModel.selectedTab.collectAsState()

                ClubsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { 
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    onClubClick = { club ->
                        when (selectedTab) {
                            ClubTab.MY_CLUBS -> {
                                navController.navigate("chat/${club.chatId}")
                            }
                            ClubTab.ALL_CLUBS -> {
                                navController.navigate("club_detail/${club.clubId}")
                            }
                        }
                    }
                )
            }

            composable(
                "club_detail/{clubId}",
                arguments = listOf(navArgument("clubId") { type = NavType.StringType })
            ) { backStackEntry ->
                val clubId = backStackEntry.arguments?.getString("clubId") ?: return@composable
                val viewModel = remember { ClubDetailViewModel() }
                
                LaunchedEffect(clubId) {
                    try {
                        val club = db.collection("clubs")
                            .document(clubId)
                            .get()
                            .await()
                            
                        viewModel.updateClubDetails(
                            clubName = club.getString("name") ?: "",
                            clubDescription = club.getString("description") ?: "",
                            clubIcon = club.getString("icon") ?: "default",
                            memberCount = (club.get("members") as? List<*>)?.size ?: 0
                        )
                    } catch (e: Exception) {
                        println("Hata: ${e.message}")
                    }
                }
                ClubDetailScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() },
                    onJoinClubClick = {
                        navController.navigate("chatlist") {
                            popUpTo("chatlist") { inclusive = true }
                        }
                    },
                    onJoinError = { errorMessage: String ->  
                        println("Hata: $errorMessage")
                    }
                )
            }

            composable(
                "teacher_details/{teacherId}",
                arguments = listOf(navArgument("teacherId") { type = NavType.IntType })
            ) { backStackEntry ->
                val teacherId = backStackEntry.arguments?.getInt("teacherId") ?: return@composable
                val teacher = when (teacherId) {
                    1 -> dummyTeacher1
                    2 -> dummyTeacher2
                    else -> return@composable
                }
                
                TeacherDetailsScreen(
                    teacher = teacher,
                    onNavigateBack = { navController.navigateUp() },
                    onRateTeacherClick = { /* Rate işlemi */ }
                )
            }

            composable("help") {
                HelpScreen(
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}//s