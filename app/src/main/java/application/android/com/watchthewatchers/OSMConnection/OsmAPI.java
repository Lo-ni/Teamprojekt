package application.android.com.watchthewatchers.OSMConnection;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;



public class OsmAPI extends DefaultApi10a {

    private static final String REQUEST_TOKEN_ENDPOINT = "https://www.openstreetmap.org/oauth/request_token";
    private static final String ACCESS_TOKEN_ENDPOINT = "https://www.openstreetmap.org/oauth/access_token";
    private static final String AUTHORIZE_URL = "https://www.openstreetmap.org/oauth/authorize?oauth_token=%s";

    private static final String TEST_REQUEST_TOKEN_ENDPOINT = "https://master.apis.dev.openstreetmap.org/oauth/request_token";
    private static final String TEST_ACCESS_TOKEN_ENDPOINT = "https://master.apis.dev.openstreetmap.org/oauth/access_token";
    private static final String TEST_AUTHORIZE_URL = "https://master.apis.dev.openstreetmap.org/oauth/authorize?oauth_token=%s";



    public enum OSM_PERM {
        READ, WRITE, DELETE
    };

    /**
     * read, write, or delete (delete includes read/write)
     */
    private final String permString;

    protected OsmAPI() {
        permString = null;
    }

    protected OsmAPI(OSM_PERM perm) {
        permString = perm.name().toLowerCase();
    }

    private static class InstanceHolder {
        private static final OsmAPI INSTANCE = new OsmAPI();
    }

    public static OsmAPI instance() {
        return OsmAPI.InstanceHolder.INSTANCE;
    }

    public static OsmAPI instance(OSM_PERM perm) {
        return perm == null ? instance() : new OsmAPI(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        String authUrl = String.format(AUTHORIZE_URL, requestToken.getToken());

        if (permString != null) {
            authUrl += "&perms=" + permString;
        }

        return authUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_ENDPOINT;
    }
}
