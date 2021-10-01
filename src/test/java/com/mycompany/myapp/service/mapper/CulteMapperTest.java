package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CulteMapperTest {

    private CulteMapper culteMapper;

    @BeforeEach
    public void setUp() {
        culteMapper = new CulteMapperImpl();
    }
}
