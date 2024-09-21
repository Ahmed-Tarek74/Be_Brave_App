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
import androidx.navigation.fragment.navArgs
import com.compose.presentation.events.ChattingEvent.*
import com.compose.presentation.views.composeScreens.ChatScreen
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.ui.theme.ChatAppTheme
import com.compose.presentation.viewModels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChattingFragment : Fragment() {
    private val args: ChattingFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        observeViewModelEvents()
        return ComposeView(requireContext()).apply {
            setContent {
                ChatAppTheme {
                    ChatScreen(
                        awayUser = args.awayUser,
                        setIntent = viewModel::setIntent,
                        viewState = viewModel.viewState.collectAsState()
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
                }
            }
        }
    }

    private fun navigateToHomeScreen(homeUser: UserUiModel) {
        val action = ChattingFragmentDirections.actionChattingFragmentToHomeFragment(homeUser)
        findNavController().navigate(action)
    }


}