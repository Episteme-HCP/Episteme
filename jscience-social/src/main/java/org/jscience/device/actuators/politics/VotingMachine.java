package org.jscience.device.actuators.politics;

import org.jscience.device.Device;

/**
 * A Voting Machine device for electronic voting.
 */
public class VotingMachine implements Device {

    private String id;
    private boolean connected;
    private int voteCount;

    public VotingMachine(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return "Secure Voting Terminal";
    }

    @Override
    public String getManufacturer() {
        return "Democracy Tech";
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void connect() throws java.io.IOException {
        this.connected = true;
    }

    @Override
    public void disconnect() {
        this.connected = false;
    }

    @Override
    public void close() {
        disconnect();
    }

    public void castVote() {
        if (connected) {
            voteCount++;
        }
    }

    public int getVoteCount() {
        return voteCount;
    }
}
