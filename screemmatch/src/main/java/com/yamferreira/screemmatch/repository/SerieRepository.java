package com.yamferreira.screemmatch.repository;

import com.yamferreira.screemmatch.model.Categoria;
import com.yamferreira.screemmatch.model.Episodios;
import com.yamferreira.screemmatch.model.Serie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);


    @Query("SELECT s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, Double avaliacao);

    @Query("SELECT e from Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodios%")
    List<Episodios> episodiosPorTrecho(String trechoEpisodios);

    @Query("SELECT e from Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodios> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serieBuscada AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodios> episodiosPorSerieEAno(Optional<Serie> serieBuscada, int anoLancamento);

    @Query ("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX (e.dataLancamento) DESC LIMIT 5")
    List<Serie> lancamentosMaisRecentes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodios> obterEpisodiosPorTempordas(Long id, Long numero);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id ORDER BY e.avaliacao DESC")
    List<Episodios> top5Episodios(@Param("id") Long id, Pageable pageable);
}
