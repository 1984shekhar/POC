package com.mycompany.test.simulators.stomp;

public enum Ack {
    
    auto, client, client_individual;

    public String nameWithConversion() {
        String ackStr = this.name();
        if (ackStr.equals("client_individual")) {
            ackStr = "client-individual";
        }
        return ackStr;
    }
    
    public static Ack valueOfWithConversion(String ackStr) {
        if (ackStr.equals("client-individual")) {
            return Ack.valueOf("client_individual");
        }
        return Ack.valueOf(ackStr);
    }
}
