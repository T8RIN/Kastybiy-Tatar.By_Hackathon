package ru.tech.kastybiy.domain.use_case.cuisine.get_cuisine

import kotlinx.coroutines.flow.Flow
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.model.Cuisine
import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class GetCuisineUseCase @Inject constructor(
    private val repository: KastybiyRepository
) {

    operator fun invoke(): Flow<Action<List<Cuisine>>> {
        return repository.getCuisineList()
    }

}