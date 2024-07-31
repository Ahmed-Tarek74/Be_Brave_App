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
import com.compose.chatapp.presentation.viewModels.SearchUsersViewModel
import com.compose.chatapp.presentation.views.composeScreens.SearchUsersScreen
import com.compose.domain.entities.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUsersFragment : Fragment() {
    private val viewModel: SearchUsersViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                SearchUsersScreen(
                    viewState = viewModel.viewState.collectAsState(),
                    setIntent = viewModel::setIntent
                )
            }
        }
    }

    private fun observeViewModelEvents() {
        lifecycleScope.launch {
            viewModel.event.collectLatest { event ->
                if (event is NavigationEvent.NavigateToChattingScreen) navigateToChattingScreen(
                    homeUser = event.homeUser,
                    awayUser = event.awayUser
                )
                else if (event is NavigationEvent.NavigateToHome) {
                    navigateToHomeScreen(event.homeUser)
                }
            }
        }
    }

    private fun navigateToChattingScreen(homeUser: User, awayUser: User) {
        val action = SearchUsersFragmentDirections.actionSearchUsersFragmentToChattingFragment(
            homeUser = homeUser,
            awayUser = awayUser
        )
        findNavController().navigate(action)
    }

    private fun navigateToHomeScreen(homeUser:User) {
        val action = SearchUsersFragmentDirections.actionSearchUsersFragmentToHomeFragment(homeUser)
        findNavController().navigate(action)
    }
}