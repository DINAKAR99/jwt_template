package cgg.blogapp.blogapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private String resourcename;
    private String fieldval;
    private String feildname;

    public ResourceNotFoundException(String resourcename, String feildname, int userid) {

        super(String.format("%s not found with %s :%s", resourcename, feildname, userid));

    }

}
