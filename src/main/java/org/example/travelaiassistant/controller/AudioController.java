package org.example.travelaiassistant.controller;

import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.AudioChatResponse;
import org.example.travelaiassistant.dto.AudioUploadResponse;
import org.example.travelaiassistant.dto.ChatRequest;
import org.example.travelaiassistant.dto.ChatResponse;
import org.example.travelaiassistant.service.AudioService;
import org.example.travelaiassistant.service.TravelChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioController {

    private final AudioService audioService;

    private final TravelChatService travelChatService;

    @PostMapping("/to-text")
    public ResponseEntity<ChatResponse> toText(@RequestParam("file") MultipartFile file) {
        AudioUploadResponse uploadResponse = audioService.store(file);
        String text = audioService.speechToText(uploadResponse.getStoredFilename());
        return ResponseEntity.ok(new ChatResponse("", text));
    }


    @PostMapping("/upload")
    public ResponseEntity<AudioUploadResponse> uploadAudio(@RequestParam("file") MultipartFile file) {

        AudioUploadResponse response = audioService.store(file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat")
    public AudioChatResponse chatWithAudio (@RequestParam("file") MultipartFile file) {
        // 1. upload file
        AudioUploadResponse uploadResponse = audioService.store(file);

        // 2. convert audio into text
        String transcript = audioService.speechToText(uploadResponse.getStoredFilename());

        // 3. send transcript to chat service
        ChatResponse chatResponse = travelChatService.chat(ChatRequest.builder()
                .message(transcript)
                .build());

        // 4. return the response
        return new AudioChatResponse(transcript, chatResponse.getResponse());
    }

    @PostMapping("/to-speech")
    public ResponseEntity<byte[]> textToSpeech(@RequestParam("text") String text) {

        byte[] audio = audioService.textToSpeech(text);
        return ResponseEntity.ok()
                .header("Content-Type", "audio/mpeg")
                .body(audio);
    }
}
