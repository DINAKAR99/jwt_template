package cgg.blogapp.blogapp.RefreshToken;

import java.time.Instant;

import cgg.blogapp.blogapp.entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue
    public int id;
    public String rtoken;
    public Instant expiry;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User u1;
}
