package com.itransition.courseproject.service.impl;


// Asatbek Xalimjonov 6/15/22 9:44 AM


import com.itransition.courseproject.dto.UserDto;
import com.itransition.courseproject.dto.UserRegisterDto;
import com.itransition.courseproject.entity.enums.Role;
import com.itransition.courseproject.entity.enums.ValidationResult;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.repository.UserRepository;
import com.itransition.courseproject.service.interfaces.GenericInterface;
import com.itransition.courseproject.service.interfaces.UserRegistrationValidator;
import com.itransition.courseproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService, GenericInterface<UserDto, Integer, String>, ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            User userEmail = userRepository.findByEmail(username);
            if (userEmail == null) {
                log.error("User not found");
                throw new UsernameNotFoundException("User not found");
            } else {
                log.info("User found {} user ", username);
            }
            return userEmail;
        } else {
            log.info("User found {} user ", username);
        }
        return user;
    }


    @Override
    public String blockOrUnBlockUserById(Integer id, RedirectAttributes ra, boolean shouldBlockUser) {
        if (userRepository.existsById(id)) {
            try {
                String message = shouldBlockUser ? "Blocked" : "Unblocked";
                User user = userRepository.findById(id).get();
                if (shouldBlockUser) user.setBlocked(true);
                else user.setBlocked(false);
                userRepository.save(user);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully " + message);
                return "redirect:/admin/user";
            } catch (Exception e) {
            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", "Not Found");
        return "redirect:/admin/user";
    }

    @Override
    public UserDto getUserData() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            User currentUser = userRepository.findByUsername(username);
            return new UserDto(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getRole(), currentUser.isBlocked(), currentUser.getLastLoginTime());
        } catch (Exception e) {
        }
        try {
            DefaultOidcUser oauthUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = oauthUser.getAttribute("email");
            User currentUser = userRepository.findByEmail(email);
            return new UserDto(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getRole(), currentUser.isBlocked(), currentUser.getLastLoginTime());
        } catch (Exception e) {
        }

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = oauthUser.getAttribute("email");
        String login = oauthUser.getAttribute("login");
        User currentUser = null;
        if (email != null) currentUser = userRepository.findByEmail(email);
        else if (login != null) currentUser = userRepository.findByEmail(login);
        return new UserDto(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getRole(), currentUser.isBlocked(), currentUser.getLastLoginTime());
    }

    @Override
    public List<UserDto> getAllData() {
        return userRepository.getAllUsers();
    }

    @Override
    public Page<UserDto> getAllDataByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<User> userPage = userRepository.findAll(pageable);
        int totalElements = (int) userPage.getTotalElements();
        return new PageImpl<UserDto>(userPage.getContent().stream().map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.isBlocked(), user.getLastLoginTime())).collect(Collectors.toList()), pageable, totalElements);
    }

    @Override
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id).get();
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.isBlocked(), user.getLastLoginTime());
    }

    @Override
    public String saveData(UserDto userDto, RedirectAttributes ra) {
        if (userRepository.findByUsername(userDto.getUsername()) == null && userRepository.findByEmail(userDto.getEmail()) == null) {

            try {
                User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), userDto.getRole(), false);

                userRepository.save(user);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully created");
                return "redirect:/admin/user";
            } catch (Exception e) {

            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", "User Already exist with this data");
        return "redirect:/admin/user/create";
    }

    @Override
    public String updateData(UserDto userDto, RedirectAttributes ra) {
        if (userRepository.existsById(userDto.getId())) {

            try {
                User user = userRepository.findById(userDto.getId()).get();
                user.setEmail(userDto.getEmail());
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                if (userDto.getRole() != null) user.setRole(userDto.getRole());
                if (userDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                userRepository.save(user);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully updated");
                if (getUserData().getId().equals(userDto.getId())) return "redirect:/user/profile";
                return "redirect:/admin/user";
            } catch (Exception e) {
            }
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", "Updating error");
        if (getUserData().getId().equals(userDto.getId())) return "redirect:/user/profile";
        return "redirect:/admin/user";
    }

    @Override
    public String deleteById(Integer id, RedirectAttributes ra) {
        if (userRepository.existsById(id)) {
            try {
                userRepository.deleteById(id);
                ra.addFlashAttribute("status", "success");
                ra.addFlashAttribute("message", "Successfully deleted");
                return "redirect:/admin/topic";
            } catch (Exception e) {
            }
        }
        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", "Deleting Error");
        return "redirect:/admin/user";
    }


    @Override
    public List<UserDto> search(String search) {
        return null;
    }

    @Override
    public String registerUser(UserRegisterDto userDto, RedirectAttributes ra) {

        String message = "";
        if (userRepository.findByEmail(userDto.getEmail()) == null) {
            try {

                ValidationResult apply = UserRegistrationValidator.isEmailValid().and(UserRegistrationValidator.isPasswordValid()).and(UserRegistrationValidator.isFirstNameLastName()).and(UserRegistrationValidator.isPasswordMatch()).apply(userDto);

                if (apply.equals(ValidationResult.SUCCESS)) {
                    User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), Role.ROLE_USER, false);

                    userRepository.save(user);

                    ra.addFlashAttribute("status", "success");
                    ra.addFlashAttribute("message", "Successfully Registered");
                    return "redirect:/signing";
                }

                switch (apply) {
                    case EMAIL_NOT_VALID:
                        message = "Email not valid";
                        break;
                    case PASSWORD_NOT_VALID:
                        message = "Password not valid";
                        break;
                    case FIRSTNAME_OR_LASTNAME_NOT_VALID:
                        message = "FirstName or LastName length at least 3";
                        break;
                    case PASSWORD_DID_NOT_MATCH:
                        message = "Password did not match";
                        break;
                }
            } catch (Exception e) {
            }
        } else {
            message = "Email already exist!";
        }

        ra.addFlashAttribute("status", "error");
        ra.addFlashAttribute("message", message);
        return "redirect:/signup";
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        try {
            String userName = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
            User currentUser = userRepository.findByUsername(userName);
            currentUser.setLastLoginTime(LocalDateTime.now());
            userRepository.save(currentUser);
        } catch (Exception e) {
            log.info("Error ", " error logi time");
        }
        try {
            DefaultOidcUser oauthUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = oauthUser.getAttribute("email");
            User currentUser = userRepository.findByEmail(email);
            currentUser.setLastLoginTime(LocalDateTime.now());
            userRepository.save(currentUser);
        } catch (Exception e) {
            log.info("Error ", " error logi time");
        }
    }

    @Override
    public User currenUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            User currentUser = userRepository.findByUsername(username);
            return currentUser;
        } catch (Exception e) {
        }
        try {
            DefaultOidcUser oauthUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = oauthUser.getAttribute("email");
            User currentUser = userRepository.findByEmail(email);
            return currentUser;
        } catch (Exception e) {
        }

        try {
            DefaultOAuth2User oauthUser = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = oauthUser.getAttribute("email");
            String login = oauthUser.getAttribute("login");
            User currentUser = null;
            if (email != null) currentUser = userRepository.findByEmail(email);
            else if (login != null) currentUser = userRepository.findByEmail(login);
        } catch (Exception e) {
        }
        return null;
    }

    public void createUserOauth2(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return;
        } else {
            User newUser = new User(email, email, email, email,
                    passwordEncoder.encode(email), Role.ROLE_USER, false);

            User save = userRepository.save(newUser);
        }
    }

}
