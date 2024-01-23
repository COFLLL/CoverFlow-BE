package com.coverflow.global.oauth2;

import com.coverflow.global.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.coverflow.global.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.coverflow.global.oauth2.userinfo.NaverOAuth2UserInfo;
import com.coverflow.global.oauth2.userinfo.OAuth2UserInfo;
import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.Role;
import com.coverflow.member.domain.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuthAttributes {

    private final String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private final OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(
            final String nameAttributeKey,
            final OAuth2UserInfo oauth2UserInfo
    ) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(
            final SocialType socialType,
            final String userNameAttributeName,
            final Map<String, Object> attributes
    ) {
        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofNaver(
            final String userNameAttributeName,
            final Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(
            final String userNameAttributeName,
            final Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(
            final String userNameAttributeName,
            final Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값)을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public Member toEntity(
            final SocialType socialType,
            final OAuth2UserInfo oauth2UserInfo
    ) {
        String email = UUID.randomUUID() + "@cofl.com";
        String age = "20-29";
        String gender = "Male";

        if (oauth2UserInfo.getEmail() != null) {
            email = oauth2UserInfo.getEmail();
        }
        if (oauth2UserInfo.getAge() != null) {
            age = oauth2UserInfo.getAge();
        }
        if (oauth2UserInfo.getGender() != null) {
            gender = oauth2UserInfo.getGender();
        }

        return Member.builder()
                .email(email)
                .nickname(email)
                .tag("취준생")
                .age(age)
                .gender(gender)
                .fishShapedBun(500)
                .status("가입")
                .role(Role.GUEST)
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .build();
    }
}
