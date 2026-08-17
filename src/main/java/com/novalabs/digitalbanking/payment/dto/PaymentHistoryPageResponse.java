package com.novalabs.digitalbanking.payment.dto;

import java.util.List;

public record PaymentHistoryPageResponse(
        List<PaymentHistoryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
