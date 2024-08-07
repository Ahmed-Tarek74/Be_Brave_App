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
import com.compose.presentation.events.LoginEvent
import com.compose.presentation.views.composeScreens.LoginScreen
import com.compose.presentation.viewModels.LoginViewModel
import com.compose.presentation.models.UserUiModel
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
                when(event){
                    is LoginEvent.LoginSuccess ->  navigateToHomeFragment(event.homeUser)
                    is LoginEvent.NavigateToRegistration -> navigateToRegistration()
                }
            }
        }
    }
    private fun navigateToRegistration() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        findNavController().navigate(action)
    }

    private fun navigateToHomeFragment(homeUser: UserUiModel) {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(homeUser =homeUser )
        findNavController().navigate(action)
    }
}