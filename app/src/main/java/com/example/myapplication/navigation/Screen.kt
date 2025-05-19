package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object InitialQuestions1 : Screen("initial_questions_1")
    object InitialQuestions2 : Screen("initial_questions_2")
    object InitialQuestions3 : Screen("initial_questions_3")
    object Main : Screen("main")
    object Statistics : Screen("statistics")
    object Trophies : Screen("trophies")
    object Settings : Screen("settings")
    object Support : Screen("support")
    object Activities : Screen("activities")
    object ExerciseSelection : Screen("exercise_selection")
    object ExerciseType1 : Screen("exercise_type_1")
    object ExerciseType2 : Screen("exercise_type_2")
    object CalculationResult : Screen("calculation_result")
} 