package dev.ykh.triplehomework.service

import dev.ykh.triplehomework.repository.PointsRepository
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class PointsServiceTest {

    private var pointsRepository = mockk<PointsRepository>()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `한글 글자 개수`() = runBlockingTest {
        val word = "한글글자개수"
        assertEquals(word.length, 6)
    }
}