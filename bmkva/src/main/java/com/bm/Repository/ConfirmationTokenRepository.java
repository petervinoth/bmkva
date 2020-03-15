package com.bm.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.bm.Model.ConfirmationToken;

@Component
public interface ConfirmationTokenRepository  extends CrudRepository<ConfirmationToken,String>{

	ConfirmationToken findByConfirmationToken(String confirmationToken);
	
}
