package com.example.garage.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carpapers")
public class CarPaper {

    //variables.........................................
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String licenseplate;

    @Lob
    public byte[] carPapers;

    //relations.........................................
    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "carpaper")
    private Car car;
}

