package com.picpay.picpay.services;

import com.picpay.picpay.domain.user.User;
import com.picpay.picpay.domain.user.UserType;
import com.picpay.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTrasaction(User sender, BigDecimal amount) throws Exception{
        if (sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuário do tipo Logista não está autorizado a realizar trasanção!");
        }
        if (sender.getBalance().compareTo(amount)<0){
            throw new Exception("Usuário não tem saldo suficiente");
        }
    }
    public User findUserBId(Long id) throws Exception {
       return this.repository.findUserById(id).orElseThrow(()-> new Exception("Usuário não encontrado"));
    }
    public void saveUser(User user){
        this.repository.save(user);
    }
}
