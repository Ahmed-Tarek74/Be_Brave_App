package com.compose.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.compose.presentation.events.StartDestinationEvent.*
import com.compose.presentation.intents.NotificationPermissionCommand.*
import com.compose.presentation.mappers.mapToUserUiModel
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        mainViewModel.onNotificationPermissionResult(isGranted)
    }

    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        lifecycleScope.launchWhenStarted {
            mainViewModel.navigationCommand.collect { command ->
                val graph = navController.navInflater.inflate(R.navigation.nav_graph)
                if (command is To) {
                    graph.setStartDestination(command.destination)
                    val bundle = createBundle(command.user?.mapToUserUiModel())
                    navController.setGraph(graph, bundle)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            mainViewModel.notificationPermissionCommand.collect { command ->
                when (command) {
                    Request -> requestNotificationPermission()
                    ShowDenied -> showNotificationPermissionDenied()
                    ShowRationale -> showPermissionRationaleDialog()
                    else -> Unit
                }
            }
        }

        mainViewModel.checkNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                mainViewModel.onNotificationPermissionResult(true)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                mainViewModel.onShowPermissionRationale()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            mainViewModel.onNotificationPermissionResult(true)
        }
    }

    private fun showNotificationPermissionDenied() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Notifications are disabled for this app. You can enable them in the app settings.")
            .setPositiveButton("OK", null)
            .show()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Required")
            .setMessage("This app requires notification permission to send you messages from other users. Please grant the permission.")
            .setPositiveButton("OK") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("No thanks", null)
            .show()
    }
    private fun createBundle(userUiModel: UserUiModel?): Bundle? {
        return userUiModel?.let {
            Bundle().apply {
                putSerializable("homeUser", it)
            }
        }
    }

}
