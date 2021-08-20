//package com.example.onedaypiece.web.controller;
//
//import com.example.onedaypiece.service.PostingService;
//import com.example.onedaypiece.util.MockSpringSecurityFilter;
//import com.example.onedaypiece.web.domain.certification.CertificationQueryRepository;
//import com.example.onedaypiece.web.domain.certification.CertificationRepository;
//import com.example.onedaypiece.web.domain.challenge.Challenge;
//import com.example.onedaypiece.web.domain.challenge.ChallengeRepository;
//import com.example.onedaypiece.web.domain.member.Member;
//import com.example.onedaypiece.web.domain.member.MemberRepository;
//import com.example.onedaypiece.web.domain.pointHistory.PointHistoryRepository;
//import com.example.onedaypiece.web.domain.posting.Posting;
//import com.example.onedaypiece.web.domain.posting.PostingQueryRepository;
//import com.example.onedaypiece.web.domain.posting.PostingRepository;
//import com.example.onedaypiece.web.dto.query.certification.CertificationQueryDto;
//import com.example.onedaypiece.web.dto.query.posting.PostingListQueryDto;
//import com.example.onedaypiece.web.dto.request.posting.PostingCreateRequestDto;
//import com.example.onedaypiece.web.dto.response.posting.PostingListDto;
//import com.example.onedaypiece.web.dto.response.posting.PostingResponseDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.security.Principal;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class PostingControllerTest2 {
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
//    @Autowired
//    private PostingRepository postingRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private ChallengeRepository challengeRepository;
//    @Autowired
//    private CertificationQueryRepository certificationQueryRepository;
//    @Autowired
//    private CertificationRepository certificationRepository;
//    @Autowired
//    private PointHistoryRepository pointHistoryRepository;
//    @Autowired
//    private PostingQueryRepository postingQueryRepository;
//
//    private Principal mockPrincipal;
//
//    private PostingCreateRequestDto postingCreateRequestDto;
//    private PostingResponseDto postingResponseDto;
//
//
//    @Autowired
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @BeforeEach
//    void setUp () {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .alwaysDo(print())
//                .build();
//
//        String encodePwd = passwordEncoder().encode("qwer1234!");
//
//        UserDetails member = User.builder()
//                .username("swcide@gmail.com")
//                .password(encodePwd)
//                .roles("MEMBER")
//                .build();
//
//        mockPrincipal = new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
//    }
//
//    @AfterEach
//    void tearDown() {
//        certificationRepository.deleteAll();
//        pointHistoryRepository.deleteAll();
//        postingRepository.deleteAll();
//    }
//
//    @DisplayName("1-1_createPosting 정상 케이스")
//    @Test
//     public void createPosting_정상() throws Exception {
//        postingCreateRequestDto =
//                PostingCreateRequestDto.builder()
//                        .postingImg("테스트포스팅이미지.jpg")
//                        .postingContent("테스트포스팅컨텐츠")
//                        .challengeId(1L)
//                        .build();
//        //given
//        String content = objectMapper.writeValueAsString(postingCreateRequestDto);
//
//        String email = mockPrincipal.getName();
//
//        // when
//
//        given(postingService.createPosting(postingCreateRequestDto,email))
//                .willReturn(1L);
//
//        mockMvc.perform(post(URI_POSTING)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .principal(mockPrincipal))
//                .andExpect(status().isOk());
//        //then
//    }
//
//    @DisplayName("1-2_createPosting_콘텐츠_null")
//    @Test
//    public void createPosting_콘텐츠_null() throws Exception{
//
//         postingCreateRequestDto =
//                 PostingCreateRequestDto.builder()
//                         .postingImg("테스트포스팅이미지.jpg")
//                         .postingContent("")
//                         .challengeId(1L)
//                         .build();
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
//                .andExpect(status().is4xxClientError());
//    }
//
//
//    @DisplayName("1-1_getPosting_정상")
//    @Test
//    public void getPosting() throws Exception {
//        //given
//        Challenge challenge = challengeRepository.findById(1L).get();
//        Member member = memberRepository.findByEmail(mockPrincipal.getName()).get();
//        for (int i = 1; i < 10; i++) {
//            Posting posting = Posting.builder()
//                    .postingContent("테스트 포스팅"+i)
//                    .postingImg("테스트 이미지"+i)
//                    .challenge(challenge)
//                    .member(member)
//                    .build();
//            Posting save = postingRepository.save(posting);
//        }
//        Long challengeId = challenge.getChallengeId();
//        int page =1;
//
//        Pageable pageable = PageRequest.of(page-1,6);
//
//        Slice<PostingListQueryDto> postingList =postingQueryRepository.findPostingList(challengeId,pageable);
//
//        List<CertificationQueryDto> certificationList = certificationQueryRepository.findAllByPosting(challengeId);
//
//        List<PostingResponseDto> postingResponseDtoList = postingList
//                .stream()
//                .map(posting -> PostingResponseDto.of(posting, certificationList))
//                .collect(Collectors.toList());
//
//        PostingListDto postingListDto = PostingListDto.createPostingListDto(postingResponseDtoList,postingList.hasNext());
//
//        //when
//
//
//        given(postingService.getPosting(page,challengeId))
//                .willReturn(postingListDto);
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/posting/1/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//    }
//
//
//    @DisplayName("1-updatePosting_정상")
//    @Test
//    public void updatePosting() throws Exception {
//        //given
//        postingCreateRequestDto =
//                PostingCreateRequestDto.builder()
//                        .postingImg("테스트포스팅이미지.jpg")
//                        .postingContent("테스트포스팅컨텐츠")
//                        .challengeId(1L)
//                        .build();
//        //given
//        String content = objectMapper.writeValueAsString(postingCreateRequestDto);
//
//        String email = mockPrincipal.getName();
//
//        // when
//
//        given(postingService.createPosting(postingCreateRequestDto,email))
//                .willReturn(1L);
//
//        mockMvc.perform(post(URI_POSTING)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//                .principal(mockPrincipal))
//                .andExpect(status().isOk());
//    }
//}