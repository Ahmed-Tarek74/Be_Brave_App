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
import com.compose.chatapp.presentation.viewModels.HomeViewModel
import com.compose.chatapp.presentation.views.composeScreens.HomeScreen
import com.compose.domain.entities.User
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
                    dateFormatter = homeViewModel::formatDate,
                    viewState = homeViewModel.viewState.collectAsState()
                )
            }
        }
    }

    private fun observeViewModelEvents() {
        lifecycleScope.launch {
            homeViewModel.event.collectLatest { event ->
                if (event !is NavigationEvent.NavigateToChattingScreen) {
                    if (event is NavigationEvent.NavigateToLoginScreen) navigateToLoginScreen()
                    else if (event is NavigationEvent.NavigateToSearchScreen) navigateToSearchScreen(
                        event.homeUser
                    )
                } else navigateToChattingScreen(
                    homeUser = event.homeUser,
                    awayUser = event.awayUser
                )
            }
        }
    }

    private fun navigateToLoginScreen() {
        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToSearchScreen(homeUser: User) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchUsersFragment(homeUser)
        findNavController().navigate(action)
    }

    private fun navigateToChattingScreen(homeUser: User, awayUser: User) {
        val action = HomeFragmentDirections.actionHomeFragmentToChattingFragment(
            homeUser = homeUser,
            awayUser = awayUser
        )
        findNavController().navigate(action)
    }
}