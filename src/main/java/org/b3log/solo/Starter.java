/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-2019, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo;

import org.apache.commons.cli.*;
import org.b3log.latke.Latkes;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.util.Strings;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.webapp.WebAppContext;

import java.awt.*;
import java.io.File;
import java.net.URI;

/**
 * Solo with embedded Jetty, <a href="https://github.com/b3log/solo/issues/12037">standalone mode</a>.
 * <ul>
 * <li>Windows: java -cp "WEB-INF/lib/*;WEB-INF/classes" org.b3log.solo.Starter</li>
 * <li>Unix-like: java -cp "WEB-INF/lib/*:WEB-INF/classes" org.b3log.solo.Starter</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.15, Dec 15, 2018
 * @since 1.2.0
 */
public final class Starter {

    static {
        try {
            Log.setLog(new Slf4jLog());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Private constructor.
     */
    private Starter() {
    }

    /**
     * Main.
     *
     * @param args the specified arguments
     * @throws java.lang.Exception if start failed
     */
    public static void main(final String[] args) throws Exception {
        final Logger logger = Logger.getLogger(Starter.class);

        final Options options = new Options();
        final Option listenPortOpt = Option.builder("lp").longOpt("listen_port").argName("LISTEN_PORT")
                .hasArg().desc("listen port, default is 8080").build();
        options.addOption(listenPortOpt);

        final Option serverSchemeOpt = Option.builder("ss").longOpt("server_scheme").argName("SERVER_SCHEME")
                .hasArg().desc("browser visit protocol, default is http").build();
        options.addOption(serverSchemeOpt);

        final Option serverHostOpt = Option.builder("sh").longOpt("server_host").argName("SERVER_HOST")
                .hasArg().desc("browser visit domain name, default is localhost").build();
        options.addOption(serverHostOpt);

        final Option serverPortOpt = Option.builder("sp").longOpt("server_port").argName("SERVER_PORT")
                .hasArg().desc("browser visit port, default is 8080").build();
        options.addOption(serverPortOpt);

        final Option staticServerSchemeOpt = Option.builder("sss").longOpt("static_server_scheme").argName("STATIC_SERVER_SCHEME")
                .hasArg().desc("browser visit static resource protocol, default is http").build();
        options.addOption(staticServerSchemeOpt);

        final Option staticServerHostOpt = Option.builder("ssh").longOpt("static_server_host").argName("STATIC_SERVER_HOST")
                .hasArg().desc("browser visit static resource domain name, default is localhost").build();
        options.addOption(staticServerHostOpt);

        final Option staticServerPortOpt = Option.builder("ssp").longOpt("static_server_port").argName("STATIC_SERVER_PORT")
                .hasArg().desc("browser visit static resource port, default is 8080").build();
        options.addOption(staticServerPortOpt);

        final Option runtimeModeOpt = Option.builder("rm").longOpt("runtime_mode").argName("RUNTIME_MODE")
                .hasArg().desc("runtime mode (DEVELOPMENT/PRODUCTION), default is DEVELOPMENT").build();
        options.addOption(runtimeModeOpt);

        options.addOption("h", "help", false, "print help for the command");
        options.addOption("no", "not_open", false, "not auto open in the browser");

        final HelpFormatter helpFormatter = new HelpFormatter();
        final CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine;

        final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        final String cmdSyntax = isWindows ? "java -cp \"WEB-INF/lib/*;WEB-INF/classes\" org.b3log.solo.Starter"
                : "java -cp \"WEB-INF/lib/*:WEB-INF/classes\" org.b3log.solo.Starter";
        final String header = "\nSolo 是一款小而美的 Java 博客系统。\n\n";
        final String footer = "\n提需求或报告缺陷请到项目网站: https://github.com/b3log/solo\n\n";
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (final ParseException e) {
            helpFormatter.printHelp(cmdSyntax, header, options, footer, true);

            return;
        }

        if (commandLine.hasOption("h")) {
            helpFormatter.printHelp(cmdSyntax, header, options, footer, true);

            return;
        }

        String portArg = commandLine.getOptionValue("listen_port");
        if (!Strings.isNumeric(portArg)) {
            portArg = "8080";
        }

        String serverScheme = commandLine.getOptionValue("server_scheme");
        Latkes.setServerScheme(serverScheme);
        String serverHost = commandLine.getOptionValue("server_host");
        Latkes.setServerHost(serverHost);
        String serverPort = commandLine.getOptionValue("server_port");
        Latkes.setServerPort(serverPort);
        String staticServerScheme = commandLine.getOptionValue("static_server_scheme");
        Latkes.setStaticServerScheme(staticServerScheme);
        String staticServerHost = commandLine.getOptionValue("static_server_host");
        Latkes.setStaticServerHost(staticServerHost);
        String staticServerPort = commandLine.getOptionValue("static_server_port");
        Latkes.setStaticServerPort(staticServerPort);
        String runtimeMode = commandLine.getOptionValue("runtime_mode");
        if (null != runtimeMode) {
            Latkes.setRuntimeMode(Latkes.RuntimeMode.valueOf(runtimeMode));
        }

        String webappDirLocation = "src/main/webapp/"; // POM structure in dev env
        final File file = new File(webappDirLocation);
        if (!file.exists()) {
            webappDirLocation = "."; // production environment
        }

        final int port = Integer.valueOf(portArg);
        final Server server = new Server(port);
        final WebAppContext root = new WebAppContext();
        root.setParentLoaderPriority(true); // Use parent class loader
        root.setContextPath("/");
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);
        server.setHandler(root);
        try {
            server.start();
        } catch (final Exception e) {
            logger.log(Level.ERROR, "Server start failed", e);

            System.exit(-1);
        }

        serverScheme = Latkes.getServerScheme();
        serverHost = Latkes.getServerHost();
        serverPort = Latkes.getServerPort();
        final String contextPath = Latkes.getContextPath();

        try {
            if (!commandLine.hasOption("no")) {
                Desktop.getDesktop().browse(new URI(serverScheme + "://" + serverHost + ":" + serverPort + contextPath));
            }
        } catch (final Throwable e) {
            // Ignored
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
            } catch (final Exception e) {
                logger.log(Level.ERROR, "Server stop failed", e);

                System.exit(-1);
            }
        }));

        server.join();
    }
}
