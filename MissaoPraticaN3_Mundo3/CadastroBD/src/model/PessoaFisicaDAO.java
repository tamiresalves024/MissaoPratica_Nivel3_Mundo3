package cadastrobd.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaDAO {

    private final ConectorBD conectorBD;

    public PessoaFisicaDAO(ConectorBD conectorBD) {
        this.conectorBD = conectorBD;
    }

    public PessoaFisicaDAO(cadastrobd.ConectorBD conectorBD) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    PessoaFisicaDAO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    PessoaFisicaDAO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public PessoaFisica getPessoa(int idPessoa) {
        try (Connection connection = conectorBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT p.nome, p.logradouro, p.cidade, p.estado, p.telefone, p.email, pf.cpf " +
                             "FROM dbo.Pessoa p " +
                             "INNER JOIN dbo.PessoaFisica pf ON p.idPessoa = pf.Pessoa_idPessoa " +
                             "WHERE p.idPessoa = ?")) {

            statement.setInt(1, idPessoa);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String logradouro = resultSet.getString("logradouro");
                    String cidade = resultSet.getString("cidade");
                    String estado = resultSet.getString("estado");
                    String telefone = resultSet.getString("telefone");
                    String email = resultSet.getString("email");
                    String cpf = resultSet.getString("cpf");
                    return new PessoaFisica(idPessoa, nome, logradouro, cidade, estado, telefone, email, cpf);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PessoaFisica> getPessoas() {
        List<PessoaFisica> pessoas = new ArrayList<>();
        try (Connection connection = conectorBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT p.idPessoa, p.nome, p.logradouro, p.cidade, p.estado, p.telefone, p.email, pf.cpf " +
                             "FROM dbo.Pessoa p " +
                             "INNER JOIN dbo.PessoaFisica pf ON p.idPessoa = pf.Pessoa_idPessoa")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idf = resultSet.getInt("idPessoa");
                    String nome = resultSet.getString("nome");
                    String logradouro = resultSet.getString("logradouro");
                    String cidade = resultSet.getString("cidade");
                    String estado = resultSet.getString("estado");
                    String telefone = resultSet.getString("telefone");
                    String email = resultSet.getString("email");
                    String cpf = resultSet.getString("cpf");
                    PessoaFisica pessoa = new PessoaFisica(idf, nome, logradouro, cidade, estado, telefone, email, cpf);
                    pessoas.add(pessoa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pessoas;
    }

    public int incluir(PessoaFisica pessoa) {
        try (Connection connection = conectorBD.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO dbo.Pessoa (nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, pessoa.getNome());
            statement.setString(2, pessoa.getLogradouro());
            statement.setString(3, pessoa.getCidade());
            statement.setString(4, pessoa.getEstado());
            statement.setString(5, pessoa.getTelefone());
            statement.setString(6, pessoa.getEmail());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idd = generatedKeys.getInt(1);
                    try (PreparedStatement statementPessoaFisica = connection.prepareStatement(
                            "INSERT INTO dbo.PessoaFisica (Pessoa_idPessoa, cpf) VALUES (?, ?)")) {
                        statementPessoaFisica.setInt(1, idd);
                        statementPessoaFisica.setString(2, pessoa.getCpf());
                        statementPessoaFisica.executeUpdate();
                    }
                    return idd;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void alterar(PessoaFisica pessoa) {
        try (Connection connection = conectorBD.getConnection();
             PreparedStatement statementPessoa = connection.prepareStatement(
                     "UPDATE dbo.Pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?");
             PreparedStatement statementPessoaFisica = connection.prepareStatement(
                     "UPDATE dbo.PessoaFisica SET cpf = ? WHERE Pessoa_idPessoa = ?")) {

            statementPessoa.setString(1, pessoa.getNome());
            statementPessoa.setString(2, pessoa.getLogradouro());
            statementPessoa.setString(3, pessoa.getCidade());
            statementPessoa.setString(4, pessoa.getEstado());
            statementPessoa.setString(5, pessoa.getTelefone());
            statementPessoa.setString(6, pessoa.getEmail());
            statementPessoa.setInt(7, pessoa.getIdPessoa());
            statementPessoa.executeUpdate();

            statementPessoaFisica.setString(1, pessoa.getCpf());
            statementPessoaFisica.setInt(2, pessoa.getIdPessoa());
            statementPessoaFisica.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(PessoaFisica pessoa) {
        try (Connection connection = conectorBD.getConnection();
             PreparedStatement statementPessoaFisica = connection.prepareStatement(
                     "DELETE FROM dbo.PessoaFisica WHERE Pessoa_idPessoa = ?");
             PreparedStatement statementPessoa = connection.prepareStatement(
                     "DELETE FROM dbo.Pessoa WHERE idPessoa = ?")) {

            statementPessoaFisica.setInt(1, pessoa.getIdPessoa());
            statementPessoaFisica.executeUpdate();

            statementPessoa.setInt(1, pessoa.getIdPessoa());
            statementPessoa.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class ConectorBD {

        public ConectorBD() {
        }

        private Connection getConnection() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}
