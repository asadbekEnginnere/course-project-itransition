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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements
        UserService,
        UserDetailsService,
        ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            User userEmail = userRepository.findByEmail(username);
            if (userEmail==null) {
                log.error("User not found");
                throw new UsernameNotFoundException("User not found");
            }else {
                log.info("User found {} user ", username);
            }
            return userEmail;
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
    public String saveUser(UserDto userDto, RedirectAttributes ra) {

        if (userRepository.findByUsername(userDto.getUsername())==null && userRepository.findByEmail(userDto.getEmail())==null) {

            try {
                User user = new User(
                        userDto.getFirstName(),
                        userDto.getLastName(),
                        userDto.getEmail(),
                        userDto.getEmail(),
                        passwordEncoder.encode(userDto.getPassword()),
                        userDto.getRole(),
                        false
                );

                userRepository.save(user);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully created");
                return "redirect:/admin/user";
            }catch (Exception e){

            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","User Already exist with this data");
        return "redirect:/admin/user/create";
    }

    @Override
    public String blockOrUnBlockUserById(Integer id, RedirectAttributes ra,boolean shouldBlockUser) {
        if (userRepository.existsById(id)) {
            try {
                String message=shouldBlockUser ? "Blocked":"Unblocked";
                User user = userRepository.findById(id).get();
                if (shouldBlockUser) user.setBlocked(true);
                else user.setBlocked(false);
                userRepository.save(user);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully "+message);
                return "redirect:/admin/user";
            }catch (Exception e){
            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Not Found");
        return "redirect:/admin/user";
    }

    @Override
    public UserDto getUserData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        User currentUser = userRepository.findByUsername(username);
        return new UserDto(
                currentUser.getId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole(),
                currentUser.isBlocked(),
                currentUser.getLastLoginTime()
        );
    }

    @Override
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id).get();
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isBlocked(),
                user.getLastLoginTime()
        );
    }

    @Override
    public String editUser(UserDto userDto, RedirectAttributes ra) {
        if (userRepository.existsById(userDto.getId())) {

            try {
                User user = userRepository.findById(userDto.getId()).get();
                user.setEmail(userDto.getEmail());
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                if (userDto.getRole()!=null)user.setRole(userDto.getRole());
                if (userDto.getPassword()!=null)user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                userRepository.save(user);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully updated");
                return "redirect:/admin/user";
            }catch (Exception e){
            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message","Updating error");
        return "redirect:/admin/user";
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
