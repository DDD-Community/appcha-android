package com.editor.appcha.ui.feed

import com.editor.appcha.core.arch.Result
import com.editor.appcha.core.arch.usecase.invoke
import com.editor.appcha.domain.usecase.GetFeedListUseCase
import com.editor.appcha.ui.base.BaseViewModel
import com.editor.appcha.ui.base.ViewEvent
import com.editor.appcha.ui.base.ViewState
import com.editor.appcha.ui.feed.FeedViewModel.Event
import com.editor.appcha.ui.feed.FeedViewModel.State
import com.editor.appcha.ui.model.AppModel
import com.editor.appcha.ui.model.FeedModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedList: GetFeedListUseCase
) : BaseViewModel<Event, State>(State()) {

    sealed class Event : ViewEvent {
        data class NavigateToDetail(
            val id: String
        ) : Event()

        data class OpenMarket(
            val url: String
        ) : Event()
    }

    data class State(
        val feeds: List<FeedModel>? = null,
        val loading: Boolean = true,
        val error: Throwable? = null,
    ) : ViewState {
        val hasError = error != null
    }

    init {
        launch {
            val result = getFeedList()
                .map { feeds -> feeds.map { FeedModel(it) } }

            when (result) {
                is Result.Success -> updateState { State(feeds = result.value, loading = false) }
                is Result.Failure -> updateState { State(error = result.throwable) }
            }
        }
    }

    fun navigateToDetail(feed: FeedModel) {
        event(Event.NavigateToDetail(feed.id))
    }

    fun openMarket(app: AppModel) {
        val url = app.marketUrl ?: return
        event(Event.OpenMarket(url))
    }
}
