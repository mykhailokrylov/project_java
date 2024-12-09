package fish.api.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fish", schema = "api_schema")
public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Weight is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Weight must be a valid decimal number")
    @Column(nullable = false)
    private double weight;

    @NotNull(message = "Length is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Length must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Length must be a valid decimal number")
    @Column(nullable = false)
    private double length;

    @NotBlank(message = "Location is mandatory")
    @Size(max = 80, message = "Location cannot be longer than 80 characters")
    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "fish", cascade = CascadeType.ALL)
    private Set<FishReaction> reactions = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<FishReaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<FishReaction> reactions) {
        this.reactions = reactions;
    }

    public int getLikes() {
        return (int) reactions.stream()
                .filter(r -> "LIKE".equals(r.getReactionType()))
                .count();
    }

    public int getDislikes() {
        return (int) reactions.stream()
                .filter(r -> "DISLIKE".equals(r.getReactionType()))
                .count();
    }
}
