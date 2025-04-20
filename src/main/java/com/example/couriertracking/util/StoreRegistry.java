package com.example.couriertracking.util;

import com.example.couriertracking.dto.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class StoreRegistry {
    private static StoreRegistry instance;
    private final List<Store> stores;

    private StoreRegistry() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("stores.json");
            this.stores = new ArrayList<>(List.of(mapper.readValue(resourceAsStream, Store[].class)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load stores", e);
        }
    }

    public static synchronized StoreRegistry getInstance() {
        if (instance == null) {
            instance = new StoreRegistry();
        }
        return instance;
    }

}
