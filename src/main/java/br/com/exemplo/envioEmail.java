package br.com.exemplo;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class envioEmail {

    public static void main(String[] args) {
        try {
            System.out.println("📧 Preparando envio de email...");

            // Obter variáveis de ambiente
            String mailServer = System.getenv("MAIL_SERVER");
            String mailPort = System.getenv("MAIL_PORT");
            String mailUsername = System.getenv("MAIL_USERNAME");
            String mailPassword = System.getenv("MAIL_PASSWORD");
            String mailTo = System.getenv("MAIL_TO");

            String pipelineStatus = System.getenv("PIPELINE_STATUS");
            String testResult = System.getenv("TEST_RESULT");
            String buildResult = System.getenv("BUILD_RESULT");
            String repoName = System.getenv("REPO_NAME");
            String branchName = System.getenv("BRANCH_NAME");
            String commitSha = System.getenv("COMMIT_SHA");
            String commitAuthor = System.getenv("COMMIT_AUTHOR");
            String runUrl = System.getenv("RUN_URL");

            // Validar variáveis obrigatórias
            if (mailServer == null || mailUsername == null || mailPassword == null || mailTo == null) {
                System.err.println("❌ Erro: Variáveis de ambiente não configuradas!");
                System.err.println("Configure as secrets: MAIL_SERVER, MAIL_USERNAME, MAIL_PASSWORD, MAIL_TO");
                System.exit(1);
            }

            // Configurar propriedades do servidor SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", mailServer);
            props.put("mail.smtp.port", mailPort != null ? mailPort : "587");
            props.put("mail.smtp.ssl.trust", mailServer);

            // Criar sessão autenticada
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailUsername, mailPassword);
                }
            });

            // Criar mensagem
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject("Pipeline CI/CD - " + (pipelineStatus != null ? pipelineStatus : "Status"));

            // Data e hora atual
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dataHora = now.format(formatter);

            // Corpo do email em HTML
            String htmlContent = criarCorpoHTML(
                    repoName, branchName, commitSha, commitAuthor,
                    dataHora, pipelineStatus, testResult, buildResult, runUrl
            );

            // Corpo do email em texto simples
            String textContent = criarCorpoTexto(
                    repoName, branchName, commitSha, commitAuthor,
                    dataHora, pipelineStatus, testResult, buildResult, runUrl
            );

            // Criar mensagem multipart
            MimeMultipart multipart = new MimeMultipart("alternative");

            // Parte em texto
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textContent, "UTF-8");
            multipart.addBodyPart(textPart);

            // Parte em HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            // Enviar email
            System.out.println("📤 Enviando email para " + mailTo + "...");
            Transport.send(message);

            System.out.println("✅ Email enviado com sucesso!");
            System.exit(0);

        } catch (MessagingException e) {
            System.err.println("❌ Erro ao enviar email: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String criarCorpoTexto(String repoName, String branchName,
                                          String commitSha, String commitAuthor,
                                          String dataHora, String pipelineStatus,
                                          String testResult, String buildResult,
                                          String runUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pipeline executado!\n\n");
        sb.append("Repositório: ").append(repoName != null ? repoName : "N/A").append("\n");
        sb.append("Branch: ").append(branchName != null ? branchName : "N/A").append("\n");
        sb.append("Commit: ").append(commitSha != null ? commitSha.substring(0, Math.min(8, commitSha.length())) : "N/A").append("\n");
        sb.append("Autor: ").append(commitAuthor != null ? commitAuthor : "N/A").append("\n");
        sb.append("Data/Hora: ").append(dataHora).append("\n\n");
        sb.append("STATUS GERAL: ").append(pipelineStatus != null ? pipelineStatus : "DESCONHECIDO").append("\n\n");
        sb.append("Detalhes dos Jobs:\n");
        sb.append("- Testes: ").append(testResult != null ? testResult.toUpperCase() : "UNKNOWN").append("\n");
        sb.append("- Build: ").append(buildResult != null ? buildResult.toUpperCase() : "UNKNOWN").append("\n\n");
        sb.append("Visualizar detalhes completos:\n");
        sb.append(runUrl != null ? runUrl : "N/A").append("\n\n");
        sb.append("---\n");
        sb.append("Mensagem automática do GitHub Actions");
        return sb.toString();
    }

    private static String criarCorpoHTML(String repoName, String branchName,
                                         String commitSha, String commitAuthor,
                                         String dataHora, String pipelineStatus,
                                         String testResult, String buildResult,
                                         String runUrl) {
        String statusClass = (pipelineStatus != null && pipelineStatus.contains("SUCESSO"))
                ? "status-success" : "status-failure";
        String commitShort = commitSha != null ? commitSha.substring(0, Math.min(8, commitSha.length())) : "N/A";

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <style>\n" +
                "    body { font-family: Arial, sans-serif; }\n" +
                "    .header { background-color: #24292e; color: white; padding: 20px; }\n" +
                "    .content { padding: 20px; }\n" +
                "    .status-success { color: #28a745; font-weight: bold; }\n" +
                "    .status-failure { color: #d73a49; font-weight: bold; }\n" +
                "    .info-box { background-color: #f6f8fa; padding: 15px; border-radius: 5px; margin: 10px 0; }\n" +
                "    .job-result { margin: 5px 0; }\n" +
                "    .button { background-color: #0366d6; color: white; padding: 10px 20px;\n" +
                "              text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 15px; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"header\">\n" +
                "    <h2>🚀 Pipeline CI/CD Executado</h2>\n" +
                "  </div>\n" +
                "  <div class=\"content\">\n" +
                "    <div class=\"info-box\">\n" +
                "      <h3>Informações do Repositório</h3>\n" +
                "      <p><strong>Repositório:</strong> " + (repoName != null ? repoName : "N/A") + "</p>\n" +
                "      <p><strong>Branch:</strong> " + (branchName != null ? branchName : "N/A") + "</p>\n" +
                "      <p><strong>Commit:</strong> " + commitShort + "</p>\n" +
                "      <p><strong>Autor:</strong> " + (commitAuthor != null ? commitAuthor : "N/A") + "</p>\n" +
                "      <p><strong>Data/Hora:</strong> " + dataHora + "</p>\n" +
                "    </div>\n" +
                "    <h3>Status da Pipeline: <span class=\"" + statusClass + "\">" +
                (pipelineStatus != null ? pipelineStatus : "DESCONHECIDO") + "</span></h3>\n" +
                "    <div class=\"info-box\">\n" +
                "      <h4>Resultados dos Jobs:</h4>\n" +
                "      <div class=\"job-result\">✓ <strong>Testes:</strong> " +
                (testResult != null ? testResult.toUpperCase() : "UNKNOWN") + "</div>\n" +
                "      <div class=\"job-result\">✓ <strong>Build:</strong> " +
                (buildResult != null ? buildResult.toUpperCase() : "UNKNOWN") + "</div>\n" +
                "    </div>\n" +
                "    <a href=\"" + (runUrl != null ? runUrl : "#") + "\" class=\"button\">Ver Detalhes no GitHub Actions</a>\n" +
                "    <hr style=\"margin-top: 30px;\">\n" +
                "    <p style=\"color: #586069; font-size: 12px;\">Mensagem automática do GitHub Actions</p>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }
}