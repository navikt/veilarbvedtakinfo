package no.nav.veilarbvedtakinfo.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.common.types.identer.AktorId;
import no.nav.common.types.identer.Fnr;
import no.nav.veilarbvedtakinfo.config.DatabaseTestConfig;
import no.nav.veilarbvedtakinfo.config.FilterTestConfig;
import no.nav.veilarbvedtakinfo.controller.v1.BehovsvurderingController;
import no.nav.veilarbvedtakinfo.db.BehovsvurderingRepository;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Besvarelse;
import no.nav.veilarbvedtakinfo.domain.behovsvurdering.Svar;
import no.nav.veilarbvedtakinfo.service.AuthService;
import no.nav.veilarbvedtakinfo.service.BehovsvurderingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        BehovsvurderingService.class,
        BehovsvurderingRepository.class,
        DatabaseTestConfig.class,
        FilterTestConfig.class
})

@WebMvcTest(BehovsvurderingController.class)
public class BehovsvurderingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    public AuthService authService;

    private Fnr FNR_1 = Fnr.of("FNR_1");
    private AktorId AKTORID_1 = AktorId.of("AKTORID_1");
    private Fnr FNR_2 = Fnr.of("FNR_2");
    private AktorId AKTORID_2 = AktorId.of("AKTORID_2");

    @Test
    public void kunne_oppretteOgOppdatere_besvarelse() throws Exception {
        when(authService.hentAktorId(FNR_1)).thenReturn(AKTORID_1);
        MvcResult mvcResult = mockMvc.perform(post("/api/behovsvurdering/svar?fnr=" + FNR_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Svar()
                        .setDato(ZonedDateTime.now())
                        .setSpm("spm")
                        .setSvar("svar")
                ))).andReturn();

        Besvarelse besvarelse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Besvarelse.class);

        mockMvc.perform(post("/api/behovsvurdering/svar?fnr=" + FNR_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Svar()
                        .setBesvarelseId(besvarelse.getBesvarelseId())
                        .setDato(ZonedDateTime.now())
                        .setSpm("spm2")
                        .setSvar("svar2")
                ))).andExpect(status().isOk());
    }


    @Test
    public void skal_ikke_kunne_oppdatere_andres_besvarelse() throws Exception {
        when(authService.hentAktorId(FNR_1)).thenReturn(AKTORID_1);
        when(authService.hentAktorId(FNR_2)).thenReturn(AKTORID_2);
        MvcResult mvcResult = mockMvc.perform(post("/api/behovsvurdering/svar?fnr=" + FNR_1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Svar()
                        .setDato(ZonedDateTime.now())
                        .setSpm("spm")
                        .setSvar("svar")
                ))).andReturn();

        Besvarelse besvarelse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Besvarelse.class);

        mockMvc.perform(post("/api/behovsvurdering/svar?fnr=" + FNR_2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new Svar()
                        .setBesvarelseId(besvarelse.getBesvarelseId())
                        .setDato(ZonedDateTime.now())
                        .setSpm("spm2")
                        .setSvar("svar2")
                ))).andExpect(status().isForbidden());
    }

}
