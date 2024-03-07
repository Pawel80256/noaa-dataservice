package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.Aspects;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationAspectSetup {
    private RoleRepository roleRepository;

    public AuthorizationAspectSetup(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void setupAspect(){
        Aspects.aspectOf(AuthorizationAspect.class)
                .setRoleRepository(this.roleRepository);
    }
}
