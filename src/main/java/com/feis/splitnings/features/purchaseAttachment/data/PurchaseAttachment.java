package com.feis.splitnings.features.purchaseAttachment.data;

import com.feis.splitnings.common.data.entity.BaseEntity;
import com.feis.splitnings.features.attachment.data.Attachment;
import com.feis.splitnings.features.purchase.data.Purchase;

import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
@Entity(name = "purchase_attachment")
public class PurchaseAttachment extends BaseEntity {
    @JoinColumn(name = "purchase_id", nullable = false)
    @ManyToOne
    private Purchase purchase;

    @JoinColumn(name = "attachment_id", nullable = false)
    @ManyToOne
    private Attachment attachment;
}

