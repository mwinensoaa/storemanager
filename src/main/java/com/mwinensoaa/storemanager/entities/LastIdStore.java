package com.mwinensoaa.storemanager.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LastIdStore {

   @Column @Id private String mKey;
   @Column  private String mValue;

   public LastIdStore(String nextProductId, String mValue){
      setMKey(nextProductId);
      setMValue(mValue);
   }

}
