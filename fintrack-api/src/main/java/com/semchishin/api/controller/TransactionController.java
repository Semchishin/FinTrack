package com.semchishin.api.controller;

import com.semchishin.api.dto.TransactionDto;
import com.semchishin.api.exception.TransactionNotFoundException;
import com.semchishin.api.mapper.TransactionDtoMapper;
import com.semchishin.api.util.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semchishin.core.model.Transaction;
import semchishin.core.service.transaction.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(Path.API + Path.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionDtoMapper transactionDtoMapper;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionDtoMapper.toDtoList(transactionService.findAllTransactions());
    }

    @GetMapping(Path.ID)
    public TransactionDto getTransactionById(@PathVariable Long id) {
        return transactionDtoMapper.toDto(transactionService.findTransactionById(id)
                .orElseThrow(
                        () -> new TransactionNotFoundException(
                                String.format("Transaction with id = %d not found", id)
                        )
                )
        );
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionService.addTransaction(transactionDtoMapper.toEntity(transactionDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDtoMapper.toDto(transaction));
    }

    @PutMapping((Path.ID))
    public ResponseEntity<Void> updateTransaction(
            @RequestBody TransactionDto transactionDto,
            @PathVariable Long id
    ) {
        Transaction transaction = transactionDtoMapper.toEntity(transactionDto);
        transaction.setTransactionId(id);
        transactionService.updateTransaction(transaction);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(Path.ID)
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

