package br.edu.ifpr.gep.model;

import java.time.LocalDate;
import java.util.Objects;
 
public class Portaria {
   private String    emissor;
   private Integer   número;
   private LocalDate publicação;
   private String    membro;

   public Portaria() {}
   public Portaria(String emissor, Integer número,
		           LocalDate publicação, String membro) {
	   this.emissor    = emissor;
	   this.número     = número;
	   this.publicação = publicação;
	   this.membro     = membro;
   }
   public String getEmissor() {
	return emissor;
   }
   public void setEmissor(String emissor) {
	this.emissor = emissor;
   }
   public Integer getNúmero() {
	return número;
   }
   public void setNúmero(Integer número) {
	this.número = número;
   }
   public LocalDate getPublicação() {
	return publicação;
   }
   public void setPublicação(LocalDate publicação) {
	this.publicação = publicação;
   }
   public String getMembro() {
	return membro;
   }
   public void setMembro(String membro) {
	this.membro = membro;
   }

   @Override
   public String toString() {
	   return "Portaria[emissor=" + emissor + ", número=" +
              número + ", publicação=" + publicação +
              ", membro=" + membro + "]";
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) return false;
      if (this.getClass() != obj.getClass()) return false;
      if (this == obj) return true;

      Portaria p = (Portaria) obj;
      if (!this.emissor.equals(p.emissor)) return false;
      if (!this.número.equals(p.número)) return false;
      if (!this.publicação.equals(p.publicação)) return false;
      if (!this.membro.equals(p.membro)) return false;

      return true;
/* Opcionalmente, trocar os "ifs" e o "return" acima por esta única linha
      return Objects.equals(emissor,p.emissor) &&
             Objects.equals(número,p.número) &&
             Objects.equals(publicação,p.publicação) &&
             Objects.equals(membro,p.membro);
*/
   }

   @Override
   public int hashCode() {
	return Objects.hash(emissor, membro, número, publicação);
   }
   public void setData(LocalDate newData) {
	// TODO Auto-generated method stub
	
   }
}




