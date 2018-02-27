package main.dao;

import main.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByEmail(String email);
    Collection<User> findTop10ByEmailContaining(String email);
}
