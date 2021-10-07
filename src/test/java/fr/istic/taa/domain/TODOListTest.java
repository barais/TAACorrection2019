package fr.istic.taa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.istic.taa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TODOListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TODOList.class);
        TODOList tODOList1 = new TODOList();
        tODOList1.setId(1L);
        TODOList tODOList2 = new TODOList();
        tODOList2.setId(tODOList1.getId());
        assertThat(tODOList1).isEqualTo(tODOList2);
        tODOList2.setId(2L);
        assertThat(tODOList1).isNotEqualTo(tODOList2);
        tODOList1.setId(null);
        assertThat(tODOList1).isNotEqualTo(tODOList2);
    }
}
