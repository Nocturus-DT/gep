package br.edu.ifpr.gep.model.repository;

import java.util.Objects;

/**
 * Classe imutável (somente getters e sem setters), uma vez que o objeto seja
 * criado, não há como modificar seu estado.
 * Esta classe serve unicamente a representar a parte 'key' da estrutura do
 * tipo mapa usada em PortariaRepository.
 */
public class PortariaPK {
   private String  emissor;
   private Integer número;
   private Integer ano;

   public PortariaPK(String emissor, Integer número, Integer ano) {
	   // mantém o emissor em letras minúsculas para facilitar a busca,
	   // pois o programador pode passar o emissor escrito de diferentes
	   // formas, com letras maiúsculas, minúsculas ou uma mistura de
	   // ambos
	   this.emissor = emissor.toLowerCase();
	   this.número  = número;
	   this.ano     = ano;
   }

   public String getEmissor() { return emissor; }
   public Integer getNúmero() { return número; }
   public Integer getAno() { return ano; }

   @Override
   public String toString() {
	   return "PortariaPK [emissor=" + emissor + ", número=" + número +
			  ", ano=" + ano + "]";
   }

   @Override
   public int hashCode() {
	   return Objects.hash(emissor,número,ano);
   }

   @Override
   public boolean equals(Object obj) {
	if (this == obj) return true;
	if (obj == null) return false;
	if (getClass() != obj.getClass()) return false;

	PortariaPK other = (PortariaPK) obj;
	return Objects.equals(ano,other.ano) &&
		   Objects.equals(emissor,other.emissor) &&
		   Objects.equals(número,other.número);
   }
}
