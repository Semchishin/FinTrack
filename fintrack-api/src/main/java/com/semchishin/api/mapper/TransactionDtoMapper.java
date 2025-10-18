package com.semchishin.api.mapper;

import com.semchishin.api.dto.TransactionDto;
import org.mapstruct.Mapper;
import semchishin.core.model.Transaction;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionDtoMapper {

    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);

    List<TransactionDto> toDtoList(List<Transaction> transactions);

}
