package com.vegi.vegilabback.validation;

import com.vegi.vegilabback.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthentificationValidation {

    private String adminSecret;
    @Autowired
    public String isAdminOrnot(@Value("${vegilab.app.adminSecretName}") String adminSecretName) {
        this.adminSecret = adminSecretName;
        return this.adminSecret;
    }

    public String getAdminSecret() {
        return adminSecret;
    }

    private List<String> moderatorSecretNames = new ArrayList<>();

    @Autowired
    public void setModeratorSecretNames(@Value("#{'${vegilab.app.moderatorSecretNames}'.split(',')}") List<String> moderatorSecretNames) {
        this.moderatorSecretNames.addAll(moderatorSecretNames);
    }

    public List<String> getModeratorSecretNames() {
        return moderatorSecretNames;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public UserDetailsImpl getUserDetails(){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails;
    }

    public Long getTokenId(){
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
        if(getTokenUsername() == getAdminSecret()){
            return true;
        }else {
            return false;
        }
    }

    public boolean isModerator() {
        for (var names : getModeratorSecretNames()) {
            if (getTokenUsername().equals(names)) {
                return true;
            }
        }
        return false;
    }
}
