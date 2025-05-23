package com.migros.couriertracking.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ObjectMapperCreatorTest {

    @Test
    void should_get() {
        assertNotNull(ObjectMapperCreator.getInstance());
    }
}
