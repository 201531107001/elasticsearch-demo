package httputil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
/**
 * http������
 * 
 * @author ����
 *
 */
public class RpcHttpUtil {

	public static final String GET = "GET";
	public static final String POST = "POST";

	public static String invokeHttp(String url, String method, Map<String, String> paramMap) 
			throws UnsupportedOperationException, IOException {
		// ����HttpClient�������Ӧ����
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		// �ж����󷽷���get����post
		if (StringUtils.equalsIgnoreCase(method, GET)) {
			// �����get����Ҫƴ������url�Ĳ���
			StringBuilder urlSb = new StringBuilder(url);
			int paramIndex = 0;
			for (Entry<String, String> entry : paramMap.entrySet()) {
				// get����Ҫ׷�Ӳ������м���һ��?
				if (paramIndex == 0) {
					urlSb.append("?");
				}
				// ƴ�Ӳ���
				urlSb.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
			// ǰ����ƴ�Ӳ���ʱ������һ��&��Ӧȥ��
			urlSb.delete(urlSb.length() - 1, urlSb.length());
			HttpGet get = new HttpGet(urlSb.toString());
			// ��HttpClientȥ����get���󣬵õ���Ӧ
			response = client.execute(get);
		} else if (StringUtils.equalsIgnoreCase(method, POST)) {
			HttpPost post = new HttpPost(url);
			// �����post����Ҫ���������������װ����
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : paramMap.entrySet()) {
				paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// �����������ĵı���
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(paramList, "GBK");
			post.setEntity(uefEntity);
			// ��HttpClientȥ����post���󣬵õ���Ӧ
			response = client.execute(post);
		} else {
			// �����������Ͳ�֧��
			throw new RuntimeException("�Բ��𣬸�����ʽ��֧�֣�");
		}
		
		// ��ȡ��Ӧ���ģ�����װ��Map
		InputStream is = response.getEntity().getContent();
		String result = IOUtils.toString(is, "GBK");

		is.close();
		client.close();
		return result;
	}

}
