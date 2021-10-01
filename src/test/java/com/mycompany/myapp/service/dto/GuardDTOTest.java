package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuardDTO.class);
        GuardDTO guardDTO1 = new GuardDTO();
        guardDTO1.setId(1L);
        GuardDTO guardDTO2 = new GuardDTO();
        assertThat(guardDTO1).isNotEqualTo(guardDTO2);
        guardDTO2.setId(guardDTO1.getId());
        assertThat(guardDTO1).isEqualTo(guardDTO2);
        guardDTO2.setId(2L);
        assertThat(guardDTO1).isNotEqualTo(guardDTO2);
        guardDTO1.setId(null);
        assertThat(guardDTO1).isNotEqualTo(guardDTO2);
    }
}
