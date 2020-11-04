package com.tests.username.list.problem.db.repositories;

import com.tests.username.list.problem.db.entities.UsersList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserListRepository extends JpaRepository<UsersList, Long> {
    UsersList findByUsername(String username);
}
