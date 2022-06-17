package com.itransition.courseproject.common;


// Asatbek Xalimjonov 6/15/22 11:14 AM


import com.itransition.courseproject.entity.enums.Role;
import com.itransition.courseproject.entity.user.User;
import com.itransition.courseproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.sql.init.mode}")
    String initMode;

    final PasswordEncoder passwordEncoder;
    final UserRepository userRepo;

    public DataLoader(PasswordEncoder passwordEncoder,UserRepository userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }


    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("always")) {

//            User user1 = new User("Asadbek",
//                    "Xalimjonov",
//                    "asadbek2000",
//                    "asatbekxalimjonov2000@gmail.com",
//                    passwordEncoder.encode("111"),
//                    Role.ROLE_SUPER_ADMIN,
//                    false);
//
//            User user2 = new User("Eldor",
//                    "Choriyev",
//                    "eldor1999",
//                    "ch.eldor@gmail.com",
//                    passwordEncoder.encode("111"),
//                    Role.ROLE_USER,
//                    false);
//
//            userRepo.save(user1);
//            userRepo.save(user2);

        }
    }
}
