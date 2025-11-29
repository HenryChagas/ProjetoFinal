<%-- 
    Document   : consultarOS
    Created on : 20 de nov. de 2025, 16:29:23
    Author     : Henry Wallace
--%>

<%@page import="com.gps.dao.GrupoTrabalhoDAO"%>
<%@page import="com.gps.model.OrdemServico"%>
<%@page import="com.gps.dao.OrdemServicoDAO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    OrdemServicoDAO osDAO = new OrdemServicoDAO();
    // Método para buscar a lista de OS com o nome do Grupo
    List<OrdemServico> listaOS = osDAO.readAll(); // Implementar com JOIN
    
    String sucesso = request.getParameter("sucesso");
    if (sucesso != null && sucesso.equals("1")) {
        out.println("<p style='color: green;'>Operação realizada com sucesso!</p>");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Consulta de Ordens de Serviço</title>
        <link rel="stylesheet" href="styleGeral.css">
    </head>
    <body>
        <div class="header">
            <div class="logo-area">
                <img src="imagens/logo_gps.png" alt="Logo Grupo GPS">
            </div>
            <h1>Ordens de Serviço em Aberto e Finalizadas</h1>
        </div>
        <a href="cadastroOS.jsp">Cadastrar Nova O.S.</a>
        <br><br>
        
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Número O.S.</th>
                    <th>Grupo</th>
                    <th>Solicitante</th>
                    <th>Local</th>
                    <th>Ramal</th>
                    <th>Entregue?</th>
                    <th>Hora Entrega</th>
                    <th>Hora Devolução</th>
                    <th>Ação</th>
                </tr>
            </thead>
            <tbody>
                <% for (OrdemServico os : listaOS) { %>
                    <tr>
                        <td><%= os.getIdOS() %></td>
                        <td><%= os.getNumeroOSImpressa() %></td>
                        <td><%= os.getGrupo().getNomeGrupo() %></td>
                        <td><%= os.getSolicitante() %></td>
                        <td><%= os.getLocalAndar() %>/<%= os.getLocalPorta() %></td>
                        <td><%= os.getRamal() %></td>
                        <td><%= os.isEntregue() ? "Sim" : "Não" %></td>
                        <td><%= os.getHoraEntrega() != null ? os.getHoraEntrega() : "Pendente" %></td>
                        <td style="color: <%= os.getHoraDevolucao() != null ? "blue" : "red" %>;">
                            <%= os.getHoraDevolucao() != null ? os.getHoraDevolucao() : "Pendente" %>
                        </td>
                        <td>
                            <% if (os.getHoraDevolucao() == null) { %>
                                <a href="finalizarOS.jsp?id=<%= os.getIdOS() %>">Finalizar</a>
                            <% } else { %>
                                Finalizado
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </body>
</html>