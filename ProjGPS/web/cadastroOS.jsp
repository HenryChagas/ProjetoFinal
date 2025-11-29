<%-- 
    Document   : cadastroOS
    Created on : 20 de nov. de 2025, 16:20:45
    Author     : Henry Wallace
--%>

<%@page import="com.gps.dao.GrupoTrabalhoDAO"%>
<%@page import="com.gps.model.GrupoTrabalho"%>
<%@page import="com.gps.dao.OrdemServicoDAO"%>
<%@page import="com.gps.model.OrdemServico"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String acao = request.getParameter("acao");
    String msg = "";

    if ("cadastrar".equals(acao)) {
        try {
            OrdemServico os = new OrdemServico();
            OrdemServicoDAO osDAO = new OrdemServicoDAO();

            // Recebe os parâmetros do formulário
            os.setSolicitante(request.getParameter("solicitante"));
            os.setLocalAndar(Integer.parseInt(request.getParameter("localAndar")));
            os.setLocalPorta(request.getParameter("localPorta"));
            os.setDanoDescricao(request.getParameter("danoDescricao"));
            os.setRamal(request.getParameter("ramal"));
            os.setNumeroOSImpressa(request.getParameter("numeroOSImpressa"));
            
            // Trata o Grupo (Chave Estrangeira)
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo_fk"));
            GrupoTrabalho grupo = new GrupoTrabalho();
            grupo.setIdGrupo(idGrupo);
            os.setGrupo(grupo);

            // Controle de Entrega (Preenchimento inicial)
            boolean entregue = request.getParameter("entregue") != null;
            os.setEntregue(entregue);
            
            if (entregue) {
                os.setHoraEntrega(new Date()); // Define a hora de entrega como a hora atual
            } else {
                os.setHoraEntrega(null); // Se não foi entregue, fica null
            }

            // Persiste no banco de dados
            osDAO.create(os);
            
            response.sendRedirect("consultarOS.jsp?sucesso=1");
            return; // Interrompe a execução do restante do JSP para fazer o redirecionamento
            
        } catch (Exception e) {
            msg = "Erro ao cadastrar O.S.: " + e.getMessage();
        }
    }

    // Dados necessários para popular Grupos de Trabalho
    GrupoTrabalhoDAO grupoDAO = new GrupoTrabalhoDAO();
    List<GrupoTrabalho> listaGrupos = grupoDAO.readAll();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Cadastro de Ordem de Serviço</title>
        <link rel="stylesheet" href="styleGeral.css">
    </head>
    <body>
        <div class="header">
            <div class="logo-area">
                <img src="imagens/logo_gps.png" alt="Logo Grupo GPS">
            </div>
        <h1>Cadastro de O.S.</h1>
        </div>
        <% if (!"".equals(msg)) { %> <p style="color: red;"><%= msg %></p> <% } %>
        
        <form action="cadastroOS.jsp?acao=cadastrar" method="POST">
            
            <label>Número O.S. (Impressa):</label>
            <input type="text" name="numeroOSImpressa" required><br>

            <label>Solicitante:</label>
            <input type="text" name="solicitante" required><br>

            <label>Local (Andar):</label>
            <input type="number" name="localAndar" required><br>
            
            <label>Local (Porta):</label>
            <input type="text" name="localPorta" required><br>
            
            <label>Ramal:</label>
            <input type="text" name="ramal" maxlength="4"><br>

            <label>Grupo de Trabalho:</label>
            <select name="idGrupo_fk" required>
                <% for (GrupoTrabalho g : listaGrupos) { %>
                    <option value="<%= g.getIdGrupo() %>"><%= g.getNomeGrupo() %></option>
                <% } %>
            </select><br>

            <label>Descrição do Dano:</label>
            <textarea name="danoDescricao"></textarea><br>

            <label>O.S. Impressa Entregue Agora?</label>
            <input type="checkbox" name="entregue" value="true"><br>

            <button type="submit">Salvar Ordem de Serviço</button>
        </form>
    </body>
</html>