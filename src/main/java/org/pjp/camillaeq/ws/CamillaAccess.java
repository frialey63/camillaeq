package org.pjp.camillaeq.ws;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;

/**
 * This class contains the logic to send queries to the CamillaDSP via its Web Socket interface.
 *
 */
public final class CamillaAccess {

    /**
     * Message to set the config.
     */
    public static final String SET_CONFIG = "SetConfig";
    /**
     * Message to get the config.
     */
    public static final String GET_CONFIG = "GetConfig";
    /**
     * Message to set the volume.
     */
    public static final String SET_VOLUME = "SetVolume";
    /**
     * Message to get the volume.
     */
    public static final String GET_VOLUME = "GetVolume";
    /**
     * Message to get the version.
     */
    public static final String GET_VERSION = "GetVersion";

    private static final Logger LOGGER = LoggerFactory.getLogger(CamillaAccess.class);

    private static final String STR_VALUE = "\"value\":\"";

    private static final String NUM_VALUE = "\"value\":";

    private static String camillaUrl;

    public static String getCamillaUrl() {
        return camillaUrl;
    }

    public static void setCamillaUrl(String camillaUrl) {
        CamillaAccess.camillaUrl = camillaUrl;
    }

    /**
     * Send a message and obtain a value in response.
     * @param message The message
     * @return The value which is the result of sending the message
     */
    public static String queryForValue(String message) {
        String str = query(message);

        if (str.indexOf(message) == 2) {
            int beginIndex = str.indexOf(STR_VALUE);
            int endIndex = str.indexOf("\"}}");

            if (beginIndex == -1) {
                beginIndex = str.indexOf(NUM_VALUE);
                endIndex = str.indexOf("}}");

                return str.substring(beginIndex + NUM_VALUE.length(), endIndex);
            }

            return str.substring(beginIndex + STR_VALUE.length(), endIndex);
        }

        return null;
    }

    /**
     * Send a message and obtain a response which could be a status (for a setter message) or a value (for a getter message).
     * @param message The message
     * @return The response in the form of a JSON string
     */
    public static String query(String message, Object... args) {
        String text;

        if (args.length == 0) {
            text = String.format("\"%s\"", message);
        } else {
            if (args[0] instanceof String str) {
                text = String.format("{ \"%s\" : \"%s\" }", message, str);
            } else if (args[0] instanceof Number num) {
                text = String.format("{ \"%s\" : %f }", message, num);
            } else {
                throw new IllegalArgumentException();
            }
        }

        return doQuery(text);
    }

    private static String doQuery(String message) {
        final StringBuffer result = new StringBuffer();

        try {
            CountDownLatch messageLatch = new CountDownLatch(1);

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

            ClientManager client = ClientManager.createClient();
            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.addMessageHandler(new MessageHandler.Whole<String>() {

                            @Override
                            public void onMessage(String message) {
                                result.append(message);
                                messageLatch.countDown();
                            }
                        });
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        LOGGER.error("", e);
                    }
                }
            }, cec, new URI(camillaUrl));

            if (!messageLatch.await(100, TimeUnit.SECONDS)) {
                LOGGER.warn("wait time elapsed before latch coutdown reached zero");
            }

        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return result.toString();
    }

    private CamillaAccess() {
        // prevent instantiation
    }

}
