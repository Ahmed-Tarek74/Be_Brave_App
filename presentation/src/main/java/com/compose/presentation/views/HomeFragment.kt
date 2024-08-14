package com.compose.presentation.views

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
import com.compose.presentation.views.composeScreens.HomeScreen
import com.compose.presentation.events.HomeEvent.*
import com.compose.presentation.viewModels.HomeViewModel
import com.compose.presentation.models.UserUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen(
                    setIntent = homeViewModel::setIntent,
                    viewState = homeViewModel.viewState.collectAsState()
                )
            }
        }
    }

    private fun observeViewModelEvents() {
        lifecycleScope.launch {
            homeViewModel.event.collectLatest { event ->
                when (event) {
                    is ChatSelected -> navigateToChattingScreen(
                        homeUser = event.homeUser,
                        awayUser = event.awayUser
                    )
                    is LoggedOut -> navigateToLoginScreen()
                    is NavigateToSearchScreen -> navigateToSearchScreen(
                        event.homeUser
                    )
                }
            }
        }
    }
    private fun navigateToLoginScreen() {
        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToSearchScreen(homeUser: UserUiModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchUsersFragment(homeUser)
        findNavController().navigate(action)
    }

    private fun navigateToChattingScreen(homeUser: UserUiModel, awayUser: UserUiModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToChattingFragment(
            homeUser = homeUser,
            awayUser = awayUser
        )
        findNavController().navigate(action)
    }
}