package com.vegi.vegilabback.validation;

import com.vegi.vegilabback.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthentificationValidation {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public UserDetailsImpl getUserDetails(){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails;
    }

    public Long getTokenUserId(){
        UserDetailsImpl userDetails = getUserDetails();
        Long tokenUserId = userDetails.getId();
        return tokenUserId;
    }

    public String getTokenUsername(){
        UserDetailsImpl userDetails = getUserDetails();
        String tokenUsername = userDetails.getUsername();
        return tokenUsername;
    }

    public void getTokenAuthority(){
        UserDetailsImpl userDetails = getUserDetails();
        System.out.println(userDetails.getAuthorities());
    }

    public boolean isAdmin(){
        if (getTokenUsername().matches("^Yulanlaf56$")){
            return true;
        }else{
            return false;
        }
    }

}
