package co.inversity.bh.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Battery extends PanacheEntity {
    public String serialNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "battery_insights")
    public Set<String> insights;

    public static Optional<Battery> findBySerialNumber(String serialNumber) {
        return find("serialNumber", serialNumber).firstResultOptional();
    }

    @Transactional
    public void addInsights(Set<String> insights) {
        if (this.insights == null) {
            this.insights = new HashSet<>();
        }
        this.insights.addAll(insights);
    }
}
