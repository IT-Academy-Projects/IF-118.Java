package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Image;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.projection.IdNameTupleProjection;
import com.softserve.itacademy.projection.UserFullTinyProjection;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.service.ImageService;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.UserConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final InvitationService invitationService;
    private final ImageService imageService;

    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder, InvitationService invitationService, ImageService imageService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.invitationService = invitationService;
        this.imageService = imageService;
    }

    @Override
    public UserFullTinyProjection findById(Integer id) {
        return userRepository.findProjectedById(id).orElseThrow(() -> new NotFoundException("User with such email was not found"));
    }

    @Override
    public IdNameTupleProjection findUserNameById(Integer id) {
        return userRepository.findUserProjectedById(id).orElseThrow(() -> new NotFoundException("User with such email was not found"));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userConverter::of).collect(Collectors.toList());
    }

    @Override
    public void updateDisabled(Integer id, Boolean disabled) {
        if (userRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException("User with such id was not found");
        }
    }


    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with such id was not found"));
    }

    @Override
    public void updateProfileInfo(Integer id, String name, String email) {
        if (userRepository.updateProfileInfo(id, name, email) == 0)
            throw new NotFoundException("User with such id was not found");
    }

    @Override
    public List<UserResponse> findByGroupId(Integer id) {
        return userRepository.findByGroupId(id).stream()
                .map(userConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void changePass(Integer id, String oldPass, String newPass) {

        if (passwordEncoder.matches(oldPass, getById(id).getPassword())) {
            userRepository.updatePass(id, passwordEncoder.encode(newPass));
        } else throw new OperationNotAllowedException("wrong current password");
    }

    @Override
    public void deleteInvitation(Integer id, Integer invitationId) {
        invitationService.delete(invitationId);
        userRepository.deleteInvitation(id);
    }

    @Override
    public void setAvatar(MultipartFile file, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Image avatar = user.getAvatar();
        byte[] compressedFile = imageService.compress(file);

        if (avatar != null) {
            avatar.setFile(compressedFile);
        } else {
            user.setAvatar(imageService.save(compressedFile));
        }
        userRepository.save(user);
    }

    @Override
    public byte[] getAvatar(Integer id) {
        return userRepository.getAvatarById(id);
    }

    @Override
    public UserResponse getUserById(Integer id) {
        return userConverter.of(getById(id));
    }


}
