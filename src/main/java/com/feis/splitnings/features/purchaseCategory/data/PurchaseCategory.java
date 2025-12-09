package com.feis.splitnings.features.purchaseCategory.data;

import com.feis.splitnings.common.data.entity.BaseLookupTable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "purchase_category")
public class PurchaseCategory extends BaseLookupTable {
    @Column(name = "user_selectable", nullable = false)
    private Boolean userSelectable;
}

