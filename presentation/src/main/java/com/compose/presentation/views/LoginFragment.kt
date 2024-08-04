package com.compose.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.compose.presentation.views.composeScreens.LoginScreen
import com.compose.presentation.events.NavigationEvent.*
import com.compose.presentation.viewModels.LoginViewModel
import com.compose.domain.entities.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                LoginScreen(
                    viewState = loginViewModel.viewState.collectAsState(),
                    setIntent = loginViewModel::setIntent,
                )
            }
        }
    }

    private fun observeViewModelEvents() {
        lifecycleScope.launch {
            loginViewModel.event.collectLatest { event ->
                if (event is NavigateToHome) {
                    navigateToHomeFragment(event.homeUser)
                }
                else if (event is NavigateToRegistration) navigateToRegistration()
            }
        }
    }

    private fun navigateToRegistration() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        findNavController().navigate(action)
    }

    private fun navigateToHomeFragment(homeUser: User) {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(homeUser =homeUser )
        findNavController().navigate(action)
    }
}