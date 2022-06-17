package com.itransition.courseproject.service.interfaces;

import com.itransition.courseproject.dto.UserRegisterDto;
import com.itransition.courseproject.entity.enums.ValidationResult;

import java.util.function.Function;
import java.util.regex.Pattern;

import static com.itransition.courseproject.entity.enums.ValidationResult.*;

public interface UserRegistrationValidator extends Function<UserRegisterDto, ValidationResult> {

    static UserRegistrationValidator isEmailValid() {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" +
                "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
        return user -> Pattern.compile(regexPattern).matcher(user.getEmail()).matches() ? SUCCESS : EMAIL_NOT_VALID;
    }

    static UserRegistrationValidator isPasswordValid() {
        return user -> user.getPassword().length()>2 ? SUCCESS : PASSWORD_NOT_VALID;
    }

    static UserRegistrationValidator isFirstNameLastName() {
        return user -> (user.getLastName().length()>0 && user.getFirstName().length()>0) ? SUCCESS : FIRSTNAME_OR_LASTNAME_NOT_VALID;
    }

    static UserRegistrationValidator isPasswordMatch() {
        return user -> (user.getPassword().equals(user.getConfirmPassword())) ? SUCCESS : PASSWORD_DID_NOT_MATCH;
    }

    default UserRegistrationValidator and(UserRegistrationValidator other) {
        return user -> {
            ValidationResult result = this.apply(user);
            return result.equals(SUCCESS) ? other.apply(user) : result;
        };
    }
}
