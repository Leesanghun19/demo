package SenierProject.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Food {
    @Id
    @GeneratedValue
    @Column(name="food_id")
    private Long id;

    private String name;

    private Long price;

    private LocalDateTime update;

    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "food")
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    public static Food createFood(Store store,String name,Long price){
        Food food= new Food();
        food.setStatus(FoodStatus.ONSALE);
        food.setStore(store);
        food.setPrice(price);
        food.setName(name);
        return food;
    }
}
