package mtd.validator;

public class UsuarioValidator {

    private static final int NOME_MAXIMO_CARACTERES = 12;
    private static final int SENHA_MINIMO_CARACTERES = 8;
    private static final int SENHA_MAXIMO_CARACTERES = 128;
    private static final int SENHA_MINIMO_TIPOS_CARACTERES = 3;

    // -------------------------------------------------------------------------
    // Validacao de nome
    // -------------------------------------------------------------------------

    public void validarNome(String nome) {
        if (nomeVazio(nome)) {
            throw new IllegalArgumentException("O nome nao pode ser vazio.");
        }
        if (nomeExcedeCaracteresMaximos(nome)) {
            throw new IllegalArgumentException(
                    "O nome nao pode ter mais de " + NOME_MAXIMO_CARACTERES + " caracteres."
            );
        }
        if (nomeContemNumeros(nome)) {
            throw new IllegalArgumentException("O nome nao pode conter numeros.");
        }
    }

    private boolean nomeVazio(String nome) {
        return nome == null || nome.isBlank();
    }

    private boolean nomeExcedeCaracteresMaximos(String nome) {
        return nome.length() > NOME_MAXIMO_CARACTERES;
    }

    private boolean nomeContemNumeros(String nome) {
        return nome.matches(".*\\d.*");
    }

    // -------------------------------------------------------------------------
    // Validacao de senha
    // -------------------------------------------------------------------------

    public void validarSenha(String senha, String nome, String email) {
        if (senhaVazia(senha)) {
            throw new IllegalArgumentException("A senha nao pode ser vazia.");
        }
        if (senhaAbaixoDoMinimo(senha)) {
            throw new IllegalArgumentException(
                    "A senha deve ter no minimo " + SENHA_MINIMO_CARACTERES + " caracteres."
            );
        }
        if (senhaAcimaDoMaximo(senha)) {
            throw new IllegalArgumentException(
                    "A senha deve ter no maximo " + SENHA_MAXIMO_CARACTERES + " caracteres."
            );
        }
        if (senhaComPoucosTiposDeCaracteres(senha)) {
            throw new IllegalArgumentException(
                    "A senha deve conter ao menos " + SENHA_MINIMO_TIPOS_CARACTERES +
                    " dos seguintes tipos: maiusculas, minusculas, numeros, caracteres especiais (EX: ' ! @ e etc)."
            );
        }
        if (senhaIgualAoNome(senha, nome)) {
            throw new IllegalArgumentException("A senha nao pode ser identica ao nome.");
        }
        if (senhaIgualAoEmail(senha, email)) {
            throw new IllegalArgumentException("A senha nao pode ser identica ao email.");
        }
    }

    private boolean senhaVazia(String senha) {
        return senha == null || senha.isBlank();
    }

    private boolean senhaAbaixoDoMinimo(String senha) {
        return senha.length() < SENHA_MINIMO_CARACTERES;
    }

    private boolean senhaAcimaDoMaximo(String senha) {
        return senha.length() > SENHA_MAXIMO_CARACTERES;
    }

    private boolean senhaComPoucosTiposDeCaracteres(String senha) {
        int tipos = 0;
        if (senha.matches(".*[A-Z].*"))                    tipos++;
        if (senha.matches(".*[a-z].*"))                    tipos++;
        if (senha.matches(".*\\d.*"))                      tipos++;
        if (senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|'].*")) tipos++;
        return tipos < SENHA_MINIMO_TIPOS_CARACTERES;
    }

    private boolean senhaIgualAoNome(String senha, String nome) {
        return senha.equalsIgnoreCase(nome);
    }

    private boolean senhaIgualAoEmail(String senha, String email) {
        return senha.equalsIgnoreCase(email);
    }
}
