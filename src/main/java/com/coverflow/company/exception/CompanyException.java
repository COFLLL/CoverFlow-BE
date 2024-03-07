package com.coverflow.company.exception;

public class CompanyException extends RuntimeException {

    public CompanyException(final String message) {
        super(message);
    }

    public static class CompanyNotFoundException extends CompanyException {

        public CompanyNotFoundException() {
            super("회사가 존재하지 않습니다.");
        }

        public CompanyNotFoundException(final Object data) {
            super(String.format("회사가 존재하지 않습니다. - request info => %s", data));
        }
    }

    public static class CompanyExistException extends CompanyException {

        public CompanyExistException(final Object data) {
            super(String.format("이미 회사가 존재합니다. - request info => companyId : %s", data));
        }
    }
}
