package co.inversity.bh.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RawData extends PanacheEntity {

    public String time;
    @Lob
    public String payload;
    public String serialNumber;
    public Boolean processed;

    public static List<RawData> unprocessed() {
        return list("processed IS NULL OR processed = ?1", false);
    }
}
