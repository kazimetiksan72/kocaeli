package tr.gov.kocaeli.sehir.catalog.domain;
import jakarta.persistence.*; import lombok.Getter; import java.util.UUID;
@Entity @Table(name="catalog_item") @Getter public class CatalogItem{@Id private UUID id;private String category;private String code;private String name;private String description;@Column(name="sort_order")private int sortOrder;private boolean active;protected CatalogItem(){}}

