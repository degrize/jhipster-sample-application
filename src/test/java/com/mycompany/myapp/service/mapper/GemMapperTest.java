package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GemMapperTest {

    private GemMapper gemMapper;

    @BeforeEach
    public void setUp() {
        gemMapper = new GemMapperImpl();
    }
}
