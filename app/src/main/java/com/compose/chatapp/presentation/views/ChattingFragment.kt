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
import androidx.navigation.fragment.navArgs
import com.compose.chatapp.presentation.events.NavigationEvent.*
import com.compose.chatapp.presentation.viewModels.ChatViewModel
import com.compose.chatapp.presentation.views.composeScreens.ChatScreen
import com.compose.domain.entities.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChattingFragment : Fragment() {
    private val args: ChattingFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                ChatScreen(
                    awayUser = args.awayUser,
                    setIntent = viewModel::setIntent,
                    dateFormatter = viewModel::formatDate,
                    viewState = viewModel.viewState.collectAsState()
                )
            }
        }
    }

    private fun observeViewModelEvents() {
        lifecycleScope.launch {
            viewModel.event.collectLatest { event ->
                if (event is NavigateToHome)
                    navigateToHomeScreen(event.homeUser)
            }
        }
    }

    private fun navigateToHomeScreen(homeUser: User) {
        val action = ChattingFragmentDirections.actionChattingFragmentToHomeFragment(homeUser)
        findNavController().navigate(action)
    }

}