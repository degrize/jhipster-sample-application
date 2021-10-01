package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommunauteMapperTest {

    private CommunauteMapper communauteMapper;

    @BeforeEach
    public void setUp() {
        communauteMapper = new CommunauteMapperImpl();
    }
}
