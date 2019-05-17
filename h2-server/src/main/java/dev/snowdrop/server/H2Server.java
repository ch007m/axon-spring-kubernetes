package dev.snowdrop.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;

public class H2Server {
    public static void main(String[] args) {
        String[] params = new String[]{"-tcpPort", "8081", "-tcp", "-tcpAllowOthers", "-baseDir", "./h2-server","-ifNotExists"};
        Server h2serve = new Server();
        try {
            Server s = Server.createTcpServer(params);
            s.start();
            System.out.println(s);
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:8081/axondb");
            System.out.println(conn);
            conn.close();
            Thread.sleep(Long.MAX_VALUE);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
