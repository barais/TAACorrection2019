package fr.istic.taa.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.istic.taa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TODOListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TODOListDTO.class);
        TODOListDTO tODOListDTO1 = new TODOListDTO();
        tODOListDTO1.setId(1L);
        TODOListDTO tODOListDTO2 = new TODOListDTO();
        assertThat(tODOListDTO1).isNotEqualTo(tODOListDTO2);
        tODOListDTO2.setId(tODOListDTO1.getId());
        assertThat(tODOListDTO1).isEqualTo(tODOListDTO2);
        tODOListDTO2.setId(2L);
        assertThat(tODOListDTO1).isNotEqualTo(tODOListDTO2);
        tODOListDTO1.setId(null);
        assertThat(tODOListDTO1).isNotEqualTo(tODOListDTO2);
    }
}
