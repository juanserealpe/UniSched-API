package co.unicauca.edu.unisched.domain.model;

public class Career {

    private final Long id;

    private final String name;

    public Career(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {return id;}
    public String getName() {return name;}
}