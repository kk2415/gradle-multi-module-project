package com.together.levelup.repository.member;

import com.together.levelup.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    /**
     * 생성
     * */
    @Override
    public void save(Member member) {
        em.persist(member);
    }



    /**
     * 조회
     * */
    @Override
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public List<Member> findByChannelId(Long channelId) {
        String query = "select cm.member from ChannelMember cm join cm.channel c where c.id = :channelId";

        return em.createQuery(query, Member.class)
                .setParameter("channelId", channelId)
                .getResultList();
    }

    @Override
    public List<Member> findByChannelId(Long channelId, int page, int count) {
        int firstPage = (page - 1) * count; //0, 5, 10, 15
        int lastPage = page * count; //5, 10, 15, 20

        String query = "select cm.member from ChannelMember cm join cm.channel c where c.id = :channelId";

        return em.createQuery(query, Member.class)
                .setParameter("channelId", channelId)
                .setFirstResult(firstPage)
                .setMaxResults(lastPage)
                .getResultList();
    }

    @Override
    public List<Member> findWaitingMemberByChannelId(Long channelId) {
        String query = "select cm.waitingMember from ChannelMember cm join cm.channel c " +
                "where c.id = :channelId";

        return em.createQuery(query, Member.class)
                .setParameter("channelId", channelId)
                .getResultList();
    }

    @Override
    public List<Member> findWaitingMemberByChannelId(Long channelId, int page, int count) {
        int firstPage = (page - 1) * count; //0, 5, 10, 15
        int lastPage = page * count; //5, 10, 15, 20

        String query = "select cm.waitingMember from ChannelMember cm join cm.channel c " +
                "where c.id = :channelId";

        return em.createQuery(query, Member.class)
                .setParameter("channelId", channelId)
                .setFirstResult(firstPage)
                .setMaxResults(lastPage)
                .getResultList();
    }



    /**
     * 수정
     * */
    @Override
    public void delete(Long id) {
        Member findMember = findById(id);
        em.remove(findMember);
    }

}
