package com.kkks.pofolling.mypage.service;

import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.mypage.dto.CareerDTO;
import com.kkks.pofolling.mypage.dto.EducationDTO;
import com.kkks.pofolling.mypage.dto.ProfileResponseDTO;
import com.kkks.pofolling.mypage.dto.ProfileUpdateDTO;
import com.kkks.pofolling.mypage.entity.Career;
import com.kkks.pofolling.mypage.entity.Education;
import com.kkks.pofolling.mypage.repository.CareerRepository;
import com.kkks.pofolling.mypage.repository.EducationRepository;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;

    // 마이페이지 - 프로필 조회
    @Override
    @Transactional
    public ProfileResponseDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List<CareerDTO> careers = careerRepository.findByUserUserId(userId).stream()
                .map(c -> CareerDTO.builder()
                        .careerId(c.getCareerId())
                        .companyName(c.getCompanyName())
                        .position(c.getPosition())
                        .startedAt(c.getStartedAt())
                        .endedAt(c.getEndedAt())
                        .build())
                .collect(Collectors.toList());

        List<EducationDTO> educations = educationRepository.findByUserUserId(userId).stream()
                .map(e -> EducationDTO.builder()
                        .educationId(e.getEducationId())
                        .schoolName(e.getSchoolName())
                        .major(e.getMajor())
                        .degree(e.getDegree())
                        .admissionDate(e.getAdmissionDate())
                        .graduationDate(e.getGraduationDate())
                        .educationStatus(e.getEducationStatus())
                        .build())
                .collect(Collectors.toList());

        return ProfileResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .profileImage(user.getProfileImage())
                .phoneNumber(user.getPhoneNumber())
                .jobType(user.getJobType())
                .role(user.getRole())
                .careers(careers)
                .educations(educations)
                .build();
    }

    // 마이페이지 - 프로필 수정
    @Override
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateDTO updateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        if (updateDTO.getName() != null && !updateDTO.getName().isBlank()) {
            user.setName(updateDTO.getName());
        }

        if (updateDTO.getNickname() != null && !updateDTO.getNickname().isBlank()) {
            // 현재 닉네임과 다를 때만 중복 체크
            if (!user.getNickname().equals(updateDTO.getNickname())
                    && userRepository.existsByNickname(updateDTO.getNickname())) {
                throw new BusinessException(ExceptionCode.NICKNAME_ALREADY_EXISTS);
            }
            user.setNickname(updateDTO.getNickname());
        }

        if (updateDTO.getPhoneNumber() != null && !updateDTO.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(updateDTO.getPhoneNumber());
        }

        if (updateDTO.getProfileImage() != null && !updateDTO.getProfileImage().isBlank()) {
            user.setProfileImage(updateDTO.getProfileImage());
        }

        if (updateDTO.getJobType() != null) {
            user.setJobType(updateDTO.getJobType());
        }

        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            user.setPassword(updateDTO.getPassword()); // 추후 인코딩 필요
        }

        if (updateDTO.getCareers() != null && !updateDTO.getCareers().isEmpty()) {
            careerRepository.deleteAll(careerRepository.findByUserUserId(userId));
            List<Career> careers = updateDTO.getCareers().stream()
                    .map(dto -> Career.builder()
                            .user(user)
                            .companyName(dto.getCompanyName())
                            .position(dto.getPosition())
                            .startedAt(dto.getStartedAt())
                            .endedAt(dto.getEndedAt())
                            .build())
                    .collect(Collectors.toList());
            careerRepository.saveAll(careers);
        }

        if (updateDTO.getEducations() != null && !updateDTO.getEducations().isEmpty()) {
            educationRepository.deleteAll(educationRepository.findByUserUserId(userId));
            List<Education> educations = updateDTO.getEducations().stream()
                    .map(dto -> Education.builder()
                            .user(user)
                            .schoolName(dto.getSchoolName())
                            .major(dto.getMajor())
                            .degree(dto.getDegree())
                            .admissionDate(dto.getAdmissionDate())
                            .graduationDate(dto.getGraduationDate())
                            .educationStatus(dto.getEducationStatus())
                            .build())
                    .collect(Collectors.toList());
            educationRepository.saveAll(educations);
        }
    }

    // 닉네임 중복 확인
    @Override
    public boolean isNicknameAvailable(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        // 현재 닉네임이면 사용 가능
        if (user.getNickname().equals(nickname)) {
            return true;
        }

        // 다른 사람이 사용 중인지 확인
        return !userRepository.existsByNickname(nickname);
    }

}
