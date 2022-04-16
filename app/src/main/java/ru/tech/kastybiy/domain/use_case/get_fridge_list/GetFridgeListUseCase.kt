package ru.tech.kastybiy.domain.use_case.get_fridge_list

import kotlinx.coroutines.flow.Flow
import ru.tech.kastybiy.core.Action
import ru.tech.kastybiy.domain.model.Product
import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Inject

class GetFridgeListUseCase @Inject constructor(
    private val repository: KastybiyRepository
) {

    operator fun invoke(): Flow<Action<List<Product>>> {
        return repository.getFridgeList()
    }

}