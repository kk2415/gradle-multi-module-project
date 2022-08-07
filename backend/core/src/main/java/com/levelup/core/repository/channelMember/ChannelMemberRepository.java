package com.levelup.core.repository.channelMember;


import com.levelup.core.domain.channelMember.ChannelMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {

    List<ChannelMember> findByChannelIdAndMemberId(Long channelId, Long memberId);

    @EntityGraph(attributePaths = {"member", "channel"})
    @Query("select cm from ChannelMember cm where cm.channel.id = :channelId and cm.isWaitingMember = :isWaitingMember")
    Page<ChannelMember> findByChannelIdAndIsWaitingMember(@Param("channelId") Long channelId,
                                                          @Param("isWaitingMember") Boolean isWaitingMember,
                                                          Pageable pageable);

    Optional<List<ChannelMember>> findByChannelIdAndIsManager(Long channelId, Boolean isManager);

}
