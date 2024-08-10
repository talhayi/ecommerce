import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.ecommerce.TestCoroutineRule
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.usecase.products.GetProductsUseCase
import com.example.ecommerce.presentation.adapter.ProductAdapter.Companion.diffCallBack
import com.example.ecommerce.presentation.filter.FilterOptions
import com.example.ecommerce.presentation.products.HomeViewModel
import com.example.ecommerce.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @MockK
    val getProductsUseCase = mockk<GetProductsUseCase>(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(getProductsUseCase)
    }

    @Test
    fun `test getProducts success`() = runBlocking {
        val expectedProduct = Product(
            id = 1,
            createdAt = "2023-07-17T07:21:02.529Z",
            name = "Bentley Focus",
            image = "https://loremflickr.com/640/480/food",
            price = "51.00",
            description = "Product description here",
            model = "CTS",
            brand = "Lamborghini",
            quantity = 1
        )

        val pagingData = PagingData.from(listOf(expectedProduct))

        coEvery { getProductsUseCase.executeGetProducts(any()) } returns flowOf(Result.Success(pagingData))

        viewModel.getProducts()

        val productState = viewModel.products.first()
        val productsList = collectData(productState.products)

        assertEquals(listOf(expectedProduct), productsList)
        assertTrue(productState.isLoading.not())
        assertTrue(productState.error == null)
    }

    @Test
    fun `test getProducts returns empty list`() = runBlocking {
        val pagingData = PagingData.empty<Product>()

        coEvery { getProductsUseCase.executeGetProducts(any()) } returns flowOf(Result.Success(pagingData))

        viewModel.getProducts()

        val productState = viewModel.products.first()
        val productsList = collectData(productState.products)

        assertEquals(emptyList<Product>(), productsList)
        assertTrue(productState.isLoading.not())
        assertTrue(productState.error == null)
    }

    @Test
    fun `test getProducts loading state`() = runBlocking {

        coEvery { getProductsUseCase.executeGetProducts(any()) } returns flowOf(Result.Loading)

        viewModel.getProducts()

        val productState = viewModel.products.first()
        val actualProducts = collectData(productState.products)

        assertTrue(productState.isLoading)
        assertTrue(productState.error == null)
        assertTrue(actualProducts.isEmpty())
    }

    @Test
    fun `test getProducts error`() = runBlocking {
        val errorMessage = "An unexpected error occurred"
        coEvery { getProductsUseCase.executeGetProducts(any()) } returns flowOf(Result.Error(errorMessage))

        viewModel.getProducts()

        val productState = viewModel.products.first()
        val actualProducts = collectData(productState.products)
        assertFalse(productState.isLoading)
        assertTrue(productState.error != null)
        assertEquals(errorMessage, productState.error)
        assertTrue(actualProducts.isEmpty())
    }

    @Test
    fun `test setFilterOptions calls getProducts`() = runBlocking {
        val filterOptions = FilterOptions()
        val pagingData = PagingData.from(listOf<Product>())

        coEvery { getProductsUseCase.executeGetProducts(filterOptions) } returns flowOf(Result.Success(pagingData))

        viewModel.setFilterOptions(filterOptions)

        coVerify { getProductsUseCase.executeGetProducts(filterOptions) }

        val productState = viewModel.products.first()
        assertFalse(productState.isLoading)
        assertTrue(productState.error == null)
        assertEquals(pagingData, productState.products)
    }


    @Test
    fun `test loading state when filterOptions changed`() = runBlocking {
        val filterOptions = FilterOptions()

        coEvery { getProductsUseCase.executeGetProducts(filterOptions) } returns flow {
            emit(Result.Loading)
        }

        viewModel.setFilterOptions(filterOptions)

        val productState = viewModel.products.first()
        assertTrue(productState.isLoading)
        assertTrue(productState.error == null)
    }


    @After
    fun teardown() {
        unmockkAll()
    }

    // Function used to collect items from the PagingData
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun collectData(pagingData: PagingData<Product>): List<Product> {
        val differ = AsyncPagingDataDiffer(
            diffCallback = diffCallBack,
            updateCallback = NOOP_LIST_UPDATE_CALLBACK,
            mainDispatcher = coroutineRule.testCoroutineDispatcher,
            workerDispatcher = coroutineRule.testCoroutineDispatcher
        )

        differ.submitData(pagingData)
        return differ.snapshot().items
    }

    private val NOOP_LIST_UPDATE_CALLBACK = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }
}
