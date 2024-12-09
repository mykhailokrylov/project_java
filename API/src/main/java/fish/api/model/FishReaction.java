package fish.api.model;

import javax.persistence.*;

@Entity
@Table(name = "fish_reactions", schema = "api_schema")
public class FishReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fish_id", nullable = false)
    private Fish fish;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reaction_type", nullable = false)
    private String reactionType;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}
