package com.duoc.recetas;

import org.junit.jupiter.api.Test;

class RecetasApplicationTest {

    @Test
    void mainHonorsTestSkipRunProperty() {
        System.setProperty("app.test.skipRun", "true");
        RecetasApplication.main(new String[]{});
        System.clearProperty("app.test.skipRun");
    }
}
