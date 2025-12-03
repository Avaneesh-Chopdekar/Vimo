import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";

interface VideoListItem {
  id: string;
  title: string;
  thumbnailUrl: string;
}

export default function HomePage() {
  const [videos, setVideos] = useState<VideoListItem[]>([]);

  const navigate = useNavigate();

  async function fetchVideos() {
    const response = await axios.get("http://localhost:8080/api/v1/listing");
    return response.data as VideoListItem[];
  }

  useEffect(() => {
    fetchVideos().then((videos) => {
      console.log(videos);
      setVideos(videos);
    });
  }, []);

  return (
    <div className="h-svh w-full">
      <h1>Welcome to Vimo!</h1>

      <div className="grid grid-cols-3 gap-3">
        {videos.map((video) => (
          <div
            key={video.id}
            className="p-2 border border-gray-400 rounded-md shadow-md cursor-pointer h-[250px] aspect-video"
            onClick={() => navigate(`/stream/${video.id}`)}
          >
            <img
              src={"https://placehold.co/1280x720/png?text=Video Thumbnail"}
              height={200}
              className="aspect-video"
              alt={video.title}
            />
            <h2>{video.title}</h2>
          </div>
        ))}
      </div>
    </div>
  );
}
