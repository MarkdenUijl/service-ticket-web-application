package nl.helvar.servicetickets.projects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProjectDTO {
    public Long id;
    public String name;
    public String address;
}
