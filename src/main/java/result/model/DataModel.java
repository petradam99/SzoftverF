package result.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DataModel {
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private ZonedDateTime created;
    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }
}