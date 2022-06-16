package com.itransition.courseproject.service;


// Asatbek Xalimjonov 6/15/22 9:44 AM


import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.repository.UserRepository;
import com.itransition.courseproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService, ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found {} user ", username);
        }
        return user;
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Map<String, String> deleteUserById(Integer id) {

        Map<String,String> messages = new HashMap<>();

        if (userRepository.existsById(id)) {
            try {
                userRepository.deleteById(id);
                messages.put("success","Successfully deleted");
            }catch (Exception e){
                messages.put("error","Deleting error");
            }
        }else{
            messages.put("notFound","Not Found");
        }
        return messages;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userName = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
        User currentUser = userRepository.findByUsername(userName);
        currentUser.setLastLoginTime(LocalDateTime.now());
        userRepository.save(currentUser);
    }

}
