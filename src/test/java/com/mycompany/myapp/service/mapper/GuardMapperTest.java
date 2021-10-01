package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuardMapperTest {

    private GuardMapper guardMapper;

    @BeforeEach
    public void setUp() {
        guardMapper = new GuardMapperImpl();
    }
}
