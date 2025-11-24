package com.duoc.recetas.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class PasswordGeneratorPrivateTest {

    @Test
    void getEnvOrPlaceholderAndMaskCoverage() throws Exception {
        Method getEnv = PasswordGenerator.class.getDeclaredMethod("getEnvOrPlaceholder", String.class);
        getEnv.setAccessible(true);
        Method mask = PasswordGenerator.class.getDeclaredMethod("mask", String.class);
        mask.setAccessible(true);

        // Env likely not set; should fall back to placeholder
        String value = (String) getEnv.invoke(null, "SOME_NON_EXISTENT_ENV");
        assertNotNull(value);

        assertEquals("<empty>", mask.invoke(null, (String) null));
        assertEquals("****...", mask.invoke(null, "abcd1234"));
    }
}
