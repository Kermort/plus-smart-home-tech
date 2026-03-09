package ru.yandex.practicum.commerce.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.cart.mapper.CartMapper;
import ru.yandex.practicum.commerce.cart.model.Cart;
import ru.yandex.practicum.commerce.cart.model.CartProduct;
import ru.yandex.practicum.commerce.cart.repository.CartProductRepository;
import ru.yandex.practicum.commerce.cart.repository.CartRepository;
import ru.yandex.practicum.commerce.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction.cart.enums.CartState;
import ru.yandex.practicum.commerce.interaction.cart.exception.NoProductInShoppingCartException;
import ru.yandex.practicum.commerce.interaction.cart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction.general.exception.ValidationException;
import ru.yandex.practicum.commerce.interaction.warehouse.WarehouseFeignClient;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final WarehouseFeignClient warehouseClient;

    @Override
    public ShoppingCartDto getActualCart(String username) {
        log.debug("[Cart service] get actual cart for username = {} ", username);
        validateUsername(username);

        Cart cart = cartRepository.findByUsername(username).orElseGet(() -> createCart(username));

        List<CartProduct> productList = cartProductRepository.findByCartId(cart.getCartId());

        return CartMapper.toDto(cart, productList);
    }

    @Transactional
    @Override
    public ShoppingCartDto putProductIntoCart(String username, Map<UUID, Integer> products) {
        validateUsername(username);

        log.debug("[Cart service] add products ({}) to the cart (username {})", products, username);
        Cart cart = cartRepository.findByUsername(username).orElseGet(() -> createCart(username));
        if (cart.getCartState().equals(CartState.DEACTIVATED)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Trying put product to deactivated cart");
        }

        List<CartProduct> cartProductList = products.entrySet().stream()
                .map(entry -> CartProduct.builder()
                        .cartId(cart.getCartId())
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
                        .build()).toList();

        BookedProductsDto bookedProductsDto = warehouseClient.checkProductsInCart(CartMapper.toDto(cart, cartProductList)).getBody();
        cartProductRepository.saveAll(cartProductList);

        return CartMapper.toDto(cart, cartProductList);
    }

    @Override
    public void deactivateCart(String username) {
        log.debug("[Cart service] deactivate cart for username");
        validateUsername(username);

        Cart cart = cartRepository.findByUsername(username).orElseGet(() -> createCart(username));
        if (cart.getCartState().equals(CartState.ACTIVE)) {
            cart.setCartState(CartState.DEACTIVATED);
            cartRepository.save(cart);
        }
    }

    @Transactional
    @Override
    public ShoppingCartDto removeProductsFromCart(String username, List<UUID> products) {
        log.debug("[Cart service] remove products ({}) from cart for username {} ", products, username);
        validateUsername(username);
        if (products.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The list of products to be removed from the cart is empty");
        }

        Cart cart = cartRepository.findByUsername(username).orElseGet(() -> createCart(username));
        List<CartProduct> cartProductList = cartProductRepository.findByCartId(cart.getCartId());

        if (cartProductList.isEmpty()) {
            throw new NoProductInShoppingCartException(HttpStatus.BAD_REQUEST, "Attempt to remove products from empty cart");
        }

        Set<UUID> productIds = cartProductList.stream().map(CartProduct::getProductId).collect(Collectors.toSet());
        if (!productIds.containsAll(products)) {
            throw new NoProductInShoppingCartException(HttpStatus.BAD_REQUEST, "Attempt to remove a missing product from the cart");
        }

        Set<UUID> productIdsToRemove = new HashSet<>(products);
        List<CartProduct> productsToRemove = cartProductList.stream()
                .filter(cp -> productIdsToRemove.contains(cp.getProductId()))
                .toList();

        cartProductRepository.deleteAll(productsToRemove);

        return CartMapper.toDto(cart, cartProductList.stream().filter(cp -> !productIdsToRemove.contains(cp.getProductId())).toList());
    }

    @Transactional
    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        log.debug("[Cart service] change product quantity request ({}) ", request);
        validateUsername(username);

        Cart cart = cartRepository.findByUsername(username).orElseGet(() -> createCart(username));
        List<CartProduct> cartProductList = cartProductRepository.findByCartId(cart.getCartId());

        if (cartProductList.isEmpty()) {
            throw new NoProductInShoppingCartException(HttpStatus.BAD_REQUEST, "Attempt to change product quantity in empty cart");
        }

        CartProduct cartProductToEdit = cartProductList.stream()
                .filter(cp -> cp.getProductId().equals(request.getProductId())).findFirst()
                .orElseThrow(() -> new NoProductInShoppingCartException(HttpStatus.BAD_REQUEST, "Attempt to change quantity of missing product"));

        log.debug("[Cart service] change quantity {} -> {} of product {} ",
                cartProductToEdit.getQuantity(), request.getNewQuantity(), request.getProductId());
        cartProductToEdit.setQuantity(request.getNewQuantity());

        for (int i = 0; i < cartProductList.size(); i++) {
            if (cartProductList.get(i).getProductId().equals(cartProductToEdit.getProductId())) {
                cartProductList.set(i, cartProductToEdit); // Обновленная версия
                break;
            }
        }

        log.debug("[Cart service] cartProductList = {} ", toJson(cartProductList));
        ShoppingCartDto dto = CartMapper.toDto(cart, cartProductList);
        BookedProductsDto bookedProductsDto = warehouseClient.checkProductsInCart(dto).getBody();

        cartProductRepository.save(cartProductToEdit);

        List<CartProduct> productList = cartProductRepository.findByCartId(cart.getCartId());
        log.debug("[Cart service] productList = {} ", toJson(productList));
        return CartMapper.toDto(cart, productList);
    }

    private Cart createCart(String username) {
        log.debug("[Cart service] generating a new cart for user {} ", username);
        Cart cart = Cart.builder()
                .cartId(UUID.randomUUID())
                .username(username)
                .cartState(CartState.ACTIVE)
                .build();

        return cartRepository.save(cart);
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException(HttpStatus.UNAUTHORIZED, "Username is null or blank");
        }
    }

    private String toJson(Object obj) {
        ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }
}
