import { useEffect, useRef, useState } from "react";
import Hls from "hls.js";
import Plyr from "plyr";
import "plyr/dist/plyr.css";
import { useParams } from "react-router";
import axios from "axios";

interface VideoInfo {
  title: string;
  description: string;
  thumbnailUrl: string;
  views: number;
  likes: number;
  dislikes: number;
  visibility: string;
  uploadDate: string;
}

export default function StreamPage() {
  const { id } = useParams();

  const [videoInfo, setVideoInfo] = useState<VideoInfo | null>(null);

  const videoRef = useRef<HTMLVideoElement>(null);

  const src = `http://localhost:8080/api/v1/stream/${id}/master.m3u8`;
  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;

    video.controls = true;
    if (video.canPlayType("application/vnd.apple.mpegurl")) {
      // This will run in safari, where HLS is supported natively
      video.src = src;
    } else if (Hls.isSupported()) {
      // This will run in all other modern browsers
      const hls = new Hls();
      hls.loadSource(src);
      new Plyr(video, {
        keyboard: { global: true },
        tooltips: { controls: true },
        ratio: "16:9",
        controls: [
          "play-large",
          "rewind",
          "play",
          "fast-forward",
          "progress",
          "current-time",
          "duration",
          "mute",
          // "volume",
          "settings",
          // "download",
          "pip",
          "airplay",
          "fullscreen",
        ],
      });
      hls.attachMedia(video);
    } else {
      console.error(
        "This is an old browser that does not support MSE https://developer.mozilla.org/en-US/docs/Web/API/Media_Source_Extensions_API",
      );
    }

    getVideoInfo();

    return () => {
      video.src = "";
    };
  }, [videoRef, src]);

  async function getVideoInfo() {
    const response = await axios.get(
      `http://localhost:8080/api/v1/insights/metadata/${id}`,
    );
    const data = await response.data;
    console.log(data);
    setVideoInfo(data);
  }

  return (
    <div className="h-svh w-full">
      <h1>Welcome to Vimo!</h1>
      <video
        key={src}
        data-displaymaxtap
        className="aspect-video h-[180px] sm:h-[225px] md:h-[340px]"
        ref={videoRef}
      />
      <h2 className="text-2xl font-bold">{videoInfo?.title}</h2>
      {videoInfo?.description && <p>{videoInfo.description}</p>}
      <br />
      Likes: {videoInfo?.likes}
      <br />
      Dislikes: {videoInfo?.dislikes}
      <br />
      Views: {videoInfo?.views}
    </div>
  );
}
