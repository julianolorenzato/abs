<h1 align="center">
Adaptive Bitrate Streaming (Proof of Concept)</h1>

This project is a proof of concept for an on-demand video streaming platform that supports adaptive bitrate streaming via the HLS (HTTP Live Streaming) protocol. It converts uploaded video files into HLS format using FFmpeg and makes them available for playback using the Video.js HTML video player.

> Note: Support to MPEG-DASH is still not available.

## Features

- On-demand video streaming.
- Adaptive bitrate streaming using HLS.

## How to run

To run this project locally, follow these steps:


### Prerequisites

- Docker
- Docker Compose

### Instructions

1. Clone this repository to your local machine.
   
```
git clone https://github.com/julianolorenzato/abs.git
```

2. Navigate to the project directory and start the containers using Docker Compose
   
```
cd abs && docker compose up --build
```

### Usage

On the homepage, use the provided form to upload new videos.

1. Select a video file.
2. Enter a title for the video.
3. Submit the form.
4. After uploading, you can watch the video in the player by appending ?v=[YOUR-VIDEO-TITLE] to the URL.

Example: http://localhost:8080?v=your-video-title

The video will be played using adaptive bitrate streaming.