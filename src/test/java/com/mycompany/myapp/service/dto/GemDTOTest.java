package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GemDTO.class);
        GemDTO gemDTO1 = new GemDTO();
        gemDTO1.setId(1L);
        GemDTO gemDTO2 = new GemDTO();
        assertThat(gemDTO1).isNotEqualTo(gemDTO2);
        gemDTO2.setId(gemDTO1.getId());
        assertThat(gemDTO1).isEqualTo(gemDTO2);
        gemDTO2.setId(2L);
        assertThat(gemDTO1).isNotEqualTo(gemDTO2);
        gemDTO1.setId(null);
        assertThat(gemDTO1).isNotEqualTo(gemDTO2);
    }
}
