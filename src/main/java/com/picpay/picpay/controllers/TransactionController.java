package com.picpay.picpay.controllers;

import com.picpay.picpay.domain.transaction.Transaction;
import com.picpay.picpay.dtos.TransactionDTO;
import com.picpay.picpay.services.TrasactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TrasactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
        Transaction newTransaction = this.transactionService.createTransaction(transaction);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

}
