package com.semchishin.api.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import semchishin.core.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a transaction.
 * <p>
 * Used for transferring transaction data between API layer and core domain.
 * References the domain entity: {@link Transaction}.
 * </p>
 *
 * @author Sergey Semchishin
 * @see Transaction
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TransactionDto {

    /**
     * The amount of the transaction. Cannot be null.
     */
    @NonNull
    private BigDecimal amount;

    /**
     * The category of the transaction. Cannot be null.
     */
    @NonNull
    private String category;

    /**
     * The date and time when the transaction was created.
     * If null, defaults to current date and time.
     */
    private LocalDateTime dateTime;

    /**
     * Gets the transaction date and time.
     * If the dateTime is null, returns current date and time.
     *
     * @return the transaction date and time, never null
     */
    public LocalDateTime getDateTime() {
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        return dateTime;
    }
}
