<%-- 
    Document   : gerenciarGrupos
    Created on : 20 de nov. de 2025, 17:06:34
    Author     : Henry Wallace
--%>

<%@page import="com.gps.dao.GrupoTrabalhoDAO"%>
<%@page import="com.gps.model.GrupoTrabalho"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.SQLException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Controller: Lógica para Inserir e Deletar Grupos
    
    GrupoTrabalhoDAO grupoDAO = new GrupoTrabalhoDAO();
    String acao = request.getParameter("acao");
    String msg = null;

    try {
        if ("cadastrar".equals(acao)) {
            String nomeGrupo = request.getParameter("nomeGrupo");
            if (nomeGrupo != null && !nomeGrupo.trim().isEmpty()) {
                GrupoTrabalho novoGrupo = new GrupoTrabalho();
                novoGrupo.setNomeGrupo(nomeGrupo.trim());
                grupoDAO.create(novoGrupo); // Chama o método de inserção
                msg = "Grupo '" + nomeGrupo + "' cadastrado com sucesso!";
            } else {
                msg = "O nome do grupo não pode ser vazio.";
            }
        } else if ("deletar".equals(acao)) {
            int idGrupo = Integer.parseInt(request.getParameter("id"));
            grupoDAO.delete(idGrupo); // Chama o método de exclusão
            msg = "Grupo excluído com sucesso!";
        }
    } catch (SQLException ex) {
        // Trata exceções do banco de dados
        if (ex.getMessage().contains("a foreign key constraint fails")) {
            msg = "ERRO: Não é possível excluir o grupo, pois há Ordens de Serviço (O.S.) vinculadas a ele.";
        } else {
            msg = "ERRO no Banco de Dados: " + ex.getMessage();
        }
    } catch (Exception ex) {
        msg = "ERRO: " + ex.getMessage();
    }
    
    // Lista todos os grupos após as ações
    List<GrupoTrabalho> listaGrupos = null;
    try {
        listaGrupos = grupoDAO.readAll(); // Busca todos os grupos para exibição
    } catch (SQLException ex) {
        // Se houver erro ao listar, exibe a mensagem, mas não interrompe
        msg = (msg != null) ? msg + " | Erro ao listar: " + ex.getMessage() : "Erro ao listar grupos: " + ex.getMessage();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Gerenciamento de Grupos de Trabalho</title>
        <link rel="stylesheet" href="styleGeral.css">
        <style>
            .mensagem { padding: 10px; margin-bottom: 15px; border-radius: 4px; }
            .sucesso { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
            .erro { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
            table { border-collapse: collapse; width: 50%; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
    </head>
    <body>
        <div class="header">
            <div class="logo-area">
                <img src="imagens/logo_gps.png" alt="Logo Grupo GPS">
            </div>
            <h1>Gerenciamento de Grupos de Trabalho</h1>
        </div>
        
        <p><a href="consultarOS.jsp">← Voltar para a Consulta de O.S.</a></p>

        <% 
            // Exibe a mensagem de feedback
            if (msg != null) { 
                String tipo = msg.startsWith("ERRO") ? "erro" : "sucesso";
        %>
            <div class="mensagem <%= tipo %>">
                <%= msg %>
            </div>
        <% 
            } 
        %>

        <hr>

        <h2>Cadastrar Novo Grupo</h2>
        <form action="gerenciarGrupos.jsp?acao=cadastrar" method="POST">
            <label for="nomeGrupo">Nome do Grupo (Ex: Elétrica, Hidráulica):</label><br>
            <input type="text" id="nomeGrupo" name="nomeGrupo" required style="width: 300px;"><br><br>
            <button type="submit">Adicionar Grupo</button>
        </form>

        <hr>

        <h2>Grupos Cadastrados (<%= (listaGrupos != null ? listaGrupos.size() : 0) %>)</h2>
        <% if (listaGrupos != null && !listaGrupos.isEmpty()) { %>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome do Grupo</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (GrupoTrabalho grupo : listaGrupos) { %>
                        <tr>
                            <td><%= grupo.getIdGrupo() %></td>
                            <td><%= grupo.getNomeGrupo() %></td>
                            <td>
                                <a href="gerenciarGrupos.jsp?acao=deletar&id=<%= grupo.getIdGrupo() %>" 
                                   onclick="return confirm('Tem certeza que deseja excluir o grupo <%= grupo.getNomeGrupo() %>? Se houver O.S. vinculada, a exclusão falhará.')"
                                   style="color: red;">
                                    Excluir
                                </a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else if (listaGrupos != null) { %>
            <p>Não há grupos de trabalho cadastrados.</p>
        <% } %>
    </body>
</html>