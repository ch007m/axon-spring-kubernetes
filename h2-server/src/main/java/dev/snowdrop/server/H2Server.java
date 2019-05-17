package dev.snowdrop.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;

public class H2Server {
    public static void main(String[] args) {
        String[] params = new String[]{"-tcpPort", "8556", "-tcp", "-tcpAllowOthers", "-baseDir", "./h2-server","-ifNotExists","-trace"};
        try {
            Server s = Server.createTcpServer(params).start();
            System.out.println(s.getStatus());

            Server webServer = Server.createWebServer("-webPort", "9090", "-webAllowOthers").start();
            System.out.println(webServer.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
