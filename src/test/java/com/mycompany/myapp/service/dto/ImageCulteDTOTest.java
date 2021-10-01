package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageCulteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageCulteDTO.class);
        ImageCulteDTO imageCulteDTO1 = new ImageCulteDTO();
        imageCulteDTO1.setId(1L);
        ImageCulteDTO imageCulteDTO2 = new ImageCulteDTO();
        assertThat(imageCulteDTO1).isNotEqualTo(imageCulteDTO2);
        imageCulteDTO2.setId(imageCulteDTO1.getId());
        assertThat(imageCulteDTO1).isEqualTo(imageCulteDTO2);
        imageCulteDTO2.setId(2L);
        assertThat(imageCulteDTO1).isNotEqualTo(imageCulteDTO2);
        imageCulteDTO1.setId(null);
        assertThat(imageCulteDTO1).isNotEqualTo(imageCulteDTO2);
    }
}
