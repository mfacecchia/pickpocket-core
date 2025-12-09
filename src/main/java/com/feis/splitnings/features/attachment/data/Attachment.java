package com.feis.splitnings.features.attachment.data;

import com.feis.splitnings.common.data.entity.BaseEntity;

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
@Entity(name = "attachment")
public class Attachment extends BaseEntity {
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "mimetype", nullable = true)
    private String mimetype;

    @Column(name = "size", nullable = false)
    private Long size;
}

