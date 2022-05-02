package SenierProject.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue
    @Column(name="food_id")
    private Long id;

    private String name;

    private String price;

    private Float rateAverage;
    private Long rateNum;
    private String text;
    @Enumerated(EnumType.STRING)
    private FoodStatus status;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "food")
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;
    @OneToOne(mappedBy = "food")
    private Photo photo;
    public static Food createFood(Store store,String name,String price,String text){
        Food food= new Food();
        food.setStatus(FoodStatus.ONSALE);
        food.setStore(store);
        food.setPrice(price);
        food.setName(name);
        food.setRateAverage(null);
        food.setText(text);
        food.setRateNum(0L);
        return food;
    }
}
