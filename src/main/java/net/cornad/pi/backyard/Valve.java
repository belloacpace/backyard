package net.cornad.pi.backyard;

public class Valve {
    int pin;
    String name;
    boolean state;
    boolean isOn;

    public Valve(int pin, String name, boolean state) {
        this.pin = pin;
        this.name = name;
        this.state = state;
        this.isOn = false;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
