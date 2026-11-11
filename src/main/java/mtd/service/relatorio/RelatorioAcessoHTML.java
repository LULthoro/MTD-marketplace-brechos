package mtd.service.relatorio;

import mtd.model.Usuario;

import java.util.List;
import java.util.Locale;

public class RelatorioAcessoHTML extends RelatorioEstatisticaAcessoTemplate {

    @Override
    protected String montarCabecalho(String titulo, String dataHoraGeracao) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"pt-BR\">\n" +
                "<head><meta charset=\"UTF-8\"><title>" + titulo + "</title></head>\n" +
                "<body>\n" +
                "  <header>\n" +
                "    <h1>" + titulo + "</h1>\n" +
                "    <p>Gerado em: " + dataHoraGeracao + "</p>\n" +
                "  </header>\n";
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
        sb.append("  <section class=\"resumo\">\n")
          .append("    <ul>\n")
          .append("      <li><strong>Total de Usuarios:</strong> ").append(totalUsuarios).append("</li>\n")
          .append("      <li><strong>Total de Acessos no Sistema:</strong> ").append(totalAcessos).append("</li>\n")
          .append("      <li><strong>Media de Acessos por Usuario:</strong> ")
          .append(String.format(Locale.US, "%.2f", mediaAcessos)).append("</li>\n")
          .append("      <li><strong>Usuario Mais Ativo:</strong> ").append(usuarioMaisAtivo).append("</li>\n")
          .append("    </ul>\n")
          .append("  </section>\n")
          .append("  <table border=\"1\">\n")
          .append("    <thead><tr><th>ID</th><th>Nome</th><th>Email</th><th>Acessos</th></tr></thead>\n")
          .append("    <tbody>\n");

        for (Usuario u : usuarios) {
            sb.append("      <tr><td>").append(u.getId()).append("</td>")
              .append("<td>").append(u.getNome()).append("</td>")
              .append("<td>").append(u.getEmail()).append("</td>")
              .append("<td>").append(u.getQuantidadeAcessos()).append("</td></tr>\n");
        }

        sb.append("    </tbody>\n  </table>\n");
        return sb.toString();
    }

    @Override
    protected String montarRodape(int totalEntidades) {
        return "  <footer>\n" +
                "    <p>Total de Entidades Cadastradas no Marketplace: " + totalEntidades + "</p>\n" +
                "  </footer>\n" +
                "</body>\n" +
                "</html>";
    }
}
