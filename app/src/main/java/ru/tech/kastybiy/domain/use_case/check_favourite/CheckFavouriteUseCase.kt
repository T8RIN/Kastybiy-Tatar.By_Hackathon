package ru.tech.kastybiy.domain.use_case.check_favourite

import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class CheckFavouriteUseCase @Inject constructor(
    private val repository: KastybiyRepository
) {

    suspend operator fun invoke(id: Int): Boolean {
        return repository.checkFavoriteId(id)
    }

}