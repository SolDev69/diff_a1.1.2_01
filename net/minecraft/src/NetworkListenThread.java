package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class NetworkListenThread {
	public static Logger logger = Logger.getLogger("Minecraft");
	private ServerSocket serverSocket;
	private Thread listenThread;
	public volatile boolean isListening = false;
	private int connectionNumber = 0;
	private ArrayList pendingConnections = new ArrayList();
	private ArrayList playerList = new ArrayList();
	public MinecraftServer mcServer;

	public NetworkListenThread(MinecraftServer minecraftServer, InetAddress address, int port) throws IOException {
		this.mcServer = minecraftServer;
		this.serverSocket = new ServerSocket(port, 0, address);
		this.serverSocket.setPerformancePreferences(0, 2, 1);
		this.isListening = true;
		this.listenThread = new NetworkAcceptThread(this, "Listen thread", minecraftServer);
		this.listenThread.start();
	}

	public void addPlayer(NetServerHandler netServerHandler) {
		this.playerList.add(netServerHandler);
	}

	private void addPendingConnection(NetLoginHandler netLoginHandler) {
		if(netLoginHandler == null) {
			throw new IllegalArgumentException("Got null pendingconnection!");
		} else {
			this.pendingConnections.add(netLoginHandler);
		}
	}

	public void handleNetworkListenThread() {
		int var1;
		for(var1 = 0; var1 < this.pendingConnections.size(); ++var1) {
			NetLoginHandler var2 = (NetLoginHandler)this.pendingConnections.get(var1);

			try {
				var2.tryLogin();
			} catch (Exception var5) {
				var2.kickUser("Internal server error");
				logger.log(Level.WARNING, "Failed to handle packet: " + var5, var5);
			}

			if(var2.finishedProcessing) {
				this.pendingConnections.remove(var1--);
			}
		}

		for(var1 = 0; var1 < this.playerList.size(); ++var1) {
			NetServerHandler var6 = (NetServerHandler)this.playerList.get(var1);

			try {
				var6.handlePackets();
			} catch (Exception var4) {
				var6.kickPlayer("Internal server error");
				logger.log(Level.WARNING, "Failed to handle packet: " + var4, var4);
			}

			if(var6.connectionClosed) {
				this.playerList.remove(var1--);
			}
		}

	}

	static ServerSocket getServerSocket(NetworkListenThread listenThread) {
		return listenThread.serverSocket;
	}

	static int incrementConnections(NetworkListenThread listenThread) {
		return listenThread.connectionNumber++;
	}

	static void addPendingConnection(NetworkListenThread listenThread, NetLoginHandler netLoginHandler) {
		listenThread.addPendingConnection(netLoginHandler);
	}
}
