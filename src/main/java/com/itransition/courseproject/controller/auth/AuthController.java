package com.itransition.courseproject.controller.auth;


// Asatbek Xalimjonov 6/15/22 9:46 AM

import com.itransition.courseproject.dto.UserRegisterDto;
import com.itransition.courseproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
@Slf4j
public class AuthController {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final String authorizationRequestBaseUri = "oauth2/authorization";
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserServiceImpl userService;


    @GetMapping("/signing")
    public String getSigningPage(Model model){
        getSocialClient(model);
        return "signing";
    }

    @GetMapping("/signup")
    public String getSignUpPage(Model model){
        getSocialClient(model);
        model.addAttribute("user",new UserRegisterDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpUser(UserRegisterDto userDto,RedirectAttributes ra){
        return userService.registerUser(userDto,ra);
    }

    @GetMapping("/success-login")
    public String successLogin(HttpServletRequest request, OAuth2AuthenticationToken authentication){

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        System.out.println(client);

        String email = authentication.getPrincipal().getAttribute("email");
        userService.createUserOauth2(email);

        return "redirect:/";
    }

    private void getSocialClient(Model model) {
        Map<String, String> oauth2ClientsAuthorizationUrls = new HashMap<>();
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);

        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        Objects.requireNonNull(clientRegistrations).forEach(registration ->
                oauth2ClientsAuthorizationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2ClientsAuthorizationUrls);
    }


}
