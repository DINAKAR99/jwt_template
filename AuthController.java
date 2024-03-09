package cgg.blogapp.blogapp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cgg.blogapp.blogapp.RefreshToken.RTRequest;
import cgg.blogapp.blogapp.RefreshToken.RefreshToken;
import cgg.blogapp.blogapp.RefreshToken.RefreshTokenRepo;
import cgg.blogapp.blogapp.RefreshToken.RefreshTokenService;
import cgg.blogapp.blogapp.entities.User;
import cgg.blogapp.blogapp.entities.UserDTO;
import cgg.blogapp.blogapp.jwt.JwtRequest;
import cgg.blogapp.blogapp.jwt.JwtResponse;
import cgg.blogapp.blogapp.jwt.JwtService;
import cgg.blogapp.blogapp.repos.UserRepo;
import cgg.blogapp.blogapp.services.UserService;

@RestController
@CrossOrigin("*")
// @RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    JwtService service;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RefreshTokenRepo refreshTokenRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/dologin")
    public JwtResponse authbyNameAndPass(@RequestParam("username") String username,
            @RequestParam("password") String password) {

        JwtRequest req = new JwtRequest();

        req.setUsername(username);
        req.setPassword(password);

        System.out.println(req);

        boolean doAuthenticate = doAuthenticate(req.getUsername(), req.getPassword());

        if (doAuthenticate) {

            System.out.println(" authenticatted for genisis token generation ");
            // find user
            User user1 = userRepo.findByName(req.getUsername());
            // mapp it to dto
            UserDTO userdto1 = modelMapper.map(user1, UserDTO.class);
            // jwt token
            String jwtToken = service.generateJWTToken(req.getUsername());
            // refresh token
            RefreshToken refreshtoken = refreshTokenService.createToken(req.getUsername());

            return JwtResponse.builder().jwt_token(jwtToken).refresh_token(refreshtoken.getRtoken()).user(
                    userdto1)
                    .build();

        } else {

            throw new UsernameNotFoundException("user not founduuu");
        }

    }

    @PostMapping("/recharge")
    public JwtResponse recharge(@RequestBody RTRequest req) {

        return

        refreshTokenRepo.findByRtoken(req.token)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getU1)
                .map(u1 -> {
                    String jwttoken = service.generateJWTToken(u1.getName());
                    return JwtResponse.builder().jwt_token(jwttoken).refresh_token(req.getToken()).build();

                }).orElseThrow(() -> new RuntimeException("refresh token is not in the db"));
    }

    private boolean doAuthenticate(String username, String password) {

        System.out.println(username + "------------");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, password);

        try {

            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {

            throw new BadCredentialsException("invalid username or passowrd");
        }

        return true;

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(BadCredentialsException b) {

        return b.getMessage();

    }

}
