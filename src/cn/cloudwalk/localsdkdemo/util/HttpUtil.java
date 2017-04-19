package cn.cloudwalk.localsdkdemo.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class HttpUtil {
	private static final String TAG = LogUtils.makeLogTag("HttpUtil");

	public static JSONObject postga(String urlServer, Map<String, String> map) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);//连接时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(urlServer);

		JSONObject jb = new JSONObject();
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			jb.put(entry.getKey(), entry.getValue());
		}

		LinkedList<NameValuePair> param = new LinkedList<NameValuePair>();
		param.add(new BasicNameValuePair("params", jb.toString()));

		httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		String json = "";
		JSONObject jb1 = null;
		if (resEntity != null) {
			json = EntityUtils.toString(resEntity, "utf-8");
			jb1 = new JSONObject(json);
		}
		if (resEntity != null) {
			resEntity.consumeContent();
		}

		httpclient.getConnectionManager().shutdown();
		return jb1;

	}

}
