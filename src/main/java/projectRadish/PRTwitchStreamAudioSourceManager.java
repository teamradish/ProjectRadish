package projectRadish;

import com.sedmelluq.discord.lavaplayer.source.twitch.*;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.ExceptionTools;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.JsonBrowser;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpConfigurable;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.config.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

public class PRTwitchStreamAudioSourceManager implements AudioSourceManager, HttpConfigurable
{
    private static final String STREAM_NAME_REGEX = "^https://(?:www\\.|go\\.)?twitch.tv/([^/]+)$";
    private static final Pattern streamNameRegex = Pattern.compile("^https://(?:www\\.|go\\.)?twitch.tv/([^/]+)$");
    public static final String CLIENT_ID = "jzkbprff40iqj646a697cyrvl0zt2m6";
    private final HttpInterfaceManager httpInterfaceManager = HttpClientTools.createDefaultThreadLocalManager();

    //Kimimaru: We need this for now
    private TwitchStreamAudioSourceManager sourceManager = null;

    public PRTwitchStreamAudioSourceManager() {
        sourceManager = new TwitchStreamAudioSourceManager();
    }

    public String getSourceName() {
        return "twitch";
    }

    public AudioItem loadItem(DefaultAudioPlayerManager manager, AudioReference reference) {
        String streamName = getChannelIdentifierFromUrl(reference.identifier);
        if (streamName == null) {
            return null;
        } else {
            JsonBrowser channelInfo = this.fetchStreamChannelInfo(streamName);
            if (channelInfo == null) {
                return AudioReference.NO_TRACK;
            } else {
                String displayName = streamName;
                JsonBrowser obj = channelInfo.get("data");
                List<JsonBrowser> browsers = obj.values();

                if (browsers.size() == 0)
                {
                    return null;
                }

                JsonBrowser realObj = browsers.get(0);
                String status = realObj.get("title").text();
                return new TwitchStreamAudioTrack(new AudioTrackInfo(status, displayName, 9223372036854775807L, reference.identifier, true, reference.identifier), sourceManager);
            }
        }
    }

    public boolean isTrackEncodable(AudioTrack track) {
        return true;
    }

    public void encodeTrack(AudioTrack track, DataOutput output) throws IOException {
    }

    public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) throws IOException {
        return new TwitchStreamAudioTrack(trackInfo, sourceManager);
    }

    public static String getChannelIdentifierFromUrl(String url) {
        Matcher matcher = streamNameRegex.matcher(url);
        return !matcher.matches() ? null : matcher.group(1);
    }

    public static HttpUriRequest createGetRequest(String url) {
        return addClientHeaders(new HttpGet(url));
    }

    public static HttpUriRequest createGetRequest(URI url) {
        return addClientHeaders(new HttpGet(url));
    }

    public HttpInterface getHttpInterface() {
        return this.httpInterfaceManager.getInterface();
    }

    public void configureRequests(Function<RequestConfig, RequestConfig> configurator) {
        this.httpInterfaceManager.configureRequests(configurator);
    }

    public void configureBuilder(Consumer<HttpClientBuilder> configurator) {
        this.httpInterfaceManager.configureBuilder(configurator);
    }

    private static HttpUriRequest addClientHeaders(HttpUriRequest request) {
        request.setHeader("Client-ID", "jzkbprff40iqj646a697cyrvl0zt2m6");
        return request;
    }

    private JsonBrowser fetchStreamChannelInfo(String name) {
        try {
            HttpInterface httpInterface = this.getHttpInterface();
            Throwable var3 = null;

            JsonBrowser var5;
            try {
                HttpUriRequest request = createGetRequest("https://api.twitch.tv/helix/streams?user_login=" + name);
                var5 = HttpClientTools.fetchResponseAsJson(httpInterface, request);
            } catch (Throwable var15) {
                var3 = var15;
                throw var15;
            } finally {
                if (httpInterface != null) {
                    if (var3 != null) {
                        try {
                            httpInterface.close();
                        } catch (Throwable var14) {
                            var3.addSuppressed(var14);
                        }
                    } else {
                        httpInterface.close();
                    }
                }

            }

            return var5;
        } catch (IOException var17) {
            throw new FriendlyException("Loading Twitch channel information failed.", Severity.SUSPICIOUS, var17);
        }
    }

    public void shutdown() {
        ExceptionTools.closeWithWarnings(this.httpInterfaceManager);
    }
}
