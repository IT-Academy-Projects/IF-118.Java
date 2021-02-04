package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.BasicEntity;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.MaterialExpirationRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.MaterialExpirationRequest;
import com.softserve.itacademy.response.MaterialExpirationResponse;
import com.softserve.itacademy.service.ExpirationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.converters.MaterialExpirationConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpirationServiceImpl implements ExpirationService {
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final GroupRepository groupRepository;
    private final MaterialExpirationRepository materialExpirationRepository;
    private final MaterialExpirationConverter materialExpirationConverter;

    public ExpirationServiceImpl(MaterialRepository materialRepository, UserRepository userRepository, MailSender mailSender, GroupRepository groupRepository, MaterialExpirationRepository materialExpirationRepository, MaterialExpirationConverter materialExpirationConverter) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.groupRepository = groupRepository;
        this.materialExpirationRepository = materialExpirationRepository;
        this.materialExpirationConverter = materialExpirationConverter;
    }

    @Override
    @Transactional
    public void setMaterialExpiration(MaterialExpirationRequest materialExpirationRequest) {
        List<Integer> groupIds = materialExpirationRequest.getGroupIds();
        materialExpirationRepository.setMaterialExpiration(materialExpirationRequest.getExpirationDate(), materialExpirationRequest.getMaterialId(), groupIds);
        List<User> usersByGroupIds = userRepository.findAllByGroupIds(groupIds);
        if (!usersByGroupIds.isEmpty()) {
            usersByGroupIds.forEach(user -> mailSender.send(user.getEmail(), "SoftClass Lection time",
                    "Hello" + user.getName() + "! Check Your's courses on SoftClass. Teacher set expiration date for materials."));
        }
    }

    @Override
    public MaterialExpirationResponse getMaterialExpiration(Integer materialId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> groupIds;
        Optional<Material> material = materialRepository.findById(materialId);
        MaterialExpirationResponse expiration;
        if (material.isPresent()) {
            if(material.get().getOwnerId().equals(principal.getId())) {
                groupIds = groupRepository.findByOwnerId(principal.getId()).stream().map(BasicEntity::getId).collect(Collectors.toList());
            } else {
                groupIds = groupRepository.findAllByUserId((principal).getId()).stream().map(BasicEntity::getId).collect(Collectors.toList());
            }
            expiration = materialExpirationConverter.of(materialExpirationRepository.getMaterialExpiration(materialId, groupIds));
            if (expiration == null) {
                throw new NotFoundException("Expiration for this material for this group was not found");
            }
            return expiration;
        } else {
            throw new NotFoundException("Material with such id was not found");
        }
    }
}
