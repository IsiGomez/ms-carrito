package cl.duoc.carrito.mapper;

import cl.duoc.carrito.dto.response.CartItemResponseDto;
import cl.duoc.carrito.model.CartItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-16T17:19:51-0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Microsoft)"
)
@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItem toEntity(CartItemRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        cartItem.setProductId( dto.getProductId() );
        cartItem.setProductName( dto.getProductName() );
        cartItem.setUnitPrice( dto.getUnitPrice() );
        cartItem.setQuantity( dto.getQuantity() );

        return cartItem;
    }

    @Override
    public CartItemResponseDto toDto(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();

        cartItemResponseDto.setId( cartItem.getId() );
        cartItemResponseDto.setProductId( cartItem.getProductId() );
        cartItemResponseDto.setProductName( cartItem.getProductName() );
        cartItemResponseDto.setUnitPrice( cartItem.getUnitPrice() );
        cartItemResponseDto.setQuantity( cartItem.getQuantity() );
        cartItemResponseDto.setTotal( cartItem.getTotal() );

        return cartItemResponseDto;
    }

    @Override
    public List<CartItemResponseDto> toDtoList(List<CartItem> cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        List<CartItemResponseDto> list = new ArrayList<CartItemResponseDto>( cartItems.size() );
        for ( CartItem cartItem : cartItems ) {
            list.add( toDto( cartItem ) );
        }

        return list;
    }
}
