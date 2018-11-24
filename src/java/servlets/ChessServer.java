package servlets;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint("/chess")
public class ChessServer {

    private static Session white, black;
    private static Board board;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + " has opened a connection");
        try {
            if (white == null) {
                white = session;
            } else if (black == null) {
                board = new Board();
                black = session;
                white.getBasicRemote().sendText("{\"type\": 0, \"color\": 0, \"turn\": " + board.getTURN() + "}");
                black.getBasicRemote().sendText("{\"type\": 0, \"color\": 1, \"turn\": " + board.getTURN() + "}");
                sendMessage("{\"type\": 4, \"board\": " + board + ",\"turn\": " + board.getTURN() + "}");
            } else {
                session.getBasicRemote().sendText("{\"type\": 2, \"message\": \"No more players allowed!\"}");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from " + session.getId() + ": " + message);
        try {
            JSONObject obj = new JSONObject(message);
            if ((session == white && board.getTURN() == 0) || (session == black && board.getTURN() == 1)) {
                int ox = obj.getInt("ox"), oy = obj.getInt("oy");
                int dx = obj.getInt("dx"), dy = obj.getInt("dy");
                board.movePiece(ox, oy, dx, dy);
                board.printTabuleiro();
                String msg = "{\"type\": 4, \"board\": " + board + ",\"turn\": " + board.getTURN() + "}";
                black.getBasicRemote().sendText(msg);
                white.getBasicRemote().sendText(msg);
            } else if ((session == white && board.getTURN() == 1) || (session == black && board.getTURN() == 0)) {
                session.getBasicRemote().sendText("{\"type\": 2, \"message\": \"Wait for youre turn!\"}");
            } else {
                session.getBasicRemote().sendText("{\"type\": 2, \"message\": \"No more players allowed!\"}");
            }
        } catch (IOException | JSONException ex) {
            System.out.println(ex);
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        try {
            if (session == white) {
                black.getBasicRemote().sendText("{\"type\": 3}");
            } else {
                white.getBasicRemote().sendText("{\"type\": 3}");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void sendMessage(String msg) throws IOException {
        black.getBasicRemote().sendText(msg);
        white.getBasicRemote().sendText(msg);
    }
}
