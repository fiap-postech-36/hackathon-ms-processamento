package br.com.videoprocessing.application.service;


public record EmailRabbitDTO(String recipient, String subject, String body) {
}
