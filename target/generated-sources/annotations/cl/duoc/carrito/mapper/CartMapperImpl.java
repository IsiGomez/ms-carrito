package cl.duoc.carrito.mapper;

import cl.duoc.carrito.dto.request.CartRequestDto;
import cl.duoc.carrito.dto.response.CartResponseDto;
import cl.duoc.carrito.model.Cart;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-16T17:19:51-0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Microsoft)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public Cart toEntity(CartRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setCustomerId( dto.getCustomerId() );

        return cart;
    }

    @Override
    public CartResponseDto toDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartResponseDto cartResponseDto = new CartResponseDto();

        cartResponseDto.setId( cart.getId() );
        cartResponseDto.setCustomerId( cart.getCustomerId() );
        cartResponseDto.setItems( cartItemMapper.toDtoList( cart.getItems() ) );
        cartResponseDto.setTotal( cart.getTotal() );

        return cartResponseDto;
    }

    @Override
    public List<CartResponseDto> toDtoList(List<Cart> carts) {
        if ( carts == null ) {
            return null;
        }

        List<CartResponseDto> list = new ArrayList<CartResponseDto>( carts.size() );
        for ( Cart cart : carts ) {
            list.add( toDto( cart ) );
        }

        return list;
    }
}
