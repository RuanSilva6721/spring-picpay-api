package com.picpay.picpay.services;

import com.picpay.picpay.domain.user.User;
import com.picpay.picpay.domain.user.UserType;
import com.picpay.picpay.dtos.UserDTO;
import com.picpay.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

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

    public User createUser(UserDTO user) {
        User newUser = new User(user);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }
}
