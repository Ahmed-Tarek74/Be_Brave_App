package com.compose.chatapp.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.compose.chatapp.presentation.events.NavigationEvent
import com.compose.chatapp.presentation.viewModels.RegistrationViewModel
import com.compose.chatapp.presentation.views.composeScreens.RegistrationScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val registrationViewModel: RegistrationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeOnViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                RegistrationScreen(
                   viewState = registrationViewModel.state.collectAsState(),
                    setIntent = registrationViewModel::setIntent
                )
            }
        }
    }

    private fun observeOnViewModelEvents() {
        lifecycleScope.launch {
            registrationViewModel.event.collectLatest { event ->
                if (event is NavigationEvent.NavigateToLoginScreen){
                    navigateToLogin()
                }
            }
        }
    }

    private fun navigateToLogin() {
        val action =
            RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}