package ru.tech.kastybiy.domain.use_case.cuisine.update_fridge

import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class UpdateFridgeUseCase @Inject constructor(
    private val repository: KastybiyRepository
) {

    suspend operator fun invoke(id: Int, inFridge: Boolean) {
        repository.updateProduct(id, inFridge)
    }

}