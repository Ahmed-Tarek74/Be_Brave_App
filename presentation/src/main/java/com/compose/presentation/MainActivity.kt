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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.compose.presentation.events.UserCacheEvent.*
import com.compose.presentation.intents.NotificationPermissionCommand
import com.compose.presentation.intents.NotificationPermissionCommand.*
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        mainViewModel.onNotificationPermissionResult(isGranted)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        observeViewModel()
        mainViewModel.checkNotificationPermission()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.userLoadedEvent.collect { event ->
                when (event) {
                    is UserLoaded -> setupStartDestination(event.isUserLoggedIn, event.user)
                    is UserLoadFailed -> {
                        onLoadingCachedUserFailure()
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            mainViewModel.notificationPermissionCommand.collect { notificationPermissionCommand ->
                handleNotificationPermissionCommand(notificationPermissionCommand)
            }
        }
    }

    private fun setupStartDestination(isUserLoggedIn: Boolean = false, user: UserUiModel? = null) {
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        val startDestination = if (isUserLoggedIn) R.id.homeFragment else R.id.loginFragment
        graph.setStartDestination(startDestination)
        val bundle = createBundle(user)
        navController.setGraph(graph, bundle)
    }

    private fun createBundle(userUiModel: UserUiModel?): Bundle? {
        return userUiModel?.let {
            Bundle().apply {
                putSerializable("homeUser", it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleNotificationPermissionCommand(command: NotificationPermissionCommand?) {
        when (command) {
            Request -> requestNotificationPermission()
            ShowDenied -> showNotificationPermissionDenied()
            ShowRationale -> showPermissionRationaleDialog()
            else -> Unit
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                isPermissionGranted() -> mainViewModel.onNotificationPermissionResult(true)
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> mainViewModel.onShowPermissionRationale()
                else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            mainViewModel.onNotificationPermissionResult(true)
        }
    }

    private fun onLoadingCachedUserFailure() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.signIn))
            .setMessage(getString(R.string.saved_user_failure_msg))
            .setPositiveButton(getString(R.string.accept)) { _, _ -> setupStartDestination() }
            .show()
    }
    private fun showNotificationPermissionDenied() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.permission_denied_message))
            .setPositiveButton(getString(R.string.accept), null)
            .show()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notification_permission_required))
            .setMessage(getString(R.string.notification_permission_required_msg))
            .setPositiveButton(getString(R.string.accept)) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton(getString(R.string.reject), null)
            .show()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isPermissionGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
}
