package com.example.ecommerce

import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.usecase.cart.ClearCartUseCase
import com.example.ecommerce.domain.usecase.cart.DeleteCartUseCase
import com.example.ecommerce.domain.usecase.cart.GetCartsUseCase
import com.example.ecommerce.domain.usecase.cart.SaveCartUseCase
import com.example.ecommerce.domain.usecase.cart.UpdateProductQuantityUseCase
import com.example.ecommerce.presentation.cart.CartState
import com.example.ecommerce.presentation.cart.CartViewModel
import com.example.ecommerce.util.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @MockK
    val clearCartUseCase = mockk<ClearCartUseCase>(relaxed = true)

    @MockK
    val deleteCartUseCase = mockk<DeleteCartUseCase>(relaxed = true)

    @MockK
    val getCartsUseCase = mockk<GetCartsUseCase>(relaxed = true)

    @MockK
    val saveCartUseCase = mockk<SaveCartUseCase>(relaxed = true)

    @MockK
    val updateProductQuantityUseCase = mockk<UpdateProductQuantityUseCase>(relaxed = true)

    private lateinit var viewModel: CartViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel =  CartViewModel(
            clearCartUseCase = clearCartUseCase,
            deleteCartUseCase = deleteCartUseCase,
            getCartsUseCase = getCartsUseCase,
            saveCartUseCase = saveCartUseCase,
            updateProductQuantityUseCase = updateProductQuantityUseCase)
    }

    @Test
    fun `test getCart success`() = runBlocking {
        val expectedCart = listOf(
            Product(
                id = 1,
                name = "Bentley Focus",
                price = "51.00",
                quantity = 1
            ),
            Product(
                id = 1,
                name = "Aston Martin Durango",
                price = "374.00",
                quantity = 1
            ),
        )

        val successResult = Result.Success(expectedCart)

        coEvery { getCartsUseCase.invoke() } returns flowOf(successResult)

        viewModel.getCartList()

        val cartState = viewModel.cartState.value
        assertTrue(cartState is CartState)
        assertEquals(expectedCart, cartState?.products)
    }

    @Test
    fun `test getCart loading`() = runBlocking {
        val loadingResult = Result.Loading

        coEvery { getCartsUseCase.invoke() } returns flowOf(loadingResult)

        viewModel.getCartList()

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == true)
        cartState?.products?.isEmpty()?.let { assertTrue(it) }
        assertEquals(null, cartState?.error)
    }

    @Test
    fun `test getCart error`() = runBlocking {
        val errorResult = Result.Error("An error occurred")

        coEvery { getCartsUseCase.invoke() } returns flowOf(errorResult)

        viewModel.getCartList()

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == false)
        cartState?.products?.isEmpty()?.let { assertTrue(it) }
        assertEquals("An error occurred", cartState?.error)
    }
//-------------------------------------------------------
    @Test
    fun `test clearCart success`() = runBlocking {
        val successResult = Result.Success(Unit)

        coEvery { clearCartUseCase.invoke() } returns flowOf(successResult)

        viewModel.clearCart()

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == false)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals(null, cartState?.error)
    }

    @Test
    fun `test clearCart loading`() = runBlocking {
        val loadingResult = Result.Loading

        coEvery { clearCartUseCase.invoke() } returns flowOf(loadingResult)

        viewModel.clearCart()

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == true)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals(null, cartState?.error)
    }

    @Test
    fun `test clearCart error`() = runBlocking {
        val errorResult = Result.Error("An error occurred")

        coEvery { clearCartUseCase.invoke() } returns flowOf(errorResult)

        viewModel.clearCart()

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == false)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals("An error occurred", cartState?.error)
    }

    @Test
    fun `test deleteCart success`() = runBlocking {
        val expectedCart =
            Product(
                id = 1,
                name = "Bentley Focus",
                price = "51.00",
                quantity = 1
            )
        val successResult = Result.Success(Unit)

        coEvery { deleteCartUseCase.invoke(any()) } returns flowOf(successResult)

        viewModel.deleteCart(expectedCart)

        val cartState = viewModel.cartState.value
        cartState?.products?.isEmpty()?.let { assertTrue(it) }
        assertEquals(null, cartState?.error)
    }

    @Test
    fun `test deleteCart loading`() = runBlocking {
        val expectedCart =
            Product(
                id = 1,
                name = "Bentley Focus",
                price = "51.00",
                quantity = 1
            )
        val loadingResult = Result.Loading

        coEvery { deleteCartUseCase.invoke(any()) } returns flowOf(loadingResult)

        viewModel.deleteCart(expectedCart)

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == true)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals(null, cartState?.error)
    }

    @Test
    fun `test deleteCart error`() = runBlocking {
        val expectedCart =
            Product(
                id = 1,
                name = "Bentley Focus",
                price = "51.00",
                quantity = 1
            )
        val errorResult = Result.Error("An error occurred")

        coEvery { deleteCartUseCase.invoke(any()) } returns flowOf(errorResult)

        viewModel.deleteCart(expectedCart)

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == false)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals("An error occurred", cartState?.error)
    }


    @Test
    fun `test saveCart success`() = runBlocking {
        val expectedCart =
            Product(
                id = 2,
                name = "Bentley Focus2",
                price = "51.00",
                quantity = 1
            )
        val successResult = Result.Success(Unit)

        coEvery { saveCartUseCase.invoke(any()) } returns flowOf(successResult)

        viewModel.saveCart(expectedCart)

        val cartState = viewModel.cartState.value
        //assertTrue(cartState?.isLoading == false)
        cartState?.products?.isEmpty()?.let { assertTrue(it) }
        assertEquals(null, cartState?.error)
    }
    @Test
    fun `test saveCart loading`() = runBlocking {
        val expectedCart =
            Product(
                id = 1,
                name = "Bentley Focus",
                price = "51.00",
                quantity = 1
            )
        val loadingResult = Result.Loading

        coEvery { saveCartUseCase.invoke(any()) } returns flowOf(loadingResult)

        viewModel.saveCart(expectedCart)

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == true)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals(null, cartState?.error)
    }
    @Test
    fun `test saveCart error`() = runBlocking {
        val expectedCart =
            Product(
                id = 1,
                name = "Bentley Focus",
                price = "51.00",
                quantity = 1
            )
        val errorResult = Result.Error("An error occurred")

        coEvery { saveCartUseCase.invoke(any()) } returns flowOf(errorResult)

        viewModel.saveCart(expectedCart)

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == false)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals("An error occurred", cartState?.error)
    }
    @Test
    fun `test updateProductQuantity success`() = runBlocking {
        val successResult = Result.Success(Unit)

        coEvery { updateProductQuantityUseCase.invoke(any(), any()) } returns flowOf(successResult)

        viewModel.updateQuantity(1, 2)

        val cartState = viewModel.cartState.value
        assertEquals(null, cartState?.error)
    }

    @Test
    fun `test updateProductQuantity loading`() = runBlocking {
        val loadingResult = Result.Loading

        coEvery { updateProductQuantityUseCase.invoke(any(), any()) } returns flowOf(loadingResult)

        viewModel.updateQuantity(1, 2)

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == true)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals(null, cartState?.error)
    }


    @Test
    fun `test updateProductQuantity error`() = runBlocking {
        val errorResult = Result.Error("An error occurred")

        coEvery { updateProductQuantityUseCase.invoke(any(), any()) } returns flowOf(errorResult)

        viewModel.updateQuantity(1, 2)

        val cartState = viewModel.cartState.value
        assertTrue(cartState?.isLoading == false)
        assertTrue(cartState?.products?.isEmpty() == true)
        assertEquals("An error occurred", cartState?.error)
    }
    @After
    fun teardown() {
        // Clears all mock implementations
        unmockkAll()
    }
}

