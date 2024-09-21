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
import com.compose.presentation.events.SearchUsersEvent.*
import com.compose.presentation.viewModels.SearchUsersViewModel
import com.compose.presentation.views.composeScreens.SearchUsersScreen
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchUsersFragment : Fragment() {
    private val viewModel: SearchUsersViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        observeViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                ChatAppTheme {
                    SearchUsersScreen(
                        viewState = viewModel.viewState.collectAsState(),
                        setIntent = viewModel::setIntent
                    )
                }
            }
        }
    }

    private fun observeViewModelEvents() {
        lifecycleScope.launch {
            viewModel.event.collectLatest { event ->
                when (event) {
                    is BackToHome -> navigateToHomeScreen(event.homeUser)
                    is UserSelected -> navigateToChattingScreen(
                        homeUser = event.homeUser,
                        awayUser = event.awayUser
                    )
                }
            }
        }
    }

    private fun navigateToChattingScreen(homeUser: UserUiModel, awayUser: UserUiModel) {
        val action = SearchUsersFragmentDirections.actionSearchUsersFragmentToChattingFragment(
            homeUser = homeUser,
            awayUser = awayUser
        )
        findNavController().navigate(action)
    }

    private fun navigateToHomeScreen(homeUser: UserUiModel) {
        val action = SearchUsersFragmentDirections.actionSearchUsersFragmentToHomeFragment(homeUser)
        findNavController().navigate(action)
    }
}