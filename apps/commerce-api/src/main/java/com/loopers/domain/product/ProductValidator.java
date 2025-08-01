package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class ProductValidator {

    public static void validate(int stock, int price) {
        if (stock < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "재고는 음수가 될 수 없습니다.");
        }

        if  (price < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "가격은 음수가 될 수 없습니다.");
        }
    }
}
