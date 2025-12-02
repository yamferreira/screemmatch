package com.yamferreira.screemmatch.service;

import com.yamferreira.screemmatch.dto.EpisodioDTO;
import com.yamferreira.screemmatch.dto.SerieDTO;
import com.yamferreira.screemmatch.model.Categoria;
import com.yamferreira.screemmatch.model.Serie;
import com.yamferreira.screemmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    private List<SerieDTO> converteParaDTO(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
                        s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasAsSeries() {
        return converteParaDTO(repository.findAll());
    }

    public List<SerieDTO> obterTopCincoSeries() {
        return converteParaDTO(repository.findTop5ByOrderByAvaliacaoDesc());
    }


    public List<SerieDTO> obterLancamentos() {
        return converteParaDTO(repository.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
//Retornando o optional
        Optional<Serie> serie = repository.findById(id);

//Tratando se a serie em optional for ou não encontrada
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(),
                    s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .toList();
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporada(Long id, Long numero) {
        return repository.obterEpisodiosPorTempordas(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .toList();

    }

    public List<SerieDTO> obterCategoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return converteParaDTO(repository.findByGenero(categoria));
    }

    public List<EpisodioDTO> obterTop5(Long id) {
        Pageable pageable = PageRequest.of(0, 5);

        return repository.top5Episodios(id, pageable)
                .stream()
                .map(e -> new EpisodioDTO(
                        e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()
                ))
                .toList();
    }
}

