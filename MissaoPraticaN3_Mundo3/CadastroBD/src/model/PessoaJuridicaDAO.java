package cadastrobd.model;

import cadastrobd.CadastroBD;
import cadastrobd.model.util.ConectorBD;
import cadastrobd.model.util.SequenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaDAO {
    private final ConectorBD conector;
    private final SequenceManager sequenceManager;

    public PessoaJuridicaDAO(ConectorBD conector, SequenceManager sequenceManager) {
        this.conector = conector;
        this.sequenceManager = sequenceManager;
    }

    PessoaJuridicaDAO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public PessoaJuridicaDAO(CadastroBD.ConectorBD conectorBD) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public PessoaJuridica getPessoa(int id) {
        PessoaJuridica pessoa = null;
        String sql = "SELECT * FROM Pessoa p INNER JOIN PessoaJuridica pj ON p.id = pj.id WHERE p.id = ?";

        try (PreparedStatement preparedStatement = conector.getPrepared(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    pessoa = new PessoaJuridica(
                            resultSet.getInt("id"),
                            resultSet.getString("nome"),
                            resultSet.getString("logradouro"),
                            resultSet.getString("cidade"),
                            resultSet.getString("estado"),
                            resultSet.getString("telefone"),
                            resultSet.getString("email"),
                            resultSet.getString("cnpj")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoa;
    }

    public List<PessoaJuridica> getPessoas() {
        List<PessoaJuridica> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM Pessoa p INNER JOIN PessoaJuridica pj ON p.id = pj.id";

        try (ResultSet resultSet = conector.getSelect(sql)) {
            while (resultSet.next()) {
                PessoaJuridica pessoa = new PessoaJuridica(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        resultSet.getString("logradouro"),
                        resultSet.getString("cidade"),
                        resultSet.getString("estado"),
                        resultSet.getString("telefone"),
                        resultSet.getString("email"),
                        resultSet.getString("cnpj")
                );
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoas;
    }

    public void incluir(PessoaJuridica pessoaJuridica) {
        String sqlPessoa = "INSERT INTO Pessoa (nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPessoaJuridica = "INSERT INTO PessoaJuridica (id, cnpj) VALUES (?, ?)";

        int novoId = sequenceManager.getValue("nome_da_sua_sequencia"); // Substitua pelo nome correto da sequência

        try {
            conector.getConnection().setAutoCommit(false);

            try (PreparedStatement preparedStatementPessoa = conector.getPrepared(sqlPessoa)) {
                preparedStatementPessoa.setString(1, pessoaJuridica.getNome());
                preparedStatementPessoa.setString(2, pessoaJuridica.getLogradouro());
                preparedStatementPessoa.setString(3, pessoaJuridica.getCidade());
                preparedStatementPessoa.setString(4, pessoaJuridica.getEstado());
                preparedStatementPessoa.setString(5, pessoaJuridica.getTelefone());
                preparedStatementPessoa.setString(6, pessoaJuridica.getEmail());
                preparedStatementPessoa.execute();
            }

            try (PreparedStatement preparedStatementPessoaJuridica = conector.getPrepared(sqlPessoaJuridica)) {
                preparedStatementPessoaJuridica.setInt(1, novoId);
                preparedStatementPessoaJuridica.setString(2, pessoaJuridica.getCnpj());
                preparedStatementPessoaJuridica.execute();
            }

            conector.getConnection().commit();
            conector.getConnection().setAutoCommit(true);

        } catch (SQLException e) {
            try {
                conector.getConnection().rollback();
                conector.getConnection().setAutoCommit(true);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void alterar(PessoaJuridica pessoaJuridica) {
        String sqlPessoa = "UPDATE Pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE id = ?";
        String sqlPessoaJuridica = "UPDATE PessoaJuridica SET cnpj = ? WHERE id = ?";

        try {
            conector.getConnection().setAutoCommit(false);

            try (PreparedStatement preparedStatementPessoa = conector.getPrepared(sqlPessoa)) {
                preparedStatementPessoa.setString(1, pessoaJuridica.getNome());
                preparedStatementPessoa.setString(2, pessoaJuridica.getLogradouro());
                preparedStatementPessoa.setString(3, pessoaJuridica.getCidade());
                preparedStatementPessoa.setString(4, pessoaJuridica.getEstado());
                preparedStatementPessoa.setString(5, pessoaJuridica.getTelefone());
                preparedStatementPessoa.setString(6, pessoaJuridica.getEmail());
                preparedStatementPessoa.setInt(7, pessoaJuridica.getId());
                preparedStatementPessoa.execute();
            }

            try (PreparedStatement preparedStatementPessoaJuridica = conector.getPrepared(sqlPessoaJuridica)) {
                preparedStatementPessoaJuridica.setString(1, pessoaJuridica.getCnpj());
                preparedStatementPessoaJuridica.setInt(2, pessoaJuridica.getId());
                preparedStatementPessoaJuridica.execute();
            }

            conector.getConnection().commit();
            conector.getConnection().setAutoCommit(true);

        } catch (SQLException e) {
            try {
                conector.getConnection().rollback();
                conector.getConnection().setAutoCommit(true);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sqlPessoaJuridica = "DELETE FROM PessoaJuridica WHERE id = ?";
        String sqlPessoa = "DELETE FROM Pessoa WHERE id = ?";

        try {
            conector.getConnection().setAutoCommit(false);

            try (PreparedStatement preparedStatementPessoaJuridica = conector.getPrepared(sqlPessoaJuridica)) {
                preparedStatementPessoaJuridica.setInt(1, id);
                preparedStatementPessoaJuridica.execute();
            }

            try (PreparedStatement preparedStatementPessoa = conector.getPrepared(sqlPessoa)) {
                preparedStatementPessoa.setInt(1, id);
                preparedStatementPessoa.execute();
            }

            conector.getConnection().commit();
            conector.getConnection().setAutoCommit(true);

        } catch (SQLException e) {
            try {
                conector.getConnection().rollback();
                conector.getConnection().setAutoCommit(true);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void excluir(PessoaJuridica pessoaJuridica) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static class ConectorBD {

        public ConectorBD() {
        }

        private PreparedStatement getPrepared(String sql) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private ResultSet getSelect(String sql) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private Object getConnection() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }

    private static class SequenceManager {

        public SequenceManager() {
        }

        private int getValue(String idPessoa) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}
