package cgg.blogapp.blogapp.RefreshToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cgg.blogapp.blogapp.entities.User;

@Repository
@Transactional
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByRtoken(String rtoken);

    RefreshToken findByU1(User u1);

}
