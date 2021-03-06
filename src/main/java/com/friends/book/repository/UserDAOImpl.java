package com.friends.book.repository;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.friends.book.domain.User;
import com.friends.book.exception.EntityNotFoundException;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO{

	@Autowired
	EntityManager em;
	
	@Override
	public void addUserProfile(User user) {
		em.createNativeQuery("INSERT INTO User (user_id, first_name, last_name,email_id,address,city,phone_number) VALUES (?,?,?,?,?,?,?)")
	      .setParameter(1, user.getUser_id())
	      .setParameter(2, user.getFirst_name())
	      .setParameter(3, user.getLast_name())
	      .setParameter(4, user.getEmail_id())
	      .setParameter(5, user.getAddress())
	      .setParameter(6, user.getCity())
	      .setParameter(7, user.getPhone_number())
	      .executeUpdate();
	}
	
	@Override
	public void addUserToFriendList(int userId, int friendId) throws EntityNotFoundException {
		
		if(em.find(User.class, friendId)!=null) {
			em.createNativeQuery("INSERT INTO friend_list (user_id, friend_user_id) VALUES (?,?)")
		      .setParameter(1, userId)
		      .setParameter(2, friendId)
		      .executeUpdate();
		}else {
			throw new EntityNotFoundException("User Profile does not exist");
		}
	}
	
	@Override
	public List<User> viewAllFriendsForUser(int id) {
		
		Query q = em.createNativeQuery("select * from user where user_id in (select friend_user_id from friend_list where user_id = ? Union select user_id from friend_list where friend_user_id = ?)");
		q.setParameter(1, id);
		q.setParameter(2, id);
		List<User> results = q.getResultList();
		return results;
	}

	@Override
	public User findById(int id) {
		return em.find(User.class, id);
	}

	@Override
	public List<User> findAll() {
		Query q = em.createQuery("select p from User as p");
		List<User> results = q.getResultList();
		return results;
	}

	@Override
	public void removeAnotherUserFromFriendList(int userId,int friendId) {
		// TODO Auto-generated method stub
		
		em.createNativeQuery("DELETE from friend_list where user_id = ? and friend_user_id = ?")
	      .setParameter(1, userId)
	      .setParameter(2, friendId)
	      .executeUpdate();
		
	}

}
