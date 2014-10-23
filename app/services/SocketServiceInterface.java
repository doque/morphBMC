package services;

import com.google.inject.ImplementedBy;

@ImplementedBy(SocketService.class)
public interface SocketServiceInterface {

	/**
	 * Register new client
	 * 
	 * @param userId
	 * @param in
	 *            play.mvc.WebSocket.In<String>
	 * @param out
	 *            play.mvc.WebSocket.Out<String>
	 */
	void registerClient(String userId, play.mvc.WebSocket.In<String> in,
			play.mvc.WebSocket.Out<String> out);

	/**
	 * Remove a client
	 * 
	 * @param userId
	 */
	void removeClient(String userId);

	/**
	 * Send message to client
	 * 
	 * @param userId
	 * @param message
	 *            Message
	 */
	void sendMessage(String userId, String message);

	/**
	 * Broadcast to all clients
	 * 
	 * @param message
	 *            Message
	 */
	void broadcast(String message);

	/**
	 * Remove last message from queue. No state-recover necessary anymore
	 * 
	 * @param userId
	 */
	void flush(String userId);
}