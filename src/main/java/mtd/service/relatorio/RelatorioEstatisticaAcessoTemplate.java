package mtd.service.relatorio;

import mtd.model.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public abstract class RelatorioEstatisticaAcessoTemplate {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Template Method (final para garantir o esqueleto imutavel do algoritmo)
    public final String gerarRelatorio(List<Usuario> usuarios, int totalProdutos) {
        int totalUsuarios = usuarios.size();
        int totalAcessos = calcularTotalAcessos(usuarios);
        double mediaAcessos = totalUsuarios > 0 ? (double) totalAcessos / totalUsuarios : 0.0;
        String usuarioMaisAtivo = identificarUsuarioMaisAtivo(usuarios);
        int totalEntidades = totalUsuarios + totalProdutos;
        String dataHora = LocalDateTime.now().format(FORMATTER);

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(montarCabecalho("Relatorio de Estatisticas de Acesso - MTD Marketplace", dataHora));
        relatorio.append(montarCorpoEstatisticas(totalUsuarios, totalAcessos, mediaAcessos, usuarioMaisAtivo, usuarios));
        relatorio.append(montarRodape(totalEntidades));

        return relatorio.toString();
    }

    private int calcularTotalAcessos(List<Usuario> usuarios) {
        return usuarios.stream()
                .mapToInt(Usuario::getQuantidadeAcessos)
                .sum();
    }

    private String identificarUsuarioMaisAtivo(List<Usuario> usuarios) {
        return usuarios.stream()
                .max(Comparator.comparingInt(Usuario::getQuantidadeAcessos))
                .map(u -> u.getNome() + " (" + u.getQuantidadeAcessos() + " acessos)")
                .orElse("Nenhum usuario registrado");
    }

    protected abstract String montarCabecalho(String titulo, String dataHoraGeracao);

    protected abstract String montarCorpoEstatisticas(
            int totalUsuarios,
            int totalAcessos,
            double mediaAcessos,
            String usuarioMaisAtivo,
            List<Usuario> usuarios
    );

    protected abstract String montarRodape(int totalEntidades);
}

