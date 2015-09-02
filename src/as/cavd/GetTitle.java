package as.cavd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.io.OutputStream;
import java.io.FileOutputStream;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoCategoryListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoCategorySnippet;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
//import com.google.api.services.youtube.model.CaptionSnippet;
import com.google.api.services.youtube.YouTube.Captions.Download;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
//import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.common.collect.Lists;

public class GetTitle {
	
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	//identifies file containing developer's API key
	private static final String PROPERTIES_FILENAME = "youtube.properties";
	
	private static final String SRT = "srt";
	
	//Used to make YouTube Data API requests
	private static YouTube youtube;
	
	public static void main(String[] args) {
		
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		//read developer key from properties file
		Properties properties = new Properties();
		try {
            InputStream in = GetTitle.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);
        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }
		
		try {
			
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				} 
			}).setApplicationName("youtube-get-video-info").build();
			
			String apiKey = properties.getProperty("youtube.apikey");
			
			//prompt for video id
			String videoId = getVideoIdFromUser();
			
			//retrieve resources representing specified videos
			YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet").setId(videoId);
			listVideosRequest.setKey(apiKey);
			VideoListResponse listResponse = listVideosRequest.execute();
			
			List<Video> videoList = listResponse.getItems();
//			if(videoList.isEmpty()) {
//				System.out.println("Can't find video with ID: " + videoId);
//				return;
//			}
			
			Video video = videoList.get(0);
			VideoSnippet snippet = video.getSnippet();
			String categoryId = snippet.getCategoryId();
			
			YouTube.VideoCategories.List listCategoriesRequest = youtube.videoCategories().list("snippet").setId(categoryId);
			listCategoriesRequest.setKey(apiKey);
			VideoCategoryListResponse categoryListResponse = listCategoriesRequest.execute();
			
			List<VideoCategory> categoryList = categoryListResponse.getItems();
			
			VideoCategory category = categoryList.get(0);
			VideoCategorySnippet categorySnippet = category.getSnippet();
			
			/*YouTube.Captions.List listCaptions = youtube.captions().list("snippet", videoId);
			listCaptions.setKey(apiKey);
			CaptionListResponse captionListResponse = listCaptions.execute();
			
			List<Caption> captions = captionListResponse.getItems();*/
//			System.out.println("Returned caption tracks:");
//			CaptionSnippet capSnippet;
			
			System.out.println("Title: " + snippet.getTitle());
			System.out.println("Description: " + snippet.getDescription());
			System.out.println("Category: " + categorySnippet.getTitle());
			
			/*for(Caption caption:captions) {
//				capSnippet = caption.getSnippet();
				downloadCaption(caption.getId(), apiKey);
//				System.out.println("ID: " + caption.getId());
//				System.out.println("Name: " + capSnippet.getName());
//				System.out.println("Language: " + capSnippet.getLanguage());
			}*/
			
		} catch (GoogleJsonResponseException e) {
        	System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	private static String getVideoIdFromUser() throws IOException {
		
		String videoId = "";
		System.out.print("Please enter video id: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		videoId = bReader.readLine();
		
//		if(videoId.length()<1) {
//			System.out.print("Video Id can't be empty!");
//			System.exit(1);
//		}
		
		return videoId;
	}
	
	private static void downloadCaption(String captionId, String apiKey) throws IOException {
		
		Download captionDownload = youtube.captions().download(captionId).setTfmt(SRT);
		
		MediaHttpDownloader downloader = captionDownload.getMediaHttpDownloader();
		
		downloader.setDirectDownloadEnabled(false);
		
//		MediaHttpDownloaderProgressListener downloadProgressListener = new MediaHttpDownloaderProgressListener() {
//			@Override
//			public void progressChanged(MediaHttpDownloader downloader) throws IOException {
//				switch(downloader.getDownloadState()) {
//				case MEDIA_IN_PROGRESS:
//					System.out.println("Download in progress");
//					System.out.println("Download percentage: " + downloader.getProgress());
//					break;
//				case MEDIA_COMPLETE:
//					System.out.println("Download completed!");
//					break;
//				case NOT_STARTED:
//					System.out.println("Download not started!");
//					break;
//				}
//			}
//		};
//		downloader.setProgressListener(downloadProgressListener);
		
		OutputStream outputFile = new FileOutputStream("captionFile.srt");
		captionDownload.setKey(apiKey);
		captionDownload.executeAndDownloadTo(outputFile);
		
	}

}
