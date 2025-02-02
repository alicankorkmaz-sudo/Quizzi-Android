package studio.astroturf.quizzi.ui.screen.create.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import studio.astroturf.quizzi.domain.repository.CategoryRepository
import studio.astroturf.quizzi.domain.result.onFailure
import studio.astroturf.quizzi.domain.result.onSuccess
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
    @Inject
    constructor(
        private val categoryRepository: CategoryRepository,
    ) : ViewModel() {
        private val _categoriesUiModel = MutableStateFlow<List<CategoryUiModel>>(emptyList())
        val categoriesUiModel = _categoriesUiModel.asStateFlow()

        init {
            getCategories()
        }

        fun getCategories() =
            viewModelScope.launch {
                categoryRepository
                    .getCategories()
                    .onSuccess { categories ->
                        _categoriesUiModel.value = categories.map { CategoryUiModel.from(it) }
                    }.onFailure { error ->
                        // TODO:
                    }
            }

        /**
         * toggles the selection of the category in the list,
         * and deselects all other categories
         */
        fun selectCategory(category: CategoryUiModel) {
            _categoriesUiModel.value =
                _categoriesUiModel.value.map {
                    it.copy(isSelected = if (it == category) !it.isSelected else false)
                }
        }
    }
