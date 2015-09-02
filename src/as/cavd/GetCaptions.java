package as.cavd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.File;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Captions.Download;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.google.common.collect.Lists;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;

public class GetCaptions {
//	private static final String PROPERTIES_FILENAME = "youtube.properties";
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";
	private static final String SRT = "srt";
	private static YouTube youtube;
	public static void main(String[] args) {
		
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
//		Properties properties = new Properties();
		try {
//            InputStream in = GetCaptions.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
//            properties.load(in);
			Credential credential = authorize(scopes, "captions");
			
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("youtube-cmdline-captions-sample").build();
			
			String videoId = "BuJVO-FqK2k";
			YouTube.Captions.List listCaptions = youtube.captions().list("snippet", videoId);
			CaptionListResponse captionListResponse = listCaptions.execute();
			List<Caption> captionList = captionListResponse.getItems();
			for(Caption caption: captionList) {
				System.out.println(caption);
			}
			Caption caption = captionList.get(0);
			Download captionDownload = youtube.captions().download(caption.getId()).setTfmt(SRT);
			MediaHttpDownloader downloader = captionDownload.getMediaHttpDownloader();
			downloader.setDirectDownloadEnabled(false);
			OutputStream outputFile = new FileOutputStream("captionFile.srt");
			captionDownload.executeAndDownloadTo(outputFile);
        } catch (GoogleJsonResponseException e) {
        	System.err.println("GoogleJsonResponseException code: " + e.getMessage());
        	e.printStackTrace();
		} catch (IOException e) {
            System.err.println("IOException: " + e.getCause() + " : " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
        	System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

	}
	public static Credential authorize(List<String> scopes, String credentialDataStore) throws IOException {
		Reader clientSecretReader = new InputStreamReader(GetCaptions.class.getResourceAsStream("/client_secrets.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
		
		if(clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println("Enter Client ID and Secret from https://console.developers.google.com"
					+ "into client_secrets.json");
			System.exit(1);
		}
		
		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDataStore);
		
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore).build();
		
		LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();
		
		return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}
}
