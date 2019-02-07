package org.b3log.solo.processor.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JsonRenderer;
import org.json.JSONObject;


@RequestProcessor
public class LrcProcessor {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(LrcProcessor.class);

	/**
	 * @param context the specified http request context
	 */
	@RequestProcessing("/getLrc")
	public void sendPost(final RequestContext context) {
		final HttpServletRequest request = context.getRequest();
		String id = request.getParameter("id");
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL("http://music.163.com/api/song/lyric?id=" + id + "&lv=1&kv=1");
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10 * 1000);
			conn.setDoOutput(true); // 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		final JsonRenderer renderer = new JsonRenderer();
		context.setRenderer(renderer);
		final JSONObject jsonObject = new JSONObject();
		renderer.setJSONObject(jsonObject);
		// 把歌词中的/n去掉
		jsonObject.put("lrc", StringUtils.replace(result, "\\n", ""));
	}

}
