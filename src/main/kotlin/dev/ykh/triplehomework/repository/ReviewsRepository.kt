package dev.ykh.triplehomework.repository

import dev.ykh.triplehomework.domain.Points
import dev.ykh.triplehomework.domain.Reviews
import dev.ykh.triplehomework.domain.common.EventDetailType
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewsRepository: CoroutineCrudRepository<Reviews, String> {

}