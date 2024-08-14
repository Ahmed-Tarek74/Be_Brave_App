package com.compose.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.compose.presentation.R
import com.compose.presentation.events.RegistrationEvent.*
import com.compose.presentation.viewModels.RegistrationViewModel
import com.compose.presentation.views.composeScreens.RegistrationScreen
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
        return ComposeView(requireContext()).apply {
            setContent {
                RegistrationScreen(
                    viewState = registrationViewModel.state.collectAsState(),
                    setIntent = registrationViewModel::setIntent
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnViewModelEvents()
    }


    private fun observeOnViewModelEvents() {
        lifecycleScope.launch {
            registrationViewModel.event.collectLatest { event ->
                when (event) {
                    RegisterSuccessfully -> {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.registerSuccessfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToLogin()
                    }
                    backToLogin -> navigateToLogin()
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