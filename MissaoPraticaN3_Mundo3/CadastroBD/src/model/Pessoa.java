package cadastrobd.model;

public record Pessoa(int idPessoa, String nome, String logradouro, String cidade, String estado, String telefone, String email) {

    //const padrão
    public Pessoa {
        validarCampos();
    }

    private void validarCampos() {
        if (idPessoa < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }
    }

    //método p exibir dados no console
    public void exibir() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Id: " + idPessoa +
                "\nNome: " + nome +
                "\nLogradouro: " + logradouro +
                "\nCidade: " + cidade +
                "\nEstado: " + estado +
                "\nTelefone: " + telefone +
                "\nE-mail: " + email;
    }
}
