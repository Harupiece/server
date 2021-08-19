//package com.example.onedaypiece.web.controller;
//
//import com.example.onedaypiece.config.SecurityConfig;
//import com.example.onedaypiece.service.PostingService;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.net.URI;
//import java.security.Principal;
//import java.util.Collections;
//
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = PostingController.class,
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = SecurityConfig.class
//                )
//        })
//class PostingControllerTest {
//
//    public static final String URI_POSTING = "/api/posting"; // URI로 쓰이는 상수
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private PostingService postingService;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    private Principal mockPrincipal;
//
//    private PostingCreateRequestDto postingCreateRequestDto;
//
//
//    @Autowired
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @BeforeEach
//    void setUp () {
//
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .alwaysDo(print())
//                .build();
//
//        postingCreateRequestDto =
//                PostingCreateRequestDto.builder()
//                        .postingImg("테스트포스팅이미지.jpg")
//                        .postingContent("테스트포스팅컨텐츠")
//                        .challengeId(1L)
//                        .build();
//
//        String encodePwd = passwordEncoder().encode("qwer1234!");
//
//        UserDetails member = User.builder()
//                .username("swcide@gmail.com")
//                .password(encodePwd)
//                .roles("MEMBER")
//                .build();
//        mockPrincipal = new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
//    }
//
//    @DisplayName("createPosting 정상 케이스")
//    @Test
//    void createPosting_정상() throws Exception {
//
//        //given
//        String name = mockPrincipal.getName();
//        System.out.println("name = " + name);
//
//        URI uri = UriComponentsBuilder.newInstance()
//                .path(URI_POSTING)
//                .build()
//                .toUri();
//
//        String content = objectMapper.writeValueAsString(postingCreateRequestDto);
//
//        // when
//        mockMvc.perform(post(URI_POSTING)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .principal(mockPrincipal))
//                .andExpect(status().isOk());
//
//        //then
//
//
//    }
//}