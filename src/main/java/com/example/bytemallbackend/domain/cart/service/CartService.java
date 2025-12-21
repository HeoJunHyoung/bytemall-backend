package com.example.bytemallbackend.domain.cart.service;

import com.example.bytemallbackend.domain.cart.dto.request.CartItemAddRequest;
import com.example.bytemallbackend.domain.cart.dto.request.CartItemDeleteRequest;
import com.example.bytemallbackend.domain.cart.dto.request.CartItemUpdateRequest;
import com.example.bytemallbackend.domain.cart.dto.response.CartResponse;
import com.example.bytemallbackend.domain.cart.entity.Cart;
import com.example.bytemallbackend.domain.cart.entity.CartItem;
import com.example.bytemallbackend.domain.cart.exception.CartErrorCode;
import com.example.bytemallbackend.domain.cart.repository.CartItemRepository;
import com.example.bytemallbackend.domain.cart.repository.CartRepository;
import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.catalog.product.repository.ProductRepository;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 장바구니 담기
    @Transactional
    public void addCart(Long memberId, CartItemAddRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> cartRepository.save(Cart.createCart(member)));

        cart.addCartItem(product, request.getCount());
    }

    // 장바구니 상품 수량 변경
    @Transactional
    public void updateCartItemCount(Long memberId, CartItemUpdateRequest request) {

        if (request.getCount() < 1) {
            throw new BusinessException(CartErrorCode.INVALID_QUANTITY);
        }

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new BusinessException(CartErrorCode.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getMember().getId().equals(memberId)) {
            throw new BusinessException(CartErrorCode.UNAUTHORIZED_ACCESS);
        }

        cartItem.updateCount(request.getCount());

    }

    // 장바구니 항목 제거
    @Transactional
    public void deleteCartItems(Long memberId, List<CartItemDeleteRequest> requests) {
        // 1. 회원의 장바구니 조회
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(CartErrorCode.CART_NOT_FOUND));

        // 2. 삭제할 ID 목록 추출
        List<Long> deleteIds = requests.stream()
                .map(CartItemDeleteRequest::getCartItemId)
                .toList();

        // 3. 장바구니 리스트에서 해당 ID들을 제거 (OrphanRemoval에 의해 DB 삭제됨)
        cart.getCartItems()
                .removeIf(item -> deleteIds.contains(item.getId()));
    }

    // 장바구니 비우기
    @Transactional
    public void removeCartItemsByProductIds(Long memberId, List<Long> productIds) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(CartErrorCode.CART_NOT_FOUND));

        // 해당 상품 ID들을 장바구니 리스트에서 제거 (orphanRemoval로 인해 DB 삭제)
        cart.getCartItems().removeIf(item -> productIds.contains(item.getProduct().getId()));
    }

    // 장바구니 조회
    public CartResponse getCart(Long memberId) {
        Cart cart = cartRepository.findByMemberIdWithItems(memberId)
                .orElseThrow(() -> new BusinessException(CartErrorCode.CART_NOT_FOUND));

        return CartResponse.fromEntity(cart);
    }

}
