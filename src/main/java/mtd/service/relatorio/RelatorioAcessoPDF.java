package mtd.service.relatorio;

import mtd.model.Usuario;

import java.util.List;
import java.util.Locale;

public class RelatorioAcessoPDF extends RelatorioEstatisticaAcessoTemplate {

    @Override
    protected String montarCabecalho(String titulo, String dataHoraGeracao) {
        return "%PDF-1.4 [MTD-MARKETPLACE-REPORT]\n" +
                "============================================================\n" +
                " [PDF DOCUMENT] " + titulo.toUpperCase() + "\n" +
                " Data de Emissao: " + dataHoraGeracao + "\n" +
                "============================================================\n";
    }

    @Override
    protected String montarCorpoEstatisticas(
            int totalUsuarios,
            int totalAcessos,
            double mediaAcessos,
            String usuarioMaisAtivo,
            List<Usuario> usuarios
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append(" [SUMARIO EXECUTIVO DE ACESSOS]\n")
          .append("  * Usuarios Cadastrados : ").append(totalUsuarios).append("\n")
          .append("  * Acessos Totais       : ").append(totalAcessos).append("\n")
          .append("  * Media por Usuario    : ").append(String.format(Locale.US, "%.2f", mediaAcessos)).append("\n")
          .append("  * Destaque de Acesso   : ").append(usuarioMaisAtivo).append("\n")
          .append("------------------------------------------------------------\n")
          .append(" [DETALHAMENTO POR USUARIO]\n");

        for (Usuario u : usuarios) {
            sb.append(String.format("  - [ID %02d] %-12s | %-20s | Acessos: %d%n",
                    u.getId(), u.getNome(), u.getEmail(), u.getQuantidadeAcessos()));
        }
        return sb.toString();
    }

    @Override
    protected String montarRodape(int totalEntidades) {
        return "------------------------------------------------------------\n" +
                " [RODAPE PDF] Total de Entidades (Usuarios + Produtos): " + totalEntidades + "\n" +
                "============================================================\n" +
                "%%EOF";
    }
}
