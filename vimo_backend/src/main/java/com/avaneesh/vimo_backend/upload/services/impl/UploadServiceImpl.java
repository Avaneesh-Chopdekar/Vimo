package com.avaneesh.vimo_backend.upload.services.impl;

import com.avaneesh.vimo_backend.common.entities.Video;
import com.avaneesh.vimo_backend.common.repositories.VideoRepository;
import com.avaneesh.vimo_backend.upload.services.UploadService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${custom.video.path}")
    String DIR;

    @Value("${custom.video.hls_path}")
    String HLS_DIR;

    private final VideoRepository videoRepository;

    public UploadServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @PostConstruct
    public void init() {
        File dir = new File(DIR);
        if (!dir.exists()) {
            dir.mkdir();
            System.out.println("Directory created");
        }

        File hlsDir = new File(HLS_DIR);
        if (!hlsDir.exists()) {
            hlsDir.mkdir();
            System.out.println("HSL Directory created");
        }
    }

    @Override
    public Video save(Video video, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        InputStream inputStream = file.getInputStream();

        assert fileName != null;
        String cleanedFileName = StringUtils.cleanPath(fileName);
        String cleanedFolderName = StringUtils.cleanPath(DIR);

        Path path = Paths.get(cleanedFolderName, cleanedFileName);

        video.setFilePath(path.toString());
        video.setContentType(contentType);

        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

        video.setContentType(contentType);
        video.setFilePath(path.toString());
        Video savedVideo = videoRepository.save(video);

        processVideo(savedVideo.getId());

        // delete original video file later

        return savedVideo;
    }

    @Override
    public void processVideo(Long videoId) {

        Optional<Video> video = videoRepository.findById(videoId);

        if (video.isEmpty()) {
            throw new RuntimeException("Video not found with id: " + videoId);
        }

        String filePath = video.get().getFilePath();

        //path where to store data:
        Path videoPath = Paths.get(filePath);


//        String output360p = HLS_DIR + videoId + "/360p/";
//        String output720p = HLS_DIR + videoId + "/720p/";
//        String output1080p = HLS_DIR + videoId + "/1080p/";

        try {
//            Files.createDirectories(Paths.get(output360p));
//            Files.createDirectories(Paths.get(output720p));
//            Files.createDirectories(Paths.get(output1080p));

            // ffmpeg command
            Path outputPath = Paths.get(HLS_DIR, videoId.toString());

            Files.createDirectories(outputPath);


            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
                    videoPath, outputPath, outputPath
            );

//            StringBuilder ffmpegCmd = new StringBuilder();
//            ffmpegCmd.append("ffmpeg  -i ")
//                    .append(videoPath.toString())
//                    .append(" -c:v libx264 -c:a aac")
//                    .append(" ")
//                    .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
//                    .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
//                    .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
//                    .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
//                    .append("-master_pl_name ").append(HLS_DIR).append(videoId).append("/master.m3u8 ")
//                    .append("-f hls -hls_time 10 -hls_list_size 0 ")
//                    .append("-hls_segment_filename \"").append(HLS_DIR).append(videoId).append("/v%v/fileSequence%d.ts\" ")
//                    .append("\"").append(HLS_DIR).append(videoId).append("/v%v/prog_index.m3u8\"");


            System.out.println(ffmpegCmd);
            //file this command
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd); // For Linux or Mac
//            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCmd); // For Windows
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }


        } catch (IOException ex) {
            throw new RuntimeException("Video processing fail!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
