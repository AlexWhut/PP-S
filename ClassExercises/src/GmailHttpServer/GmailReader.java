package GmailHttpServer;

import java.util.Properties;
import javax.mail.*;
import javax.mail.search.FlagTerm;

public class GmailReader {
    private String username;
    private String password;

    public GmailReader(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUnreadMessages() {
        StringBuilder result = new StringBuilder();
        result.append("<html><head><title>Gmail Unread Messages</title></head><body>");
        result.append("<h1>Unread Gmail Messages</h1>");
        
        try {
            // Configurar propiedades para IMAP
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", "imap.gmail.com");
            props.put("mail.imaps.port", "993");
            props.put("mail.imaps.ssl.enable", "true");
            
            // Conectar a Gmail
            Session session = Session.getInstance(props);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, password);
            
            // Abrir carpeta INBOX
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            
            // Buscar mensajes no leídos
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            
            result.append("<p>Total unread messages: ").append(messages.length).append("</p>");
            result.append("<hr>");
            
            if (messages.length == 0) {
                result.append("<p>No unread messages found.</p>");
            } else {
                // Limitar a los últimos 10 mensajes
                int count = Math.min(10, messages.length);
                for (int i = messages.length - 1; i >= messages.length - count; i--) {
                    Message msg = messages[i];
                    result.append("<div style='border: 1px solid #ccc; padding: 10px; margin: 10px 0;'>");
                    result.append("<h3>").append(escapeHtml(msg.getSubject())).append("</h3>");
                    result.append("<p><strong>From:</strong> ").append(escapeHtml(msg.getFrom()[0].toString())).append("</p>");
                    result.append("<p><strong>Date:</strong> ").append(msg.getSentDate()).append("</p>");
                    result.append("</div>");
                }
                
                if (messages.length > 10) {
                    result.append("<p><em>Showing last 10 of ").append(messages.length).append(" unread messages</em></p>");
                }
            }
            
            // Cerrar conexiones
            inbox.close(false);
            store.close();
            
        } catch (Exception e) {
            result.append("<p style='color: red;'><strong>Error:</strong> ").append(escapeHtml(e.getMessage())).append("</p>");
            result.append("<p>Make sure you:</p>");
            result.append("<ul>");
            result.append("<li>Use an App Password instead of your Gmail password</li>");
            result.append("<li>Enable IMAP in Gmail settings</li>");
            result.append("<li>Allow less secure apps or use 2-factor authentication with App Password</li>");
            result.append("</ul>");
            e.printStackTrace();
        }
        
        result.append("</body></html>");
        return result.toString();
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
