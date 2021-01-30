package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.IdNameTupleProjection;
import com.softserve.itacademy.projection.UserFullTinyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from users", nativeQuery = true)
    List<User> findAll();

    @Query(value = "select * from users u where u.email = ?", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "select * from users u where u.activation_code = ?", nativeQuery = true)
    Optional<User> findByActivationCode(String code);

    @Modifying
    @Transactional
    @Query(value = "update users u set u.email = ?1 where u.id = ?2", nativeQuery = true)
    int updateEmail(String email, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update users u set u.name = ?1 where u.id = ?2", nativeQuery = true)
    int updateName(String name, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update users set users.disabled = :disabled where users.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    @Modifying
    @Transactional
    @Query(value = "update User u set u.invitationCode = ?1 where u.email = ?2")
    int updateInvite(String code, String email);

    Optional<UserFullTinyProjection> findProjectedById(Integer id);

    Optional<IdNameTupleProjection> findUserProjectedById(Integer id);

    @Query(value = "SELECT * FROM users u JOIN groups_users gu ON u.id = gu.user_id JOIN student_groups sg ON gu.group_id = sg.id WHERE gu.group_id=:id", nativeQuery = true)
    List<User> findByGroupId(Integer id);

    @Modifying
    @Transactional
    @Query("update User u set u.password = ?2 where u.id = ?1")
    void updatePass(Integer id, String pass);

    @Modifying
    @Transactional
    @Query("update User u set u.invitationCode = null where u.id=?1")
    void deleteInvitation(Integer id);

    byte[] getAvatarById(Integer id);

    @Query(value = "SELECT `name` FROM users WHERE id = :id ", nativeQuery = true)
    String findNameById(Integer id);
}



