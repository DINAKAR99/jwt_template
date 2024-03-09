package cgg.blogapp.blogapp.jwt;

import cgg.blogapp.blogapp.entities.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    public String jwt_token;
    public String refresh_token;
    public UserDTO user;

}
