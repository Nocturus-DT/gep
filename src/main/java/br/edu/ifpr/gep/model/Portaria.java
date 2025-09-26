package br.edu.ifpr.gep.model;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Portaria {
   private String emissor;
   private Integer numero;  // Removido acento para compatibilidade com PropertyValueFactory
   private LocalDate publicacao;  // Removido acento
   private String membro;

   public Portaria() {}
   
   @JsonCreator  // Anotação Jackson: Permite deserialização do JSON para este construtor
   public Portaria(@JsonProperty("emissor") String emissor,
                   @JsonProperty("numero") Integer numero,  // Nome sem acento no JSON
                   @JsonProperty("publicacao") LocalDate publicacao,  // Nome sem acento no JSON
                   @JsonProperty("membro") String membro) {
      this.emissor = emissor;
      this.numero = numero;
      this.publicacao = publicacao;
      this.membro = membro;
   }
   
   public String getEmissor() {
      return emissor;
   }
   public void setEmissor(String emissor) {
      this.emissor = emissor;
   }
   public Integer getNumero() {  // Renomeado sem acento
      return numero;
   }
   public void setNumero(Integer numero) {  // Renomeado sem acento
      this.numero = numero;
   }
   public LocalDate getPublicacao() {  // Renomeado sem acento
      return publicacao;
   }
   public void setPublicacao(LocalDate publicacao) {  // Renomeado sem acento
      this.publicacao = publicacao;
   }
   public String getMembro() {
      return membro;
   }
   public void setMembro(String membro) {
      this.membro = membro;
   }

   @Override
   public String toString() {
      return "Portaria[emissor=" + emissor + ", numero=" +  // Ajustado sem acento
             numero + ", publicacao=" + publicacao +  // Ajustado sem acento
             ", membro=" + membro + "]";
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) return false;
      if (this.getClass() != obj.getClass()) return false;
      if (this == obj) return true;

      Portaria p = (Portaria) obj;
      if (!this.emissor.equals(p.emissor)) return false;
      if (!this.numero.equals(p.numero)) return false;  // Ajustado sem acento
      if (!this.publicacao.equals(p.publicacao)) return false;  // Ajustado sem acento
      if (!this.membro.equals(p.membro)) return false;

      return true;
      /* Opcionalmente, trocar os "ifs" e o "return" acima por esta única linha
         return Objects.equals(emissor, p.emissor) &&
                Objects.equals(numero, p.numero) &&
                Objects.equals(publicacao, p.publicacao) &&
                Objects.equals(membro, p.membro);
      */
   }

   @Override
   public int hashCode() {
      return Objects.hash(emissor, membro, numero, publicacao);  // Ajustado sem acento
   }
}