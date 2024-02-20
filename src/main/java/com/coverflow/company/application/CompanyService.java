package com.coverflow.company.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.*;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * [자동 완성 메서드]
     * 특정 이름으로 시작하는 회사 5개를 조회하는 메서드
     */
    public List<FindAutoCompleteResponse> autoComplete(final String name) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());
        final List<Company> companies = companyRepository.findByNameStartingWithAndStatus(name, pageable, "등록")
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));
        final List<FindAutoCompleteResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            findCompanies.add(i, FindAutoCompleteResponse.from(companies.get(i)));
        }
        return findCompanies;
    }

    /**
     * [회사 검색 메서드]
     * 특정 이름으로 시작하는 회사 n개를 조회하는 메서드
     */
    public List<SearchCompanyResponse> searchCompanies(final String name) {
        final List<Company> companies = companyRepository.findAllCompaniesStartingWithNameAndStatus(name + "%", "등록")
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));
        final List<SearchCompanyResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            findCompanies.add(i, SearchCompanyResponse.from(companies.get(i)));
        }
        return findCompanies;
    }

    /**
     * [특정 회사와 질문 조회 메서드]
     * 특정 회사와 질문 리스트를 조회하는 메서드
     */
    public FindCompanyResponse findCompanyById(final Long companyId) {
        final Company company = companyRepository.findRegisteredCompany(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));
        final Optional<List<Question>> optionalQuestions = companyRepository.findRegisteredQuestions(companyId);
        final List<QuestionDTO> questions = new ArrayList<>();

        if (optionalQuestions.isPresent()) {
            List<Question> questionList = optionalQuestions.get();
            for (int i = 0; i < questionList.size(); i++) {
                questions.add(i, new QuestionDTO(
                        questionList.get(i).getId(),
                        questionList.get(i).getMember().getNickname(),
                        questionList.get(i).getMember().getTag(),
                        questionList.get(i).getTitle(),
                        questionList.get(i).getContent(),
                        questionList.get(i).getViewCount(),
                        questionList.get(i).getAnswerCount(),
                        questionList.get(i).getReward(),
                        questionList.get(i).getCreatedAt()));
            }
        }

        return FindCompanyResponse.of(company, questions);
    }

    /**
     * [관리자 전용: 전체 회사 조회 메서드]
     * 전체 회사를 조회하는 메서드
     */
    public List<FindAllCompaniesResponse> findAllCompanies() {
        final List<Company> companies = companyRepository.findAllCompanies()
                .orElseThrow(CompanyException.CompanyNotFoundException::new);
        final List<FindAllCompaniesResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            findCompanies.add(i, FindAllCompaniesResponse.from(companies.get(i)));
        }
        return findCompanies;
    }

    /**
     * [관리자 전용: 특정 상태 회사 조회 메서드]
     * 특정 상태(검토/등록/삭제)의 회사를 조회하는 메서드
     */
    public List<FindPendingResponse> findPending(final String status) {
        final List<Company> companies = companyRepository.findByStatus(status)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(status));
        final List<FindPendingResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            findCompanies.add(i, FindPendingResponse.from(companies.get(i)));
        }
        return findCompanies;
    }

    /**
     * [회사 등록 메서드]
     */
    @Transactional
    public void saveCompany(final SaveCompanyRequest request) {
        if (companyRepository.findByName(request.name()).isPresent()) {
            throw new CompanyException.CompanyExistException(request.name());
        }

        final Company company = Company.builder()
                .name(request.name())
                .type(request.type())
                .city(request.city())
                .district(request.district())
                .establishment(request.establishment())
                .questionCount(0)
                .status("검토")
                .build();

        companyRepository.save(company);
    }

    /**
     * [관리자 전용: 회사 수정 메서드]
     */
    @Transactional
    public void updateCompany(final UpdateCompanyRequest request) {
        final Company company = companyRepository.findByName(request.name())
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(request.name()));

        company.updateCompany(Company.builder()
                .name(request.name())
                .type(request.type())
                .city(request.city())
                .district(request.district())
                .establishment(request.establishment())
                .status(request.status())
                .build());
    }

    /**
     * [관리자 전용: 회사 삭제 메서드]
     */
    @Transactional
    public void deleteCompany(final Long companyId) {
        final Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        company.updateStatus("삭제");
    }

    /**
     * [관리자 전용: 회사 물리 삭제 메서드]
     */
    public void deleteCompanyReal(final Long companyId) {
        final Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        companyRepository.delete(company);
    }
}
