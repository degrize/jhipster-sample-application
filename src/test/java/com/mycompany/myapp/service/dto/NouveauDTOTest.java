package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NouveauDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NouveauDTO.class);
        NouveauDTO nouveauDTO1 = new NouveauDTO();
        nouveauDTO1.setId(1L);
        NouveauDTO nouveauDTO2 = new NouveauDTO();
        assertThat(nouveauDTO1).isNotEqualTo(nouveauDTO2);
        nouveauDTO2.setId(nouveauDTO1.getId());
        assertThat(nouveauDTO1).isEqualTo(nouveauDTO2);
        nouveauDTO2.setId(2L);
        assertThat(nouveauDTO1).isNotEqualTo(nouveauDTO2);
        nouveauDTO1.setId(null);
        assertThat(nouveauDTO1).isNotEqualTo(nouveauDTO2);
    }
}
