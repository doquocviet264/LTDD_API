package com.ltdd.QuanLyThuChiAPI.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdDate"},
        allowGetters = true
)
public class Auditing {
}
