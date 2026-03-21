package caixaeletronico;

import cliente.*;
import conta.Conta;
import java.util.Scanner;
//import java.util.ArrayList;
import database.ContaDAO;
import java.sql.*;

public class CaixaEletronico {

    // 1. Array Dinâmico de Contas (substituindo o array fixo de Pessoas)
    //private static final ArrayList<Conta> listaContas = new ArrayList<>();
    //private static int contadorContas = 1001; // Para gerar números de conta automáticos

    public static void main(String[] args) {
    
    // 1. Cria o arquivo e as tabelas se elas não existirem
    ContaDAO.inicializarBanco();
        
        
        Scanner tec = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("\n--- MENU CAIXA ELETRÔNICO ---");
            System.out.println("1 - Cadastrar Pessoa Física");
            System.out.println("2 - Cadastrar Pessoa Jurídica");
            System.out.println("3 - Listar Contas e Clientes");
            System.out.println("4 - Depositar");
            System.out.println("5 - Sacar");
            System.out.println("6 - Consultar Saldo");
            System.out.println("7 - Sair");
            System.out.print("Opção: ");
            opcao = tec.nextInt();
            tec.nextLine(); // Limpa buffer

            switch(opcao) {
                case 1: 
                    cadastrarNovoCliente(tec, new Fisica());
                    break;
                case 2: 
                    cadastrarNovoCliente(tec, new Juridica());
                    break;
                case 3: 
                    imprimirContas(); 
                    break;
                case 4: 
                    realizarOperacao(tec, "deposito");
                    break;
                case 5: 
                    realizarOperacao(tec, "saque");
                    break;
                case 6: 
                    realizarOperacao(tec, "saldo");
                    break;
                case 7: 
                    System.out.println("Saindo..."); 
                    break;
                default: 
                    System.out.println("Opção inválida!");
            }
        }
    }

    // Método auxiliar para criar a conta junto com o cliente
    private static void cadastrarNovoCliente(Scanner tec, Pessoa novaPessoa) {
        novaPessoa.cadastra(tec); // Chama o cadastro da pessoa (Física ou Jurídica)// Preenche nome, telefone, endereço, CPF/CNPJ
        
        Conta novaConta = new Conta();
        novaConta.setTitular(novaPessoa);
        
        // AQUI: O sistema vai no banco, olha o último e soma 1 07.03.26
        int proximoNumero = ContaDAO.buscarProximoNumeroConta();
        novaConta.setNumConta(proximoNumero); // Define número e incrementa para o próximo
        novaConta.setSaldo(0); // Começa com saldo zero
        
        //Antes
        //listaContas.add(novaConta); // Guarda no Array Dinâmico
        // Agora: Chama o DAO para gravar permanentemente no SQLite
        try {
            // Movemos a mensagem de sucesso para DENTRO do try. 
            // Se o salvarNoBanco der erro, ele pula direto para o catch e não lê esta linha.
            ContaDAO.salvarNoBanco(novaConta);
                System.out.println("Conta número " + novaConta.getNumConta() + " criada com sucesso e salva no banco de dados!");
        } catch (Exception e) {
            System.out.println("Não foi possível salvar a conta." + e.getMessage()
            );
        }
    }

    // Método para buscar uma conta pelo número e realizar operações
    
    //private static void realizarOperacao(Scanner tec, String tipo) {
    //    System.out.print("Digite o número da conta: ");
    //    int numero = tec.nextInt();
    //    
    //    for (Conta conta : listaContas) {
    //        if (conta.getNumConta() == numero) {
    //            if (tipo.equals("deposito")) {
    //                System.out.print("Valor do depósito: ");
    //                float valor = tec.nextFloat();
    //                conta.setSaldo(conta.getSaldo() + valor);
    //                System.out.println("Depósito realizado!");
    //            } else if (tipo.equals("saque")) {
    //                System.out.print("Valor do saque: ");
    //                float valor = tec.nextFloat();
    //                if (valor <= conta.getSaldo()) {
    //                    conta.setSaldo(conta.getSaldo() - valor);
    //                    System.out.println("Saque realizado!");
    //                } else {
    //                    System.out.println("Saldo insuficiente!");
    //                }
    //            } else if (tipo.equals("saldo")) {
    //                System.out.println("Titular: " + conta.getTitular().getNomePessoa());
    //                System.out.println("Saldo atual: R$ " + conta.getSaldo());
    //            }
    //            return;
    //        }
    //    }
    //    System.out.println("Conta não encontrada!");
    //}
    
    //private static void realizarOperacao(Scanner tec, String tipo) {
    //System.out.print("Digite o número da conta: ");
    //int numero = tec.nextInt();
    
    // Buscamos os dados necessários no banco
    //String sql = "SELECT titular, saldo FROM contas WHERE numConta = ?";
    //String sql = "SELECT " +
    //             " c.saldo, " +
    //             " t.nome AS nome_pessoa, " +
    //             " e.razao_social AS nome_empresa " +
    //             " FROM contas c " +
    //             " LEFT JOIN titular t ON c.id_titular = t.id_titular " +
    //             " LEFT JOIN empresa e ON c.id_empresa = e.id_empresa" + // Espaço adicionado
    //            " WHERE c.numero_conta = ?";
    //
    //try (Connection conn = ContaDAO.conectar(); 
    //     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //    
    //    pstmt.setInt(1, numero);
    //    ResultSet rs = pstmt.executeQuery();
    //
    //    if (rs.next()) {
    //        float saldoAtual = rs.getFloat("saldo");
    //        //String nome = rs.getString("titular");
    //        // Lógica para decidir qual nome exibir
    //        String nomeExibicao;
    //            if (rs.getString("nome_pessoa") != null) {
    //                nomeExibicao = rs.getString("nome_pessoa") + " (PF)";
    //            } else if (rs.getString("nome_empresa") != null) {
    //                nomeExibicao = rs.getString("nome_empresa") + " (PJ)";
    //            } else {
    //                nomeExibicao = "Titular desconhecido";
    //           }
    //
    //        // --- LÓGICA DE DEPÓSITO ---
    //        if (tipo.equals("deposito")) {
    //            System.out.print("Valor do depósito: ");
    //            float valor = tec.nextFloat();
    //            ContaDAO.atualizarSaldo(numero, saldoAtual + valor);
    //            System.out.println("Depósito realizado com sucesso!"); /*entendendo a lógica 19/02/26 */
    //
    //        // --- LÓGICA DE SAQUE ---
    //        } else if (tipo.equals("saque")) {
    //            System.out.print("Valor do saque: ");
    //            float valor = tec.nextFloat();
    //            
    //            if (valor <= saldoAtual) {
    //                ContaDAO.atualizarSaldo(numero, saldoAtual - valor);
    //                System.out.println("Saque realizado com sucesso!");
    //            } else {
    //                System.out.println("Erro: Saldo insuficiente! Saldo atual: R$ " + saldoAtual);
    //            }
    //
    //        // --- LÓGICA DE CONSULTA ---
    //        } else if (tipo.equals("saldo")) {
    //            System.out.println("\n--- EXTRATO DISPONÍVEL ---");
    //            System.out.println("Titular: " + nomeExibicao);
    //            System.out.println("Saldo atual: R$ " + saldoAtual);
    //        }
    //
    //    } else {
    //        System.out.println("Conta número " + numero + " não encontrada!");
    //    }
    // } catch (SQLException e) {
    //    System.out.println("Erro ao acessar o banco: " + e.getMessage());
        //}
    //}
    
    //método atualizado 21.03.2026
    private static void realizarOperacao(Scanner tec, String tipo) {
    System.out.print("Digite o número da conta: ");
    int numero = tec.nextInt();
    
    float saldoAtual = 0;
    String nomeExibicao = "";
    boolean contaEncontrada = false;

    // --- PASSO 1: CONSULTA (Abre e fecha rápido) ---
    String sqlBusca = "SELECT c.saldo, t.nome AS nome_pessoa, e.razao_social AS nome_empresa " +
                     "FROM contas c " +
                     "LEFT JOIN titular t ON c.id_titular = t.id_titular " +
                     "LEFT JOIN empresa e ON c.id_empresa = e.id_empresa " +
                     "WHERE c.numero_conta = ?";

    try (Connection conn = ContaDAO.conectar(); 
         PreparedStatement pstmt = conn.prepareStatement(sqlBusca)) {
        
        pstmt.setInt(1, numero);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                saldoAtual = rs.getFloat("saldo");
                contaEncontrada = true;
                
                // Define o nome para exibição
                if (rs.getString("nome_pessoa") != null) {
                    nomeExibicao = rs.getString("nome_pessoa") + " (PF)";
                } else if (rs.getString("nome_empresa") != null) {
                    nomeExibicao = rs.getString("nome_empresa") + " (PJ)";
                } else {
                    nomeExibicao = "Titular desconhecido";
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Erro ao buscar dados: " + e.getMessage());
        return; // Sai do método se der erro na busca
    }

    // --- PASSO 2: EXECUÇÃO (Fora do bloco de consulta) ---
    if (contaEncontrada) {
        try {
            if (tipo.equals("deposito")) {
                System.out.print("Valor do depósito: ");
                float valor = tec.nextFloat();
                
                // Agora o arquivo está livre para o UPDATE
                ContaDAO.atualizarSaldo(numero, saldoAtual + valor);
                System.out.println("Depósito realizado com sucesso!");

            } else if (tipo.equals("saque")) {
                System.out.print("Valor do saque: ");
                float valor = tec.nextFloat();
                
                if (valor <= saldoAtual) {
                    ContaDAO.atualizarSaldo(numero, saldoAtual - valor);
                    System.out.println("Saque realizado com sucesso!");
                } else {
                    System.out.println("Erro: Saldo insuficiente!");
                }

            } else if (tipo.equals("saldo")) {
                System.out.println("\n--- EXTRATO ---");
                System.out.println("Titular: " + nomeExibicao);
                System.out.println("Saldo atual: R$ " + String.format("%.2f", saldoAtual));
            }
       } catch (Exception e) { //retirado o SQLException 21.03.26
            System.out.println("Erro na operação bancária: " + e.getMessage());
        }
    } else {
        System.out.println("Conta " + numero + " não encontrada!");
        }
    }

//    public static void imprimirContas() {
//        System.out.println("\n--- LISTA DE CONTAS CADASTRADAS ---");
//        for (Conta conta : listaContas) {
//            System.out.println("Conta: " + conta.getNumConta());
//            conta.getTitular().imprime(); // Polimorfismo: chama imprime() de Fisica ou Juridica
//            System.out.println("Saldo: R$ " + conta.getSaldo());
//            System.out.println("-----------------------------------");
//        }
//    }
    
    //método incluso 26.02.26
    public static void imprimirContas() {
        //System.out.println("\n--- LISTA DE CONTAS NO BANCO DE DADOS ---");
        
        System.out.println("\n--- LISTA GERAL DE CONTAS (FÍSICA E JURÍDICA) ---");
        // SQL para buscar tudo da tabela conta e juntar com o nome do titular
        // Se você estiver usando a tabela simplificada 'contas', use:
        //String sql = "SELECT numConta, titular, saldo FROM contas";
        // SQL reformulado para trazer o nome do titular da tabela titular 26.02.26
        //String sql = "SELECT contas.numero_conta, titular.nome, contas.saldo FROM contas INNER JOIN titular ON contas.id_titular = titular.id_titular";
        
        //Atualizado para pegar contas com pessoa física e jurídica 14.03.26
        // SQL que busca na tabela contas e tenta "casar" com titular OU empresa
        String sql ="SELECT " +
                    "  c.numero_conta, " +
                    "  c.saldo, " +
                    "  t.nome AS nome_pessoa, " +
                    "  e.razao_social AS nome_empresa " +
                    "FROM contas c " +
                    "LEFT JOIN titular t ON c.id_titular = t.id_titular " +
                    "LEFT JOIN empresa e ON c.id_empresa = e.id_empresa";
        
        try (Connection conn = ContaDAO.conectar(); 
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            boolean encontrou = false;

            // O while percorre linha por linha do banco de dados
            while (rs.next()) {
                encontrou = true;
                //int numero = rs.getInt("numero_conta");
                //String nome = rs.getString("nome");
                String numero = rs.getString("numero_conta");
                float saldo = rs.getFloat("saldo");

                // Lógica para decidir qual nome exibir
                String nomeExibicao;
                if (rs.getString("nome_pessoa") != null) {
                    nomeExibicao = rs.getString("nome_pessoa") + " (PF)";
                } else if (rs.getString("nome_empresa") != null) {
                    nomeExibicao = rs.getString("nome_empresa") + " (PJ)";
                } else {
                    nomeExibicao = "Titular desconhecido";
                }

                System.out.println("Conta: " + numero);
                System.out.println("Titular: " + nomeExibicao);
                System.out.println("Saldo: R$ " + String.format("%.2f", saldo));
                System.out.println("-----------------------------------");
            }

            if (!encontrou) {
                System.out.println("Nenhuma conta cadastrada no banco.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar contas: " + e.getMessage());
        }
    }
}