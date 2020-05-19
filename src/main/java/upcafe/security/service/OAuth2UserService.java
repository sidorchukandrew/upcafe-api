package upcafe.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import upcafe.entity.signin.User;
import upcafe.repository.signin.UserRepository;
import upcafe.security.model.OAuth2UserInfo;
import upcafe.security.model.OAuth2UserInfoFactory;
import upcafe.security.model.Role;
import upcafe.security.model.UserPrincipal;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(request.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        Optional<User> userOptional = userRepo.findByEmail(userInfo.getEmail());
        User localUser = null;

        if(userOptional.isPresent()) {
            localUser = userOptional.get();

            if(localUser.getProvider().compareToIgnoreCase(request.getClientRegistration().getRegistrationId()) != 0) {
                // THROW HERE

                // TODO
            }

            // Update the user
            localUser = updateExistingUser(localUser, userInfo);
        }
        else {
            // Register a new user
            localUser = registerNewUser(userInfo, request);
        }

        return UserPrincipal.create(localUser, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserInfo userInfo, OAuth2UserRequest request) {
        User user = new User.Builder(userInfo.getEmail())
                .accountCreatedOn(LocalDateTime.now())
                .name(userInfo.getName())
                .provider(request.getClientRegistration().getRegistrationId())
                .imageUrl(userInfo.getImageUrl())
                .roles(Collections.singleton(new Role.Builder("ROLE_CUSTOMER").setId(1).build()))
                .build();

        return userRepo.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo userInfo) {

        existingUser.setName(userInfo.getName());
        existingUser.setImageUrl(userInfo.getImageUrl());

        return userRepo.save(existingUser);
    }
}
