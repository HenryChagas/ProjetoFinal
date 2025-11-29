<%-- 
    Document   : finalizarOS
    Created on : 20 de nov. de 2025, 16:34:55
    Author     : Henry Wallace
--%>

<%@page import="com.gps.dao.OrdemServicoDAO"%>
<%@page import="com.gps.model.OrdemServico"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Recebe o ID da OS a ser finalizada
    int idOS = Integer.parseInt(request.getParameter("id"));
    OrdemServicoDAO osDAO = new OrdemServicoDAO();
    OrdemServico os = osDAO.readById(idOS); // Você deve implementar este método

    // Lógica de Ação (POST)
    String acao = request.getParameter("acao");
    if ("devolver".equals(acao)) {
        
        // Se ainda não foi entregue, precisamos registrar a entrega antes de devolver
        if (!os.isEntregue()) {
            os.setEntregue(true);
            os.setHoraEntrega(new Date()); 
        }
        
        os.setHoraDevolucao(new Date()); // Define a hora da devolução como a hora atual
        
        osDAO.updateDevolucao(os);

        response.sendRedirect("consultarOS.jsp?sucesso=1");
        return; 
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Finalizar Ordem de Serviço #<%= idOS %></title>
        <link rel="stylesheet" href="styleGeral.css">
    </head>
    <body>
        <div class="header">
            <div class="logo-area">
                <img src="imagens/logo_gps.png" alt="Logo Grupo GPS">
            </div>
            <h1>Finalizar O.S. #<%= idOS %></h1>
        </div>

        <p> **Grupo:** <%= os.getGrupo().getNomeGrupo() %></p>
        <p> **Local:** <%= os.getLocalAndar() %> / <%= os.getLocalPorta() %></p>
        <p> **Descrição:** <%= os.getDanoDescricao() %></p>

        <% if (os.getHoraDevolucao() == null) { %>
            <p style="color: red;">Esta O.S. ainda está **PENDENTE** de devolução.</p>
            
            <form action="finalizarOS.jsp?id=<%= idOS %>&acao=devolver" method="POST">
                <button type="submit">Confirmar Devolução e Finalizar Serviço Agora</button>
            </form>
        <% } else { %>
             <p style="color: blue;">Esta O.S. foi finalizada em: <%= os.getHoraDevolucao() %></p>
        <% } %>
        
        <br><a href="consultarOS.jsp">Voltar para a Consulta</a>
    </body>
</html>