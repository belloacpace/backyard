package net.cornad.pi.backyard;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloWorldController {
    // create gpio controller
    final GpioController gpio = GpioFactory.getInstance();


    final GpioPinDigitalOutput[] pins = new GpioPinDigitalOutput[8];


    public HelloWorldController(){
        pins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "Relay0_0", PinState.HIGH);
        pins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "Relay0_1", PinState.HIGH);
        pins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Relay0_2", PinState.HIGH);
        pins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Relay0_3", PinState.HIGH);
        pins[4] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Relay0_4", PinState.HIGH);
        pins[5] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Relay0_5", PinState.HIGH);
        pins[6] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Relay0_6", PinState.HIGH);
        pins[7] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Relay0_7", PinState.HIGH);

        for(int i =0 ; i < 8 ; i++){
               // set shutdown state for this pin
            pins[i].setShutdownOptions(true, PinState.HIGH);
        }
    }
    // inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private List<Valve> valves = new ArrayList<>();

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);

        for (int i = 0; i < 8; i++) {
            valves.add(new Valve(i, "Relay" + i, false));
        }

        model.addAttribute("valves", valves);
        
        return "HelloWorld"; //view
    }

    @GetMapping("/relay")
    public String relayControl(@RequestParam(name = "on", required = false, defaultValue = "")
                                final String on, @RequestParam(name = "off", required = false, defaultValue = "")
                                String off, Model model) {
        StringBuilder sb = new StringBuilder();
        List<String> tasks = new ArrayList<>();
        if(on != null && on.length() > 0) {
            String[] turnOns = on.split(",");

            if(turnOns != null && turnOns.length > 0){
                for(int i = 0; i < turnOns.length; i++){
                    pins[Integer.valueOf(turnOns[i])].high();
                    sb.append("Turned on ").append(turnOns[i]).append("<br>");
                    tasks.add("Turned On " + turnOns[i]);
                }
            }
        }

        if(off != null && off.length() > 0) {
            String[] turnOffs = off.split(",");

            if(turnOffs != null && turnOffs.length > 0){
                for(int i = 0; i < turnOffs.length; i++){
                    pins[Integer.valueOf(turnOffs[i])].low();
                    sb.append("Turned off ").append(turnOffs[i]).append("<br>");
                    tasks.add("Turned Off " + turnOffs[i]);
                }

            }
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("message", sb.toString());
        return "HelloWorld";

    }

    @GetMapping("/toggle")
    public String relayControl(@RequestParam(name = "valve", required = true, defaultValue = "")
                               final String valve, Model model) {
        List<String> tasks = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(valve != null && valve.length() > 0) {
            String[] turnOns = valve.split(",");

            if(turnOns != null && turnOns.length > 0){
                for(int i = 0; i < turnOns.length; i++){
                    pins[Integer.valueOf(turnOns[i])].toggle();
                    sb.append("Toggled").append(turnOns[i]).append("<br>");
                }
            }
        }
        return sb.toString();
    }


}