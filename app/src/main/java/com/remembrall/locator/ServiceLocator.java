package com.remembrall.locator;

import java.util.HashMap;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private HashMap<Class<?>, Object> map = new HashMap<>();

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    void put(Class<?> clazz, Object obj) {
        map.put(clazz, obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) map.get(clazz);
    }
}
