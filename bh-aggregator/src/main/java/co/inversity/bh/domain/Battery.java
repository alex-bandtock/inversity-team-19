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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Battery extends PanacheEntity {
    public String serialNumber;

    public String insights;

    public static Optional<Battery> findBySerialNumber(String serialNumber) {
        return find("serialNumber", serialNumber).firstResultOptional();
    }

    @Transactional
    public void addInsights(Set<String> insights) {
        if (this.insights == null) {
            this.insights = String.join("|", insights);
        } else {
            Set<String> strs = new HashSet<>(List.of(this.insights.split("\\|")));
            strs.addAll(insights);
            this.insights = String.join("|", insights);
        }
    }
}
