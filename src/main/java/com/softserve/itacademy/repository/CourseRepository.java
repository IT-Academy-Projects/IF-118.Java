package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update courses set courses.disabled = :disabled where courses.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    @Query(value = "select * from courses where owner_id=:id and disabled = false", nativeQuery = true)
    List<Course> findByOwnerId(Integer id);

    @Query(value = "select * from courses c inner join users_courses uc on c.id = uc.course_id where uc.user_id = :id and c.disabled = false", nativeQuery = true)
    List<Course> findAllByUserId(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update courses set courses.description = :description where courses.id = :id", nativeQuery = true)
    int updateDescription(Integer id, String description);

    @Query(value = "select * from courses where id in (:ids)", nativeQuery = true)
    Set<Course> findByIds (Set<Integer> ids);

    @Query(value = "select avatar from courses where id = :id", nativeQuery = true)
    byte[] getAvatarById(Integer id);

}
