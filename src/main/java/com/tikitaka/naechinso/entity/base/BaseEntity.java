package com.tikitaka.naechinso.entity.base;

import com.tikitaka.naechinso.constant.DeleteStatus;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/** 상속받으면 생성시간 업데이트시간
 * 생성한 사람, 업데이트한 사람 필드 자동으로 만들어주는 엔티티입니다
 * jpa의 audit(감시) 기능을 사용합니다 */
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
@Getter
public class BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "Text default 'N'")
    private DeleteStatus delStatus=DeleteStatus.N;

    public void changeDeleteStatus(){
        if(this.delStatus ==DeleteStatus.Y){this.delStatus =DeleteStatus.N;}
        else{this.delStatus=DeleteStatus.Y;}
    }
}
