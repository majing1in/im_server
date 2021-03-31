package com.xiaoma.im.listener;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class TransactionContainer {

    private Map<String, Supplier<Boolean>> transactionMap = new ConcurrentHashMap<>();

    public Boolean execute(String key) throws Exception {
        Supplier<Boolean> supplier = transactionMap.get(key);
        if (null == supplier) {
            throw new Exception(String.format("key[%s] has no supplier", key));
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            throw e;
        } finally {
            transactionMap.remove(key, supplier);
        }

    }

    public void put(String key, Supplier<Boolean> supplier) {
        transactionMap.put(key, supplier);
    }

}