import React, { useState } from "react";
import axios from "axios";
import {
  Form,
  Text,
  TextField,
  TextArea,
  FileTrigger,
  ProgressBar,
  ButtonGroup,
  Button,
} from "@adobe/react-spectrum";

export default function UploadPage() {
  const [file, setFile] = useState<File | null>(null);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadedUrl, setUploadedUrl] = useState("");
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");

  function handleFileSelect(selected: FileList) {
    const videoFile = selected[0];

    if (!videoFile) return;
    if (!videoFile.type.startsWith("video/")) {
      alert("Please select a video file");
      return;
    }

    setFile(videoFile);
  }

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("title", title);
    formData.append("description", description);

    try {
      setIsUploading(true);

      const response = await axios.post(
        "http://localhost:8080/api/v1/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          onUploadProgress: (progressEvent) => {
            const percent = Math.round(
              (progressEvent.loaded / progressEvent.total!) * 100,
            );
            setUploadProgress(percent);
          },
        },
      );

      // setUploadedUrl(response.data.url);
      setIsUploading(false);
    } catch (error) {
      console.error(error);
      setIsUploading(false);
    }
  }

  return (
    <main className="h-svh flex justify-center items-center flex-col gap-8">
      <h1 className="text-2xl font-bold text-center">Upload Video</h1>

      <Form onSubmit={handleSubmit} maxWidth="size-3000">
        <TextField label="Title" value={title} onChange={setTitle} />
        <TextArea
          label="Description"
          value={description}
          onChange={setDescription}
        />
        <FileTrigger
          acceptedFileTypes={["video/*"]}
          onSelect={(e) => handleFileSelect(e!)}
        >
          <Button variant="primary">Select Video</Button>
        </FileTrigger>

        <div>
          {file ? <Text>Selected: {file?.name}</Text> : null}

          {isUploading ? (
            <ProgressBar
              label="Uploading..."
              value={uploadProgress}
              minValue={0}
              maxValue={100}
              showValueLabel
            />
          ) : null}
        </div>

        <ButtonGroup>
          <Button
            type="submit"
            variant="primary"
            isDisabled={!file || isUploading}
          >
            Submit
          </Button>
          <Button
            type="reset"
            variant="secondary"
            isDisabled={!file || isUploading}
          >
            Reset
          </Button>
        </ButtonGroup>
      </Form>
    </main>
  );
}
