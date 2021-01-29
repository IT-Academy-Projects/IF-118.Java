package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.BasicEntity;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MailReminder implements Reminder {
    private final MailSender mailSender;
    private final MaterialRepository materialRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public MailReminder(MailSender mailSender, MaterialRepository materialRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.materialRepository = materialRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void remind() {
        List<Material> collectedMaterials = materialRepository.findAllDueDateTimeExpiring();
        log.info("Selected {} materials with expiring dueDate", collectedMaterials.size());
        if (!collectedMaterials.isEmpty()) {
            Set<Integer> courseIds = collectedMaterials.stream().map(material -> material.getCourse().getId()).collect(Collectors.toSet());
            List<Group> groupsByCourseIds = groupRepository.findAllByCourseIds(courseIds);
            if (!groupsByCourseIds.isEmpty()) {
                List<Integer> groupIds = groupsByCourseIds.stream().map(BasicEntity::getId).collect(Collectors.toList());
                List<User> usersByGroupIds = userRepository.findAllByGroupIds(groupIds);
                if (!usersByGroupIds.isEmpty()) {
                    usersByGroupIds.forEach(user -> mailSender.send(user.getEmail(), "Lection Due Date!", "Finish"));
                }
            }
        }
    }
}
