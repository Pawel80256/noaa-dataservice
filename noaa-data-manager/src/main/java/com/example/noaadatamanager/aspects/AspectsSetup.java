package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.repository.NOAADataRepository;
import com.example.noaadatamanager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.Aspects;
import org.springframework.stereotype.Component;

@Component
public class AspectsSetup {
    private final RoleRepository roleRepository;
    private final NOAADataRepository noaaDataRepository;

    public AspectsSetup(RoleRepository roleRepository, NOAADataRepository noaaDataRepository) {
        this.roleRepository = roleRepository;
        this.noaaDataRepository = noaaDataRepository;
    }

    @PostConstruct
    private void setupAspect(){
        Aspects.aspectOf(AuthorizationAspect.class)
                .setRoleRepository(this.roleRepository);

        Aspects.aspectOf(ValidationAspect.class)
                .setNoaaDataRepository(this.noaaDataRepository);
    }
}
