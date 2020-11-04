package com.tests.username.list.problem.db.repositories;

import com.tests.username.list.problem.db.entities.RestrictedWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictedWordsRepository extends JpaRepository<RestrictedWords, Long> {
}
