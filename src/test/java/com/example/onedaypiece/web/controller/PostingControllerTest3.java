package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.config.SecurityConfig;
import com.example.onedaypiece.service.PostingService;
import com.example.onedaypiece.util.MockSpringSecurityFilter;
import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
import com.example.onedaypiece.web.dto.request.posting.PostingUpdateRequestDto;
import com.example.onedaypiece.web.dto.response.posting.PostingListDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostingController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        })
class PostingControllerTest3 {

    public static final String URI_POSTING = "/api/posting"; // URI로 쓰이는 상수

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostingService postingService;

    private Principal mockPrincipal;
    private PostingCreateRequestDto postingCreateRequestDto;
    private PostingUpdateRequestDto postingUpdateRequestDto;



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

        UserDetails member = User.builder()
                .username("swcide@gmail.com")
                .password(encodePwd)
                .roles("MEMBER")
                .build();

        mockPrincipal = new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
    }


    @DisplayName("1-1_createPosting 정상 케이스")
    @Test
     public void createPosting_정상() throws Exception {
        postingCreateRequestDto =
                PostingCreateRequestDto.builder()
                        .postingImg("테스트포스팅이미지.jpg")
                        .postingContent("테스트포스팅컨텐츠")
                        .challengeId(1L)
                        .build();
        //given
        String content = objectMapper.writeValueAsString(postingCreateRequestDto);

        String email = mockPrincipal.getName();

        // when

        given(postingService.createPosting(postingCreateRequestDto,email))
                .willReturn(1L);

        mockMvc.perform(post(URI_POSTING)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(mockPrincipal))
                .andExpect(status().isOk());
        //then
    }

    @DisplayName("1-2_createPosting_콘텐츠_null")
    @Test
    public void createPosting_콘텐츠_null() throws Exception{
         postingCreateRequestDto =
                 PostingCreateRequestDto.builder()
                         .postingImg("테스트포스팅이미지.jpg")
                         .postingContent("")
                         .challengeId(1L)
                         .build();

        String content = objectMapper.writeValueAsString(postingCreateRequestDto);

        // when
        mockMvc.perform(post(URI_POSTING)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(mockPrincipal))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("2-1_getPosting")
    @Test
    public void getPosting() throws Exception {
        //given
        int page =1;
        Long challengeId = 1L;

        PostingListDto postingListDto = new PostingListDto();
        //when

        given(postingService.getPosting(page,challengeId))
                .willReturn(postingListDto);

        mockMvc.perform(get("/api/posting/1/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("3_getPosting")
    @Test
    public void updatePosting() throws Exception {
        //given
        Long postingId = 1L;
        String email = mockPrincipal.getName();

        postingUpdateRequestDto = PostingUpdateRequestDto
                .builder()
                .postingImg("테스트이미지")
                .postingContent("업데이트콘텐츠")
                .build();
        //when
        Long id =0L;

        given(postingService.updatePosting(postingId,email,postingUpdateRequestDto))
                .willReturn(id);

        String content = objectMapper.writeValueAsString(postingUpdateRequestDto);

        mockMvc.perform(put("/api/posting/update/1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(mockPrincipal))
                .andExpect(status().isOk());
    }

    @DisplayName("4_deletePosting")
    @Test
    public void deletePosting() throws Exception {
        //given
        Long postingId = 1L;
        String email = mockPrincipal.getName();

        //when
        Long id =0L;
        given(postingService.deletePosting(postingId,email))
                .willReturn(id);

        mockMvc.perform(delete("/api/posting/delete/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
                .andExpect(status().isOk());
    }

}