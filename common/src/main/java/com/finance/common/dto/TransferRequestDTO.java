package com.finance.common.dto;

import lombok.NonNull;

public record TransferRequestDTO(
                @NonNull Long fromAccountId,

                @NonNull Long toAccountId,

                @NonNull Double amount) {
}
