package com.mycompany.test.simulators.stomp;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

class StompFrame {

    private static final String CHAR_SET = "UTF-8";
    public static final String LF = "\n";
    public static final String NULL = "\0";

    private StompCommand command;

    private Map<String, String> header = new HashMap<String, String>();
    private String body;

    public StompFrame() {
    }

    public StompFrame(StompCommand command) {
        this.command = command;
    }

    public StompCommand getCommand() {
        return command;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String aBody) {
        body = aBody;
    }

    public String toString() {
        return String.format("command: %s, header: %s, body: %s", this.command,
                this.header.toString(), this.body);
    }

    public byte[] getBytes() {
        StringBuilder frame = new StringBuilder();
        frame.append(this.command.toString()).append('\n');

        for (Map.Entry<String, String> entry : header.entrySet()) {
            frame.append(entry.getKey()).append(":").append(this.header.get(entry.getKey())).append('\n');
        }
        frame.append('\n');

        if (this.body != null) {
            frame.append(this.body);
        }
        frame.append("\0");
        return frame.toString().getBytes();
    }

    public String getText() {
        StringBuilder frame = new StringBuilder();
        frame.append(this.command.toString()).append('\n');

        for (Map.Entry<String, String> entry : header.entrySet()) {
            frame.append(entry.getKey()).append(":").append(this.header.get(entry.getKey())).append('\n');
        }
        frame.append('\n');

        if (this.body != null) {
            frame.append(this.body);
        }
        frame.append("\0");

        return frame.toString();
    }

    public ByteBuffer getByteBuffer() {
        StringBuilder frame = new StringBuilder();
        frame.append(this.command.toString()).append('\n');

        for (Map.Entry<String, String> entry : header.entrySet()) {
            frame.append(entry.getKey()).append(":").append(this.header.get(entry.getKey())).append('\n');
        }
        frame.append('\n');

        if (this.body != null) {
            frame.append(this.body);
        }
        frame.append("\0");
        ByteBuffer buffer = ByteBuffer.allocate(frame.toString().getBytes().length).put(frame.toString().getBytes());

        return buffer;
    }

    public static StompFrame parse(String raw) {
        StompFrame frame = new StompFrame();

        String commandheaderSections = raw.split("\n\n")[0];
        String[] headerLines = commandheaderSections.split("\n");

        frame.command = StompCommand.valueOf(headerLines[0]);

        for (int i = 1; i < headerLines.length; i++) {
            String key = headerLines[i].split(":")[0];
            frame.header.put(key, headerLines[i].substring(key.length() + 1));
        }

        frame.body = raw.substring(commandheaderSections.length() + 2);

        return frame;
    }

    public static String toString(ByteBuffer message) {
        return new String(message.array(), Charset.forName(CHAR_SET));
    }

}
