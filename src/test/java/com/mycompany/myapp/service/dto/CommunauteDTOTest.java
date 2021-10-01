package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunauteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunauteDTO.class);
        CommunauteDTO communauteDTO1 = new CommunauteDTO();
        communauteDTO1.setId(1L);
        CommunauteDTO communauteDTO2 = new CommunauteDTO();
        assertThat(communauteDTO1).isNotEqualTo(communauteDTO2);
        communauteDTO2.setId(communauteDTO1.getId());
        assertThat(communauteDTO1).isEqualTo(communauteDTO2);
        communauteDTO2.setId(2L);
        assertThat(communauteDTO1).isNotEqualTo(communauteDTO2);
        communauteDTO1.setId(null);
        assertThat(communauteDTO1).isNotEqualTo(communauteDTO2);
    }
}
