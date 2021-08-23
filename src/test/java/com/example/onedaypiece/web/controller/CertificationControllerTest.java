package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.config.SecurityConfig;
import com.example.onedaypiece.service.CertificationService;
import com.example.onedaypiece.util.MockSpringSecurityFilter;
import com.example.onedaypiece.web.dto.request.certification.CertificationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CertificationController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        })
class CertificationControllerTest {

    public static final String URI_CERTIFICATION = "/api/certification"; // URI로 쓰이는 상수

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private CertificationService certificationService;

    private Principal mockPrincipal;
    private UserDetails member;

    @Autowired
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @BeforeEach
    void setUp () {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();

        String encodePwd = passwordEncoder().encode("qwer1234!");

        member = User.builder()
                .username("swcide@gmail.com")
                .password(encodePwd)
                .roles("MEMBER")
                .build();

        mockPrincipal = new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
    }


    @Test
    void createCertification() throws Exception {

        CertificationRequestDto certificationRequestDto =
                CertificationRequestDto.builder()
                        .postingId(1L)
                        .totalNumber(10L)
                        .build();
        //given
        String content = objectMapper.writeValueAsString(certificationRequestDto);

        given(certificationService.createCertification(certificationRequestDto,member))
                .willReturn(1L);

        mockMvc.perform(post(URI_CERTIFICATION)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(mockPrincipal))
                .andExpect(status().isOk());
    }
}