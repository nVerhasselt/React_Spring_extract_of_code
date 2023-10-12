package com.itso.occupancy.occupancy.repository;

import com.itso.occupancy.occupancy.model.User;
import com.itso.occupancy.occupancy.model.WorkTask;

import io.netty.handler.codec.http2.Http2Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    //Get user by username
    Optional<User> findByUsernameAndIsDeletedIsFalseAndJobSupprimerIsNullOrJobSupprimerIsFalseAndIsDeletedIsFalseAndUsername(String username,String username2);

    //Get users by job
    List<User> findByJobIdAndIsDeletedIsFalseAndJobSupprimerIsFalse(Long id);

    //Get all existing users ordered by role Id
    List<User> IsDeletedIsFalseAndJobSupprimerIsNullOrJobSupprimerIsFalseAndIsDeletedIsFalseOrderByRoleId();

    //Get all existing users 
    List<User> JobSupprimerIsNullOrJobSupprimerIsFalse();

    List<User> findByRoleNameIsNotAndIsDeletedIsFalseAndJobSupprimerIsNullOrJobSupprimerIsFalseAndIsDeletedIsFalseAndRoleNameIsNot(String a, String b);
    
    //Get user by id wher isDeleted is false and JobSupprimer is False and JobSupprimer is Null 
    Optional<User> findByIdAndIsDeletedIsFalseAndJobSupprimerIsFalseOrJobSupprimerIsNullAndIsDeletedIsFalseAndId(Long id,Long id2);

    //Custom query to find users by their first name or related fields
    @Query("FROM User u " + // The query selects data from the "User" table, aliased as "u"
            "LEFT JOIN Role r ON r.id = u.role.id " + //performs a left join with the "Role" table, joining on the "id" columns of "Role" and "User"
            "WHERE " +
            "LOWER(u.firstName) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(u.lastName) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(r.name) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(u.email) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(u.username) LIKE '%' || LOWER(?1) || '%' "

            //LOWER converts the value of the u.firstName column to lowercase
            //LIKE is for pattern matching in SQL
            //'% || LOWER(?1) || '%' creates a pattern that matches any sequence of characters before and after the parameter value.

    )
    List<User> findByFirstName(String firstName);

    @Query("FROM User")
    List<User> findAllUsers();

   @Query("FROM User u " + // The query selects data from the "User" table, aliased as "u"
            "LEFT JOIN Role r ON r.id = u.role.id " + //performs a left join with the "Role" table, joining on the "id" columns of "Role" and "User"
            "LEFT JOIN Job j ON j.id = u.job.id " +
            "WHERE " +
            "LOWER(u.firstName) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(u.lastName) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(r.name) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(u.email) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(u.username) LIKE '%' || LOWER(?1) || '%' " +
            "OR LOWER(j.name) LIKE '%' || LOWER(?1) || '%' "
   )
    List<User> findAllUsersBySearch(String search);

    Optional<User> findByUsername(String username);
}
