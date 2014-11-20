package mydietplan.cnunezba.com.mydietplan;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by cnunezba on 11/11/2014.
 */
public class HttpClientFactory {

    private static DefaultHttpClient defaultHttpClient;
    private static final int HTTP_CONNECTION_TIMEOUT = 15000;

    private synchronized static DefaultHttpClient getThreadSafeClient() {

        if (defaultHttpClient != null) {

            return HttpClientFactory.defaultHttpClient;
        }

        HttpClientFactory.defaultHttpClient = new DefaultHttpClient();

        ClientConnectionManager mgr = HttpClientFactory.defaultHttpClient
                .getConnectionManager();

        HttpParams params = defaultHttpClient.getParams();

        HttpProtocolParams.setUserAgent(params, "PJCOM-ANDROID v1.0");
        HttpConnectionParams.setConnectionTimeout(params,
                HTTP_CONNECTION_TIMEOUT);

        HttpClientFactory.defaultHttpClient = new DefaultHttpClient(
                new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()),
                params);

        return HttpClientFactory.defaultHttpClient;
    }

    private static synchronized ClientHttpRequestFactory getClientHttpRequestFactory() {

        return new HttpComponentsClientHttpRequestFactory(
                HttpClientFactory.getThreadSafeClient());
    }

    public static synchronized RestTemplate getRestTemplate() {

        return new RestTemplate(HttpClientFactory.getClientHttpRequestFactory());
    }
}
