package controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.WebSocket;

import services.SocketService;
import services.SocketServiceInterface;

import com.google.inject.Inject;

public class WebSocketController extends Controller {

	@Inject
	public WebSocketController(SocketServiceInterface _socketService) {
		socketService = checkNotNull(_socketService);
	}

	static SocketServiceInterface socketService = new SocketService();

	/**
	 * Opens a new WebSocket connection if a userId is present
	 * and binds the in/out to the SocketService
	 */
	public WebSocket<String> open(final long problemId, final String userId) {

		return new WebSocket<String>() {

			// Called when the Websocket Handshake is done.
			public void onReady(WebSocket.In<String> in,
					WebSocket.Out<String> out) {
				
				// socketService manages all clients connections
				socketService.registerClient(userId, in, out);

				// When the socket is closed.
				in.onClose(new Callback0() {
					public void invoke() {
						socketService.removeClient(userId);
					}
				});
			}

		};
	}

}
