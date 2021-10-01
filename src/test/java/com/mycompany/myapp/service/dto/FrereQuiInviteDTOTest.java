package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrereQuiInviteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrereQuiInviteDTO.class);
        FrereQuiInviteDTO frereQuiInviteDTO1 = new FrereQuiInviteDTO();
        frereQuiInviteDTO1.setId(1L);
        FrereQuiInviteDTO frereQuiInviteDTO2 = new FrereQuiInviteDTO();
        assertThat(frereQuiInviteDTO1).isNotEqualTo(frereQuiInviteDTO2);
        frereQuiInviteDTO2.setId(frereQuiInviteDTO1.getId());
        assertThat(frereQuiInviteDTO1).isEqualTo(frereQuiInviteDTO2);
        frereQuiInviteDTO2.setId(2L);
        assertThat(frereQuiInviteDTO1).isNotEqualTo(frereQuiInviteDTO2);
        frereQuiInviteDTO1.setId(null);
        assertThat(frereQuiInviteDTO1).isNotEqualTo(frereQuiInviteDTO2);
    }
}
