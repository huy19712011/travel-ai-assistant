package org.example.travelaiassistant.service;

import com.openai.models.audio.AudioResponseFormat;
import lombok.RequiredArgsConstructor;
import org.example.travelaiassistant.dto.AudioUploadResponse;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioService {

    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;

    @Value("${app.audio.upload-dir}")
    private String uploadDir;

    public AudioUploadResponse store(MultipartFile file) {
        try {

            Path audioDir = Path.of(uploadDir);
            Files.createDirectories(audioDir);

            String fileId = UUID.randomUUID().toString();
            String storedFilename = fileId + "_" + file.getOriginalFilename();
            Path targetPath = audioDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath);

            return AudioUploadResponse.builder()
                    .fileId(fileId)
                    .originalFilename(file.getOriginalFilename())
                    .storedFilename(storedFilename)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }

    }

    public String speechToText(String storedFilename) {

        try {

            Path audioPath = Path.of(uploadDir).resolve(storedFilename);
            Resource audio = new FileSystemResource(audioPath);

            // many options here
            OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                    .responseFormat(AudioResponseFormat.JSON)
                    //.language("vi")
                    .build();

            AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audio, options);
            AudioTranscriptionResponse response = transcriptionModel.call(prompt);

            return response.getResult().getOutput();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public byte[] textToSpeech(String text) {

        // Many options here
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .voice(OpenAiAudioSpeechOptions.Voice.ONYX)
                .build();

        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text, options);
        TextToSpeechResponse response = textToSpeechModel.call(prompt);
        return response.getResult().getOutput();
    }
}
