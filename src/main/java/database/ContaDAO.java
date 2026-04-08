    package database;

import java.sql.*;
import conta.Conta;
import cliente.*;

public class ContaDAO {
    private static final String URL = "jdbc:sqlite:dados_bancarios.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Cria as tabelas caso não existam
    public static void inicializarBanco() {
        String sql_titular = "CREATE TABLE IF NOT EXISTS titular (" +
                     "id_titular INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nome TEXT NOT NULL," +
                     "cpf TEXT UNIQUE NOT NULL," +
                     "telefone TEXT," +
                     "logradouro TEXT," +
                     "numero TEXT," +
                     "bairro TEXT," +
                     "cidade TEXT," +
                     "cep TEXT);";
        
        String sql_empresa = "CREATE TABLE IF NOT EXISTS empresa (" +
                         "id_empresa INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "razao_social TEXT NOT NULL," +
                         "cnpj TEXT UNIQUE NOT NULL," +
                         "telefone TEXT," +
                         "logradouro TEXT," +
                         "numero TEXT," +
                         "bairro TEXT," +
                         "cidade TEXT," +
                         "cep TEXT);";
        
        String sql_conta = "CREATE TABLE IF NOT EXISTS contas (" +
                     "id_conta INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "numero_conta TEXT UNIQUE NOT NULL," +
                     //"agencia TEXT NOT NULL DEFAULT '1'," +
                     "saldo REAL DEFAULT 0.0," +
                     "id_titular INTEGER," +
                     "id_empresa INTEGER," +    
                     "FOREIGN KEY (id_titular) REFERENCES titular(id_titular)" +
                     "FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)" +
                     ");";
        
        //inclusão da tabela UF na inicialização do banco 07.04.2026
        String sql_uf = "CREATE TABLE IF NOT EXISTS 'UF'(ID INTEGER PRIMARY KEY AUTOINCREMENT, UF text, NOME_UF text);";
                
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql_titular);
            stmt.execute(sql_empresa);
            stmt.execute(sql_conta);
            //inclusão da tabela UF na inicialização do banco 07.04.2026
            stmt.execute(sql_uf);
            System.out.println("Tabelas verificadas/criadas com sucesso!");        
        } catch (SQLException e) {
            System.out.println("Erro ao iniciar banco: " + e.getMessage());
        }
    }
    
    //******************incluso o método salvarNoBanco 21.02.26
    // No ContaDAO, adicione "throws Exception" na assinatura 07.03.2026
    public static void salvarNoBanco(Conta conta) throws Exception {
    Pessoa titular = conta.getTitular();
    
    //Remova o try-catch de dentro do DAO para que o erro chegue até o seu CaixaEletronico 07.03.26
    //try (Connection conn = conectar()) {
        Connection conn = conectar();
        int idGerado = -1;

        // 1. Verifica se é Pessoa Física ou Jurídica e salva na tabela correspondente
        if (titular instanceof Fisica) {
            idGerado = salvarPessoaFisica(conn, (Fisica) titular);
            // 2. Salva a conta vinculada ao id_titular
            salvarConta(conn, conta, idGerado, "fisica");
        } 
        else if (titular instanceof Juridica) {
            idGerado = salvarPessoaJuridica(conn, (Juridica) titular);
            // 2. Salva a conta vinculada ao id_empresa
            salvarConta(conn, conta, idGerado, "juridica");
        }

    //} catch (SQLException e) {
        //System.out.println("Erro na operação: " + e.getMessage());
    }

    
    //******************incluso o método salvarPessoaFisica 22.02.26
    private static int salvarPessoaFisica(Connection conn, Fisica f) throws SQLException {
    String sql = "INSERT INTO titular (nome, cpf, telefone, logradouro, numero, cidade) VALUES (?,?,?,?,?,?)";
    
    // O parâmetro RETURN_GENERATED_KEYS serve para o banco nos devolver o ID criado
    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, f.getNomePessoa());
        pstmt.setString(2, f.getCpf());
        pstmt.setString(3, f.getTelefone());
        pstmt.setString(4, f.getE().getRua());
        pstmt.setInt(5, f.getE().getNumero());
        pstmt.setString(6, f.getE().getCidade());
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : -1; //forma simplificada de if/else
        
    }
}
    //******************incluso método salvarPessoaJuridica 23.02.26
    private static int salvarPessoaJuridica(Connection conn, Juridica j) throws SQLException {
    // SQL focado na tabela empresa
    String sql = "INSERT INTO empresa (razao_social, cnpj, telefone, logradouro, numero, cidade) VALUES (?,?,?,?,?,?)";
    
    // Preparando o comando e pedindo para o banco retornar o ID gerado automaticamente
    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        // Preenchendo os campos com os dados do objeto Juridica
        pstmt.setString(1, j.getNomePessoa()); // No Java você usa nomePessoa para Razão Social
        pstmt.setString(2, j.getCnpj());
        pstmt.setString(3, j.getTelefone());
        pstmt.setString(4, j.getE().getRua());
        pstmt.setInt(5, j.getE().getNumero());
        pstmt.setString(6, j.getE().getCidade());
        
        pstmt.executeUpdate();

        // Recuperando o ID (Primary Key) que o SQLite criou para esta empresa
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // Retorna o ID para ser usado na tabela conta
        }
        return -1;
    }
}
    

    //public static void salvarConta(Conta conta) {
    //    String sql = "INSERT INTO contas(numConta, titular, saldo) VALUES(?,?,?)";
    //    try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //        pstmt.setInt(1, conta.getNumConta());
    //        pstmt.setString(2, conta.getTitular().getNomePessoa());
    //        pstmt.setFloat(3, conta.getSaldo());
    //        pstmt.executeUpdate();
    //    } catch (SQLException e) {
    //        System.out.println(e.getMessage());
    //    }
    //}
    
    //atualizado 01.03.2026
    // Adicionamos os parâmetros idDono e tipoDono
    private static void salvarConta(Connection conn, Conta conta, int idDono, String tipoDono) throws SQLException {
        String sql;

        // Decidimos em qual coluna de "chave estrangeira" o ID vai entrar
        if (tipoDono.equals("fisica")) {
            sql = "INSERT INTO contas (numero_conta, saldo, id_titular) VALUES (?,?,?)";
        } else {
            sql = "INSERT INTO contas (numero_conta, saldo, id_empresa) VALUES (?,?,?)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, conta.getNumConta());
            pstmt.setFloat(2, conta.getSaldo());
            pstmt.setInt(3, idDono); // Aqui salvamos o "crachá" do dono, não o nome!

            pstmt.executeUpdate();
        }
    }

    public static void atualizarSaldo(int numConta, float novoSaldo) {
        String sql = "UPDATE contas SET saldo = ? WHERE numero_conta = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, novoSaldo);
            pstmt.setInt(2, numConta);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo" + e.getMessage());
        }
    }

    //adiciona a lógica de pegar o último número do banco de dados e adicionar 1, caso não tenha nenhum número ele criar a conta com 1001 07.03.26
    public static int buscarProximoNumeroConta() {
        // Busca o valor máximo da coluna numero_conta e tenta converter para inteiro
        String sql = "SELECT MAX(CAST(numero_conta AS INTEGER)) FROM contas";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int ultimoNumero = rs.getInt(1);
                // Se o banco estiver vazio, o rs.getInt(1) retornará 0
                if (ultimoNumero == 0) {
                    return 1001;
                }
                return ultimoNumero + 1;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar próximo número: " + e.getMessage());
        }
        return 1001; // Caso ocorra algum erro, inicia com o padrão
    }
    
    //método para validar a UF 07.04.2026
    public static boolean validarUF(String siglaUF) {
        //SQL conta quantas linhas correspondem à sigla fornecida
        String sql = "SELECT COUNT(*) FROM UF WHERE sigla = ?";
        
        try (Connection conn = conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, siglaUF.toUpperCase()); // Garante que compara em maiúsculas
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Se o count for maior que 0, a UF existe
                return rs.getInt(1) > 0;
            }
        }
        } catch (SQLException e) {
            System.out.println("Erro na validação da UF: " + e.getMessage());
        }
        return false; // Retorna falso se houver erro ou se não encontrar
    }
            
}
        
    
