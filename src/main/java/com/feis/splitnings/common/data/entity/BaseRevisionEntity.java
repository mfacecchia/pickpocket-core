package com.feis.splitnings.common.data.entity;

import org.hibernate.envers.DefaultRevisionEntity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "revision_info")
public class BaseRevisionEntity extends DefaultRevisionEntity {
}
