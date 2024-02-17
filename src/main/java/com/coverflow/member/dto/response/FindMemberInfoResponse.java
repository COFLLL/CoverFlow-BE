package com.coverflow.member.dto.response;

import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.Role;
import com.coverflow.member.domain.SocialType;

import java.time.LocalDateTime;

public record FindMemberInfoResponse(
        String email,
        String nickname,
        String tag,
        String age,
        String gender,
        int fishShapedBun,
        String status,
        LocalDateTime connected_at,
        Role role,
        SocialType socialType
) {
    public static FindMemberInfoResponse from(final Member member) {
        return new FindMemberInfoResponse(
                member.getEmail(),
                member.getNickname(),
                member.getTag(),
                member.getAge(),
                member.getGender(),
                member.getFishShapedBun(),
                member.getStatus(),
                member.getConnectedAt(),
                member.getRole(),
                member.getSocialType()
        );
    }
}
