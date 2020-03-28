package ba.unsa.etf.si.local_server.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRequest {
    private String orderBy;
    private Boolean descending;
    private Boolean discount;
    private Integer minPrice;
    private Integer maxPrice;
    private String name;
}
