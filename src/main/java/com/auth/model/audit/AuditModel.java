package com.auth.model.audit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel<T> {

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Column(name = "createdAt", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Column(name = "updatedAt", nullable = false)
    protected LocalDateTime updatedAt;

}
