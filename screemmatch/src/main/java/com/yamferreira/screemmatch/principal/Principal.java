package com.yamferreira.screemmatch.principal;

import java.util.*;
import java.util.stream.Collectors;

import com.yamferreira.screemmatch.model.*;
import com.yamferreira.screemmatch.repository.SerieRepository;
import com.yamferreira.screemmatch.service.ConsumoApi;
import com.yamferreira.screemmatch.service.ConverterDados;
import com.yamferreira.screemmatch.model.Serie;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3950dc29";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por titulo
                    5 - Buscar séries por ator
                    6 - Buscar Top 5 séries
                    7 - Buscar série por categoria
                    8 - Buscar série personalizada
                    9 - Buscar episodio por trecho
                    10 - Buscar top 5 episodios
                    11 - Filtar episodios por data
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4 :
                    buscarSeriePorTitulo();
                    break;
                case 5 :
                    buscarSeriePorAtor();
                    break;
                case 6 :
                    buscarTopCincoSeries();
                    break;
                case 7 :
                    buscarSeriePorCategoria();
                    break;
                case 8 :
                    buscarSeriePersonalizada();
                    break;
                case 9 :
                    buscarEpisodioPorTrecho();
                    break;
                case 10 :
                    buscarTopCincoEpisodios();
                    break;
                case 11 :
                    buscarEpisodioPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
//        dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodios> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodios(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        } else {
            System.out.println("Serie não encontrada");
        }

//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
//
//        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
//        List<Episodios> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodios(t.numero(), d)))
//                .collect(Collectors.toList());
//
//        episodios.forEach(System.out::println);
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da serie: " + serieBuscada.get());
        } else {
            System.out.println("Serie não encontrada");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Escolha um ator para determinada serie: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de que valor");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas =
                repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries quem que " + nomeAtor + " trabalhou");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação " + s.getAvaliacao()));
    }

    private void buscarTopCincoSeries() {
        List<Serie> topCinco = repositorio.findTop5ByOrderByAvaliacaoDesc();
        topCinco.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Deseja buscar séries de qual categoria/genero");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println(("Séries da categoria " + nomeGenero));
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePersonalizada() {
        System.out.println("Quantas temporadas a série deve ter?");
        var numeroTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Qual avaliação minima a série deve ter?");
        var avaliacaoMinima = leitura.nextDouble();
        leitura.nextLine();

        List<Serie> listaSerie = repositorio
                .seriesPorTemporadaEAvaliacao(numeroTemporadas, avaliacaoMinima);
        System.out.println("Séries Recomendadas:");
        listaSerie.forEach(s ->
                System.out.println("Titulo: " + s.getTitulo() +" Avaliação " +
                        s.getAvaliacao() + " Temporadas " + s.getTotalTemporadas()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual trecho do episodio?");
        var trechoEpisodios = leitura.nextLine();

        List<Episodios> listaEpisodio = repositorio.episodiosPorTrecho(trechoEpisodios);
        listaEpisodio.forEach(s ->
                System.out.println("Serie: " + s.getSerie().getTitulo() + " Temporada: " + s.getTemporada()
                + " Episodio: " + s.getNumeroEpisodio() + " Titulo: " + s.getTitulo()));
    }

    private void buscarTopCincoEpisodios() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodios> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.println("Serie: " + e.getSerie().getTitulo() + " Temporada: " + e.getTemporada()
                            + " Episodio: " + e.getNumeroEpisodio() + " Titulo: " + e.getTitulo()
                    + " Avaliação " + e.getAvaliacao()));

        }
    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent())
            System.out.println("Digite o ano limite de lançamento");
        var anoLancamento = leitura.nextInt();
        leitura.nextLine();

            List<Episodios> episodioAno = repositorio.episodiosPorSerieEAno(serieBuscada,anoLancamento);

            episodioAno.forEach(System.out::println);

    }



}
