package com.example.noaadatamanager;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.dtos.update.StationUpdateNameDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StationServiceTest {
    @Test
    public void testUpdateNameMethodSignature() {
        try {
            Class<?> stationServiceClass = Class.forName("com.example.noaadatamanager.service.StationService");

            Class<?>[] parameterTypes = new Class<?>[] { StationInputDto.class };

            Method method = stationServiceClass.getDeclaredMethod("create", parameterTypes);

            assertNotNull(method, "Method create with the expected signature does not exist.");
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Class StationService not found.", e);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Method create with the expected signature does not exist.", e);
        }
    }
}
