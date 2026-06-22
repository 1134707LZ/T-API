package br.edu.atitus.apisample.dtos;

public record BusStopDTO(
        Double latitude,
        Double longitude,
        Integer lineNumber,
        String lineName,
        String description
) {
}
