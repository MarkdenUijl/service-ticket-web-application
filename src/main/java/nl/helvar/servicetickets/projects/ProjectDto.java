package nl.helvar.servicetickets.projects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProjectDto {

    public Long id;
    @Size(min = 5, max = 128)
    public String name;
    @Size(min = 5, max = 128)
    @Pattern(regexp = "^\\s*\\w+(\\s+\\w+)*\\s+\\d+,\\s+\\w+(\\s+\\w+)*$", flags = Pattern.Flag.CASE_INSENSITIVE)
    public String address;
}
