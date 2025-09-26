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

    private final Map<PortariaPK, Portaria> portarias = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File dataFile = new File("portarias.json");

    PortariaRepository() {
        objectMapper.registerModule(new JavaTimeModule());
        loadData();
    }

    private void loadData() {
        if (dataFile.exists()) {
            try {
                Map<PortariaPK, Portaria> loaded = objectMapper.readValue(dataFile,
                        objectMapper.getTypeFactory().constructMapType(Map.class, PortariaPK.class, Portaria.class));
                portarias.putAll(loaded);
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
            }
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
            p.setMembro(portaria.getMembro());
            saveData();
            return true;
        }
        return false;
    }

    public boolean delete(String emissor, Integer numero, Integer ano) {
        PortariaPK pk = new PortariaPK(emissor, numero, ano);
        boolean deleted = portarias.remove(pk) != null;
        if (deleted) {
            saveData();
        }
        return deleted;
    }

    public int delete() {
        int total = portarias.size();
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

    public List<Portaria> findByNumero(Integer number) {  // Mudado de findByNúmero
        if (number == null) return Collections.emptyList();
        return portarias.entrySet()
                .stream()
                .filter(p -> p.getValue().getNumero().equals(number))  // Mudado de getNúmero
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByPeriodo(LocalDate start, LocalDate end) {  // Mudado de findByPeríodo
        if (start == null || end == null) return Collections.emptyList();

        return portarias.entrySet()
                .stream()
                .filter(p -> {
                    LocalDate date = p.getValue().getPublicacao();  // Mudado de getPublicação
                    return (date.isEqual(start) || date.isAfter(start)) &&
                            (date.isEqual(end) || date.isBefore(end));
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByPublicacao(LocalDate start) {  // Mudado de findByPublicação
        if (start == null) return Collections.emptyList();

        return portarias.entrySet()
                .stream()
                .filter(p -> p.getValue().getPublicacao().isEqual(start))  // Mudado de getPublicação
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

    public Optional<Portaria> findPortaria(String emissor, Integer numero, Integer ano) {  // Mudado de número
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