package com.tistory.whitepaek.demospringsecurityform.form;

import com.tistory.whitepaek.demospringsecurityform.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class SampleService {

    @Secured("ROLE_USER") // Get Spring, SpEL 미지원
//    @RolesAllowed("ROLE_USER") // Get Java, SpEL 미지원
//    @PreAuthorize("hasRole('USER')") // Method 실행 전 권한 확인, SpEL 지원
//    @PostAuthorize() // Method 실행 후 권한 확인, SpEL 지원
    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        System.out.println(authentication);
        System.out.println(userDetails.getUsername());
    }

    @Async
    public void asyncService() {
        SecurityLogger.log("Async Service");
        System.out.println("Async service is called.");
    }
}
