package cl.duoc.carrito.service;

import cl.duoc.carrito.dto.remote.InventoryDto;
import cl.duoc.carrito.dto.remote.ProductDto;
import cl.duoc.carrito.dto.request.CartItemRequestDto;
import cl.duoc.carrito.dto.request.CartRequestDto;
import cl.duoc.carrito.dto.response.CartResponseDto;
import cl.duoc.carrito.mapper.CartMapper;
import cl.duoc.carrito.model.Cart;
import cl.duoc.carrito.model.CartItem;
import cl.duoc.carrito.repository.CartItemRepository;
import cl.duoc.carrito.repository.CartRepository;
import cl.duoc.carrito.service.apis.CatalogoClient;
import cl.duoc.carrito.service.apis.InventarioClient;
import cl.duoc.carrito.service.apis.PromocionesClient;
import cl.duoc.carrito.service.apis.PuntosClient;
import cl.duoc.carrito.service.impl.CartImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - CartImpl")
public class CartImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CatalogoClient catalogoClient;

    @Mock
    private InventarioClient inventarioClient;

    @Mock
    private PromocionesClient promocionesClient;

    @Mock
    private PuntosClient puntosClient;

    private CartImpl cartService;

    private Cart cart;
    private ProductDto product;
    private InventoryDto stock;
    private final Long userId = 1L;
    private final Long productId = 10L;

    @BeforeEach
    void setUp() {
        CartMapper mapper = new CartMapper();
        cartService = new CartImpl(
                cartRepository, cartItemRepository, catalogoClient,
                inventarioClient, promocionesClient, puntosClient, mapper);

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(userId);
        cart.setTotal(0);

        product = new ProductDto(productId, "Arroz 1kg", "Arroz grado 1", 1500, null);
        stock = new InventoryDto(1L, productId, 20);
    }


    @Test
    @DisplayName("getCart: debería crear un carrito nuevo si el usuario no tiene uno")
    void getCart_deberiaCrearCarritoNuevo_cuandoUsuarioNoTieneUno() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDto result = cartService.getCart(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getItems()).isEmpty();
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


    @Test
    @DisplayName("addItem: debería agregar el producto cuando hay stock y no estaba en el carrito")
    void addItem_deberiaAgregarProducto_cuandoHayStockYNoExisteEnCarrito() {
        CartRequestDto request = new CartRequestDto(productId, 2);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(catalogoClient.getProductById(productId)).thenReturn(product);
        when(inventarioClient.consultarStock(productId)).thenReturn(stock);
        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), productId))
                .thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDto result = cartService.addItem(userId, request);

        assertThat(result).isNotNull();
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(cart);
    }


    @Test
    @DisplayName("addItem: debería lanzar excepción cuando el producto no existe en el catálogo")
    void addItem_deberiaLanzarExcepcion_cuandoProductoNoExisteEnCatalogo() {
        CartRequestDto request = new CartRequestDto(productId, 2);

        when(catalogoClient.getProductById(productId)).thenReturn(null);

        assertThatThrownBy(() -> cartService.addItem(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no existe en el catálogo");

        verify(inventarioClient, never()).consultarStock(anyLong());
        verify(cartItemRepository, never()).save(any());
    }


    @Test
    @DisplayName("addItem: debería lanzar excepción cuando el stock es insuficiente")
    void addItem_deberiaLanzarExcepcion_cuandoStockEsInsuficiente() {
        CartRequestDto request = new CartRequestDto(productId, 50);
        InventoryDto stockBajo = new InventoryDto(1L, productId, 5);

        when(catalogoClient.getProductById(productId)).thenReturn(product);
        when(inventarioClient.consultarStock(productId)).thenReturn(stockBajo);

        assertThatThrownBy(() -> cartService.addItem(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock insuficiente");

        verify(cartItemRepository, never()).save(any());
        verify(cartRepository, never()).save(any());
    }


    @Test
    @DisplayName("addItem: debería lanzar excepción cuando el producto ya está en el carrito")
    void addItem_deberiaLanzarExcepcion_cuandoProductoYaEstaEnCarrito() {
        CartRequestDto request = new CartRequestDto(productId, 1);
        CartItem itemExistente = new CartItem();
        itemExistente.setProductId(productId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(catalogoClient.getProductById(productId)).thenReturn(product);
        when(inventarioClient.consultarStock(productId)).thenReturn(stock);
        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), productId))
                .thenReturn(Optional.of(itemExistente));

        assertThatThrownBy(() -> cartService.addItem(userId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya esta en el carrito");

        verify(cartItemRepository, never()).save(any());
    }


    @Test
    @DisplayName("updateQuantity: debería actualizar la cantidad cuando el ítem existe y hay stock")
    void updateQuantity_deberiaActualizarCantidad_cuandoItemExisteYHayStock() {
        CartItemRequestDto request = new CartItemRequestDto(5);
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(2);
        item.setSubtotal(3000);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), productId))
                .thenReturn(Optional.of(item));
        when(inventarioClient.consultarStock(productId)).thenReturn(stock);
        when(catalogoClient.getProductById(productId)).thenReturn(product);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateQuantity(userId, productId, request);

        assertThat(item.getQuantity()).isEqualTo(5);
        verify(cartItemRepository, times(1)).save(item);
    }


    @Test
    @DisplayName("updateQuantity: debería lanzar excepción cuando el producto no está en el carrito")
    void updateQuantity_deberiaLanzarExcepcion_cuandoProductoNoEstaEnCarrito() {
        CartItemRequestDto request = new CartItemRequestDto(3);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), productId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateQuantity(userId, productId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("no está en el carrito");

        verify(inventarioClient, never()).consultarStock(anyLong());
    }


    @Test
    @DisplayName("removeItem: debería eliminar el producto cuando existe en el carrito")
    void removeItem_deberiaEliminarProducto_cuandoExisteEnCarrito() {
        CartItem item = new CartItem();
        item.setProductId(productId);
        cart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), productId))
                .thenReturn(Optional.of(item));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.removeItem(userId, productId);

        assertThat(cart.getItems()).doesNotContain(item);
        verify(cartRepository, times(1)).save(cart);
    }


    @Test
    @DisplayName("removeItem: debería lanzar excepción cuando el producto no está en el carrito")
    void removeItem_deberiaLanzarExcepcion_cuandoProductoNoEstaEnCarrito() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(cart.getId(), productId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeItem(userId, productId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(cartRepository, never()).save(any());
    }


    @Test
    @DisplayName("clearCart: debería vaciar el carrito y poner el total en cero")
    void clearCart_deberiaVaciarCarritoYPonerTotalEnCero() {
        CartItem item = new CartItem();
        item.setProductId(productId);
        cart.getItems().add(item);
        cart.setTotal(1500);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.clearCart(userId);

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotal()).isZero();
        verify(cartRepository, times(1)).save(cart);
    }
}
