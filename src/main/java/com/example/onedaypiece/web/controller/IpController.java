package com.example.onedaypiece.web.controller;

import com.example.onedaypiece.web.domain.Ip.Ip;
import com.example.onedaypiece.web.domain.Ip.IpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class IpController {

    private final IpRepository ipRepository;

    @GetMapping("")
    public void ipCollector() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String visitorIp = req.getHeader("X-FORWARDED-FOR");
        if (visitorIp == null) {
            visitorIp = req.getRemoteAddr();
        }
        Ip ip = new Ip(visitorIp);
        ipRepository.save(ip);
    }

    @GetMapping("/")
    public void ipCollector2() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String visitorIp = req.getHeader("X-FORWARDED-FOR");
        if (visitorIp == null) {
            visitorIp = req.getRemoteAddr();
        }
        Ip ip = new Ip(visitorIp);
        ipRepository.save(ip);
    }

    @GetMapping("/ip")
    public List<Ip> checkIp() {
        return ipRepository.findAll();
    }
}
