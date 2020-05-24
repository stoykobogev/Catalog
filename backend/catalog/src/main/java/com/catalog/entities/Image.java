package com.catalog.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {

    @Id
    @Column
    private String code;
    
    @Column
    @Basic(fetch = FetchType.LAZY)
    private byte[] bytes;
}
