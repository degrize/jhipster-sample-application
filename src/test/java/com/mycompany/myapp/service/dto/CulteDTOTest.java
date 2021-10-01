package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CulteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CulteDTO.class);
        CulteDTO culteDTO1 = new CulteDTO();
        culteDTO1.setId(1L);
        CulteDTO culteDTO2 = new CulteDTO();
        assertThat(culteDTO1).isNotEqualTo(culteDTO2);
        culteDTO2.setId(culteDTO1.getId());
        assertThat(culteDTO1).isEqualTo(culteDTO2);
        culteDTO2.setId(2L);
        assertThat(culteDTO1).isNotEqualTo(culteDTO2);
        culteDTO1.setId(null);
        assertThat(culteDTO1).isNotEqualTo(culteDTO2);
    }
}
