package br.edu.ifpr.gep.model.repository;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe imutável (somente getters e sem setters), uma vez que o objeto seja
 * criado, não há como modificar seu estado.
 * Esta classe serve unicamente a representar a parte 'key' da estrutura do
 * tipo mapa usada em PortariaRepository.
 */
public class PortariaPK {
   private String emissor;
   private Integer numero;  // Removido acento para consistência
   private Integer ano;

   @JsonCreator  // Anotação Jackson: Permite deserialização do JSON para este construtor
   public PortariaPK(@JsonProperty("emissor") String emissor,
                     @JsonProperty("numero") Integer numero,  // Nome sem acento no JSON
                     @JsonProperty("ano") Integer ano) {
      // mantém o emissor em letras minúsculas para facilitar a busca,
      // pois o programador pode passar o emissor escrito de diferentes
      // formas, com letras maiúsculas, minúsculas ou uma mistura de
      // ambos
      this.emissor = emissor.toLowerCase();
      this.numero = numero;
      this.ano = ano;
   }

   public String getEmissor() { return emissor; }
   public Integer getNumero() { return numero; }  // Renomeado sem acento
   public Integer getAno() { return ano; }

   @Override
   public String toString() {
       return emissor + "_" + numero + "_" + ano;
   }


   @Override
   public int hashCode() {
      return Objects.hash(emissor, numero, ano);  // Ajustado sem acento
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;

      PortariaPK other = (PortariaPK) obj;
      return Objects.equals(ano, other.ano) &&
             Objects.equals(emissor, other.emissor) &&
             Objects.equals(numero, other.numero);  // Ajustado sem acento
   }
}