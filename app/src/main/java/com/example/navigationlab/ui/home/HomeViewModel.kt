package com.example.navigationlab.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.navigationlab.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        observeSearch()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    loadForQuery(query)
                }
        }
    }

    private suspend fun loadForQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        try {
            val students = if (query.isBlank()) {
                repository.getStudents()
            } else {
                repository.searchStudents(query.trim())
            }
            _uiState.value = HomeUiState(students = students)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.value = HomeUiState(
                errorMessage = e.message ?: "Gagal mengambil data"
            )
        }
    }

    fun retry() {
        val query = searchQuery.value
        viewModelScope.launch {
            loadForQuery(query)
        }
    }

    // Tugas Praktikum: refresh mengikuti query yang sedang aktif.
    fun refresh() {
        val query = searchQuery.value
        viewModelScope.launch {
            loadForQuery(query)
        }
    }

    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                        return HomeViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
