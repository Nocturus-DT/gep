package br.edu.ifpr.gep.model.repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.StringSearch;

public enum PortariaRepository {
    INSTANCE; // Única instância da classe (singleton)
    
    // a "base de dados" de portaias é uma coleção do tipo mapa (Map).
    // uma estrutura do tipo se assemelha a um dicionário, ou seja,
    // é constituídas de dois dados: uma chave (key) e um valor (value),
    // por isso, frequentemente, é denominada estrutra key-value.
    // o dados representando 'key' é usado para realizar buscas
    // (procuras) na estrutura, quando ela é encontrado, seu 'value'
    // torna-se disponível.
    // neste caso, um objeto PortariaPK representa a 'key' e um objeto
    // Portaria representa o 'value'.
    private final Map<PortariaPK, Portaria> portarias = new HashMap<>();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File dataFile = new File("portarias.json");
    
    PortariaRepository() {
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println("Iniciando repositório... Arquivo JSON: " + dataFile.getAbsolutePath());  // Mostra onde o JSON fica
        loadData();
        System.out.println("Dados carregados: " + portarias.size() + " portarias.");  // Verifica quantos carregaram
    }

    private void loadData() {
        if (dataFile.exists()) {
            System.out.println("JSON existe, tentando carregar...");  // Debug
            try {
                @SuppressWarnings("unchecked")
                Map<PortariaPK, Portaria> loaded = objectMapper.readValue(dataFile, 
                    objectMapper.getTypeFactory().constructMapType(Map.class, PortariaPK.class, Portaria.class));
                portarias.putAll(loaded);
                System.out.println("Carregamento OK: " + loaded.size() + " itens.");  // Confirma
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
                e.printStackTrace();  // Mostra o erro exato (ex.: mismatch de campos)
                portarias.clear();  // Limpa se falhar
            }
        } else {
            System.out.println("JSON não existe, iniciando vazio.");  // Confirma vazio
        }
    }
    
    private void saveData() {
        try {
            objectMapper.writeValue(dataFile, portarias);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    public boolean insert(Portaria portaria) {
        if (findPortaria(portaria.getEmissor(),
                portaria.getNumero(),
                portaria.getPublicacao().getYear()).isPresent()) {
            return false; // Portaria com emissor, número e ano da publicação já existe
        }

        PortariaPK pk = new PortariaPK(portaria.getEmissor(),
                portaria.getNumero(),
                portaria.getPublicacao().getYear());
        // quando o método put() retorna 'null' indica que não havia
        // um objeto associado ao objeto representado pela chave do
        // mapa
        boolean inserted = portarias.put(pk, portaria) == null;
        if (inserted) {
            saveData();
        }
        return inserted;
    }

    public boolean update(Portaria portaria) {
        Optional<Portaria> temp = findPortaria(portaria.getEmissor(),
                portaria.getNumero(),
                portaria.getPublicacao()
                        .getYear());

        if (temp.isPresent()) {
            Portaria p = temp.get();
            // emissor, número e ano (publicação) formam a chave do mapa
            // e não podem ser alterados
            p.setMembro(portaria.getMembro());
            // encontrou e alterou a portaria
            saveData();
            return true;
        }
        // não encontrou a portaria para alterar
        return false;
    }

    public boolean delete(String emissor, Integer numero, Integer ano) {
        PortariaPK pk = new PortariaPK(emissor, numero, ano);
        // se "pk" é encontrado na estrutura, o método remove() o
        // retornará para indicar que a exclusão foi bem sucedida
        boolean deleted = portarias.remove(pk) != null;
        if (deleted) {
            saveData();
        }
        return deleted;
    }

    public int delete() {
        // recupera a quantidade de "registros" na base de dados
        int total = portarias.size();
        // "limpa" os objetos da estrutura (exclui os registros da base de dados)
        portarias.clear();
        saveData();

        return total;
    }

    public List<Portaria> findByEmissor(String emissor, StringSearch searchMode) {
        if (emissor == null || emissor.isEmpty()) {
            return Collections.emptyList();
        }
        return portarias.entrySet()
                .stream()
                .filter(p -> search(p.getValue().getEmissor(), emissor, searchMode))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByNumero(Integer number) {
        if (number == null) return Collections.emptyList();
        return portarias.entrySet()
                .stream()
                .filter(p -> p.getValue().getNumero().equals(number))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByPeriodo(LocalDate start, LocalDate end) {
        if (start == null || end == null) return Collections.emptyList();

        return portarias.entrySet()
                .stream()
                .filter(p -> {
                    LocalDate date = p.getValue().getPublicacao();
                    return (date.isEqual(start) || date.isAfter(start)) &&
                            (date.isEqual(end) || date.isBefore(end));
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByPublicacao(LocalDate start) {
        if (start == null) return Collections.emptyList();

        return portarias.entrySet()
                .stream()
                .filter(p -> p.getValue().getPublicacao().isEqual(start))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByMembro(String nome, StringSearch searchMode) {
        if (nome == null || nome.isEmpty()) return Collections.emptyList();

        return portarias.entrySet()
                .stream()
                .filter(p -> search(p.getValue().getMembro(), nome, searchMode))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Optional<Portaria> findPortaria(String emissor, Integer numero, Integer ano) {
        PortariaPK pk = new PortariaPK(emissor, numero, ano);

        return portarias.entrySet().stream()
                .filter(p -> p.getKey().equals(pk))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public List<Portaria> findAll() {
        return new ArrayList<>(portarias.values());
    }

    private Boolean search(String str1, String str2, StringSearch searchMode) {
        switch (searchMode) {
            case EXACT:
                return str1.equals(str2);
            case EXACT_CASE_INSENSITIVE:
                return str1.equalsIgnoreCase(str2);
            case PARTIAL:
                return str1.contains(str2);
            case PARTIAL_CASE_INSENSITIVE:
                return str1.toLowerCase().contains(str2.toLowerCase());
        }
        return false;
    }
}