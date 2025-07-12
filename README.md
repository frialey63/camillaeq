# camillaeq
A graphic equaliser for CamillaDSP based on Weq8

_This is work in progress but the core functionality is working_

### Release Build

TODO

## Development

Prerequisites: JDK 17, Maven 3.9.2

    git clone <this repo>
    cd camillaeq
    mvn spring-boot:run

## TODO

- configure the CamillaDSP host:port
- save/load named configs
- reset config

## ISSUES

### WEQ8

- filter drop down: Add+ and dark stylng
- redraw missing for plot when resized
- get/set state
- grid labelling for the graph
- option to relocate the controls above/below
- config file for number of biquad, types for selection

### CAMILLADSP

- "names" in config for the Filter pipeline step
