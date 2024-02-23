package com.coverflow.report.presentation;

import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.annotation.MemberAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import com.coverflow.report.application.ReportService;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.response.FindReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/report")
@RestController
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/find-report/{memberId}")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReportByMemberId(
            @PathVariable @Valid final UUID memberId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회원의 신고 조회에 성공했습니다.")
                        .data(reportService.findReportsByMemberId(memberId))
                        .build());
    }

    @GetMapping("/find-reports")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindReportResponse>>> findReports(
            @PathVariable @Valid final UUID memberId
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindReportResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회원의 신고 조회에 성공했습니다.")
                        .data(reportService.findReports())
                        .build());
    }

    @PostMapping("/save-report")
    @MemberAuthorize
    public ResponseEntity<ResponseHandler<Void>> saveReport(
            @RequestBody @Valid final SaveReportRequest saveReportRequest,
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        reportService.saveReport(saveReportRequest, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("신고 등록에 성공했습니다.")
                        .build());
    }
}