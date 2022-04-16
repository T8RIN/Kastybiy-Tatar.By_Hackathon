package ru.tech.kastybiy.domain.use_case.get_dish_by_id

import kotlinx.coroutines.flow.Flow
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.model.Cuisine
import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class GetDishByIdUseCase @Inject constructor(
    private val repository: KastybiyRepository
) {

    operator fun invoke(id: Int): Flow<Action<Cuisine>> {
        return repository.getDishById(id)
    }

}