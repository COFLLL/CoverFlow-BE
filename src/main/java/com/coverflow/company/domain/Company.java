package com.coverflow.company.domain;

import com.coverflow.global.entity.BaseTimeEntity;
import com.coverflow.question.domain.Question;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_company")
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기업 고유 번호
    @Column
    private String name; // 이름
    @Column
    private String type; // 업종
    @Column
    private String city; // 도시
    @Column
    private String district; // 구
    @Column
    private String establishment; // 설립일
    @Column
    private int questionCount; // 질문 수

    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus; // 기업 상태 (검토/등록/삭제)

    @Builder.Default
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    public void updateCompany(final Company company) {
        this.name = company.getName();
        this.type = company.getType();
        this.city = company.getCity();
        this.district = company.getDistrict();
        this.establishment = company.getEstablishment();
        this.companyStatus = company.getCompanyStatus();
    }

    public void updateQuestionCount(final int questionCount) {
        this.questionCount = questionCount;
    }

    public void updateCompanyStatus(final CompanyStatus companyStatus) {
        this.companyStatus = companyStatus;
    }
}
