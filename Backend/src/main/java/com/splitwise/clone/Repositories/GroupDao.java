package com.splitwise.clone.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.splitwise.clone.Entities.Group;

import jakarta.transaction.Transactional;

import java.util.*;

public interface GroupDao extends JpaRepository<Group, Integer> {

    public List<Group> findByGroupName(String groupName);

    @Query(value = "SELECT user.user_id, user.email, user.image_url, user.phone_no, user.user_name " +
            "FROM user " +
            "JOIN user_groups ON user_groups.user_id = user.user_id " +
            "WHERE user_groups.group_id = :groupId", nativeQuery = true)
    public List<Object[]> usersInGroupRaw(@Param("groupId") int groupId);

    //
    @Query(value = "select group_entity.group_id , group_entity.group_name, group_entity.image_url from group_entity join user_groups on user_groups.group_id=group_entity.group_id where user_groups.user_id= :userId ", nativeQuery = true)
    public Set<Group> userGroups(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_groups (group_id, user_id) VALUES (:groupId, :userId)", nativeQuery = true)
    void addMember(@Param("groupId") int groupId, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "delete from user_groups where user_id= :userId and group_id= :groupId",nativeQuery = true)
    public void deleteMember(@Param("groupId") int groupId,@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "delete from user_groups where group_id= :groupId",nativeQuery = true)
    public void deleteGroupData(@Param("groupId") int groupId);
}
