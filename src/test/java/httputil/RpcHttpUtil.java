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
 * http工具类
 * 
 * @author 清明
 *
 */
public class RpcHttpUtil {

	public static final String GET = "GET";
	public static final String POST = "POST";

	public static String invokeHttp(String url, String method, Map<String, String> paramMap) 
			throws UnsupportedOperationException, IOException {
		// 创建HttpClient对象和响应对象
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		// 判断请求方法是get还是post
		if (StringUtils.equalsIgnoreCase(method, GET)) {
			// 如果是get请求，要拼接请求url的参数
			StringBuilder urlSb = new StringBuilder(url);
			int paramIndex = 0;
			for (Entry<String, String> entry : paramMap.entrySet()) {
				// get请求要追加参数，中间有一个?
				if (paramIndex == 0) {
					urlSb.append("?");
				}
				// 拼接参数
				urlSb.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
			// 前面在拼接参数时最后多了一个&，应去掉
			urlSb.delete(urlSb.length() - 1, urlSb.length());
			HttpGet get = new HttpGet(urlSb.toString());
			// 让HttpClient去发送get请求，得到响应
			response = client.execute(get);
		} else if (StringUtils.equalsIgnoreCase(method, POST)) {
			HttpPost post = new HttpPost(url);
			// 如果是post请求，要构造虚拟表单，并封装参数
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : paramMap.entrySet()) {
				paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// 设置请求正文的编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(paramList, "GBK");
			post.setEntity(uefEntity);
			// 让HttpClient去发送post请求，得到响应
			response = client.execute(post);
		} else {
			// 其他请求类型不支持
			throw new RuntimeException("对不起，该请求方式不支持！");
		}
		
		// 提取响应正文，并封装成Map
		InputStream is = response.getEntity().getContent();
		String result = IOUtils.toString(is, "GBK");

		is.close();
		client.close();
		return result;
	}

}
