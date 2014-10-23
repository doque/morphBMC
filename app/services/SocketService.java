package services;

import play.mvc.WebSocket;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

/**
 * SocketService provides endpoints for socket interaction with clients
 * identified by their userId
 */
@Singleton
public class SocketService implements SocketServiceInterface {

	/**
	 * All connected clients
	 */
	Map<String, SocketIO> clients = new HashMap<>();

	/**
	 * Last messages sent to clients
	 */
	Map<String, String> lastMessages = new HashMap<>();

	@Override
	public void registerClient(String userId, WebSocket.In<String> in,
			WebSocket.Out<String> out) {
		clients.put(userId, new SocketIO(in, out));
	}

	@Override
	public void removeClient(String userId) {
		// Logger.debug("Removing client " + userId);
		clients.remove(userId);
	}

	@Override
	public void sendMessage(String userId, String message) {
		SocketIO userIO = clients.get(userId);
		if (userIO != null) {
			clients.get(userId).getOut().write(message);
			lastMessages.put(userId, message);
		}
	}

	@Override
	public void broadcast(String message) {
		for (String client : clients.keySet()) {
			clients.get(client).getOut().write(message);
		}
	}

	@Override
	public void flush(String userId) {
		lastMessages.remove(userId);
	}

	public class SocketIO {
		private WebSocket.In<String> fIn;
		private WebSocket.Out<String> fOut;

		public SocketIO(WebSocket.In<String> in, final WebSocket.Out<String> out) {
			fIn = in;
			fOut = out;
		}

		public WebSocket.In<String> getIn() {
			return fIn;
		}

		public WebSocket.Out<String> getOut() {
			return fOut;
		}
	}

}