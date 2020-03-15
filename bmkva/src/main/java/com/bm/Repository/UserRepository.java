package com.bm.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.bm.Model.User;
@Component
@Repository("UserRepository")
public interface UserRepository extends CrudRepository<User,String> {
	User findByEmail(String email);

}
