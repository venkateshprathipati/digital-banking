package com.novalabs.digitalbanking.audit.controller;

import com.novalabs.digitalbanking.audit.dto.AuditEventResponse;
import com.novalabs.digitalbanking.audit.service.AuditService;
import com.novalabs.digitalbanking.common.response.ApiResponse;
import com.novalabs.digitalbanking.common.response.ApiResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    private final ApiResponseFactory factory;

    /**
     * Get audit history for a resource.
     *
     */
    @GetMapping("/{resourceType}/{resourceId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<ApiResponse<List<AuditEventResponse>>> getResourceAuditHistory(
            @PathVariable String resourceType,
            @PathVariable String resourceId,
            HttpServletRequest request
    ) {
        List<AuditEventResponse> auditEvents = auditService.findByResource(
                resourceType,
                resourceId
        );

        return ResponseEntity.ok(
                factory.ok(
                        auditEvents,
                        "Audit history fetched successfully",
                        request.getRequestURI()
                )
        );
    }
}
