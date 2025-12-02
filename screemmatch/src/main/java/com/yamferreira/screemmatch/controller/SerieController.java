package com.yamferreira.screemmatch.controller;

import com.yamferreira.screemmatch.dto.EpisodioDTO;
import com.yamferreira.screemmatch.dto.SerieDTO;
import com.yamferreira.screemmatch.model.Categoria;
import com.yamferreira.screemmatch.model.Serie;
import com.yamferreira.screemmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDTO> obterSeries() {
        return serieService.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTopCincoSeries() {
        return serieService.obterTopCincoSeries();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id) {
        return serieService.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id) {
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporada(@PathVariable Long id, @PathVariable Long numero) {
        return serieService.obterTemporada(id, numero);
    }

    @GetMapping("categoria/{nomeGenero}")
    public List<SerieDTO> obterCategoria(@PathVariable String nomeGenero) {
        return serieService.obterCategoria(nomeGenero);
    }

    @GetMapping("{id}/temporadas/top")
    public List<EpisodioDTO> obterTop5(@PathVariable Long id) {
        return serieService.obterTop5(id);
    }
}
