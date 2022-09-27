package com.vegi.vegilabback.model;

import com.vegi.vegilabback.model.enums.DayEnum;
import com.vegi.vegilabback.model.enums.MomentEnum;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Planning {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    private MomentEnum moment;
    private DayEnum day;
}
