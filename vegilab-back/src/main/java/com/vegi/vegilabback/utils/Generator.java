package com.vegi.vegilabback.utils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.vegi.vegilabback.model.*;
import com.vegi.vegilabback.model.enums.RoleEnum;
import com.vegi.vegilabback.repository.RoleRepository;
import com.vegi.vegilabback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class Generator implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        generateData();
    }

    private void generateData() throws IOException {
        String filePath = "src/main/resources/data.json";
        byte[] bytesFile = Files.readAllBytes(Paths.get(filePath));

        JsonIterator iter = JsonIterator.parse(bytesFile);
        Any any = iter.readAny();


        Any roleAny = any.get("roles");
        for (Any a : roleAny) {
            String name = a.get("name").toString();
            RoleEnum roles = null;
            if(name.equals("USER")){
                roles = RoleEnum.USER;
            } else if(name.equals("ADMIN")){
                roles = RoleEnum.ADMIN;
            } else {
                roles = RoleEnum.MODERATOR;
            }
            Role role = new Role(
                    null,
                    roles
            );
            this.roleRepository.save(role);
        };

    }
}
