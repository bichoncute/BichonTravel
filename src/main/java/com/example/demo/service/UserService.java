package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;
    public Users createUser(String name,String email, String role,String phone,
    		String password_hash,String account_status) {
    	
    		Users users = new Users(
    		    name,
    		    email,
    		    role,
    		    phone,
    		    password_hash,
    		    account_status
    		);
		return userRepository.save(users);
	}
    public Optional<Users> getUserById(Integer id) {
        return userRepository.findById(id);
    }
    
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    public Integer checkregister(String inputemail, String inputpassword) {
    		Optional<Users> existingUseremail = userRepository.findByEmail(inputemail);
    		return userRepository.findByEmail(inputemail).isPresent() ? 0 : 1;
    	
    }
    public Integer loginprocess(String inputemail, String inputpassword) {
		Optional<Users> existingUser = userRepository.findByEmail(inputemail);
		 if (existingUser.isPresent()) {
			 Users users = existingUser.get();
			 if (users.getPassword_hash().equals(inputpassword) && users.getAccount_status().equals("active"))
				 return users.getId();
			 else 
				 return 0;
		 }
		 
		 throw new RuntimeException("使用者不存在:");
}
    
    public Users updateUser(Integer id, String name,  String role,
    		String phone,String password_hash,String account_status) {
        Optional<Users> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            Users users = existingUser.get();
            users.setName(name);
            //users.setEmail(email);
            users.setRole(role);
            users.setPhone(phone);
            users.setPassword_hash(password_hash);
            users.setAccount_status(account_status);
            return userRepository.save(users);
        }
        throw new RuntimeException("使用者不存在: " + id);
    }
    
    public boolean deleteUser(Integer id) {
        Optional<Users> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            Users users = existingUser.get();
            users.setAccount_status("disabled");
            userRepository.save(users);
            return true;
        }
        return false;
    }
    public long getUserCount() {
        return userRepository.count();
    }

}
