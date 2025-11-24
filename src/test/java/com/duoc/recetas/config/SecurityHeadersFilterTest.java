package com.duoc.recetas.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SecurityHeadersFilterTest {

    private final SecurityHeadersFilter filter = new SecurityHeadersFilter();

    @Test
    void shouldAddSecurityAndCacheHeadersOnLogin() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", AppConstants.LOGIN_PATH);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(SecurityPolicies.CONTENT_SECURITY_POLICY, response.getHeader("Content-Security-Policy"));
        assertEquals("nosniff", response.getHeader("X-Content-Type-Options"));
        assertEquals("none", response.getHeader("X-Permitted-Cross-Domain-Policies"));
        assertEquals(SecurityPolicies.REFERRER_POLICY, response.getHeader("Referrer-Policy"));
        assertEquals(SecurityPolicies.EXTENDED_PERMISSIONS_POLICY, response.getHeader("Permissions-Policy"));
        assertEquals("no-store, no-cache, must-revalidate, max-age=0", response.getHeader("Cache-Control"));
        assertEquals("0", response.getHeader("Expires"));
    }
}
