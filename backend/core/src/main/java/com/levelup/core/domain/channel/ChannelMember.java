package com.levelup.core.domain.channel;

import com.levelup.core.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "channel_member")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelMember {

    @Id @GeneratedValue
    @Column(name = "channel_member_id")
    private Long id;

    private Boolean isManager;
    private Boolean isWaitingMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_member_id")
    private Member waitingMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getChannelMembers().add(this);
    }


    //==생성 메서드==//
    public static ChannelMember createChannelMember(Member member, Boolean isManager, Boolean isWaitingMember) {
        ChannelMember channelMember = new ChannelMember();

        //여기서 연관관계 메서드를 실행하지 않으면 channel_member 테이블의 member_id 컬럼에 아무 값도 들어가지 않는다.
        channelMember.setMember(member);
        channelMember.setIsManager(isManager);
        channelMember.setIsWaitingMember(isWaitingMember);

        return channelMember;
    }

}
