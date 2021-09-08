package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.service.two_factor_verif.SignUpResponse2fa;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpManager;
import ita.softserve.course_evaluation.service.two_factor_verif.TotpRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TotpController.class, SpringSecurityTestConfiguration.class})
class TotpControllerTest {
    public static final String API_TOTP_URL_CHANGE_STATUS = "/api/v1/totp/change2faStatus";

    @MockBean
    private TotpManager totpManager;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testUpdateStatus2FAEnable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SignUpResponse2fa expected = new SignUpResponse2fa(true, "Test image");
        TotpRequestDto requestDto = new TotpRequestDto();
        requestDto.setEmail("test@mail.com");
        requestDto.setActive2fa(true);

        doNothing().when(totpManager).switch2faStatus(Mockito.anyString(), Mockito.anyBoolean());
        when(totpManager.getUriForImage(Mockito.anyString())).thenReturn("Test image");

        String actual = mockMvc.perform(post(API_TOTP_URL_CHANGE_STATUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.using2FA").value(expected.isUsing2FA()))
                .andExpect(jsonPath("$.qrCodeImage").value(expected.getQrCodeImage()))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals(mapper.writeValueAsString(expected), actual);

        verify(totpManager, times(1)).getUriForImage(Mockito.anyString());
        verify(totpManager, times(1)).switch2faStatus(Mockito.anyString(), Mockito.anyBoolean());
        verifyNoMoreInteractions(totpManager);
    }

    @Test
    void testUpdateStatus2FADisable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SignUpResponse2fa expected = new SignUpResponse2fa(false, "Test image");
        TotpRequestDto requestDto = new TotpRequestDto();
        requestDto.setEmail("test@mail.com");
        requestDto.setActive2fa(false);

        doNothing().when(totpManager).switch2faStatus(Mockito.anyString(), Mockito.anyBoolean());

        String actual = mockMvc.perform(post(API_TOTP_URL_CHANGE_STATUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("2FA turned off"))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        assertEquals("2FA turned off", actual);

        verify(totpManager, times(1)).switch2faStatus(Mockito.anyString(), Mockito.anyBoolean());
        verifyNoMoreInteractions(totpManager);

    }
}