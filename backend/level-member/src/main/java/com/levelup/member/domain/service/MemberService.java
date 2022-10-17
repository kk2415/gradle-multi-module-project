package com.levelup.member.domain.service;

import com.levelup.common.domain.FileType;
import com.levelup.common.exception.EntityDuplicationException;
import com.levelup.common.exception.EntityNotFoundException;
import com.levelup.common.exception.ErrorCode;
import com.levelup.common.exception.FileNotFoundException;
import com.levelup.common.util.file.FileStore;
import com.levelup.common.util.file.UploadFile;
import com.levelup.event.events.*;
import com.levelup.member.domain.MemberPrincipal;
import com.levelup.member.domain.entity.Member;
import com.levelup.member.domain.entity.Role;
import com.levelup.member.domain.entity.RoleName;
import com.levelup.member.domain.service.dto.UpdateMemberDto;
import com.levelup.member.domain.repository.MemberRepository;
import com.levelup.member.domain.service.dto.MemberDto;
import com.levelup.member.domain.service.dto.UpdatePasswordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final FileStore fileStore;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberDto save(MemberDto dto, MultipartFile file) throws IOException {
        validateDuplicationMember(dto.getEmail(), dto.getNickname());

        UploadFile profileImage = fileStore.storeFile(FileType.MEMBER, file);

        Member member = dto.toEntity(profileImage);
        member.updatePassword(passwordEncoder.encode(member.getPassword()));
        member.addRole(Role.of(RoleName.ANONYMOUS, member));
        memberRepository.save(member);

        EventPublisher.raise(MemberCreatedEvent.of(member.getId(), member.getEmail(), member.getNickname()));

        return MemberDto.from(member);
    }

    private void validateDuplicationMember(String email, String nickname) {
        memberRepository.findByEmail(email)
                .ifPresent(user -> {throw new EntityDuplicationException(ErrorCode.EMAIL_DUPLICATION);});

        memberRepository.findByNickname(nickname)
                .ifPresent(user -> {throw new EntityDuplicationException(ErrorCode.NICKNAME_DUPLICATION);});
    }


    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "member", key = "#memberId")
    public MemberDto get(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberDto.from(member);
    }


    @CacheEvict(cacheNames = "member", key = "#memberId")
    public void update(UpdateMemberDto dto, Long memberId, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        UploadFile profileImage = storeUpdateProfileImage(member, file);
        member.update(dto.getNickname(), profileImage);

        EventPublisher.raise(MemberUpdatedEvent.of(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getProfileImage()));
    }

    private UploadFile storeUpdateProfileImage(Member member, MultipartFile file) throws IOException {
        if (member.getProfileImage() != null) {
            fileStore.deleteFile(member.getProfileImage().getStoreFileName());
        }

        return fileStore.storeFile(FileType.MEMBER, file);
    }

    public void updatePassword(UpdatePasswordDto dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updatePassword(passwordEncoder.encode(dto.getPassword()));
    }


    @CacheEvict(cacheNames = "member", key = "#memberId")
    public void delete(Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        EventPublisher.raise(MemberDeletedEvent.of(member.getId(), member.getEmail(), member.getNickname()));

        memberRepository.delete(member);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.error("start loadUserByUsername");

       final Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Collection<GrantedAuthority> authorities = new ArrayList<>(10);
        List<Role> roles = member.getRoles();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName().getName())));
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(RoleName.ADMIN.getName()));

        return new MemberPrincipal(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                isAdmin,
                true,
                true,
                true,
                true,
                authorities);
    }
}
