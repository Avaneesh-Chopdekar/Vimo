import { useEffect, useRef } from "react";
import Hls from "hls.js";
import Plyr from "plyr";
import "plyr/dist/plyr.css";
import { useParams } from "react-router";

export default function StreamPage() {
  const { id } = useParams();
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

    return () => {
      video.src = "";
    };
  }, [videoRef, src]);

  return (
    <div className="h-svh w-full">
      <h1>Welcome to Vimo!</h1>
      <video
        key={src}
        data-displaymaxtap
        className="aspect-video h-[180px] sm:h-[225px] md:h-[340px]"
        ref={videoRef}
      />
    </div>
  );
}
