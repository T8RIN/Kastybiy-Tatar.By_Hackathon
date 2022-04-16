package ru.tech.kastybiy.domain.use_case.cuisine.update_favorite

import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class UpdateFavDishUseCase @Inject constructor(
    private val repository: KastybiyRepository
) {

    suspend operator fun invoke(id: Int, fav: Boolean) {
        repository.updateFav(id, fav)
    }

}