package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuartierMapperTest {

    private QuartierMapper quartierMapper;

    @BeforeEach
    public void setUp() {
        quartierMapper = new QuartierMapperImpl();
    }
}
