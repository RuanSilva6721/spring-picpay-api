package com.picpay.picpay.services;

import com.picpay.picpay.domain.transaction.Transaction;
import com.picpay.picpay.domain.user.User;
import com.picpay.picpay.dtos.TransactionDTO;
import com.picpay.picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TrasactionService {
    @Autowired
    TransactionRepository repository;

    @Autowired
    UserService userService;


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserBId(transactionDTO.senderId());
        User receiver = this.userService.findUserBId(transactionDTO.receiverId());

        userService.validateTrasaction(sender, transactionDTO.value());
        if (!this.authorizeTransaction(sender, transactionDTO.value())){
            throw new Exception("Transação não autorizada!");
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

        repository.save(transaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);

        this.notificationService.senNotification(sender, "Transação efetuada com sucesso!");
        this.notificationService.senNotification(receiver, "Transação efetuada com sucesso!");

        return transaction;

    }
    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorization =  restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);
        if (authorization.getStatusCode() == HttpStatus.OK){
            String message = (String) authorization.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }else {
            return false;
        }
    }
}
