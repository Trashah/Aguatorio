package com.example.myapplication

import RegisterScreen
import androidx.compose.ui.platform.LocalContext
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.NotificationUtils
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.FirebaseApp
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.work.Worker
import androidx.work.WorkerParameters


class MainActivity : FragmentActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        NotificationUtils.scheduleNotifications(this)
        FirebaseApp.initializeApp(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

        // Configurar autenticación biométrica
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Si la autenticación fue exitosa, seguimos con la app
                    setMainContent()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Opcional: cerrar la app o mostrar error
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Falló la autenticación, puede volver a intentar
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación requerida")
            .setSubtitle("Utiliza tu huella o contraseña del dispositivo para entrar")
            .setNegativeButtonText("Usar contraseña")
            .build()

        // Verificar si el dispositivo soporta biometría
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                // Si no soporta biometría, simplemente entra a la app
                setMainContent()
            }
        }
    }

    private fun setMainContent() {
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AguatorioApp()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "aguatorio_channel",
                "Recordatorios de Agua",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para recordar tomar agua"
                enableVibration(true) // Habilitar vibración
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}

class HydrationReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        sendNotification()
        vibratePhone() // DIEGO SE LA COME
        return Result.success()
    }

    private fun sendNotification() {
        val messages = listOf(
            "¡Recuerda tomar agua para mantenerte hidratado! 💧",
            "Tu cuerpo necesita agua para funcionar bien. ¡Bebe un poco ahora! 🚰",
            "Un vaso de agua puede hacer la diferencia. ¡Bebe uno ahora! 💙",
            "Mantente fresco y saludable con un sorbo de agua. ¡Hazlo ahora! 🏞️"
        )

        val message = messages.random()

        val builder = NotificationCompat.Builder(applicationContext, "aguatorio_channel")
            .setSmallIcon(R.drawable.ic_water)
            .setContentTitle("¡Hora de hidratarse!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), builder.build())
        }
    }

    private fun vibratePhone() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(500) // Para versiones antiguas de Android
            }
        }
    }

}

fun scheduleNotifications(context: Context) {
    // Verificar si las notificaciones están habilitadas
    val sharedPrefs = context.getSharedPreferences("aguatorio_prefs", Context.MODE_PRIVATE)
    val notificationsEnabled = sharedPrefs.getBoolean("notifications_enabled", true)

    if (!notificationsEnabled) {
        return // No programar notificaciones si están deshabilitadas
    }

    val workManager = WorkManager.getInstance(context)

    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<HydrationReminderWorker>(1, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    // Programamos el nuevo trabajo usando UPDATE en lugar de REPLACE
    workManager.enqueueUniquePeriodicWork(
        "HydrationReminder",
        ExistingPeriodicWorkPolicy.UPDATE,
        workRequest
    )

    // Forzamos una notificación inicial para comprobar que funciona
    val immediateWorkRequest = OneTimeWorkRequestBuilder<HydrationReminderWorker>().build()
    workManager.enqueue(immediateWorkRequest)
}

@Composable
fun AguatorioApp() {
    val navController = rememberNavController()

    // 🔹 AQUÍ agregas esto:
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("aguatorio_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Main.route else Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onRegisterClick = {
                    navController.navigate(Screen.InitialQuestions1.route)
                }
            )
        }

        composable(Screen.InitialQuestions1.route) {
            InitialQuestions1Screen(
                onBackClick = {
                    navController.navigateUp()
                },
                onNextClick = {
                    navController.navigate(Screen.InitialQuestions2.route)
                }
            )
        }

        composable(Screen.InitialQuestions2.route) {
            InitialQuestions2Screen(
                onBackClick = {
                    navController.navigateUp()
                },
                onNextClick = {
                    navController.navigate(Screen.InitialQuestions3.route)
                }
            )
        }

        composable(Screen.InitialQuestions3.route) {
            InitialQuestions3Screen(
                onBackClick = {
                    navController.navigateUp()
                },
                onFinishClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(navController)
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(navController)
        }

        composable(Screen.Trophies.route) {
            TrophiesScreen(navController)
        }

        composable(Screen.Activities.route) {
            ActivitiesScreen(navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
