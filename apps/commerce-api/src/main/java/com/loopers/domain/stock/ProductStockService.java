package com.loopers.domain.stock;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductStockService {
    private final ProductStockRepository productStockRepository;

    public Optional<ProductStockInfo> findStock(final ProductStockCommand.GetStock command) {
        return this.productStockRepository.findByProductId(command.productId())
                .map(ProductStockInfo::from);
    }

    public List<ProductStockInfo> getStocks(final ProductStockCommand.GetStocks command) {
        return this.productStockRepository.findAllByProductId(command.productIds()).stream()
                .map(ProductStockInfo::from)
                .toList();
    }
}
