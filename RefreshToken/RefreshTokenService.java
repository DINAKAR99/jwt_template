package cgg.blogapp.blogapp.RefreshToken;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import cgg.blogapp.blogapp.entities.User;
import cgg.blogapp.blogapp.exceptions.RefreshTokenExpired;
import cgg.blogapp.blogapp.repos.UserRepo;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;
    @Autowired
    UserRepo userRepo;

    public RefreshToken createToken(String username) {
        RefreshToken token = null;
        token = RefreshToken.builder().rtoken(UUID.randomUUID().toString())
                .u1(userRepo.findByName(username))
                .expiry(Instant.now().plusMillis(300 * 60 * 1000)).build();
        try {
            refreshTokenRepo.save(token);

        } catch (DataIntegrityViolationException e) {
            System.out.println("in catch ");
            // finding the user by his username
            User user1 = userRepo.findByName(username);

            // find refresh token based on user
            RefreshToken rf2 = refreshTokenRepo.findByU1(user1);

            // deleet the old token
            refreshTokenRepo.delete(rf2);
            System.out.println("delete success");

            // now save the new token

            refreshTokenRepo.save(token);
            // RefreshToken save = refreshTokenRepo.save(token);

            // return save;
        }

        return token;

    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiry().compareTo(Instant.now()) < 0) {
            System.out.println("refresh token is deletimg....");
            refreshTokenRepo.delete(token);

            // throw refresh token expired exception upon encountering expired one
            throw new RefreshTokenExpired();
        }
        return token;
    }
}
